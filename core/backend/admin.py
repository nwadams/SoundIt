'''
Created on Feb 2, 2013

@author: anuj
'''

from django.contrib import admin
from backend.models import Consumer
from backend.models import Location
from backend.models import Artist
from backend.models import Album
from backend.models import MusicCategory
from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
from backend.models import Vote
from backend.models import LocationMap
 

admin.site.register(Consumer)
admin.site.register(Location)
admin.site.register(LocationMap)
admin.site.register(Artist)
admin.site.register(Album)
admin.site.register(MusicCategory)
admin.site.register(MusicTrack)
admin.site.register(Playlist)
admin.site.register(PlaylistItem)
admin.site.register(Vote)
