package ca.soundit.soundit.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.LoginAsyncTask;

public class CheckInActivity extends BaseActivity {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		
		//this.getSupportActionBar().setTitle(R.string.activity_checkin_title);
		
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_USER_INFO, Context.MODE_PRIVATE);
		
		int userId = settings.getInt(Constants.PREFS_USER_ID, -1);
		String apiKey = settings.getString(Constants.PREFS_API_TOKEN, null);
		
		int locationId = settings.getInt(Constants.PREFS_LOCATION_ID, -1);
		
		LoginAsyncTask loginTask = new LoginAsyncTask(this);
		
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			loginTask.execute();
		} else {
			loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		
		if (userId < 0 || apiKey == null) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		} else if (locationId > 0) {
			startActivity(new Intent(this, SongListActivity.class));
			finish();
		}
	}
}
