package ca.soundit.soundit.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import ca.soundit.soundit.R;

import com.actionbarsherlock.app.SherlockFragment;

public class SongListFragment extends SherlockFragment {
	
	private ListView mListView;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);
        
        mListView = (ListView) view.findViewById(android.R.id.list);
        
        String[] values = {"A", "B", "C"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
        
        View header = inflater.inflate(R.layout.header_current_song, null, false);
        mListView.addHeaderView(header, null, false);
        mListView.setHeaderDividersEnabled(true);
        
        mListView.setAdapter(adapter);
        
        return view;
    }
	
}
