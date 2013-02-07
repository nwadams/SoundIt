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
		if (currentPlayingSong.getAlbumURL() != null && !currentPlayingSong.getAlbumURL().equals("none") && !currentPlayingSong.getAlbumURL().equals("null")) {
        	if (SoundITApplication.getInstance().getBitmapCache().get(currentPlayingSong.getAlbumURL()) != null) {
        		albumArt.setImageBitmap(SoundITApplication.getInstance().getBitmapCache().get(currentPlayingSong.getAlbumURL()));
        	} else {
        		albumArt.setImageResource(R.drawable.default_album_300);
                new AsyncTask<String, Void, String>() {
					@Override
					protected String doInBackground(String... params) {
						Hashtable<String,String> paramsTable = new Hashtable<String,String>();
						Bitmap bitmap = HTTPHelper.HTTPImageGetRequest(params[0], paramsTable);
						if (bitmap != null) {
							SoundITApplication.getInstance().getBitmapCache().put(params[0], bitmap);
							return Constants.OK;
						}
							
						return null;
					}
					
					@Override
					protected void onPostExecute(String result) {
						if (Constants.OK.equals(result))
							albumArt.setImageBitmap(SoundITApplication.getInstance().getBitmapCache().get(currentPlayingSong.getAlbumURL()));
					}
                	
                }.execute(currentPlayingSong.getAlbumURL());
        	}
		} else {
        	albumArt.setImageResource(R.drawable.default_album_300);
        }
	}

}
