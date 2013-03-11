package ca.soundit.soundit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ca.soundit.soundit.R;
import ca.soundit.soundit.activities.SongListActivity;

public class LogoutAlertDialogFragment extends DialogFragment{

	public static LogoutAlertDialogFragment newInstance() {
		LogoutAlertDialogFragment frag = new LogoutAlertDialogFragment(); 
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.alert_dialog_logout_title)
		.setMessage(R.string.alert_dialog_logout_message)
		.setPositiveButton(R.string.alert_dialog_logout_ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((SongListActivity)LogoutAlertDialogFragment.this.getActivity()).logoutDialogAccept();
			}
		}
				)
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						LogoutAlertDialogFragment.this.dismiss();
					}
				}
						)
						.create();
	}
}
