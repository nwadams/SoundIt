from django.http import HttpResponse
from django.utils import simplejson
import utils
from services.user_service import UserService
from services.voting_service import VotingService

def signUp(request):
    
    try: 
        device_id = request.GET['device_id']
        password = request.GET['password'] 
    except KeyError:
        return HttpResponse(utils.internalServerErrorResponse("Invalid request: Device Id and password required for sign up."), mimetype='application/json')
    
    user_service = UserService()
    new_user = user_service.register(device_id, password)
    
    # Returning user object right now so that app can store user_id. But must also return playlist.
    result = []
    result.append({"user": new_user.toJson()})
    return HttpResponse(simplejson.dumps(result), mimetype='application/json')

def login(request):
    
    try: 
        user_id = request.GET['user_id']
        password = request.GET['password']
    except KeyError:
        return HttpResponse(utils.internalServerErrorResponse("Invalid request: User id and password required for sign in."), mimetype='application/json')
    
    user_service = UserService()
    authenticated = user_service.login(user_id, password)    
    # Return playlist instead of just success.
    result = []
    result.append({"Result": ("SUCCESS" if authenticated else "FAIL")})
    return HttpResponse(simplejson.dumps(result), mimetype='application/json')

def addToPlaylist(request, location_id):
    
    try:
        user_id = request.GET['user_id']
        music_track_id = request.GET['music_track_id']
    except KeyError:
        return HttpResponse(utils.internalServerErrorResponse("Invalid request: User id and track id required for adding to playlist."), mimetype='application/json')
    
    voting_service = VotingService()
    updated_playlist = voting_service.addToPlaylist(user_id, location_id, music_track_id)
    result = []
    result.append({"playlist": updated_playlist.toJson()})
    return HttpResponse(simplejson.dumps(result), mimetype='application/json')

def voteUp(request):
    return HttpResponse("request")