package ca.soundit.soundit.activities;

import java.util.List;

import android.os.Bundle;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.GetLibraryAsyncTask;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.fragments.AddSongFragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class AddSongActivity extends SherlockFragmentActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  

        setContentView(R.layout.activity_add_song);
        
        this.getSupportActionBar().setTitle(R.string.activity_title_add_song);
        
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setSupportProgressBarIndeterminateVisibility(true);
        
        getLibrary();
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }

	private void getLibrary() {
		new GetLibraryAsyncTask(this).execute();
	}

	public void notifiyRefresh(List<Song> result) {
        this.setSupportProgressBarIndeterminateVisibility(false);

		if (result == null) {
			return;
		}
						
		AddSongFragment addSongFragment = (AddSongFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_add_song);
		
		if(addSongFragment != null)
		{
			addSongFragment.updateList(result);
		}
	}
}
