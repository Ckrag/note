from django.contrib import admin
from .models import User
from .models import Category
from .models import Tag
from .models import Note
from django.contrib.auth.admin import UserAdmin
# Register your models here.
admin.site.register(User, UserAdmin)
admin.site.register(Category)
admin.site.register(Tag)
admin.site.register(Note)