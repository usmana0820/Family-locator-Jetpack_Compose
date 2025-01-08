from django.urls import path
from .views import verify_token
from .views import verify_token,check_group_name,create_group,verify_pin

urlpatterns = [
    path('api/verify-token/', verify_token, name='verify_token'),
   path('check-group-name/', check_group_name, name='check_group_name'),
    path('create-group/', create_group, name='create_group'),
    path('verify-pin/', verify_pin, name='verify_pin'),
]
