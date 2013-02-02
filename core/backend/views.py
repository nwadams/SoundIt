from django.http import HttpResponse
from django.utils import simplejson
import utils
from services.user_service import UserService
from services.voting_service import VotingService
from django.core import serializers
from models import PlaylistItem
from models import MusicTrack
import logging
from xcptions.Errors import InvalidDeviceError
from xcptions.Errors import UnableToVoteError
from xcptions.Errors import PlaylistNotFoundError

logger = logging.getLogger('core.backend')

def signUp(request):
    
    try: 
        device_id = request.GET['device_id']
        password = request.GET['password'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    
    print "Sign up request with credentials: " + device_id + "/" + password
    
    # Check if user is already signed up
    user_service = UserService()
    is_current_user = user_service.checkIfCurrentUser(device_id)
    if is_current_user:
        is_logged_in = user_service.login(device_id, password)
        if is_logged_in:
            print "User logged in, returning playlist to device " + str(device_id)
        else:
            # TODO: this should return false. but returning playlist right now since iOS will not be able to handle it. Will deal with dupes later.
            print "User login failed, returning playlist to device " + str(device_id)
    else:
        print "Setting up new user, returning playlist to device " + str(device_id)
        user_service.register(device_id, password)
    return HttpResponse(serializers.serialize("json", PlaylistItem.objects.all(), relations={'music_track':{'relations': ('album', 'category', 'artist', )},}), mimetype='application/json')



def addToPlaylist(request):
    
    # Check input parameters.
    try:
        device_id = request.GET['device_id']
        music_track_id = request.GET['music_track_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: User id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    
    voting_service = VotingService()
    try: 
        updated_playlist_items = voting_service.addToPlaylist(device_id, location_id, music_track_id)
    except (KeyError, PlaylistNotFoundError) as pnf:
        error = utils.internalServerErrorResponse(pnf.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", updated_playlist_items, relations={'music_track': {'relations': ('album', 'category', 'artist', )}}), mimetype='application/json')


def voteUp(request):
    
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
        music_track_id = request.GET['music_track_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: User id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    
    voting_service = VotingService()
    try: 
        updated_playlist_items = voting_service.voteUp(device_id, location_id, music_track_id)
    except UnableToVoteError as utv:
        error = utils.internalServerErrorResponse(utv.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", updated_playlist_items, relations={'music_track':{'relations': ('album', 'category', 'artist', )}}), mimetype='application/json')



def refreshPlaylist(request):
    
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for refreshing playlist.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')

    # Use location id to fetch current playlist
    return HttpResponse(serializers.serialize("json", PlaylistItem.objects.all(), relations={'music_track':{'relations': ('album', 'category', 'artist', )},}), mimetype='application/json')



def getLibrary(request):
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for requesting library.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", MusicTrack.objects.all(), relations={'album', 'category', 'artist'}), mimetype='application/json')



def getVoteHistory(request):
    
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')

    voting_service = VotingService()
    try:
        votes = voting_service.getVoteHistory(device_id)
    except InvalidDeviceError as ide:
        error = utils.internalServerErrorResponse(ide.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    playlist_item_list = []
    for vote in votes:
        playlist_item_list.append(vote.playlist_item)
    return HttpResponse(serializers.serialize("json", playlist_item_list, relations={'music_track': {'relations': ('album', 'category', 'artist')}}), mimetype='application/json')