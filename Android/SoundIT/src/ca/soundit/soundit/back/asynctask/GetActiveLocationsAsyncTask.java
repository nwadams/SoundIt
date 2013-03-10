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

public class GetActiveLocationsAsyncTask extends AsyncTask<String, Void, List<Location>>{
	
	private CheckInListFragment mFragment;
	
	public GetActiveLocationsAsyncTask(CheckInListFragment fragment) {
		mFragment = fragment;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected List<Location> doInBackground(String... params) {
		
		Hashtable<String, String> paramsTable = new Hashtable<String,String>();
		SharedPreferences settings = mFragment.getActivity().getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_GET_LOCATIONS, paramsTable);

		List<Location> locationList = new ArrayList<Location>();	
		try {
			JSONArray jsonArray = new JSONArray(result);
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				Location location = new Location();
				
				location.setId(json.optInt(Constants.JSON_PK));
				
				json = json.optJSONObject(Constants.JSON_FIELDS);
				
				location.setName(json.optString(Constants.JSON_NAME));
				location.setLocation(json.optString(Constants.JSON_LOCATION));
				
				locationList.add(location);
			}
		} catch (JSONException e) {
			return null;
		}
		
		
			
		return locationList;
	}


	@Override
	protected void onPostExecute(List<Location> result)
	{
		if (mFragment != null) {
			mFragment.displayAvailableLocations(result);
		}
	}

}
