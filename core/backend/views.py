from django.http import HttpResponse
from django.utils import simplejson
from django.http import QueryDict
from django.shortcuts import render
import utils
from services.consumer_service import ConsumerService
from services.location_service import LocationService
from services.venue_service import VenueService
from services.voting_service import VotingService
from services.playlist_service import PlaylistService
from django.core import serializers
from django.db.models import Q
from models import PlaylistItem
from models import MusicCategory
from models import MusicTrack
from models import Location
import json
import logging
import random
from xcptions.Errors import InvalidDeviceError
from xcptions.Errors import UnableToVoteError
from xcptions.Errors import PlaylistNotFoundError
from xcptions.Errors import UnableToAddMusicError
from xcptions.Errors import InvalidUserError
from xcptions.Errors import InvalidLocationError
from xcptions.Errors import MusicTrackNotFoundError

logger = logging.getLogger('core.backend')

def signUp(request):
    consumer = None
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    device_id = params.get('device_id', None)
    if not device_id:
        error = utils.internalServerErrorResponse("Invalid request: Device Id required for sign up.")
        logger.warning("Invalid request: Device Id required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
   
    logger.info("Incoming request- sign up with credentials: " + str(device_id))

    auth_token = params.get('auth_token', None)
    if not auth_token:
        password = params.get('password', '')
        email_address = params.get('email', '')
        name = params.get('name', '')
        consumer = consumer_service.register(device_id, password, email_address, name)
    else:
        token_type = params.get('token_type', None)
        facebook_id = params.get('facebook_id', '')
        if not token_type:
            error = utils.internalServerErrorResponse("Invalid token type")
            logger.error("Invalid token type")
            return HttpResponse(simplejson.dumps(error), mimetype='application/json')
        
        consumer = consumer_service.register_with_token(device_id, auth_token, token_type, facebook_id) 
    
    consumer_list = []
    consumer_list.append(consumer)
    
    json = serializers.serialize("json", consumer_list, fields=('id','device_id','api_token','email_address', 'name'))
    json_obj = json[1:len(json)-1]
    
    return HttpResponse(json_obj, mimetype='application/json')

def login(request):
    consumer = None
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    device_id = params.get('device_id', None)
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    if not device_id:
        error = utils.internalServerErrorResponse("Invalid request: Device Id, user_id and api_key required for login.")
        logger.warning("Invalid request: Device Id, user_id and api_key required for login.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
   
    logger.info("Incoming request- login credentials: " + str(device_id) + ' ' + str(user_id) + ' ' + str(api_token))
    
    consumer = consumer_service.login(device_id, user_id, api_token)
    consumer_list = []
    consumer_list.append(consumer)
    
    json = serializers.serialize("json", consumer_list, fields=('id','device_id','api_token','email_address', 'name'))
    json_obj = json[1:len(json)-1]
    
    return HttpResponse(json_obj, mimetype='application/json')
    
def getCategories(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    if not consumer_service.isValidUser(user_id, api_token):
        logger.warn("Not using proper user_id and api_token")
        #raise InvalidUserError(user_id)
        
    return HttpResponse(HttpResponse(serializers.serialize("json", MusicCategory.objects.all(), fields=('name')), mimetype='application/json'))
   
def getLocations(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    if not consumer_service.isValidUser(user_id, api_token):
        logger.warn("Not using proper user_id and api_token")
        #raise InvalidUserError(user_id)
    
    return HttpResponse(HttpResponse(serializers.serialize("json", Location.objects.all().filter(is_active = True), fields=('pk', 'name', 'location', 'phone_number')), mimetype='application/json'))

def checkInLocation(request):
    location = None
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    location_id = params.get('location_id', None)
    if not location_id:
        raise InvalidLocationError(location_id)
    
    consumer = consumer_service.isValidUser(user_id, api_token)
    
    if not consumer:
        raise InvalidUserError(user_id)
    
    try: 
        location = Location.objects.get(pk=location_id)
    except (KeyError, Location.DoesNotExist):
        raise InvalidLocationError(location_id)
    
    if not location.is_active:
        raise InvalidLocationError(location_id)
    
    location_service = LocationService()   
    location_service.checkIn(consumer, location)
    
    return __refreshPlaylistHelper__(consumer, location_id)

def checkOutLocation(request):
    location = None
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
        
    consumer = consumer_service.isValidUser(user_id, api_token)
    
    if not consumer:
        raise InvalidUserError(user_id)
    
    location_service = LocationService()   
    location_service.checkOut(consumer)

    response_data = {}
    response_data['status'] = 200
    
    return HttpResponse(HttpResponse(json.dumps(response_data), mimetype='application/json')) 

def addToPlaylist(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    location_service = LocationService()
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    location_id = params.get('location_id', None)
    music_track_id = params.get('music_track_id', None)
    
    if not location_id:
        raise InvalidLocationError(location_id)
    
    if not music_track_id:
        raise MusicTrackNotFoundError(music_track_id)
        
    consumer = consumer_service.isValidUser(user_id, api_token)
    
    if not consumer:
        raise InvalidUserError(user_id)
    
    if not location_service.isActive(location_id):
        response_data = {}
        location_service.checkOut(consumer)
        response_data['message'] = 'Location is not active'
        response_data['checked_out'] = True
        response_data['status'] = 200
        
        return HttpResponse(HttpResponse(json.dumps(response_data), mimetype='application/json'))
    
    logger.info("Incoming request- add to playlist with parameters device_id " + str(user_id) + ", music_track_id " + str(music_track_id) + ", location_id " + str(location_id))

    playlist_service = PlaylistService()
    
    try: 
        playlist_service.addToPlaylist(consumer, location_id, music_track_id)
        logger.info("added music track " + str(music_track_id) + " to playlist for location " + str(location_id))
    # Improve exception handling.
    except (KeyError, PlaylistNotFoundError, UnableToAddMusicError) as exception:
        error = utils.internalServerErrorResponse(exception.value)
        logger.error(exception.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    
    return __refreshPlaylistHelper__(consumer, location_id)

def voteUp(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    location_service = LocationService()
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    location_id = params.get('location_id', None)
    music_track_id = params.get('music_track_id', None)
    
    if not location_id:
        raise InvalidLocationError(location_id)
    
    if not music_track_id:
        raise MusicTrackNotFoundError(music_track_id)
        
    consumer = consumer_service.isValidUser(user_id, api_token)
    
    if not consumer:
        raise InvalidUserError(user_id)
    
    if not location_service.isActive(location_id):
        response_data = {}
        location_service.checkOut(consumer)
        response_data['message'] = 'Location is not active'
        response_data['checked_out'] = True
        response_data['status'] = 200
        
        return HttpResponse(HttpResponse(json.dumps(response_data), mimetype='application/json'))
    
    logger.info("Incoming request- vote up with parameters user_id " + str(user_id) + ", location_id " + str(location_id) + ", music_track_id " + str(music_track_id))
    voting_service = VotingService()
    
    try: 
        voting_service.voteUp(consumer, location_id, music_track_id)
        logger.info("Updated playlist after vote up for music track " + str(music_track_id) + " at location " + str(location_id) + " from user " + str(user_id))
    except UnableToVoteError as utv:
        error = utils.internalServerErrorResponse(utv.value)
        logger.error(utv.value)
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    
    return __refreshPlaylistHelper__(consumer, location_id)


def refreshPlaylist(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    location_service = LocationService()
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    location_id = params.get('location_id', None)
    
    if not location_id:
        raise InvalidLocationError(location_id)
        
    consumer = consumer_service.isValidUser(user_id, api_token)
    
    if not consumer:
        raise InvalidUserError(user_id)
    
    if not location_service.isActive(location_id):
        response_data = {}
        location_service.checkOut(consumer)
        response_data['message'] = 'Location is not active'
        response_data['checked_out'] = True
        response_data['status'] = 200
        
        return HttpResponse(HttpResponse(json.dumps(response_data), mimetype='application/json'))
    
    location_map = location_service.getLocationMap(consumer)
    if not location_map:
        logger.warn("user with user_id" + str(user_id) + " not checked in")
        
    if location_map and str(location_map.location.pk) != location_id:
        logger.warn("user with user_id" + str(user_id) + " not checked in to correct location: " + location_id)

    logger.info("Incoming request- refresh playlist with parameters device_id " + str(user_id) + ", location_id " + str(location_id))
    # Use location id to fetch current playlist
    return __refreshPlaylistHelper__(consumer, location_id)

def __refreshPlaylistHelper__(consumer, location_id):
    playlist_service = PlaylistService()
    playlist_items = playlist_service.getPlaylistVotes(consumer, location_id)
    playlist_items_sorted = __reorderPlaylistVotes__(playlist_items)
    return HttpResponse(serializers.serialize("json", playlist_items_sorted, 
        fields=('pk', 'playlist', 'music_track', 'name',  'is_voted', 'votes', 'item_state'), 
        relations={'music_track':{'fields': ('name', 'track_URL', 'artist', 'category', 'album'),
        'relations': {'album' : {'fields':('name', 'image_URL')}, 'category'  : {'fields':('name')}, 
        'artist'  : {'fields':('name')},}}}), mimetype='application/json')
       
def __reorderPlaylistVotes__(playlist_items):
    
    reordered_list = []
    for item in playlist_items:
        if item.item_state == 1:
            reordered_list.append(item)
            break
    
    playlist_items = sorted(playlist_items, key=lambda PlaylistItemVotes: PlaylistItemVotes.votes, reverse=True)
    if len(reordered_list) == 1:
        for item in playlist_items:
            if item != reordered_list[0]:
                reordered_list.append(item)
    else:
        reordered_list = playlist_items
    return reordered_list

def getSongLibrary(request):
    params = None
    if (request.method == 'GET'):
        params = request.GET
    elif request.method == 'POST':
        params = request.POST
    
    consumer_service = ConsumerService()
    
    user_id = params.get('user_id', None)
    api_token = params.get('api_key', None)
    
    if not consumer_service.isValidUser(user_id, api_token):
        logger.warn("Not using proper user_id and api_token")
        #raise InvalidUserError(user_id)
    
    popular = params.get('popular', False)
    search_string = params.get('search_string', None)
    category = params.get('category', None)
    
    logger.info("Incoming request- get library with parameters device_id " + str(user_id))

    library = None
    kwargs = {}
        
    if popular:
        kwargs['is_popular'] = True
        
    if search_string:
        kwargs['name__icontains'] = search_string
        
    if category:
        kwargs['category_id'] = category

    if len(kwargs.keys()) > 0:
        library = MusicTrack.objects.filter(**kwargs)
    else:
        library = MusicTrack.objects.all()
        
    library = sorted(library, key=lambda MusicTrack: MusicTrack.name)
      
    return HttpResponse(serializers.serialize("json", library, fields= ('name', 'track_URL', 'artist', 'category', 'album'),
        relations={'album' : {'fields':('name', 'image_URL')}, 'category'  : {'fields':('name')}, 
        'artist'  : {'fields':('name')},}), mimetype='application/json')

def remove_items_in_playlist(library, upcoming_playlist):
    
    new_playlist = []
    playlist_tracks = []
    for item in upcoming_playlist:
        playlist_tracks.append(item.music_track)
    for library_item in library:
        if library_item not in playlist_tracks and library_item not in new_playlist:
            new_playlist.append(library_item)
    return new_playlist
                
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


def venueGetNextSong(request):
    venue_service = VenueService()
    result =  venue_service.updateCurrentPlaying()
    return HttpResponse(result.music_track.name)

def getFormattedLibrary(request):
    library = __requestLibrary__(request)
    context = {'library': library}
    return render(request, 'backend/snippets/library.html', context)

def __requestLibrary__(request):
    #error = utils.internalServerErrorResponse("Error, disabled")
    #return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    try:
        device_id = request.GET['device_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for requesting library.")
        logger.warning("Invalid request: Device Id and Location Id required for requesting library.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')

    logger.info("Incoming request- get library with parameters device_id " + str(device_id) + ", location_id " + str(location_id))
    library = MusicTrack.objects.all()
    upcoming_playlist = PlaylistItem.objects.filter(Q(item_state = 2) | Q(item_state=1))
    return remove_items_in_playlist(library, upcoming_playlist)


def index(request):
    music_tracks = MusicTrack.objects.all()
    playlist_items = PlaylistItem.objects.all()
    context = {'music_track_list': music_tracks, 'playlist_item_list': playlist_items}
    return render(request, 'backend/index.html', context)

def refreshPlaylistFormatted(request):
    playlist_items = __refreshPlaylist__(request)
    context = {'playlist_items': playlist_items}
    return render(request, 'backend/snippets/playlist.html', context)

def __refreshPlaylist__(request):
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and Location Id required for refreshing playlist.")
        logger.warning("Invalid request: Device Id and Location Id required for refreshing playlist.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    logger.info("Incoming request- refresh playlist with parameters device_id " + str(device_id) + ", location_id " + str(location_id))
    # Use location id to fetch current playlist
    return __reorderPlaylist__(PlaylistItem.objects.all())

def __reorderPlaylist__(playlist_items):
    
    reordered_list = []
    for item in playlist_items:
        if item.item_state == 1:
            reordered_list.append(item)
            break
    
    playlist_items = sorted(playlist_items, key=lambda PlaylistItem: PlaylistItem.votes, reverse=True)
    if len(reordered_list) == 1:
        for item in playlist_items:
            if item != reordered_list[0]:
                reordered_list.append(item)
    else:
        reordered_list = playlist_items
    return reordered_list
