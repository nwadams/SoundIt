package ca.soundit.soundit.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.ImageDownloaderAsyncTask;

public class ImageDownloadManager {

	private static ImageDownloadManager mImageDownloadManager;

	public static ImageDownloadManager getInstance() {
		if (mImageDownloadManager == null)
			mImageDownloadManager = new ImageDownloadManager();

		return mImageDownloadManager;
	}
	
	private ImageCacheHandler mImageCacheHandler;
	
	private Hashtable<String, List<ImageView> > mImageViewTable;
	private LinkedList<String> mDownloadQueue;
	
	private ImageDownloaderAsyncTask mImageDownloaderTask[];

	public ImageDownloadManager() {
		mImageCacheHandler = new ImageCacheHandler();
		mImageViewTable = new Hashtable<String,List<ImageView> >();
		mDownloadQueue = new LinkedList<String>();
		mImageDownloaderTask = new ImageDownloaderAsyncTask[Constants.IMAGE_DOWNLOADER_THREADS];
	}
	
	public void loadImage(String URL, ImageView imageView) {
		
		Bitmap bitmap = mImageCacheHandler.get(URL);
		
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			imageView.setImageResource(R.drawable.default_album_300);
			List<ImageView> imageList = mImageViewTable.get(URL);
			if (imageList == null)
				imageList = new LinkedList<ImageView>();
			
			imageList.add(imageView);
			imageView.setTag(URL);
			mImageViewTable.put(URL, imageList);
			downloadImage(URL);
		}
	}

	private void downloadImage(String url) {
		if (!mDownloadQueue.contains(url)) {
			mDownloadQueue.add(url);
		}
		
		notifyDownloader();	
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void notifyDownloader() {
		for (int i = 0; i < mImageDownloaderTask.length; i++) {
			ImageDownloaderAsyncTask task = mImageDownloaderTask[i];
			if (task == null || task.isIdle()) {
				try {
					task = new ImageDownloaderAsyncTask(this, mDownloadQueue.remove());
					if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
						task.execute();
					} else {
						task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
				} catch (Exception e) {
					return;
				}
			}
		}
	}


	public void downloadComplete(String url, Bitmap result) {
		putImageInCache(url, result);
		
		List<ImageView> imageList = mImageViewTable.get(url);
		Iterator<ImageView> itr = imageList.iterator();
		while (itr.hasNext()) {
			ImageView imageView = itr.next();
			if (TextUtils.equals(url, (String) imageView.getTag()))
				imageView.setImageBitmap(result);
		}
		
		notifyDownloader();
	}

	private void putImageInCache(String url, Bitmap result) {
		mImageCacheHandler.put(url, result);	
	}
	
}
