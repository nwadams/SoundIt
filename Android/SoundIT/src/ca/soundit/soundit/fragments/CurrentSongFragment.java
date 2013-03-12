package ca.soundit.soundit.fragments;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.utils.ImageCacheHandler;
import ca.soundit.soundit.utils.ImageDownloadManager;

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

	public void updateSong(final Song currentPlayingSong) {
		View v = this.getView();
		
		TextView songTitle = (TextView) v.findViewById(R.id.song_title);
		songTitle.setText(currentPlayingSong.getName());
		
		TextView artistName = (TextView) v.findViewById(R.id.artist_name);
		artistName.setText(currentPlayingSong.getArtist());
		
		final ImageView albumArt = (ImageView) v.findViewById(R.id.album_art_image);
		if (currentPlayingSong.getAlbumURL() != null) {
			ImageDownloadManager.getInstance().loadImage(currentPlayingSong.getAlbumURL(), albumArt);
		}
	}

}
