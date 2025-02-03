from django.contrib import admin
from .models import CustomUser, Group
# Register your models here.
admin.site.register(CustomUser)
admin.site.register(Group)

#admin.site.register(Group)