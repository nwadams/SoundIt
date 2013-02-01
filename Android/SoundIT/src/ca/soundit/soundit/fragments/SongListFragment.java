package ca.soundit.soundit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import ca.soundit.soundit.R;
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
        
        mArrayAdapter = new SongListArrayAdapter(getActivity(),R.layout.list_item_song_queue,R.id.song_title, new ArrayList<Song>());
        mArrayAdapter.setNotifyOnChange(false);
                        
        View currentSongHeaderView = inflater.inflate(R.layout.header_current_song, null, false);
        mListView.addHeaderView(currentSongHeaderView, null, false);
        mListView.setHeaderDividersEnabled(false);
        
        mListView.setAdapter(mArrayAdapter);
        
        return view;
    }

	public void updateList(List<Song> songQueue) {
		mArrayAdapter.clear();
		mArrayAdapter.addAll(songQueue);
		mArrayAdapter.notifyDataSetChanged();
	}
	
}
