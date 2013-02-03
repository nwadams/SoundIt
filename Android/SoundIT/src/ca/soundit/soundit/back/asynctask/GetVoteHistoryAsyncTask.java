package ca.soundit.soundit.back.asynctask;

import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.provider.Settings;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.SoundITApplication;
import ca.soundit.soundit.activities.SongListActivity;
import ca.soundit.soundit.back.data.Song;
import ca.soundit.soundit.back.http.HTTPHelper;

public class GetVoteHistoryAsyncTask extends
		AsyncTask<Void, Void, String> {

	private SongListActivity mSongListActivity;
	
	public GetVoteHistoryAsyncTask(SongListActivity songListActivity){
		mSongListActivity = songListActivity;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		//paramsTable.put(Constants.QUERY_API_KEY, Constants.API_KEY);
		String AndroidId = Settings.Secure.getString(mSongListActivity.getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		paramsTable.put(Constants.API_LOCATION_ID_KEY, "1");
		String result = HTTPHelper.HTTPGetRequest(Constants.URL_ROOT + Constants.URL_GET_VOTE_HISTORY, paramsTable);
		
		SoundITApplication myApplication = (SoundITApplication) mSongListActivity.getApplication();
		List<Song> songList = myApplication.getSongQueue();
		if (result != null)
		{
			StringBuilder sb = new StringBuilder();
			try {
				JSONArray jsonArray = new JSONArray(result);
				int size = jsonArray.length();
				for (int i = 0; i < size; i++) {
					JSONObject json = jsonArray.getJSONObject(i);
					JSONObject fields = json.getJSONObject(Constants.JSON_FIELDS);
							
					JSONObject musicTrack = fields.getJSONObject(Constants.JSON_MUSIC_TRACK);
					int musicTrackId = musicTrack.getInt(Constants.JSON_PK);
					
					for (int j = 0; j < songList.size(); j++) {
						Song song = songList.get(j);
						if (musicTrackId == song.getMusicTrackID())
							song.setVotedOn(true);
					}
				}
			} catch (JSONException e) {
				sb.append(e.toString());
			}
			
			if (sb.length() == 0)
				sb.append(Constants.OK);
			
			return sb.toString();
		}
		
		return mSongListActivity.getString(R.string.error_no_data);
	}
	
	@Override
	protected void onPostExecute(String result)
    {
		mSongListActivity.notifiyRefresh(result);
    }

}
