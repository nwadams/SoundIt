package ca.soundit.soundit.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.utils.ImageDownloadManager;

public class SongLibraryListArrayAdapter extends ArrayAdapter<Song> {
	
	private Context mContext;
	private int mLayoutResourceID;
	List<Song> mSongList;

	public SongLibraryListArrayAdapter(Context context, int resource, int textViewResourceId,
			List<Song> objects) {
		super(context, resource, textViewResourceId, objects);
		
		this.mLayoutResourceID = resource;
        this.mContext = context;
        mSongList = objects;
        
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        SongHolder holder = null;
        
        if(row == null)
        {
        	LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceID, parent, false);
            
            holder = new SongHolder();
            holder.albumArt = (ImageView)row.findViewById(R.id.album_art_image);
            holder.songTitle = (TextView)row.findViewById(R.id.song_title);
            holder.artistName = (TextView)row.findViewById(R.id.artist_name);
            
            row.setTag(holder);
        }
        else
        {
            holder = (SongHolder)row.getTag();
        }
        
        Song song = mSongList.get(position);
        
        if (song.getAlbumURL() != null) {
			ImageDownloadManager.getInstance().loadImage(song.getAlbumURL(), holder.albumArt);
		}
        
        holder.songTitle.setText(song.getName());
        holder.artistName.setText(song.getArtist());
        
        return row;
    }
    
    static class SongHolder
    {
        ImageView albumArt;
        TextView songTitle;
        TextView artistName;
    }
}

