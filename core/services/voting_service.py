'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
from backend.models import Customer
from backend.models import Location
from backend.models import Vote
import logging
from xcptions.Errors import TrackAlreadyInPlaylistError
from xcptions.Errors import PlaylistNotFoundError
from xcptions.Errors import InvalidDeviceError
from xcptions.Errors import UnableToVoteError
from xcptions.Errors import UnableToAddMusicError

logger = logging.getLogger('core.backend')

class VotingService:
    
    
    def addToPlaylist(self, device_id, location_id, music_track_id):
        
        try :
            # TODO: Improve data model so that customer that is adding playlist is checked for session.
            customer = Customer.objects.get(device_id=device_id)
            if location_id == "thepit":
                location = Location.objects.get(pk=1)
            else:        
                location = Location.objects.get(pk=location_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
        except KeyError:
            raise UnableToAddMusicError("Could not add music track " + str(music_track_id) + ", location " + str(location_id) + ", customer " + str(device_id))
        except Location.DoesNotExist:
            raise UnableToAddMusicError("Could not find location for id " + str(location_id))
        except Customer.DoesNotExist:
            raise UnableToAddMusicError("Could not find customer for device id " + str(device_id))
        except MusicTrack.DoesNotExist:
            raise UnableToAddMusicError("Could not find music track for id " + str(music_track_id))
        
        # Fetch current playlist for that location.
        try:
            playlist = Playlist.objects.get(location_id=location.id)
        except Playlist.DoesNotExist:
            raise UnableToAddMusicError("Could not find playlist for location " + str(location.id))
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        
        # TODO: verify that this is redundant. Remove.
        if playlist == None:
            raise PlaylistNotFoundError("Could not find playlist for location id: " + str(location_id))
        
        # Check if current playlist already has that track. Raise exception if so.
        for playlist_item in playlist_items:
            if playlist_item.music_track == music_track:
                raise TrackAlreadyInPlaylistError("Music track: " + str(music_track_id) + " is already in playlist: " + str(playlist.id))
        
        # If not already in playlist, add it.
        logger.debug("Creating new playlist item with music track " + str(music_track_id) + " for location " + str(location.id))
        new_playlist_item = PlaylistItem()
        new_playlist_item.playlist = playlist
        new_playlist_item.music_track = music_track
        new_playlist_item.votes = 0
        # TODO: This is weird. Fix.
        new_playlist_item.rank_played = -1
        # Assuming playlist starts from 1 and goes to n. Not 0 to n-1.
        new_playlist_item.current_ranking = len(playlist_items) + 1
        new_playlist_item.item_state = 2
        new_playlist_item.save()
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        return playlist_items
        
    def voteUp(self, device_id, location_id, music_track_id):
        
        try:
            customer = Customer.objects.get(device_id=device_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
            # hack for ios: thepit
            if location_id == "thepit":
                location = Location.objects.get(pk=1)
            else:
                location = Location.objects.get(pk=location_id)
            playlist = Playlist.objects.get(location_id=location.id)
            playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id)
        except KeyError:
            raise UnableToVoteError("Could not find objects for parameters- device_id: " + str(device_id) + ", location_id: " + str(location_id) + ", music_track_id: " + str(music_track_id))
        except Location.DoesNotExist:
            raise UnableToVoteError("Could not find location for id " + str(location_id))
        except Customer.DoesNotExist:
            raise UnableToVoteError("Could not find customer for id " + str(device_id))
        except MusicTrack.DoesNotExist:
            raise UnableToVoteError("Could not find music track for id " + str(music_track-id))
        except Playlist.DoesNotExist:
            raise UnableToVoteError("Could not find playlist for location " + str(location.id))
        
        # Vote that music track up
        voted = False
        for playlist_item in playlist_items:
            if playlist_item.music_track.id == music_track.id:
                try: 
                    Vote.objects.get(playlist_item_id = playlist_item.id, customer_id = customer.id)
                    logger.warning("Device " + str(device_id) + " has already voted for music track " + str(music_track_id))
                except Vote.DoesNotExist:
                    vote = Vote(playlist_item_id=playlist_item.id, customer_id=customer.id)
                    vote.save()
                    playlist_item.votes += 1
                    playlist_item.save()
                    logger.info("Voted for music track " + str(music_track_id))
                voted = True
                break
        
        if voted == False:
            logger.error("Could not find music track id " + str(music_track_id) + " at location " + str(location_id))
            raise UnableToVoteError("Could not find music track id " + str(music_track_id) + " at location " + str(location_id))
        # Sort playlist by votes in descending order.
        sorted_playlist_items = sorted(playlist_items, key=lambda PlaylistItem: PlaylistItem.votes, reverse=True)
        return sorted_playlist_items 
    
    def getVoteHistory(self, device_id):
        try:
            customer = Customer.objects.get(device_id = device_id)
        except (KeyError, Customer.DoesNotExist):
            # TODO: Consolidate this exception into something else. Or start using this elsewhere. 
            raise InvalidDeviceError("Could not find customer for device " + str(device_id))
        logger.debug("Found customer for device " + str(device_id) + ", returning votes.")
        return Vote.objects.filter(customer_id = customer.id)
        
            