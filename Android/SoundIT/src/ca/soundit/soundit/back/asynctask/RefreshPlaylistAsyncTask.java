package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.back.json.JSONParseHelper;

public class RefreshPlaylistAsyncTask extends
		AsyncTask<Void, Void, String> {

	private SongListActivity mSongListActivity;
	
	public RefreshPlaylistAsyncTask(SongListActivity activity){
		mSongListActivity = activity;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mSongListActivity.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		paramsTable.put(Constants.API_LOCATION_ID_KEY, String.valueOf(settings.getInt(Constants.PREFS_LOCATION_ID, 0)));
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_REFRESH_PLAYLIST, paramsTable);
		
		return JSONParseHelper.RefreshPlaylist(result, mSongListActivity);
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mSongListActivity.notifiyRefresh(result);
    }

}
