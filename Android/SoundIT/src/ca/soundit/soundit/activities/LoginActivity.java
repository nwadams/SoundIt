package ca.soundit.soundit.activities;

import android.content.Intent;
import android.os.Bundle;
import ca.soundit.soundit.Constants;
import ca.soundit.soundit.R;
import ca.soundit.soundit.fragments.LoginFragment;

import com.google.analytics.tracking.android.EasyTracker;

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginCompleteListener {

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
}
