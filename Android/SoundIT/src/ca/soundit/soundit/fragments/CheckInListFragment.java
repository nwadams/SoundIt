package ca.soundit.soundit.fragments;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.adapter.CheckInListArrayAdapter;
import ca.soundit.soundit.back.asynctask.GetActiveLocationsAsyncTask;
import ca.soundit.soundit.back.data.Location;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class CheckInListFragment extends SherlockFragment {

	private ListView mListView;
	private CheckInListArrayAdapter mArrayAdapter;

	private GetActiveLocationsAsyncTask mGetActiveLocationsAsyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_checkin_list, container, false);

		mListView = (ListView) view.findViewById(android.R.id.list);
		View empty = view.findViewById(android.R.id.empty);
		mListView.setEmptyView(empty);

		mArrayAdapter = new CheckInListArrayAdapter(getActivity(),R.layout.list_item_location,R.id.location_name, new ArrayList<Location>());
		mArrayAdapter.setNotifyOnChange(false);

		mListView.setAdapter(mArrayAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_CHECK_IN,
						String.valueOf(mArrayAdapter.getItem(position).getId()), null);

				checkIn(mArrayAdapter.getItem(position).getId());
			}

		});

		return view;
	}

	protected void checkIn(int id) {


	}

	@Override
	public void onResume() {
		super.onResume();

		getLocationList();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void getLocationList() {
		mGetActiveLocationsAsyncTask = new GetActiveLocationsAsyncTask(this);

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			mGetActiveLocationsAsyncTask.execute();
		} else {
			mGetActiveLocationsAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mGetActiveLocationsAsyncTask != null)
			mGetActiveLocationsAsyncTask.cancel(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.fragment_check_in_list_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_MENU_OPTION, Constants.GA_APP_FLOW_CHECK_IN, "", null);
			getLocationList();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void displayAvailableLocations(List<Location> result) {
		if (result == null) {
			displayFailed();
		} else {
			mArrayAdapter.clear();
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
				for (int i = 0; i < result.size(); i++) {
					mArrayAdapter.add(result.get(i));
				}
			} else {
				mArrayAdapter.addAll(result);
			}
			mArrayAdapter.notifyDataSetChanged();
		}
	}

	private void displayFailed() {

	}

}
