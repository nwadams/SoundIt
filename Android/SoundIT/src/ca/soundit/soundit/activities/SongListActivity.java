package ca.soundit.soundit.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.asynctask.GetVoteHistoryAsyncTask;
import ca.soundit.soundit.back.asynctask.RefreshPlaylistAsyncTask;
import ca.soundit.soundit.fragments.CurrentSongFragment;
import ca.soundit.soundit.fragments.SongListFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.analytics.tracking.android.EasyTracker;

public class SongListActivity extends BaseActivity {

	private Timer mRefreshTimer;
	private boolean mRefreshingPlaylist;
	
	private MenuItem mRefreshMenuItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  

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
				 refreshPlaylist(false);
			}  		
    	}, 0, 1000 * Constants.REFRESH_INTERVAL); //run every minute
    	
        notifiyRefresh(Constants.OK);

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
       	
       	mRefreshMenuItem = menu.findItem(R.id.menu_refresh);
       	
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	return true;
        case R.id.menu_settings:
        	EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_SETTINGS, "", null);
            return true;
        case R.id.menu_refresh:
        	EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_REFRESH, "", null);
            refreshPlaylist(true);
            return true;
        case R.id.menu_add_song:
        	EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_ADD_SONG, "", null);
        	startAddSongActivity();
        case R.id.menu_about:
        	EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_ABOUT, "", null);
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	private void startAddSongActivity() {
		
		startActivity(new Intent(this, AddSongActivity.class));
	}

	private void refreshPlaylist(boolean force) {
		if (!mRefreshingPlaylist || force) {
			mRefreshingPlaylist = true;
			
			runOnUiThread(new Runnable() {
			    public void run() {
			    	if (mRefreshMenuItem != null) 
						mRefreshMenuItem.setVisible(!mRefreshingPlaylist);
					
					setSupportProgressBarIndeterminateVisibility(mRefreshingPlaylist);
					
			    	new RefreshPlaylistAsyncTask(SongListActivity.this).execute();
			    	new GetVoteHistoryAsyncTask(SongListActivity.this).execute();
			    }
			});		
		}
		
	}
	
	public void forceRefresh() {
		refreshPlaylist(true);
	}

	public void notifiyRefresh(String result) {
		mRefreshingPlaylist = false;
		this.setSupportProgressBarIndeterminateVisibility(mRefreshingPlaylist);
		if (mRefreshMenuItem != null) 
			mRefreshMenuItem.setVisible(!mRefreshingPlaylist);

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
		
		CurrentSongFragment currentSongFragment = (CurrentSongFragment)
				getSupportFragmentManager().findFragmentById(R.id.current_song);
		
		if (currentSongFragment != null) {
			if (myApplication.getCurrentPlayingSong() != null) {
				currentSongFragment.updateSong(myApplication.getCurrentPlayingSong()); 
			} else {
				//TODO HANDLE ERROR
			}
		}
		
		
	}
    
}
