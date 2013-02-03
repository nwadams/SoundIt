from django.http import HttpResponse
from django.utils import simplejson
import utils
from services.customer_service import CustomerService
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
        logger.warning("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    logger.info("Incoming request- sign up with credentials: " + str(device_id) + "/ " + password)
    
    # Check if customer is already signed up
    customer_service = CustomerService()
    is_current_customer = customer_service.checkIfCurrentCustomer(device_id)
    if is_current_customer:
        is_logged_in = customer_service.login(device_id, password)
        if is_logged_in:
            logger.info("Customer logged in, returning playlist to device " + str(device_id))
        else:
            # TODO: this should return false. but returning playlist right now since iOS will not be able to handle it. Will deal with dupes later.
            logger.info("Customer login failed, returning playlist to device " + str(device_id))
    else:
        logger.info("Setting up new customer, returning playlist to device " + str(device_id))
        customer_service.register(device_id, password)
    return HttpResponse(serializers.serialize("json", PlaylistItem.objects.all(), relations={'music_track':{'relations': ('album', 'category', 'artist', )},}), mimetype='application/json')



def addToPlaylist(request):
    
    # Check input parameters.
    try:
        device_id = request.GET['device_id']
        music_track_id = request.GET['music_track_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Customer id, location id and track id required for adding to playlist.")
        logger.warning("Invalid request: Customer id, location id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    logger.info("Incoming request- add to playlist with parameters device_id " + str(device_id) + ", music_track_id " + str(music_track_id) + ", location_id " + str(location_id))
    voting_service = VotingService()
    try: 
        updated_playlist_items = voting_service.addToPlaylist(device_id, location_id, music_track_id)
        logger.info("added music track " + str(music_track_id) + " to playlist for location " + str(location_id))
    # Improve exception handling.
    except (KeyError, PlaylistNotFoundError) as pnf:
        error = utils.internalServerErrorResponse(pnf.value)
        logger.error(pnf.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", updated_playlist_items, relations={'music_track': {'relations': ('album', 'category', 'artist', )}}), mimetype='application/json')


def voteUpAndroid(request):
    
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
        music_track_id = request.GET['music_track_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Customer id and track id required for adding to playlist.")
        logger.warning("Invalid request: Customer id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    
    logger.info("Incoming request- vote up with parameters device_id " + str(device_id) + ", location_id " + str(location_id) + ", music_track_id " + str(music_track_id))
    voting_service = VotingService()
    try: 
        updated_playlist_items = voting_service.voteUp(device_id, location_id, music_track_id)
        logger.info("Updated playlist after vote up for music track " + str(music_track_id) + " at location " + str(location_id) + " from device " + str(device_id))
    except UnableToVoteError as utv:
        error = utils.internalServerErrorResponse(utv.value)
        logger.error(utv.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", updated_playlist_items, relations={'music_track':{'relations': ('album', 'category', 'artist', )}}), mimetype='application/json')


def voteUp(request):
    
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
        music_track_id = request.GET['music_track_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Customer id and track id required for adding to playlist.")
        logger.warning("Invalid request: Customer id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    
    music_track_id += 1
    logger.info("Incoming request- vote up with parameters device_id " + str(device_id) + ", location_id " + str(location_id) + ", music_track_id " + str(music_track_id))
    voting_service = VotingService()
    try: 
        updated_playlist_items = voting_service.voteUp(device_id, location_id, music_track_id)
        logger.info("Updated playlist after vote up for music track " + str(music_track_id) + " at location " + str(location_id) + " from device " + str(device_id))
    except UnableToVoteError as utv:
        error = utils.internalServerErrorResponse(utv.value)
        logger.error(utv.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    return HttpResponse(serializers.serialize("json", updated_playlist_items, relations={'music_track':{'relations': ('album', 'category', 'artist', )}}), mimetype='application/json')




def refreshPlaylist(request):
    
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for refreshing playlist.")
        logger.warning("Invalid request: Device Id and Location Id required for refreshing playlist.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    logger.info("Incoming request- refresh playlist with parameters device_id " + str(device_id) + ", location_id " + str(location_id))
    # Use location id to fetch current playlist
    return HttpResponse(serializers.serialize("json", PlaylistItem.objects.all(), relations={'music_track':{'relations': ('album', 'category', 'artist', )},}), mimetype='application/json')



def getLibrary(request):
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for requesting library.")
        logger.warning("Invalid request: Device Id and Location Id required for requesting library.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    logger.info("Incoming request- get library with parameters device_id " + str(device_id) + ", location_id " + str(location_id))
    return HttpResponse(serializers.serialize("json", MusicTrack.objects.all(), relations={'album', 'category', 'artist'}), mimetype='application/json')



def getVoteHistory(request):
    
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up.")
        logger.warning("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')

    logger.info("Incoming request- get vote history with parameters device_id " + str(device_id) + ", location_id " + str(location_id))
    voting_service = VotingService()
    try:
        votes = voting_service.getVoteHistory(device_id)
    except InvalidDeviceError as ide:
        error = utils.internalServerErrorResponse(ide.value)
        logger.error(ide.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    playlist_item_list = []
    for vote in votes:
        playlist_item_list.append(vote.playlist_item)
    return HttpResponse(serializers.serialize("json", playlist_item_list, relations={'music_track': {'relations': ('album', 'category', 'artist')}}), mimetype='application/json')


