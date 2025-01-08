# views.py
from django.conf import settings
from django.http import JsonResponse
from rest_framework.decorators import api_view
from django.contrib.auth.models import User
from .models import UserProfile
import google.auth.transport.requests
from google.oauth2 import id_token


#Authentication
@api_view(['POST'])
def verify_token(request):
    token = request.data.get('token')

    if not token:
        return JsonResponse({'error': 'Token is missing'}, status=400)

    try:
        # Verify the token using Google's public keys
        id_info = id_token.verify_oauth2_token(token, google.auth.transport.requests.Request(), settings.GOOGLE_CLIENT_ID)

        # Extract user data from the token
        name = id_info.get('name')
        email = id_info.get('email')
        photo_url = id_info.get('picture')

        # Check if the user already exists
        user, created = User.objects.get_or_create(username=email, defaults={'email': email})
        
        if created:
            # If the user is new, create a UserProfile
            UserProfile.objects.create(user=user, name=name, email=email, photo_url=photo_url)
        else:
            # If the user already exists, update their profile
            user_profile = user.userprofile
            user_profile.name = name
            user_profile.photo_url = photo_url
            user_profile.save()

        return JsonResponse({
            'name': name,
            'email': email,
            'photoUrl': photo_url,
        })

    except ValueError as e:
        return JsonResponse({'error': 'Invalid token or token verification failed'}, status=400)


#GroupManagement
# views.py
from django.http import JsonResponse
from .models import Group

@api_view(['POST'])
def check_group_name(request):
    group_name = request.GET.get('group_name')
    
    if not group_name:
        return JsonResponse({'message': 'Group name is required'}, status=400)
    
    # Check if the group name exists
    group_exists = Group.objects.filter(name=group_name).exists()
    
    return JsonResponse({'exists': group_exists})


# views.py
from django.http import JsonResponse
from .models import Group
import random

@api_view(['POST'])
def create_group(request):
    if request.method == 'POST':
        group_name = request.data.get('groupName')
        pin_code = request.data.get('pinCode')

        if not group_name or not pin_code:
            return JsonResponse({'message': 'Group name and pin are required'}, status=400)
        
        # Create the group in the database
        group = Group.objects.create(name=group_name, pin=pin_code)
        
        return JsonResponse({'success': True, 'message': 'Group created successfully'})
    
@api_view(['POST'])
def verify_pin(request):
    if request.method == 'POST':
        group_name = request.data.get('groupName')
        pin_code = request.data.get('pinCode')

        if not group_name or not pin_code:
            return JsonResponse({'message': 'Group name and pin are required'}, status=400)
        
        try:
            # Fetch the group by name
            group = Group.objects.get(name=group_name)
        except Group.DoesNotExist:
            return JsonResponse({'message': 'Group not found'}, status=404)
        
        # Check if the pin matches
        if group.pin == pin_code:
            return JsonResponse({'success': True, 'message': 'Pin verified successfully'})
        else:
            return JsonResponse({'success': False, 'message': 'Incorrect pin'})