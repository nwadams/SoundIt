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
from xcptions.Errors import TrackAlreadyInPlaylistError
from xcptions.Errors import PlaylistNotFoundError
from xcptions.Errors import InvalidDeviceError
from xcptions.Errors import UnableToVoteError
from xcptions.Errors import UnableToAddMusicError

class VotingService:
    
    
    def addToPlaylist(self, device_id, location_id, music_track_id):
        
        try :
            user = User.objects.get(device_id=device_id)
            if location_id == "thepit":
                location = Location.objects.get(pk=1)
            else:        
                location = Location.objects.get(pk=location_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
        except KeyError:
            raise UnableToAddMusicError("Could not add music track " + str(music_track_id) + ", location " + str(location_id) + ", user " + str(device_id))
        except Location.DoesNotExist:
            raise UnableToAddMusicError("Could not find location for id " + str(location_id))
        except User.DoesNotExist:
            raise UnableToAddMusicError("Could not find user for device id " + str(device_id))
        except MusicTrack.DoesNotExist:
            raise UnableToAddMusicError("Could not find music track for id " + str(music_track_id))
        
        # Fetch current playlist for that location.
        playlist = Playlist.objects.get(location_id=location.id)
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        current_playlist = Playlist.objects.get(pk=1)
        
        if playlist == None:
            raise PlaylistNotFoundError("Could not find playlist for location id: " + str(location_id))
        
        # Check if current current_playlist already has that track. Raise exception if so.
        for playlist_item in playlist_items:
            if playlist_item.music_track == music_track:
                raise TrackAlreadyInPlaylistError("Music track: " + str(music_track_id) + " is already in current_playlist: " + str(current_playlist.id))
        
        # If not already in current_playlist, add it.
        new_playlist_item = PlaylistItem()
        new_playlist_item.playlist = current_playlist
        new_playlist_item.music_track = music_track
        new_playlist_item.votes = 0
        new_playlist_item.rank_played = -1
        # Assuming playlist starts from 1 and goes to n. Not 0 to n-1.
        new_playlist_item.current_ranking = len(playlist_items) + 1
        new_playlist_item.item_state = 2
        new_playlist_item.save()
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        return playlist_items
        
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
            raise UnableToVoteError("Could not find objects for parameters- device_id: " + str(device_id) + ", location_id: " + str(location_id) + ", music_track_id: " + str(music_track_id))
        
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
            raise InvalidDeviceError("Could not find user for device " + str(device_id))
        return Vote.objects.filter(user_id = user.id)
        
            