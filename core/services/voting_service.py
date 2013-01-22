'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
from backend.models import User
from backend.models import Location
from xcptions.track_already_in_playlist_exception import TrackAlreadyInPlaylistException
from xcptions.playlist_not_found_exception import PlaylistNotFoundException

class VotingService:
    
    
    def addToPlaylist(self, user_id, location_id, music_track_id):
        
        try :
            user = User.objects.get(pk=user_id)        
            location = Location.objects.get(pk=location_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
        except KeyError:
            raise
        
        # Fetch current playlist for that location.
        # TODO: This is incorrect. Needs to be fixed.
        current_playlist = Playlist.objects.get(pk=1)
        
        if current_playlist == None:
            raise PlaylistNotFoundException("Could not find playlist for location id: " + location_id)
        
        # Check if current current_playlist already has that track. Raise exception if so.
        playlist_items = current_playlist.playlist_item_set.all()
        for playlist_item in playlist_items:
            if playlist_item.music_track == music_track:
                raise TrackAlreadyInPlaylistException("Music track: " + music_track_id + " is already in current_playlist: " + current_playlist.id)
        
        # If not already in current_playlist, add it.
        new_playlist_item = PlaylistItem()
        new_playlist_item.playlist = current_playlist
        new_playlist_item.music_track = music_track
        # Assuming playlist starts from 1 and goes to n. Not 0 to n-1.
        new_playlist_item.current_ranking = len(playlist_items) + 1
        new_playlist_item.save()
        
        return current_playlist.playlist_item_set.all()
        
    def voteUp(self):
        return
        