package ca.soundit.soundit.utils;

import com.jakewharton.DiskLruCache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

public class ImageCacheHandler {

	private int cacheSize = 4 * 1024 * 1024; //4MB
	private LruCache<String, Bitmap> mBitmapCache;
	private DiskLruCache mDiskCache;

	public ImageCacheHandler() {
		mBitmapCache = new LruCache<String, Bitmap>(cacheSize) {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
			protected int sizeOf(String key, Bitmap value) {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
					return value.getRowBytes() * value.getHeight();
				} else {
					return value.getByteCount();
				}
			}};
	}
	
	public void put(String key, Bitmap value) {
		synchronized (mBitmapCache) {
			mBitmapCache.put(key, value);
		}
	}
	
	public Bitmap get(String key) {
		synchronized (mBitmapCache) {
			return mBitmapCache.get(key);
		}
	}
}
