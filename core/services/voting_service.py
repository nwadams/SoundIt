'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
from backend.models import User
from backend.models import Location
from backend.models import Vote
from xcptions.track_already_in_playlist_exception import TrackAlreadyInPlaylistException
from xcptions.playlist_not_found_exception import PlaylistNotFoundException
from xcptions.invalid_device_exception import InvalidDeviceException
from xcptions.unable_to_vote_exception import UnableToVoteException
import random

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
        
    def voteUp(self, device_id, location_id, music_track_id):
        
        try:
            user = User.objects.get(device_id=device_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
            # hack for ios: thepit
            if location_id == "thepit":
                location = Location.objects.get(pk=1)
            else:
                location = Location.objects.get(pk=location_id)
        except (KeyError, Location.DoesNotExist, User.DoesNotExist, MusicTrack.DoesNotExist):
            raise UnableToVoteException("Could not find objects for parameters- device_id: " + device_id + ", location_id: " + location_id + ", music_track_id: " + music_track_id)
        
        # Fetch playlist and playlist item from location and music_track
        playlist = Playlist.objects.get(location_id=location.id)
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        
        # Vote that music track up
        for playlist_item in playlist_items:
            if playlist_item.music_track.id == music_track.id:
                playlist_item.votes += 1
                playlist_item.save()
                vote = Vote(playlist_item_id=playlist_item.id, user_id=user.id)
                vote.save()
        
        # Sort playlist by votes in descending order.
        sorted_playlist_items = sorted(playlist_items, key=lambda PlaylistItem: PlaylistItem.votes, reverse=True)
        
        return sorted_playlist_items 
    
    def getVoteHistory(self, device_id):
        try:
            user = User.objects.get(device_id = device_id)
        except (KeyError, User.DoesNotExist):
            raise InvalidDeviceException("Could not find user for device " + device_id)
        return Vote.objects.filter(user_id = user.id)
        
            