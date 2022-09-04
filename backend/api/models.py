from django.conf import settings
from django.db import models
from django.contrib.auth.models import AbstractUser


class User(AbstractUser):
    pass


class Category(models.Model):
    title = models.CharField('title', max_length=200)
    creation = models.DateTimeField('created datetime', auto_created=True)
    last_change = models.DateTimeField('updated datetime', auto_now=True)
    author = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
    )

    def __str__(self):
        return self.title


class Tag(models.Model):
    title = models.CharField('title', max_length=200)
    creation = models.DateTimeField('created datetime', auto_created=True)
    last_change = models.DateTimeField('updated datetime', auto_now=True)
    author = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
    )

    def __str__(self):
        return self.title


class Note(models.Model):
    title = models.CharField('title', max_length=200)
    content = models.TextField('content')
    created = models.DateTimeField('created datetime', auto_created=True)
    updated = models.DateTimeField('updated datetime', auto_now=True)
    category = models.ForeignKey(
        Category,
        on_delete=models.CASCADE
    )
    tags = models.ManyToManyField(Tag)
    author = models.ForeignKey(
        settings.AUTH_USER_MODEL,
        on_delete=models.CASCADE,
    )

    def __str__(self):
        return self.title
