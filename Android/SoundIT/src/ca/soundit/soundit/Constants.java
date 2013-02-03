package ca.soundit.soundit;

public class Constants {
	
	public static final String TAG = "SoundIT";

	public static final int REFRESH_INTERVAL = 60;
	
	public static final String URL_ROOT = "http://api.soundit.ca/backend/";
	public static final String URL_REFRESH_PLAYLIST = "refreshPlaylist/";
	public static final String URL_SIGNUP = "signUp/";
	public static final String URL_GET_LIBRARY = "getLibrary/";
	public static final String URL_ADD_TO_PLAYLIST = "addToPlaylist/";
	public static final String URL_GET_VOTE_HISTORY = "getVoteHistory/";
	public static final String URL_VOTE_UP = "voteUpAndroid/";
	
	public static final String API_DEVICE_ID_KEY = "device_id";
	public static final String API_LOCATION_ID_KEY = "location_id";
	public static final String API_PASSWORD = "password";
	public static final String API_MUSIC_TRACK_ID = "music_track_id";
	
	public static final String JSON_ERROR_MESSAGE = "Error Message";
	public static final String JSON_FIELDS = "fields";
	public static final String JSON_CURRENT_RANKING = "current_ranking";
	public static final String JSON_ITEM_STATE = "item_state";
	public static final String JSON_VOTES = "votes";
	public static final String JSON_MUSIC_TRACK = "music_track";
	public static final String JSON_TRACK_URL = "track_URL";
	public static final String JSON_TRACK_NAME = "name";
	public static final String JSON_ALBUM = "album";
	public static final String JSON_ALBUM_NAME = "name";
	public static final String JSON_ALBUM_URL = "image_URL";
	public static final String JSON_CATEOGORY = "category";
	public static final String JSON_CATEGORY_NAME = "name";
	public static final String JSON_ARTIST = "artist";
	public static final String JSON_ARTIST_NAME = "name";
	public static final String JSON_NONE = "none";
	public static final String JSON_PK = "pk";
	
	public static final int STATE_CURRENT_PLAYING = 1;
	public static final int STATE_TO_BE_PLAYED = 2;
	
	public static final String OK = "ok";
	
	public static final String PREFS_NAME = "USER_DATA_PREFS";
	public static final String PREFS_PASSWORD = "password";
}
