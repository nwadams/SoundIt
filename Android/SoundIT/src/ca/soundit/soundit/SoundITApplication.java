package ca.soundit.soundit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ca.soundit.soundit.back.data.Song;
import android.app.Application;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class SoundITApplication extends Application {
	
	private static SoundITApplication mInstance;
	
	public static SoundITApplication getInstance() {
			return mInstance;
    }
	
	private int cacheSize = 50; // 4 * 1024 * 1024; //4MB
	private LruCache<String, Bitmap> mBitmapCache;
	
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
		Collections.sort(mSongQueue, new Comparator<Song>() {

			@Override
			public int compare(Song lhs, Song rhs) {
				if (lhs.getVotes() > rhs.getVotes())
					return -1;
				else if (lhs.getVotes() == rhs.getVotes())
					return 0;
				else
					return 1;
			}
			
		});
		this.mSongQueue = mSongQueue;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        mInstance = this;
        
        setBitmapCache(new LruCache<String, Bitmap>(cacheSize));

    }

	public LruCache<String, Bitmap> getBitmapCache() {
		return mBitmapCache;
	}

	public void setBitmapCache(LruCache<String, Bitmap> mBitmapCache) {
		this.mBitmapCache = mBitmapCache;
	}
}
