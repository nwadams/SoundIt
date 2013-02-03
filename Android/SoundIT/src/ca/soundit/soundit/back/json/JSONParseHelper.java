package ca.soundit.soundit.back.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.data.Song;

public class JSONParseHelper {

	public static String RefreshPlaylist(String jsonString, Activity activity) {
		SoundITApplication myApplication = (SoundITApplication) activity.getApplication();
		if (jsonString != null)
		{
			StringBuilder sb = new StringBuilder();
			try {
				JSONArray jsonArray = new JSONArray(jsonString);
				List<Song> songList = new ArrayList<Song>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					Song song = new Song();
					JSONObject fields = json.getJSONObject(Constants.JSON_FIELDS);
					
					song.setVotes(fields.getInt(Constants.JSON_VOTES));
					song.setState(fields.getInt(Constants.JSON_ITEM_STATE));
					song.setCurrentRanking(fields.getInt(Constants.JSON_CURRENT_RANKING));
					
					JSONObject musicTrack = fields.getJSONObject(Constants.JSON_MUSIC_TRACK);
					song.setMusicTrackID(musicTrack.getInt(Constants.JSON_PK));
					fields = musicTrack.getJSONObject(Constants.JSON_FIELDS);
					
					if (Constants.JSON_NONE.equals(fields.getString(Constants.JSON_TRACK_URL)))
						song.setTrackURL(fields.getString(Constants.JSON_TRACK_URL));
					
					song.setName(fields.getString(Constants.JSON_TRACK_NAME));
					
					JSONObject album = fields.getJSONObject(Constants.JSON_ALBUM);
					album = album.getJSONObject(Constants.JSON_FIELDS);
					song.setAlbum(album.getString(Constants.JSON_ALBUM_NAME));
					
					if (Constants.JSON_NONE.equals(album.getString(Constants.JSON_ALBUM_URL)))
						song.setAlbumURL(album.getString(Constants.JSON_ALBUM_URL));
					
					JSONObject category = fields.getJSONObject(Constants.JSON_CATEOGORY);
					category = category.getJSONObject(Constants.JSON_FIELDS);
					song.setCategory(category.getString(Constants.JSON_CATEGORY_NAME));
					
					JSONObject artist = fields.getJSONObject(Constants.JSON_ARTIST);
					artist = artist.getJSONObject(Constants.JSON_FIELDS);
					song.setArtist(artist.getString(Constants.JSON_ARTIST_NAME));
					
					if (song.getState() == Constants.STATE_CURRENT_PLAYING)
						myApplication.setCurrentPlayingSong(song);
					else if (song.getState() == Constants.STATE_TO_BE_PLAYED || song.getState() == 0) //debug
						songList.add(song);
				}
				
				myApplication.setSongQueue(songList);
			} catch (JSONException e) {
				sb.append(e.toString());
			}
			
			if (sb.length() == 0)
				sb.append(Constants.OK);
			
			return sb.toString();
		}
		
		return activity.getString(R.string.error_no_data);
	}
}
