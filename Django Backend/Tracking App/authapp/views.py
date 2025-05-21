# views.py 
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth.tokens import PasswordResetTokenGenerator
from django.core.mail import send_mail
from django.conf import settings
from .models import CustomUser

from django.shortcuts import render, get_object_or_404
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
import logging
from .serializers import ForgotPasswordSerializer
from .serializers import ResetPasswordSerializer
from .serializers import ( RegisterSerializer, EmailVerificationSerializer, LoginSerializer, ForgotPasswordSerializer, ResetPasswordSerializer)
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.permissions import AllowAny
from rest_framework.decorators import api_view, permission_classes



import logging
from django.conf import settings
from django.core.mail import send_mail
from django.shortcuts import get_object_or_404
from rest_framework.views import APIView
from rest_framework.decorators import api_view, permission_classes
from rest_framework.response import Response
from rest_framework import status, permissions
from rest_framework_simplejwt.tokens import RefreshToken
from random import randint
from .serializers import RegisterSerializer, LoginSerializer
from django.utils.crypto import get_random_string
import logging




from django.contrib.auth.models import User
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from .models import Group
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework.decorators import api_view
from .models import Group, GroupMember
from .serializers import GroupSerializer


                    # <!----------------- Authentication ---------------------->

class RegisterView(APIView):
    permission_classes = [permissions.AllowAny]  # Allow unauthenticated users

    def post(self, request):
        if request.user.is_authenticated:
            return Response(
                {"error": "You are already logged in."},
                status=status.HTTP_400_BAD_REQUEST
            )

        serializer = RegisterSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()
            token = PasswordResetTokenGenerator().make_token(user)
            user.email_verification_token = token  # Store the token in the user model
            user.save()  # Save the token to the database

            # Construct the verification link
            verification_link = f"{settings.FRONTEND_URL}/api/verify-email/{token}/"

            # Send verification email
            send_mail(
                subject="Verify Your Email",
                message=f"Click the link to verify your email: {verification_link}",
                from_email=settings.EMAIL_HOST_USER,
                recipient_list=[user.email]
            )

            return Response(
                {"message": "Registration successful. Check your email for verification."},
                status=status.HTTP_201_CREATED
            )
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
logger = logging.getLogger(__name__)

# Email Verification

@permission_classes([permissions.AllowAny])
class VerifyEmailView(APIView):
    permission_classes = [AllowAny]

    def get(self, request, token):
        try:
            user = get_object_or_404(CustomUser, email_verification_token=token)
            if PasswordResetTokenGenerator().check_token(user, token):
                user.is_verified = True
                user.email_verification_token = None
                user.save()
                return render(request, 'verify_email.html', {'success': 'Email verified successfully.'})
            return render(request, 'verify_email.html', {'error': 'Invalid or expired token.'})
        except Exception as e:
            return render(request, 'verify_email.html', {'error': 'Verification failed. Please try again.'})

# Login
class LoginView(APIView):
    permission_classes = [permissions.AllowAny]

    def post(self, request):
        serializer = LoginSerializer(data=request.data)
        if serializer.is_valid():
            email = serializer.validated_data['email']
            password = serializer.validated_data['password']
            user = get_object_or_404(CustomUser, email=email)

        if not user.check_password(password):
                return Response({"error": "Invalid credentials."}, status=status.HTTP_401_UNAUTHORIZED)

        if not user.is_verified:
                return Response({"error": "Email is not verified."}, status=status.HTTP_400_BAD_REQUEST)

        refresh = RefreshToken.for_user(user)
        return Response({
                "refresh": str(refresh),
                "access": str(refresh.access_token)
            }, status=status.HTTP_200_OK)

# Forgot Password
class ForgotPasswordView(APIView):
    permission_classes = [AllowAny]

    def post(self, request):
        serializer = ForgotPasswordSerializer(data=request.data)
        if serializer.is_valid():
            email = serializer.validated_data['email']
            try:
                user = get_object_or_404(CustomUser, email=email)
                token = PasswordResetTokenGenerator().make_token(user)
                user.password_reset_token = token
                user.save()

                reset_link = f"{settings.FRONTEND_URL}/api/reset-password/{token}/"
                logger.debug(f"Generated reset link: {reset_link}")

                send_mail(
                    subject="Reset Your Password",
                    message=f"Click the link to reset your password: {reset_link}",
                    from_email=settings.EMAIL_HOST_USER,
                    recipient_list=[email]
                )

                return render(request, 'forgot_password.html', {
                    'success': 'Password reset email sent. Please check your inbox.'
                })
            except CustomUser.DoesNotExist:
                return render(request, 'forgot_password.html', {
                    'error': 'No account found with this email address.'
                })

        return render(request, 'forgot_password.html', {
            'error': 'Invalid request data. Please check your input.'
        })

