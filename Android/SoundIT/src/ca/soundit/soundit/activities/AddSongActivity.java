package ca.soundit.soundit.activities;

import java.util.List;

import android.os.Bundle;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.GetLibraryAsyncTask;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.fragments.AddSongFragment;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.analytics.tracking.android.EasyTracker;

public class AddSongActivity extends BaseActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  

        setContentView(R.layout.activity_add_song);
        
        this.getSupportActionBar().setTitle(R.string.activity_title_add_song);
        
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_CANCEL, Constants.GA_CANCEL_ADD_SONG, null);
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

	
}
