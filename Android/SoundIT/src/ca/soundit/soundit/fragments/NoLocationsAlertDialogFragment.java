package ca.soundit.soundit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ca.soundit.soundit.R;

public class NoLocationsAlertDialogFragment extends DialogFragment{

	public static NoLocationsAlertDialogFragment newInstance() {
		NoLocationsAlertDialogFragment frag = new NoLocationsAlertDialogFragment(); 
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.alert_dialog_no_locations_title)
		.setMessage(R.string.alert_dialog_no_locations_message)
		.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dismiss();
			}
		}
				).create();
	}
}