# Reset Password
@method_decorator(csrf_exempt, name='dispatch')
class ResetPasswordView(APIView):
    permission_classes = [AllowAny]

    def get(self, request, token):
        try:
            user = CustomUser.objects.get(password_reset_token=token)
            if PasswordResetTokenGenerator().check_token(user, token):
                return render(request, 'reset_password.html', {'token': token})
            return render(request, 'reset_password.html', {'error': 'Invalid or expired token.'})
        except CustomUser.DoesNotExist:
            return render(request, 'reset_password.html', {'error': 'Invalid or expired token.'})

    def post(self, request, token):
        try:
            request_data = request.data.copy()
            request_data['token'] = token

            serializer = ResetPasswordSerializer(data=request_data)
            if serializer.is_valid():
                user = CustomUser.objects.get(password_reset_token=token)
                if PasswordResetTokenGenerator().check_token(user, token):
                    new_password = serializer.validated_data['newPassword']
                    user.set_password(new_password)
                    user.password_reset_token = None
                    user.save()

                    send_mail(
                        subject="Your password has been reset",
                        message="Your password has been successfully reset. You can now log in.",
                        from_email=settings.EMAIL_HOST_USER,
                        recipient_list=[user.email],
                    )

                    return render(request, 'reset_password.html', {
                        'success': 'Password reset successful. You can now log in.'
                    })
                return render(request, 'reset_password.html', {'error': 'Invalid token.'})

            return render(request, 'reset_password.html', {'error': 'Invalid form data. Please check your input.'})
        except CustomUser.DoesNotExist:
            return render(request, 'reset_password.html', {'error': 'Invalid or expired token.'})
        except Exception as e:
            logger.error(f"Error during password reset: {e}")
            return render(request, 'reset_password.html', {'error': 'An unexpected error occurred.'})

from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from .serializers import UserSerializer

class UserInfoView(APIView):
    permission_classes = [IsAuthenticated]  # Ensure the user is authenticated

    def get(self, request):
        # Get the authenticated user
        user = request.user
        serializer = UserSerializer(user)
        return Response(serializer.data)

                    # <!----------------- GROUP MANAGEMENT ---------------------->
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework import status
from .models import Group, GroupMember
from .serializers import GroupSerializer, GroupJoinSerializer, GroupDetailSerializer
from random import randint

@api_view(['POST'])
@permission_classes([IsAuthenticated])  # Ensure only authenticated users can create a group
def create_group(request):
    """
    Creates a new group with a unique PIN and assigns the authenticated user as the admin.
    """
    user = request.user  # The authenticated user making the request
    serializer = GroupSerializer(data=request.data)

    if serializer.is_valid():
        group_name = serializer.validated_data['group_name']
        max_members = serializer.validated_data['max_members']

        # Ensure the generated PIN is unique
        while True:
            pin = randint(100000, 999999)
            if not Group.objects.filter(pin=pin).exists():
                break

        # Create the group
        group = Group.objects.create( group_name=group_name, max_members=max_members,pin=pin,created_by=user, )

        # Add the user as the admin in the GroupMember model
        GroupMember.objects.create( group=group, user=user, role='admin' )

        # Return response with all group details
        return Response({
            "status": "success",
            "message": "Group created successfully.",
            "group_id": group.id,
            "group_name": group.group_name,
            "pin": group.pin,
            "max_members": group.max_members,
            "current_member_count": group.current_member_count,
            "created_by": {
                "id": user.id,
                "username": user.username,
                "email": user.email
            }
        }, status=status.HTTP_201_CREATED)

    return Response({
        "status": "error",
        "message": "Failed to create group.",
        "errors": serializer.errors
    }, status=status.HTTP_400_BAD_REQUEST)


