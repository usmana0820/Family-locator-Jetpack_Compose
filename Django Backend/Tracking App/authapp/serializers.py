# serializers.py 
from rest_framework import serializers
from .models import CustomUser
from django.contrib.auth.password_validation import validate_password
from rest_framework_simplejwt.tokens import RefreshToken

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = CustomUser
        fields = ['id', 'email', 'is_verified']

class RegisterSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True, validators=[validate_password])
    confirmPassword = serializers.CharField(write_only=True)
    phoneNumber = serializers.CharField(write_only=True)

    class Meta:
        model = CustomUser
        fields = ['email', 'password', 'confirmPassword', 'phoneNumber']

    def validate(self, attrs):
        if attrs['password'] != attrs['confirmPassword']:
            raise serializers.ValidationError({"password": "Passwords do not match."})
        return attrs

    def create(self, validated_data):
        validated_data.pop('confirmPassword')

        # Ensure the username is generated from the email
        email = validated_data.get('email')
        username = email.split('@')[0]  # Or use any logic you prefer to generate the username

        user = CustomUser.objects.create_user(username=username, **validated_data)
        return user


class EmailVerificationSerializer(serializers.Serializer):
    token = serializers.CharField()

class LoginSerializer(serializers.Serializer):
    email = serializers.EmailField()
    password = serializers.CharField()

    def validate(self, attrs):
        email = attrs.get('email')
        password = attrs.get('password')

        user = CustomUser.objects.filter(email=email).first()
        if user is None:
            raise serializers.ValidationError("Invalid credentials.")

        if not user.check_password(password):
            raise serializers.ValidationError("Invalid credentials.")

        if not user.is_verified:
            raise serializers.ValidationError("Email is not verified.")
        
        return attrs

class ForgotPasswordSerializer(serializers.Serializer):
    email = serializers.EmailField()

class ResetPasswordSerializer(serializers.Serializer):
    token = serializers.CharField()
    newPassword = serializers.CharField(write_only=True)
    confirmPassword= serializers.CharField(write_only=True)

    def validate(self, attrs):
        if attrs['newPassword'] != attrs['confirmPassword']:
            raise serializers.ValidationError({"password": "Passwords do not match."})
        return attrs



                    # <!----------------- GROUP MANAGEMENT ---------------------->

from rest_framework import serializers
from .models import Group, GroupMember


class GroupSerializer(serializers.ModelSerializer):
    """
    Serializer for Group model.
    Includes basic group details and validates group creation inputs.
    """
    created_by = serializers.StringRelatedField()
    pin = serializers.IntegerField(read_only=True)  # Add pin to response


    class Meta:
        model = Group
        fields = ['id', 'group_name', 'max_members', 'pin', 'created_by']

    def validate(self, attrs):
        """
        Validate group name and maximum members during creation.
        """
        group_name = attrs.get('group_name')
        max_members = attrs.get('max_members')

        if not group_name:
            raise serializers.ValidationError("Group name is required.")

        if max_members is None:
            raise serializers.ValidationError("Maximum members are required.")

        if not isinstance(max_members, int) or max_members <= 0:
            raise serializers.ValidationError("Maximum members must be a positive integer.")

        return attrs

    def validate_max_members(self, value):
        """
        Ensure the maximum members limit is not reduced below the current number of members.
        """
        if self.instance and value < self.instance.members.count():
            raise serializers.ValidationError(
                "Maximum members cannot be less than the current number of members."
            )
        return value


class GroupJoinSerializer(serializers.Serializer):
    """
    Serializer for joining a group using a PIN.
    """
    pin = serializers.IntegerField()

    def validate_pin(self, value):
        if not (100000 <= value <= 999999):
            raise serializers.ValidationError("PIN must be a 6-digit number.")
        return value
    
    def validate(self, attrs):
        pin = attrs.get('pin')
        try:
            group = Group.objects.get(pin=pin)
        except Group.DoesNotExist:
            raise serializers.ValidationError("No group found with this PIN.")
        attrs['group'] = group
        return attrs

class MemberRoleSerializer(serializers.ModelSerializer):
    """
    Serializer for updating the role of a group member.
    """
    user = serializers.StringRelatedField()
    user_email = serializers.CharField(source='user.email', read_only=True)    
    role = serializers.ChoiceField(choices=GroupMember.ROLE_CHOICES)

    class Meta:
        model = GroupMember
        fields = ['user', 'role']


class GroupMemberSerializer(serializers.ModelSerializer):
    """
    Serializer for displaying group member details.
    """
    user = serializers.StringRelatedField()  # Display the username or string representation of the user
    user_email = serializers.EmailField(source="user.email", read_only=True)
    user_name = serializers.CharField(source="user.username", read_only=True)

    class Meta:
        model = GroupMember
        fields = ["id","user", "user_name", "user_email", "role", "joined_at"]

    def validate(self, attrs):
        role = attrs.get('role')
        if role not in ['admin', 'member']:
            raise serializers.ValidationError("Invalid role.")
        return attrs


    def get_user(self, obj):
            return {
                "id": obj.user.id,
                "email": obj.user.email,
                "username": obj.user.username
            }
    

    
class GroupDetailSerializer(serializers.ModelSerializer):
    """
    Serializer for group details, including its members and current member count.
    """
    members = GroupMemberSerializer(many=True, read_only=True, source='members')  # Ensure the related name is correct
    current_member_count = serializers.SerializerMethodField()

    class Meta:
        model = Group
        fields = ["id", "group_name", "max_members", "current_member_count", "members"]

    def get_current_member_count(self, obj):
        return obj.members.count()  # Ensure the correct related name is used
