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
import ca.soundit.soundit.fragments.AddSongFragment;

public class GetLibraryAsyncTask extends
		AsyncTask<String, Void, List<Song>> {

	private AddSongFragment mAddSongActivity;
	
	public GetLibraryAsyncTask(AddSongFragment activity){
		mAddSongActivity = activity;
	}
	
	@Override
	protected List<Song> doInBackground(String... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mAddSongActivity.getActivity().getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		
		if (params[0] != null) {
			paramsTable.put(Constants.API_SEARCH_STRING, params[0]);
		}
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
					
					song.setName(fields.getString(Constants.JSON_TRACK_NAME));
					
					JSONObject album = fields.getJSONObject(Constants.JSON_ALBUM);
					album = album.getJSONObject(Constants.JSON_FIELDS);
					song.setAlbum(album.getString(Constants.JSON_ALBUM_NAME));
					
					String albumURL = album.getString(Constants.JSON_ALBUM_URL);
					if (albumURL != null && !(Constants.JSON_NONE.equals(albumURL) || "".equals(albumURL) ))
						song.setAlbumURL(album.getString(Constants.JSON_ALBUM_URL));
					else 
						song.setAlbumURL(null);
					
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
