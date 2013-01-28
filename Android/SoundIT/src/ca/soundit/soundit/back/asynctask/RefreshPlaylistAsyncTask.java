package ca.soundit.soundit.back.asynctask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;

import android.os.AsyncTask;
import android.provider.Settings;

public class RefreshPlaylistAsyncTask extends
		AsyncTask<Void, Void, String> {

	private SongListActivity mSongListActivity;
	
	public RefreshPlaylistAsyncTask(SongListActivity activity){
		mSongListActivity = activity;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		//paramsTable.put(Constants.QUERY_API_KEY, Constants.API_KEY);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_LOCATION_ID_KEY, "1");
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_REFRESH_PLAYLIST, paramsTable);
		
		SoundITApplication myApplication = (SoundITApplication) mSongListActivity.getApplication();
		if (result != null)
		{
			StringBuilder sb = new StringBuilder();
			try {
				JSONArray jsonArray = new JSONArray(result);
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
					else if (song.getState() == Constants.STATE_TO_BE_PLAYED)
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
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mSongListActivity.notifiyRefresh(result);
    }

}
