package ca.soundit.soundit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.adapter.SongListArrayAdapter;
import ca.soundit.soundit.back.data.Song;

import com.actionbarsherlock.app.SherlockFragment;

public class SongListFragment extends SherlockFragment {
	
	private ListView mListView;
	private SongListArrayAdapter mArrayAdapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        
        mListView = (ListView) view.findViewById(android.R.id.list);
        
        mArrayAdapter = new SongListArrayAdapter(this,getActivity(),R.layout.list_item_song_queue,R.id.song_title, new ArrayList<Song>());
        mArrayAdapter.setNotifyOnChange(false);
                        
        View currentSongHeaderView = inflater.inflate(R.layout.header_current_song, null, false);
        mListView.addHeaderView(currentSongHeaderView, null, false);
        mListView.setHeaderDividersEnabled(false);
        
        View nextView = inflater.inflate(R.layout.list_header_queue, null, false);
        mListView.addHeaderView(nextView);
        
        mListView.setAdapter(mArrayAdapter);
        
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		
		updateList(SoundITApplication.getInstance().getSongQueue());
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

	public void notifiyVoteComplete(String result) {
		if (Constants.OK.equals(result)) {
			updateList(SoundITApplication.getInstance().getSongQueue());
			//SongListActivity activity = (SongListActivity) this.getActivity();
			//activity.forceRefresh();
		} else {
			
		}
		
	}
	
}
