package ca.soundit.soundit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.adapter.SongLibraryListArrayAdapter;
import ca.soundit.soundit.back.asynctask.AddToPlaylistAsyncTask;
import ca.soundit.soundit.back.asynctask.GetLibraryAsyncTask;
import ca.soundit.soundit.back.data.Song;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class AddSongFragment extends SherlockFragment {
	
	private ListView mListView;
	private SongLibraryListArrayAdapter mArrayAdapter;
	private ProgressDialog mProgressDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		getLibrary(null);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        
        mListView = (ListView) view.findViewById(android.R.id.list);
        
        mArrayAdapter = new SongLibraryListArrayAdapter(getActivity(),R.layout.list_item_song_library,R.id.song_title, new ArrayList<Song>());
        mArrayAdapter.setNotifyOnChange(false);
        
        mListView.setAdapter(mArrayAdapter);
        
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_ADD_SONG,
						String.valueOf(mArrayAdapter.getItem(position).getMusicTrackID()), null);
				addToPlaylist(position);
			}
        	
        });
        
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (SoundITApplication.getInstance().getSongLibrary() != null)
			updateList(SoundITApplication.getInstance().getSongLibrary());
	}

	protected void addToPlaylist(int position) {
		Song song = mArrayAdapter.getItem(position);
		
		new AddToPlaylistAsyncTask(this).execute(song.getMusicTrackID());
		mProgressDialog = ProgressDialog.show(this.getSherlockActivity(), "Please wait", "Adding song");
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void updateList(List<Song> songQueue) {
		mArrayAdapter.clear();
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			for (int i = 0; i < songQueue.size(); i++) {
				mArrayAdapter.add(songQueue.get(i));
			}
		} else {
			mArrayAdapter.addAll(songQueue);
		}
		mArrayAdapter.notifyDataSetChanged();
	}

	public void notifiyComplete(String result) {
		if (Constants.OK.equals(result)) {
			Toast.makeText(this.getSherlockActivity(), R.string.toast_song_added_to_queue, Toast.LENGTH_SHORT).show();			
		} else {
			Toast.makeText(this.getSherlockActivity(), R.string.toast_song_already_in_queue, Toast.LENGTH_SHORT).show();
		}
		EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_ADD_SONG_COMPLETE, "", null);
		
		if (mProgressDialog != null)
			mProgressDialog.cancel();
		
		this.getSherlockActivity().finish();		
	}
	
	private void getLibrary(String searchString) {
		((SherlockFragmentActivity) this.getActivity()).setSupportProgressBarIndeterminateVisibility(true);
		new GetLibraryAsyncTask(this).execute(searchString);
	}

	public void notifiyRefresh(List<Song> result) {
		((SherlockFragmentActivity) this.getActivity()).setSupportProgressBarIndeterminateVisibility(false);

		if (result == null) {
			return;
		}
		
		updateList(result);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_add_song_menu, menu);
		
		final MenuItem searchItem =  menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setQueryHint(this.getActivity().getString(R.string.add_song_search_query_hint));
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String newText) {
				if (newText.isEmpty()) {
					getLibrary(null);
					return true;
				} else {
					getLibrary(newText);
					return true;
				}
			}

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (query.isEmpty())
					getLibrary(null);
				else
					getLibrary(query);
				
				searchItem.collapseActionView();
				return true;
			}
		});
	}

	protected void searchLibrary(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_GET_SONG_LIBRARY, "", null);
			getLibrary(null);
			return true;
		case R.id.menu_search:
						
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
}
