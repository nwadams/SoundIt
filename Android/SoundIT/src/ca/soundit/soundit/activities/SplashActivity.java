package ca.soundit.soundit.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import ca.soundit.soundit.R;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

public class SplashActivity extends SherlockFragmentActivity {

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_splash);   
	        
	        Timer splashTimer = new Timer();
	        splashTimer.schedule(new TimerTask() {

				@Override
				public void run() {
					startActivity(new Intent(SplashActivity.this, SongListActivity.class));
					finish();
					
				}
	        	
	        }, 2000);
	    }
}
