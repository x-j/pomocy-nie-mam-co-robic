from __future__ import unicode_literals

from django.db import models
from django.contrib.auth.models import User

class Block(models.Model):

    start = models.DateTimeField()
    end = models.DateTimeField()

    name = models.CharField(max_length=255)

    person = models.ForeignKey(User)

class Zaczepka(models.Model):

	start = models.DateTimeField()
	end = models.DateTimeField()
	length = models.CharField(max_length=255)
	hate_mail = models.CharField(max_length=255)
	sender = models.CharField(max_length=255)
	receiver = models.CharField(max_length=255)
	
