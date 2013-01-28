package ca.soundit.soundit;

import java.util.ArrayList;
import java.util.List;

import ca.soundit.soundit.back.data.Song;
import android.app.Application;

public class SoundITApplication extends Application {
	
	private Song mCurrentPlayingSong;
	private List<Song> mSongQueue;

	public Song getCurrentPlayingSong() {
		return mCurrentPlayingSong;
	}

	public void setCurrentPlayingSong(Song mCurrentPlayingSong) {
		this.mCurrentPlayingSong = mCurrentPlayingSong;
	}

	public List<Song> getSongQueue() {
		if (mSongQueue == null) mSongQueue = new ArrayList<Song>();
		
		return mSongQueue;
	}

	public void setSongQueue(List<Song> mSongQueue) {
		this.mSongQueue = mSongQueue;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
    }
}
