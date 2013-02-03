package ca.soundit.soundit.activities;

import java.util.Hashtable;
import java.util.UUID;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.http.HTTPHelper;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class SplashActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		String password = settings.getString(Constants.PREFS_PASSWORD, null);

		new AsyncTask<String,Void,String>() {

			@Override
			protected String doInBackground(String... params) {
				Hashtable<String,String> paramsTable = new Hashtable<String,String>();
				//paramsTable.put(Constants.QUERY_API_KEY, Constants.API_KEY);
				String AndroidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
				paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);

				if (params.length > 0 && params[0] != null) {
					paramsTable.put(Constants.API_PASSWORD, params[0]);
				} else {					
					String password = UUID.randomUUID().toString();
					paramsTable.put(Constants.API_PASSWORD, password);

					SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString(Constants.PREFS_PASSWORD, password);

					// Commit the edits!
					editor.commit();					
				}
				String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_SIGNUP, paramsTable);

				return result;
			}


			@Override
			protected void onPostExecute(String result)
			{
				if (result == null) {
					//error
				} else {
					finishSplashActivity();
				}
			}
		}.execute(password);

		/*
	        Timer splashTimer = new Timer();
	        splashTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					startActivity(new Intent(SplashActivity.this, SongListActivity.class));
					finish();

				}

	        }, 2000);
		 */
	}

	protected void finishSplashActivity() {
		startActivity(new Intent(SplashActivity.this, SongListActivity.class));
		finish();
	}
}
