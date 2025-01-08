# models.py
from django.db import models
from django.contrib.auth.models import User
from django.utils import timezone 

# Authentication
class UserProfile(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    name = models.CharField(max_length=255)
    email = models.EmailField(unique=True)
    photo_url = models.URLField()

    def __str__(self):
        return self.name

# Groupmanagement# models.py
from django.db import models

class Group(models.Model):
    name = models.CharField(max_length=255, unique=True)
    pin = models.CharField(max_length=4, default='0000')  # Default value for pin

    def __str__(self):
        return self.name
