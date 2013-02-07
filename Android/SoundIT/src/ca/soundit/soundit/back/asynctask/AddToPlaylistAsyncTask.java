package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import android.os.AsyncTask;
import android.provider.Settings;
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
		//paramsTable.put(Constants.QUERY_API_KEY, Constants.API_KEY);
		String AndroidId = Settings.Secure.getString(mAddSongFragment.getSherlockActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		paramsTable.put(Constants.API_LOCATION_ID_KEY, "thepit");
		paramsTable.put(Constants.API_MUSIC_TRACK_ID, Integer.toString(params[0]));
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_ADD_TO_PLAYLIST, paramsTable);
		
		if (result != null)
		{
			return Constants.OK;
			//return JSONParseHelper.RefreshPlaylist(result, mSongListFragment.getActivity());
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mAddSongFragment.notifiyComplete(result);
    }

}
