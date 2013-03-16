package ca.soundit.soundit.activities;

import android.content.Intent;
import android.os.Bundle;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.fragments.LoginFragment;

import com.facebook.Session;
import com.google.analytics.tracking.android.EasyTracker;

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginCompleteListener {

	private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		this.getSupportActionBar().setTitle(R.string.activity_login_title);
	}

	@Override
	public void onLoginComplete() {
		EasyTracker.getTracker().sendEvent(Constants.GA_CATEGORY_APP_FLOW, Constants.GA_APP_FLOW_SIGN_UP, "", null);
		startActivity(new Intent(LoginActivity.this, CheckInActivity.class));
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		LoginFragment loginFragment = (LoginFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_login);
		
	    //loginFragment.getUIHelper().onActivityResult(requestCode, responseCode, intent);
		if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
			
			if(loginFragment != null)
			{
				loginFragment.reconnectToPlus();
			}
		} 
	}
}
