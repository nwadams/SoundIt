package ca.soundit.soundit.back.asynctask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.activities.AddSongActivity;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;

public class GetLibraryAsyncTask extends
		AsyncTask<Void, Void, List<Song>> {

	private AddSongActivity mAddSongActivity;
	
	public GetLibraryAsyncTask(AddSongActivity activity){
		mAddSongActivity = activity;
	}
	
	@Override
	protected List<Song> doInBackground(Void... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mAddSongActivity.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_GET_LIBRARY, paramsTable);
		
		if (result != null)
		{
			try {
				JSONArray jsonArray = new JSONArray(result);
				List<Song> songList = new ArrayList<Song>();
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					Song song = new Song();
					song.setMusicTrackID(json.getInt(Constants.JSON_PK));

					JSONObject fields = json.getJSONObject(Constants.JSON_FIELDS);
					
					if (!Constants.JSON_NONE.equals(fields.getString(Constants.JSON_TRACK_URL)))
						song.setAlbumURL(fields.getString(Constants.JSON_TRACK_URL));
					
					song.setName(fields.getString(Constants.JSON_TRACK_NAME));
					
					JSONObject album = fields.getJSONObject(Constants.JSON_ALBUM);
					album = album.getJSONObject(Constants.JSON_FIELDS);
					song.setAlbum(album.getString(Constants.JSON_ALBUM_NAME));
					
					if (!Constants.JSON_NONE.equals(album.getString(Constants.JSON_ALBUM_URL)))
						song.setAlbumURL(album.getString(Constants.JSON_ALBUM_URL));
					
					JSONObject category = fields.getJSONObject(Constants.JSON_CATEOGORY);
					category = category.getJSONObject(Constants.JSON_FIELDS);
					song.setCategory(category.getString(Constants.JSON_CATEGORY_NAME));
					
					JSONObject artist = fields.getJSONObject(Constants.JSON_ARTIST);
					artist = artist.getJSONObject(Constants.JSON_FIELDS);
					song.setArtist(artist.getString(Constants.JSON_ARTIST_NAME));
					
					songList.add(song);
					
				}
				
				return songList;
			} catch (JSONException e) {
				
			}	
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(List<Song> result)
    {
		SoundITApplication.getInstance().setSongLibrary(result);
		mAddSongActivity.notifiyRefresh(result);
    }

}
