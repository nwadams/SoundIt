package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.back.json.JSONParseHelper;
import ca.soundit.soundit.fragments.AddSongFragment;

public class AddToPlaylistAsyncTask extends
		AsyncTask<Integer, Void, String> {

	private AddSongFragment mAddSongFragment;
	
	public AddToPlaylistAsyncTask(AddSongFragment addSongFragment){
		mAddSongFragment = addSongFragment;
	}
	
	@Override
	protected String doInBackground(Integer... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mAddSongFragment.getActivity().getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		paramsTable.put(Constants.API_LOCATION_ID_KEY, String.valueOf(settings.getInt(Constants.PREFS_LOCATION_ID, 0)));
		paramsTable.put(Constants.API_MUSIC_TRACK_ID, Integer.toString(params[0]));
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_ADD_TO_PLAYLIST, paramsTable);
		
		if (result != null)
		{
			return JSONParseHelper.RefreshPlaylist(result, mAddSongFragment.getActivity());
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mAddSongFragment.notifiyComplete(result);
    }

}
