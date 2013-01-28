package ca.soundit.soundit.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.asynctask.RefreshPlaylistAsyncTask;
import ca.soundit.soundit.fragments.SongListFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class SongListActivity extends SherlockFragmentActivity {

	private Timer mRefreshTimer;
	private boolean mRefreshingPlaylist;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        setContentView(R.layout.activity_song_list);
        
        mRefreshTimer = new Timer();
        mRefreshingPlaylist = false;        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if (mRefreshTimer == null) mRefreshTimer = new Timer();
    	
    	mRefreshTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				 refreshPlaylist();
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	return true;
        case R.id.menu_settings:
            return true;
        case R.id.menu_refresh:
            refreshPlaylist();
            return true;
        case R.id.menu_add_song:
        	startAddSongActivity();
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	private void startAddSongActivity() {
		startActivity(new Intent(this, AddSongActivity.class));
	}

	private void refreshPlaylist() {
		if (!mRefreshingPlaylist) {
			mRefreshingPlaylist = true;
			new RefreshPlaylistAsyncTask(SongListActivity.this).execute();
		}
		
	}

	public void notifiyRefresh(String result) {
		mRefreshingPlaylist = false;

		if (!Constants.OK.equals(result)) {
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		}
		
		SoundITApplication myApplication = (SoundITApplication) getApplication();
		
		SongListFragment songListFragment = (SongListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_song_list);
		
		if(songListFragment != null)
		{
			songListFragment.updateList(myApplication.getSongQueue());
		}
		
		
	}
    
}
