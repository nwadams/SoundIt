from django.http import HttpResponse
from django.utils import simplejson
import utils
from services.user_service import UserService
from services.voting_service import VotingService
from django.core import serializers
import json
from models import User
from models import PlaylistItem

def signUp(request):
    
    try: 
        device_id = request.GET['device_id']
        password = request.GET['password'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')
    
    # Check if user is already signed up
    
    user_service = UserService()
    new_user = user_service.register(device_id, password)
    return HttpResponse(serializers.serialize("json", [new_user]), mimetype='application/json')

# Login is deprecated for now.
#def login(request):
    
#    try: 
#        user_id = request.GET['user_id']
#        password = request.GET['password']
#    except KeyError:
#        return HttpResponse(utils.internalServerErrorResponse("Invalid request: User id and password required for sign in."), mimetype='application/json')
    
#    user_service = UserService()
#    authenticated = user_service.login(user_id, password)    
    # Return playlist instead of just success.
#    result = []
#    result.append({"Result": ("SUCCESS" if authenticated else "FAIL")})
#    return HttpResponse(simplejson.dumps(result), mimetype='application/json')

def addToPlaylist(request):
    
    try:
        user_id = request.GET['user_id']
        music_track_id = request.GET['music_track_id']
        location_id = request.GET['location_id']
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: User id and track id required for adding to playlist.")
        return HttpResponse( simplejson.dumps(error), mimetype='application/json')
    
    voting_service = VotingService()
    updated_playlist = voting_service.addToPlaylist(user_id, location_id, music_track_id)
    result = []
    result.append({"playlist": updated_playlist.toDict()})
    return HttpResponse(simplejson.dumps(result), mimetype='application/json')

def voteUp(request):
    return HttpResponse("request")

def refreshPlaylist(request):
    
    try: 
        device_id = request.GET['device_id']
        location_id = request.GET['location_id'] 
    except KeyError:
        error = utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up.")
        return HttpResponse(simplejson.dumps(error), mimetype='application/json')

    # Use location id to fetch current playlist
    return HttpResponse(serializers.serialize("json", PlaylistItem.objects.all(), relations={'music_track':{'relations': ('album', 'category', 'artist', )},}), mimetype='application/json')

def getLibrary(request):
    return HttpResponse("get Library")

def getVoteHistory(request):
    return HttpResponse("get Vote History")