                    # <!----------------- model.py ---------------------->
from django.contrib.auth.models import AbstractUser, Group, Permission
from django.db import models


                    # <!----------------- Authentication ---------------------->

class CustomUser(AbstractUser):
    username = models.CharField(max_length=150, unique=True, null=False, blank=False, default='default_username')  # Provide a default value for username
    email = models.EmailField(unique=True)
    phoneNumber = models.CharField(max_length=15, blank=True, null=True)
    email_verification_token = models.CharField(max_length=255, null=True, blank=True)  
    password_reset_token = models.CharField(max_length=255, null=True, blank=True)  # Add this line
    is_verified = models.BooleanField(default=False)


    user_permissions = models.ManyToManyField(
        Permission,
        related_name="customuser_permissions",
        blank=True
    )

    def __str__(self):
        return self.email




                    # <!----------------- GROUP MANAGEMENT ---------------------->
from django.db import models
from django.conf import settings

class Group(models.Model):  # Renamed from 'Group' to avoid conflicts
    group_name = models.CharField(max_length=255, unique=True)
    max_members = models.PositiveIntegerField(default=10)  # Default max members
    pin = models.PositiveIntegerField(unique=True, null=True, blank=True)
    created_by = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='created_groups', null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True, null=True, blank=True)

    @property
    def current_member_count(self):
        return self.members.count()

    def __str__(self):
        return self.group_name

    def get_admin(self):
        admin = self.members.filter(role="admin").first()
        return admin if admin else None


    def remove_member(self, user):
        """Removes a member and assigns a new admin if necessary."""
        member = self.members.filter(user=user).first()
        if member:
            member.delete()
            if member.role == "admin":  
                next_member = self.members.first()  # Get the next available member
                if next_member:
                    next_member.role = "admin"
                    next_member.save()

    def delete_group(self):
        """Delete the group if the user is admin."""
        if self.get_admin() == self.created_by:
            self.delete()                
    
                    

class GroupMember(models.Model):
    ROLE_CHOICES = [
        ('admin', 'Admin'),  # Changed 'in_charge' to 'admin'
        ('member', 'Member'),
    ]
    group = models.ForeignKey(Group, on_delete=models.CASCADE, related_name='members')
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name='member')
    role = models.CharField(max_length=20, choices=ROLE_CHOICES, default='member')  # Fixed default value
    joined_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"{self.user.email} - {self.group.group_name} - {self.role}"
    
    def promote_to_admin(self):
        if self.role != 'admin':
            self.role = 'admin'
            self.save()

    def demote_to_member(self):
        if self.role != 'member':
            self.role = 'member'
            self.save()


  