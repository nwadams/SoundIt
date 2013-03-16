package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import ca.soundit.soundit.back.http.HTTPHelper;
import ca.soundit.soundit.utils.ImageDownloadManager;

public class ImageDownloaderAsyncTask extends AsyncTask<String, Void, Bitmap> {
	
	private ImageDownloadManager mDownloader;
	private String mUrl;
	private int mState = STATE_IDLE;
	
	public static int STATE_IDLE = 0;
	public static int STATE_ACTIVE = 1;
	
	public ImageDownloaderAsyncTask(ImageDownloadManager downloader, String url) {
		mDownloader = downloader;
		mUrl = url;
		mState = STATE_ACTIVE;
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		Bitmap bitmap = HTTPHelper.HTTPImageGetRequest(mUrl, paramsTable);
		
		if (bitmap != null)
			mDownloader.putImageInCache(mUrl, bitmap);
		
		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		mDownloader.downloadComplete(mUrl, result);
		mState = STATE_IDLE;
	}
	
	public boolean isIdle() {
		return mState == STATE_IDLE;
	}

}