package ca.soundit.soundit.adapter;

import java.util.Hashtable;
import java.util.List;

import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.asynctask.VoteUpAsyncTask;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.fragments.SongListFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

public class SongListArrayAdapter extends ArrayAdapter<Song> {
	
	private Context mContext;
	private SongListFragment mFragment;
	private int mLayoutResourceID;
	List<Song> mSongList;

	public SongListArrayAdapter(SongListFragment fragment, Context context, int resource, int textViewResourceId,
			List<Song> objects) {
		super(context, resource, textViewResourceId, objects);
		
		this.mLayoutResourceID = resource;
        this.mContext = context;
        mSongList = objects;
        mFragment = fragment;     
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
            holder.numVotes = (TextView)row.findViewById(R.id.number_votes);
            holder.voteButton = (Button)row.findViewById(R.id.vote_button);
            
            row.setTag(holder);
        }
        else
        {
            holder = (SongHolder)row.getTag();
        }
        
        Song song = mSongList.get(position);
        
        if (song.getAlbumURL() != null && !song.getAlbumURL().equals("none")) {
        	if (SoundITApplication.getInstance().getBitmapCache().get(song.getAlbumURL()) != null) {
        		holder.albumArt.setImageBitmap(SoundITApplication.getInstance().getBitmapCache().get(song.getAlbumURL()));
        	} else {
        		holder.albumArt.setImageResource(R.drawable.default_album_300);
                new AsyncTask<String, Void, Void>() {
					@Override
					protected Void doInBackground(String... params) {
						Hashtable<String,String> paramsTable = new Hashtable<String,String>();
						Bitmap bitmap = HTTPHelper.HTTPImageGetRequest(params[0], paramsTable);
						if (bitmap != null)
							SoundITApplication.getInstance().getBitmapCache().put(params[0], bitmap);
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result) {
						notifyDataSetChanged();
					}
                	
                }.execute(song.getAlbumURL());
        	}
        } else {
        	holder.albumArt.setImageResource(R.drawable.default_album_300);
        }
        
        holder.songTitle.setText(song.getName());
        holder.artistName.setText(song.getArtist());
        holder.numVotes.setText(String.valueOf(song.getVotes()));
        
        holder.voteButton.setTag(position);
        holder.voteButton.setEnabled(!song.isVotedOn());
        holder.voteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				Song song = mSongList.get(position);
				
				new VoteUpAsyncTask(mFragment).execute(song.getMusicTrackID());
				v.setEnabled(false);
				
				song.setVotes(song.getVotes() + 1);
				SongListArrayAdapter.this.notifyDataSetChanged();
			}
        	
        });
        
        return row;
    }
    
    static class SongHolder
    {
        ImageView albumArt;
        TextView songTitle;
        TextView artistName;
        TextView numVotes;
        Button voteButton;
    }
}

