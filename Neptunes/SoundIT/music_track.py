from django.db import models

class MusicTrack(models.Model):
    album = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    trackURL = models.CharField(max_length=255)
