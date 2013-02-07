package ca.soundit.soundit.adapter;

import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.back.asynctask.VoteUpAsyncTask;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.fragments.SongListFragment;

import com.google.analytics.tracking.android.EasyTracker;

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
            holder.voteButton = (Button)row.findViewById(R.id.vote_button);
            
            row.setTag(holder);
        }
        else
        {
            holder = (SongHolder)row.getTag();
        }
        
        Song song = mSongList.get(position);
        
        if (song.getAlbumURL() != null && !song.getAlbumURL().equals("none") && !song.getAlbumURL().equals("null")) {
        	if (SoundITApplication.getInstance().getBitmapCache().get(song.getAlbumURL()) != null) {
        		holder.albumArt.setImageBitmap(SoundITApplication.getInstance().getBitmapCache().get(song.getAlbumURL()));
        	} else {
        		holder.albumArt.setImageResource(R.drawable.default_album_300);
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
							notifyDataSetChanged();
					}
                	
                }.execute(song.getAlbumURL());
        	}
        } else {
        	holder.albumArt.setImageResource(R.drawable.default_album_300);
        }
        
        holder.songTitle.setText(song.getName());
        holder.artistName.setText(song.getArtist());
        
        holder.voteButton.setTag(position);
        holder.voteButton.setText(String.valueOf(song.getVotes()));
        holder.voteButton.setEnabled(!song.isVotedOn());
        holder.voteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				Song song = mSongList.get(position);
				
				EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_VOTE_UP, String.valueOf(song.getMusicTrackID()), null);
				
				new VoteUpAsyncTask(mFragment).execute(song.getMusicTrackID());
				
				song.setVotedOn(true);
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
        Button voteButton;
    }
}

