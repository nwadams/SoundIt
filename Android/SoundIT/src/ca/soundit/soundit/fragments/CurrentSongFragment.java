package ca.soundit.soundit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.data.Song;

import com.actionbarsherlock.app.SherlockFragment;

public class CurrentSongFragment extends SherlockFragment {

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		// create ContextThemeWrapper from the original Activity Context with the custom theme
		Context context = new ContextThemeWrapper(getActivity(), R.style.Theme_Sherlock);
		// clone the inflater using the ContextThemeWrapper
		LayoutInflater localInflater = inflater.cloneInContext(context);
        return localInflater.inflate(R.layout.fragment_current_song, container, false);
        
	}

	public void updateSong(Song currentPlayingSong) {
		View v = this.getView();
		
		TextView songTitle = (TextView) v.findViewById(R.id.song_title);
		songTitle.setText(currentPlayingSong.getName());
		
		TextView artistName = (TextView) v.findViewById(R.id.artist_name);
		artistName.setText(currentPlayingSong.getArtist());
		
		ImageView albumArt = (ImageView) v.findViewById(R.id.album_art_image);
		//display image
	}

}
