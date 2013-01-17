from django.db import models

import soundit_models.music_tracks

class User(models.Model):
    # Setting null=True for email address since we're not asking for it yet.
    emailAddress = models.CharField(max_length=255, null=True)
    password = models.CharField(max_length=255)
    salt = models.CharField(max_length=255)

class Consumer(models.Model):
    user = models.ForeignKey(User)
    facebookId = models.IntegerField(null=True)
    deviceId = models.IntegerField()
    
class Restaurant(models.Model):
    user = models.ForeignKey(User)
    name = models.CharField(max_length=255)
    location = models.CharField(max_length=255)
    phoneNumber = models.CharField(max_length=50)

class Artist(models.Model):
    name = models.CharField(max_length=255)
    
class Album(models.Model):
    name = models.CharField(max_length=255)
    imageURL = models.CharField(max_length=255, null=True)
    imageLatestRev = models.IntegerField(null=True)
    
class MusicCategory(models.Model):
    name = models.CharField(max_field=255)
    
class MusicTrack(models.Model):
    album = models.ForeignKey(Album)
    artist = models.ForeignKey(Artist)
    name = models.CharField(max_length=255)
    trackURL = models.CharField(max_length=255)
    trackLatestRev = models.IntegerField(null=True)
    category = models.ForeignKey(MusicCategory)
    
class Playlist(models.Model):
    restaurant = models.ForeignKey(Restaurant)

class PlaylistItem(models.Model):
    playlist = models.ForeignKey(Playlist)
    musicTrack = models.ForeignKey(MusicTrack)
    votes = models.IntegerField(default=0)
    rankPlayed = models.IntegerField()
    currentRanking = models.IntegerField()