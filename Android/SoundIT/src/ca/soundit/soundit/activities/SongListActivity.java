package ca.soundit.soundit.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.widget.Toast;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.RefreshPlaylistAsyncTask;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

public class SongListActivity extends SherlockFragmentActivity {

	private Timer mRefreshTimer;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        
        mRefreshTimer = new Timer();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (mRefreshTimer == null) mRefreshTimer = new Timer();
    	
    	mRefreshTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				 new RefreshPlaylistAsyncTask(SongListActivity.this).execute();
			}  		
    	}, 0, 1000 * Constants.REFRESH_INTERVAL); //run every minute
    	
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	
    	if (mRefreshTimer != null) {
    		mRefreshTimer.cancel();
    		mRefreshTimer = null;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       	this.getSupportMenuInflater().inflate(R.menu.activity_song_list, menu);
        return true;
    }

	public void notifiyRefresh(String result) {
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}
    
}
