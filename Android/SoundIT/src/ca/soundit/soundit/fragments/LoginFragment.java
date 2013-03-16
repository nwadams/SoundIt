package ca.soundit.soundit.fragments;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.back.asynctask.SignUpAsyncTask;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.GooglePlusUtil;
import com.google.android.gms.plus.PlusClient;

public class LoginFragment extends SherlockFragment implements ConnectionCallbacks, OnConnectionFailedListener{

	private SignUpAsyncTask mSignUpTask;
	private ProgressDialog mProgressDialog;

	private Button mLoginButton;
	private SignInButton mGoogleLoginButton;
	private LoginButton mFacebookLoginButton;

	private UiLifecycleHelper uiHelper;

	private boolean loginAttempt;

	private OnLoginCompleteListener mCallback;

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	private ProgressDialog mConnectionProgressDialog;
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;

	// Container Activity must implement this interface
	public interface OnLoginCompleteListener {
		public void onLoginComplete();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPlusClient = new PlusClient.Builder(this.getActivity(), this, this)
		.setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
		.setScopes(Scopes.PLUS_LOGIN, "https://www.googleapis.com/auth/userinfo.email")
		.build();
		// Progress bar to be displayed if the connection failure is not resolved.
		mConnectionProgressDialog = new ProgressDialog(this.getActivity());
		mConnectionProgressDialog.setMessage("Signing in...");

		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		mPlusClient.disconnect();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
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
		mGoogleLoginButton = (SignInButton) v.findViewById(R.id.sign_in_button);
		mGoogleLoginButton.setSize(SignInButton.SIZE_WIDE);

		mLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}

		});

		loginAttempt = false;
		mGoogleLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginAttempt = true;
				if (mConnectionResult == null) {
					mConnectionProgressDialog.show();
				} else {
					try {
						mConnectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
					} catch (SendIntentException e) {
						// Try connecting again.
						mConnectionResult = null;
						mPlusClient.connect();
					}
				}
			}

		});

		mFacebookLoginButton = (LoginButton) v.findViewById(R.id.facebook_authButton);
		mFacebookLoginButton.setReadPermissions(Arrays.asList("email"));
		mFacebookLoginButton.setFragment(this);

		Bundle b = getActivity().getIntent().getExtras();
		if (b != null && b.containsKey("logout")) {
			Session s = Session.getActiveSession();
			if (s != null)
				s.closeAndClearTokenInformation();
			mFacebookLoginButton.setSession(s);
		}

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		uiHelper.onResume();

		int errorCode = GooglePlusUtil.checkGooglePlusApp(this.getActivity());
		if (errorCode != GooglePlusUtil.SUCCESS) {
			mGoogleLoginButton.setVisibility(View.GONE);
		} else {
			mGoogleLoginButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		hideProgressDialog();
		uiHelper.onResume();

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


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mConnectionProgressDialog.isShowing()) {
			// The user clicked the sign-in button already. Start to resolve
			// connection errors. Wait until onConnected() to dismiss the
			// connection dialog.
			if (result.hasResolution()) {
				try {
					result.startResolutionForResult(this.getActivity(), REQUEST_CODE_RESOLVE_ERR);
				} catch (SendIntentException e) {
					mPlusClient.connect();
				}
			}
		}

		// Save the intent so that we can start an activity when the user clicks
		// the sign-in button.
		mConnectionResult = result;
	}

	@Override
	public void onConnected() {
		mConnectionProgressDialog.dismiss();
		if (!loginAttempt) {
			if (mPlusClient.isConnected()) {
				mPlusClient.clearDefaultAccount();
				mPlusClient.disconnect();
				mPlusClient.connect();
			}
		} else {
			loginGoogle();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loginGoogle() {
		EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_SIGN_UP, Constants.GA_APP_FLOW_SIGN_UP_GOOGLE, null);
		showProgressDialog();
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				try {
					String accessToken = GoogleAuthUtil.getToken(getActivity(), mPlusClient.getAccountName(),
							"oauth2:" + Scopes.PLUS_LOGIN + " https://www.googleapis.com/auth/userinfo.email");
					Hashtable<String,String> paramsTable = new Hashtable<String,String>();
					String AndroidId = Settings.Secure.getString(LoginFragment.this.getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
					paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
					paramsTable.put(Constants.API_AUTH_TOKEN, accessToken);
					paramsTable.put(Constants.API_TOKEN_TYPE, "google");

					mSignUpTask = new SignUpAsyncTask(LoginFragment.this, paramsTable);

					if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								mSignUpTask.execute();
							}
						});					
					} else {
						mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}		

				} catch (UserRecoverableAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (GoogleAuthException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
		};
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			task.execute();				
		} else {
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}		
	}

	@Override
	public void onDisconnected() {
	}

	public void reconnectToPlus() {
		mConnectionResult = null;
		mPlusClient.connect();
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
			Log.i(Constants.TAG, "Logged in...");
			final String accessToken = session.getAccessToken();

			// make request to the /me API
			Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				// callback after Graph API response with user object
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if (user != null) {
						String id = user.getId();
						loginFacebook(accessToken, id);
					}
				}
			});
		} else if (state.isClosed()) {
			Log.i(Constants.TAG, "Logged out...");
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void loginFacebook(String accessToken, String id) {
		EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_SIGN_UP, Constants.GA_APP_FLOW_SIGN_UP_GOOGLE, null);
		showProgressDialog();
		Hashtable<String,String> paramsTable = new Hashtable<String,String>();
		String AndroidId = Settings.Secure.getString(this.getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);
		paramsTable.put(Constants.API_DEVICE_ID_KEY, AndroidId);
		paramsTable.put(Constants.API_AUTH_TOKEN, accessToken);
		paramsTable.put(Constants.API_TOKEN_TYPE, "facebook");
		paramsTable.put(Constants.API_FACEBOOK_ID, id);

		mSignUpTask = new SignUpAsyncTask(this, paramsTable);

		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			mSignUpTask.execute();
		} else {
			mSignUpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}		
	}

	public UiLifecycleHelper getUIHelper() {
		return uiHelper;
	}
}
