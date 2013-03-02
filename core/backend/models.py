from django.db import models
from django.utils import simplejson

class Consumer(models.Model):
    email_address = models.CharField(max_length=255, null=True)
    device_id = models.CharField(max_length=255, null=True)
    password = models.CharField(max_length=255)
    salt = models.CharField(max_length=255)
    api_token = models.CharField(max_length=255)
    facebook_id = models.IntegerField(null=True)
    google_id = models.IntegerField(null=True)
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Consumer{" + \
    "id=" + str(self.pk) + \
    ", email_address='" + str(self.email_address) + '\'' + \
    ", device_id='" + str(self.device_id) + '\'' + \
    ", password='" + self.password + "\'" + \
    ", salt=" + str(self.salt) + "\'" + \
    ", api_token=" + self.api_token + "\'" + \
    ", facebook_id='" + self.facebook_id + "\'" + \
    ", google_id='" + self.google_id + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"email_address": self.email_address})
        result.append({"device_id": self.device_id})
        result.append({"password": self.password})
        result.append({"salt": self.salt})
        result.append({"api_token": self.api_token})
        result.append({"facebook_id": self.facebook_id})
        result.append({"google_id": self.google_id})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Location(models.Model):
    email_address = models.CharField(max_length=255, null=True)
    device_id = models.CharField(max_length=255, null=True)
    password = models.CharField(max_length=255)
    salt = models.CharField(max_length=255)
    name = models.CharField(max_length=255)
    location = models.CharField(max_length=255)
    phone_number = models.CharField(max_length=50)
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Location{" + \
    "id=" + str(self.pk) + \
    ", email_address='" + str(self.email_address) + '\'' + \
    ", device_id='" + str(self.device_id) + '\'' + \
    ", password='" + self.password + "\'" + \
    ", salt=" + str(self.salt) + "\'" + \
    ", name='" + str(self.name) + "\'" + \
    ", location=" + str(self.location) + "\'" + \
    ", phone_number=" + str(self.phone_number) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"email_address": self.email_address})
        result.append({"device_id": self.device_id})
        result.append({"password": self.password})
        result.append({"salt": self.salt})
        result.append({"name": self.name})
        result.append({"location": self.location})
        result.append({"phone_number": self.phone_number})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
    
class Artist(models.Model):
    name = models.CharField(max_length=255)
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)

    def __unicode__(self):
        return "Artist{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
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
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "Album{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", image_URL=" + self.image_URL + "\'" + \
    ", image_latest_rev=" + str(self.image_latest_rev) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
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
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "MusicCategory{" + \
    "id=" + str(self.pk) + \
    ", name='" + self.name + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
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
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "MusicTrack{" + \
    "id=" + str(self.pk) + \
    ", album='" + str(self.album) + '\'' + \
    ", artist='" + str(self.artist) + '\'' + \
    ", name='" + self.name + "\'" + \
    ", track_URL=" + self.track_URL + "\'" + \
    ", track_latest_rev=" + str(self.track_latest_rev) + "\'" + \
    ", category=" + str(self.category) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
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
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "Playlist{" + \
    "id=" + str(self.pk) + \
    ", location='" + str(self.location) + '\'' + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"  

    def toDict(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"location": self.location})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)

PLAYLIST_ITEM_STATUS = (
                        (0, 'PLAYED'),
                        (1, 'PLAYING'),
                        (2, 'TO_BE_PLAYED'),
                        )
        
class PlaylistItem(models.Model):
    playlist = models.ForeignKey(Playlist)
    music_track = models.ForeignKey(MusicTrack)
    votes = models.IntegerField(default=0)
    rank_played = models.IntegerField()
    current_ranking = models.IntegerField()
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    item_state = models.IntegerField(PLAYLIST_ITEM_STATUS)
    
    def __unicode__(self):
        return "PlaylistItem{" + \
    "id=" + str(self.pk) + \
    ", playlist='" + str(self.playlist) + '\'' + \
    ", music_track='" + str(self.music_track) + '\'' + \
    ", votes='" + str(self.votes) + "\'" + \
    ", rank_played=" + str(self.rank_played) + "\'" + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"

    def toDict(self):
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
    consumer = models.ForeignKey(Consumer)
    date_created = models.DateTimeField(auto_now_add=True, null=True)
    date_modified = models.DateTimeField(auto_now=True, null=True)
    is_deleted = models.BooleanField(default=False)
    
    def __unicode__(self):
        return "Vote{" + \
    "id=" + str(self.pk) + \
    ", playlist_item='" + str(self.playlist_item) + '\'' + \
    ", consumer='" + str(self.consumer) + '\'' + \
    ", date_created=" + str(self.date_created) + \
    ", date_modified=" + str(self.date_modified) + \
    ", is_deleted=" + str(self.is_deleted) + \
    "}"
    
    def toDict(self):
        result = []
        result.append({"id": str(self.pk)})
        result.append({"playlist_item": self.playlist_item})
        result.append({"consumer": self.consumer})
        result.append({"date_created": str(self.date_created)})
        result.append({"date_modified": str(self.date_modified)})
        result.append({"is_deleted": str(self.is_deleted)})
        return simplejson.dumps(result)
