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
            playlist = Playlist.objects.get(location_id = location_id)
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
        
