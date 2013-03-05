'''
Created on Jan 21, 2013

@author: anuj
'''

from backend.models import MusicTrack
from backend.models import Playlist
from backend.models import PlaylistItem
from backend.data_transfer_models import PlaylistItemVotes
from backend.models import Consumer
from backend.models import Location
from backend.models import Vote
import logging
from xcptions.Errors import TrackAlreadyInPlaylistError
from xcptions.Errors import PlaylistNotFoundError
from xcptions.Errors import InvalidDeviceError
from xcptions.Errors import UnableToVoteError
from xcptions.Errors import UnableToAddMusicError
from xcptions.Errors import UnableToGetVoteHistoryError

logger = logging.getLogger('core.backend')

class VotingService:
    
    def voteUp(self, consumer, location_id, music_track_id):
        
        try:
            location = Location.objects.get(pk=location_id)
            playlist = Playlist.objects.get(location=location)
            playlist_item = PlaylistItem.objects.get(playlist_id = playlist.id,music_track_id= music_track_id)
        except KeyError:
            raise UnableToVoteError("Could not find objects for parameters- user_id: " + str(consumer.pk) + ", location_id: " + str(location_id) + ", music_track_id: " + str(music_track_id))
        except Location.DoesNotExist:
            raise UnableToVoteError("Could not find location for id " + str(location_id))
        except Consumer.DoesNotExist:
            raise UnableToVoteError("Could not find consumer for id " + str(consumer.pk))
        except Playlist.DoesNotExist:
            raise UnableToVoteError("Could not find playlist for location " + str(location.id))
        
        # Vote that music track up
        voted = False
        try: 
            Vote.objects.get(playlist_item_id = playlist_item.id, consumer = consumer)
            logger.warning("user " + str(consumer.pk) + " has already voted for music track " + str(music_track_id))
            raise UnableToVoteError("has already voted for music track " + str(music_track_id) + " at location " + str(location_id))
        except Vote.DoesNotExist:
            vote = Vote(playlist_item_id=playlist_item.id, consumer=consumer)
            vote.save()
            playlist_item.votes += 1
            playlist_item.save()
            logger.debug("Voted for music track " + str(music_track_id))
            voted = True
        
        if voted == False:
            logger.error("Could not find music track id " + str(music_track_id) + " at location " + str(location_id))
            raise UnableToVoteError("Could not find music track id " + str(music_track_id) + " at location " + str(location_id))

        return
    
    def getVoteHistory(self, device_id):
        try:
            consumer = Consumer.objects.get(device_id = device_id)
        except (KeyError, Consumer.DoesNotExist):
            # TODO: Consolidate this exception into something else. Or start using this elsewhere. 
            raise InvalidDeviceError("Could not find consumer for device " + str(device_id))
        logger.debug("Found consumer for device " + str(device_id) + ", returning votes.")
        return Vote.objects.filter(consumer = consumer.id)
        