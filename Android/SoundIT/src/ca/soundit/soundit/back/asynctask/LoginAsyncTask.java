package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import org.json.JSONObject;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.fragments.LoginFragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

public class LoginAsyncTask extends AsyncTask<String, Void, String>{
	
	private Context mContext;
	
	public LoginAsyncTask(Context context) {
		mContext = context;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected String doInBackground(String... params) {
		SharedPreferences settings = mContext.getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);

		String returnString = Constants.OK;
		Hashtable<String, String> paramsTable = new Hashtable<String,String>();
		
		paramsTable.put(Constants.API_USER_ID, String.valueOf(settings.getInt(Constants.PREFS_USER_ID, 0)));
		paramsTable.put(Constants.API_API_KEY, settings.getString(Constants.PREFS_API_TOKEN, ""));
		String AndroidId = Settings.Secure.getString(mContext.getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_LOGIN, paramsTable);
		
		if (result != null) {
			SharedPreferences.Editor editor = settings.edit();

			try {
				JSONObject user = new JSONObject(result);
				editor.putInt(Constants.PREFS_USER_ID, user.optInt(Constants.JSON_PK));
				
				user = user.optJSONObject(Constants.JSON_FIELDS);
				
				editor.putString(Constants.PREFS_API_TOKEN, user.optString(Constants.JSON_API_TOKEN));
				editor.putString(Constants.PREFS_EMAIL_ADDRESS, user.optString(Constants.JSON_EMAIL_ADDRESS));
				editor.putString(Constants.PREFS_NAME, user.optString(Constants.JSON_NAME));
				editor.putString(Constants.PREFS_DEVICE_ID, user.optString(Constants.JSON_DEVICE_ID));
				
			} catch (Exception e) {
				returnString = e.getLocalizedMessage();
			}
			
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
				editor.commit();
			} else {
				editor.apply();
			}	
		}

		return returnString;
	}


	@Override
	protected void onPostExecute(String result)
	{
	}

}
