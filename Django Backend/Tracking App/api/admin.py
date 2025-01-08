from django.contrib import admin
from .models import UserProfile, Group
# Register your models here.
admin.site.register(UserProfile)
admin.site.register(Group)
