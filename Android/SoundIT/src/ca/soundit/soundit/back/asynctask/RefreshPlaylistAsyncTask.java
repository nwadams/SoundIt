package ca.soundit.soundit.back.asynctask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.back.json.JSONParseHelper;

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
		String AndroidId = Settings.Secure.getString(mSongListActivity.getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		paramsTable.put(Constants.API_LOCATION_ID_KEY, "1");
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_REFRESH_PLAYLIST, paramsTable);
		
		return JSONParseHelper.RefreshPlaylist(result, mSongListActivity);
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mSongListActivity.notifiyRefresh(result);
    }

}
