package ca.soundit.soundit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.adapter.SongLibraryListArrayAdapter;
import ca.soundit.soundit.back.asynctask.AddToPlaylistAsyncTask;
import ca.soundit.soundit.back.data.Song;

import com.actionbarsherlock.app.SherlockFragment;

public class AddSongFragment extends SherlockFragment {
	
	private ListView mListView;
	private SongLibraryListArrayAdapter mArrayAdapter;

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
				addToPlaylist(position);
			}
        	
        });
        
        return view;
    }

	protected void addToPlaylist(int position) {
		Song song = mArrayAdapter.getItem(position);
		
		new AddToPlaylistAsyncTask(this).execute(song.getMusicTrackID());
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
		this.getSherlockActivity().finish();		
	}
	
}
