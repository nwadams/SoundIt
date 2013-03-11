package ca.soundit.soundit.back.asynctask;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.back.data.Location;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.back.json.JSONParseHelper;
import ca.soundit.soundit.fragments.CheckInListFragment;
import ca.soundit.soundit.fragments.LoginFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;

public class CheckInAsyncTask extends AsyncTask<Integer, Void, String>{
	
	private CheckInListFragment mFragment;
	
	public CheckInAsyncTask(CheckInListFragment fragment) {
		mFragment = fragment;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected String doInBackground(Integer... params) {
		
		Hashtable<String, String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mFragment.getActivity().getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		paramsTable.put(Constants.API_LOCATION_ID_KEY, String.valueOf(params[0]));
		
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_CHECK_IN, paramsTable);
					
		return JSONParseHelper.RefreshPlaylist(result, mFragment.getActivity());
	}


	@Override
	protected void onPostExecute(String result)
	{
		if (mFragment != null) {
			mFragment.checkInComplete(result);
		}
	}

}
