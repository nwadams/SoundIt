from django.db import models
from django.utils import simplejson

class User(models.Model):
    # Setting null=True for email address since we're not asking for it yet.
    email_address = models.CharField(max_length=255, null=True)
    password = models.CharField(max_length=255)
    salt = models.CharField(max_length=255)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "User{" + \
    "id=" + str(self.pk) + \
    ", name='" + str(self.email_address) + '\'' + \
    ", password='" + self.password + "\'" + \
    ", salt=" + str(self.salt) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"email_address": self.email_address})
        result.append({"password": self.password})
        result.append({"salt": self.salt})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)

class Consumer(models.Model):
    user = models.ForeignKey(User)
    facebook_id = models.IntegerField(null=True)
    device_id = models.IntegerField()
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Consumer{" + \
    "id=" + str(self.pk) + \
    ", user='" + self.user + '\'' + \
    ", facebook_id='" + self.facebook_id + "\'" + \
    ", device_id=" + self.device_id + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"user": self.user})
        result.append({"facebook_id": self.facebook_id})
        result.append({"device_id": self.device_id})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Location(models.Model):
    user = models.ForeignKey(User)
    name = models.CharField(max_length=255)
    location = models.CharField(max_length=255)
    phone_number = models.CharField(max_length=50)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Location{" + \
    "id=" + str(self.pk) + \
    ", user='" + self.user + '\'' + \
    ", name='" + self.name + "\'" + \
    ", location=" + self.location + "\'" + \
    ", phone_number=" + self.phone_number + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"user": self.user})
        result.append({"name": self.name})
        result.append({"location": self.location})
        result.append({"phone_number": self.phone_number})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Artist(models.Model):
    name = models.CharField(max_length=255)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Artist{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"name": self.name})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Album(models.Model):
    name = models.CharField(max_length=255)
    image_URL = models.CharField(max_length=255, null=True)
    image_latest_rev = models.IntegerField(null=True)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "Album{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", image_URL=" + self.image_URL + "\'" + \
    ", image_latest_rev=" + self.image_latest_rev + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"name": self.name})
        result.append({"image_URL": self.image_URL})
        result.append({"image_latest_rev": self.image_latest_rev})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class MusicCategory(models.Model):
    name = models.CharField(max_length=255)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "MusicCategory{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"name": self.name})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class MusicTrack(models.Model):
    album = models.ForeignKey(Album)
    artist = models.ForeignKey(Artist)
    name = models.CharField(max_length=255)
    track_URL = models.CharField(max_length=255)
    track_latest_rev = models.IntegerField(null=True)
    category = models.ForeignKey(MusicCategory)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "MusicTrack{" + \
    "id=" + str(self.pk) + \
    ", album='" + self.album + '\'' + \
    ", artist='" + self.artist + '\'' + \
    ", name='" + self.name + "\'" + \
    ", track_URL=" + self.track_URL + "\'" + \
    ", track_latest_rev=" + self.track_latest_rev + "\'" + \
    ", category=" + self.category + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"album": self.album})
        result.append({"artist": self.artist})
        result.append({"name": self.name})
        result.append({"track_URL": self.track_URL})
        result.append({"track_latest_rev": self.track_latest_rev})
        result.append({"category": self.category})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Playlist(models.Model):
    location = models.ForeignKey(Location)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "Playlist{" + \
    "id=" + str(self.pk) + \
    ", location='" + self.location + '\'' + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"location": self.location})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)

class PlaylistItem(models.Model):
    playlist = models.ForeignKey(Playlist)
    music_track = models.ForeignKey(MusicTrack)
    votes = models.IntegerField(default=0)
    rank_played = models.IntegerField()
    current_ranking = models.IntegerField()
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "PlaylistItem{" + \
    "id=" + str(self.pk) + \
    ", playlist='" + self.playlist + '\'' + \
    ", music_track='" + self.music_track + '\'' + \
    ", votes='" + self.votes + "\'" + \
    ", rank_played=" + self.rank_played + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toJson(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"playlist": self.playlist})
        result.append({"music_track": self.music_track})
        result.append({"votes": self.votes})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Vote(models.Model):
    playlist_item = models.ForeignKey(PlaylistItem)
    user = models.ForeignKey(User)
    date_created = models.DateTimeField(auto_now_add=True)
    date_modified = models.DateTimeField(auto_now=True)
    is_deleted = models.BooleanField(default=False)