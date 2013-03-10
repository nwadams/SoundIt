package ca.soundit.soundit.fragments;

import java.util.Hashtable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.SignUpAsyncTask;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;

public class LoginFragment extends SherlockFragment {

	private SignUpAsyncTask mSignUpTask;
	private ProgressDialog mProgressDialog;
	
	private Button mLoginButton;
	
	private OnLoginCompleteListener mCallback;

    // Container Activity must implement this interface
    public interface OnLoginCompleteListener {
        public void onLoginComplete();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnLoginCompleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginCompleteListener");
        }
    }
    

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		mLoginButton = (Button) v.findViewById(R.id.login_button);
		
		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
			
		});
		return v;
	}
	
	@Override
	public void onPause() {
		hideProgressDialog();
		super.onPause();
		
		if (mSignUpTask != null)
			mSignUpTask.cancel(true);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void login() {
		EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_SIGN_UP, Constants.GA_APP_FLOW_SIGN_UP_NO_ACCOUNT, null);
		showProgressDialog();
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		String AndroidId = Settings.Secure.getString(this.getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		
		mSignUpTask = new SignUpAsyncTask(this, paramsTable);
	
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			mSignUpTask.execute();
		} else {
			mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}		
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = ProgressDialog.show(this.getActivity(), this.getActivity().getString(R.string.dialog_login_title),
					this.getActivity().getString(R.string.dialog_login_message), true, true);
		} else {
			mProgressDialog.show();
		}
		
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();	
	}

	public void signUpSuccess() {
		hideProgressDialog();
		mCallback.onLoginComplete();		
	}


	public void signUpFail() {
		hideProgressDialog();
	}

}
