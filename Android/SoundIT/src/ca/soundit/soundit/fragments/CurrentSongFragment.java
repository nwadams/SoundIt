package ca.soundit.soundit.fragments;

import android.os.Bundle;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_song, container, false);
        
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