@api_view(['POST'])
@permission_classes([IsAuthenticated])  # Ensure only authenticated users can access
def join_group(request):
    """
    Allows a user to join an existing group using a valid PIN.
    """
    serializer = GroupJoinSerializer(data=request.data)
    if serializer.is_valid():
        pin = serializer.validated_data['pin']
        user = request.user

        # Check if the group exists
        try:
            group = Group.objects.get(pin=pin)
        except Group.DoesNotExist:
            return Response({"error": "Invalid PIN. Group not found."}, status=status.HTTP_404_NOT_FOUND)

       # Check if group is full
        if group.members.count() >= group.max_members:
            return Response({"error": "This group has reached its maximum capacity."}, status=status.HTTP_400_BAD_REQUEST)

        # Check if user is already a member
        if GroupMember.objects.filter(group=group, user=user).exists():
            return Response({"error": "You are already a member of this group."}, status=status.HTTP_400_BAD_REQUEST)

        group_member = GroupMember.objects.create(group=group, user=user, role='member')


        return Response({  "message": "You have joined the group successfully.",
            "group_id": group.id,
            "group_name": group.group_name,
        }, status=status.HTTP_200_OK)
    

    return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)



@api_view(['GET'])
def user_groupsdetail(request):
    try:
        # Fetch the custom user model using get_user_model()
        User = get_user_model()

        # Get the currently logged-in user
        user = User.objects.get(email=request.user.email)

        # Get the GroupMembers for the user and filter groups
        group_memberships = GroupMember.objects.filter(user=user)
        groups = [membership.group for membership in group_memberships]  # List of groups for the user

        # Serialize the groups
        serializer = GroupSerializer(groups, many=True)
        return Response(serializer.data)
    except User.DoesNotExist:
        return Response({"error": "User not found"}, status=400)
    except Exception as e:
        return Response({"error": str(e)}, status=500)


@api_view(['POST'])
@permission_classes([IsAuthenticated])
def leave_group(request, group_id):
    """
    Allows a user to leave a group.
    """
    user = request.user

    try:
        group_member = GroupMember.objects.get(group_id=group_id, user=user)
        
        # Prevent in-charge from leaving if they are the only admin
        if group_member.role == 'in_charge':
            if GroupMember.objects.filter(group=group_member.group, role='in_charge').count() == 1:
                return Response({"error": "You cannot leave as the only in-charge. Transfer ownership first."},
                                status=status.HTTP_400_BAD_REQUEST)

        group_member.delete()
        return Response({"message": "You have left the group successfully."}, status=status.HTTP_200_OK)
    
    except GroupMember.DoesNotExist:
        return Response({"error": "You are not a member of this group."}, status=status.HTTP_404_NOT_FOUND)

from .serializers import MemberRoleSerializer

@api_view(['PATCH'])
@permission_classes([IsAuthenticated])
def update_member_role(request, group_id, member_id):
    """
    Updates a group member's role (e.g., member to in-charge).
    Only in-charge users can update roles.
    """
    user = request.user

    try:
        group = Group.objects.get(id=group_id)
        requesting_member = GroupMember.objects.get(group=group, user=user)

        # Check if the user updating roles is an in-charge
        if requesting_member.role != 'admin':
            return Response({"error": "Only admins can update roles."}, status=status.HTTP_403_FORBIDDEN)

        # Validate request data
        try:
            group_member = GroupMember.objects.get(id=member_id, group=group)
        except GroupMember.DoesNotExist:
            return Response({"error": "Group member not found."}, status=status.HTTP_404_NOT_FOUND)

        serializer = MemberRoleSerializer(group_member, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response({"message": "Member role updated successfully."}, status=status.HTTP_200_OK)
        
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    except Group.DoesNotExist:
        return Response({"error": "Member not found."}, status=status.HTTP_404_NOT_FOUND)
    

@api_view(['DELETE'])
@permission_classes([IsAuthenticated])
def delete_group(request, group_id):
    """
    Allows the admin to delete a group.
    """
    user = request.user

    try:
        group = Group.objects.get(id=group_id)

        if group.created_by != user:
            return Response({"error": "Only the creator of the group can delete it."}, status=status.HTTP_403_FORBIDDEN)

        group.delete()

        return Response({"message": "Group deleted successfully."}, status=status.HTTP_200_OK)

    except Group.DoesNotExist:
        return Response({"error": "Group not found."}, status=status.HTTP_404_NOT_FOUND)