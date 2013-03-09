'''
Created on Mar 2, 2013

@author: Nick
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

class PlaylistService:
       
    def getPlaylistVotes(self, consumer, location_id):
        playlist_votes_list = []
        try:
            playlist = Playlist.objects.get(location_id = location_id, is_active = True)
            playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id, item_state__in=[2,1])
            vote_history = Vote.objects.filter(consumer = consumer.pk, playlist_item_id__in=playlist_items)
            for item in playlist_items:
                playlist_item_vote = PlaylistItemVotes()
                playlist_item_vote.playlist = playlist
                playlist_item_vote.pk = item.pk
                playlist_item_vote.music_track = item.music_track
                playlist_item_vote.votes = item.votes
                playlist_item_vote.rank_played = item.rank_played
                playlist_item_vote.current_ranking = item.current_ranking
                playlist_item_vote.date_created = item.date_created
                playlist_item_vote.date_modified = item.date_modified
                playlist_item_vote.is_deleted = item.is_deleted
                playlist_item_vote.item_state = item.item_state
                vote = vote_history.filter(playlist_item = item)
                if not vote:
                    playlist_item_vote.is_voted = False
                else:
                    playlist_item_vote.is_voted = True
 
                playlist_votes_list.append(playlist_item_vote)
                
        except (Playlist.DoesNotExist, KeyError):
            raise UnableToGetVoteHistoryError("Could not find playlist for: " + str(consumer.pk) 
                                              + " " + str(location_id))
        logger.debug("Found vote history and playlist items for " + str(consumer.pk) 
                                              + " " + str(location_id))
        
        return playlist_votes_list
        

    def addToPlaylist(self, device_id, location_id, music_track_id):
        
        try :
            location = Location.objects.get(pk=location_id)
            music_track = MusicTrack.objects.get(pk=music_track_id)
        except KeyError:
            raise UnableToAddMusicError("Could not add music track " + str(music_track_id) + ", location " + str(location_id) + ", consumer " + str(device_id))
        except Location.DoesNotExist:
            raise UnableToAddMusicError("Could not find location for id " + str(location_id))
        except Consumer.DoesNotExist:
            raise UnableToAddMusicError("Could not find consumer for device id " + str(device_id))
        except MusicTrack.DoesNotExist:
            raise UnableToAddMusicError("Could not find music track for id " + str(music_track_id))
        
        # Fetch current playlist for that location.
        try:
            playlist = Playlist.objects.get(location_id=location.id, is_active = True)
        except Playlist.DoesNotExist:
            raise UnableToAddMusicError("Could not find playlist for location " + str(location.id))
        playlist_items = PlaylistItem.objects.filter(playlist_id = playlist.id, music_track_id = music_track_id)
        
        # TODO: verify that this is redundant. Remove.
        if playlist == None:
            raise PlaylistNotFoundError("Could not find playlist for location id: " + str(location_id))
        
        # Check if current playlist already has that track. Raise exception if so.
        for playlist_item in playlist_items:
            if playlist_item.music_track == music_track and playlist_item.item_state > 0:
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
        