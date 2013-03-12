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

public class SignUpAsyncTask extends AsyncTask<String, Void, String>{
	
	private Hashtable<String, String> mParamsTable;
	private LoginFragment mFragment;
	
	public SignUpAsyncTask(LoginFragment fragment, Hashtable<String, String> paramsTable) {
		mParamsTable = paramsTable;
		mFragment = fragment;
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected String doInBackground(String... params) {
		String returnString = Constants.OK;
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_SIGNUP, mParamsTable);
		
		if (result != null && mFragment != null) {
			SharedPreferences settings = mFragment.getActivity().getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
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
		if (mFragment != null) {
			if (Constants.OK.equals(result)) {
				mFragment.signUpSuccess();
			} else {
				mFragment.signUpFail();
			}
		}
	}

}
