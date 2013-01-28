package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.activities.SongListActivity;
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
		
		if (result != null)
		{
			StringBuilder sb = new StringBuilder();
			try {
				JSONArray jsonArray = new JSONArray(result);
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					
					if (json.has(Constants.JSON_ERROR_MESSAGE)){
						sb.append(json.getString(Constants.JSON_ERROR_MESSAGE));
					}
					
					//sb.append(json.toString());
				}
				
				if (sb.length() == 0)
					sb.append(Constants.OK);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
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
