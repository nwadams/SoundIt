package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.back.http.HTTPHelper;

public class CheckOutAsyncTask extends AsyncTask<Void, Void, Void>{
	
	private SongListActivity mContext;
	
	public CheckOutAsyncTask(SongListActivity context) {
		mContext = context;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected Void doInBackground(Void... params) {
		SharedPreferences settings = mContext.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);

		Hashtable<String, String> paramsTable = new Hashtable<String,String>();
		
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		
		HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_LOGIN, paramsTable);

		return null;
	}


	@Override
	protected void onPostExecute(Void result)
	{
		
	}

}
