#urls.py
from django.urls import path, include
from .views import RegisterView, VerifyEmailView, LoginView, ForgotPasswordView, ResetPasswordView
from . import views

urlpatterns = [
    # Authentication URLs
    path('register/', RegisterView.as_view(), name='signup'),
    path('verify-email/<str:token>/', VerifyEmailView.as_view(), name='verify-email'),
    path('login/', LoginView.as_view(), name='login'),
    path('forgot-password/', ForgotPasswordView.as_view(), name='forgot-password'),
    path('reset-password/<str:token>/', ResetPasswordView.as_view(), name='reset-password'),

    # Group Management URLs
    path('groups/create/', views.create_group, name='create_group'),
    path('groups/join/', views.join_group, name='join_group'),
    path('group_details/<int:group_id>/', views.group_details, name='group_details'),  # Get group details and members
    path('leave_group/<int:group_id>/', views.leave_group, name='leave_group'),  # Leave a group
    path('delete_group/<int:group_id>/', views.delete_group, name='delete_group'),  # Delete a group
    path('update_member_role/<int:group_id>/<int:member_id>/', views.update_member_role, name='update_member_role'),  # Update member role
]
