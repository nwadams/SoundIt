package ca.soundit.soundit.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import ca.soundit.soundit.R;
import ca.soundit.soundit.activities.SongListActivity;

public class CheckoutAlertDialogFragment extends DialogFragment{

	public static CheckoutAlertDialogFragment newInstance() {
		CheckoutAlertDialogFragment frag = new CheckoutAlertDialogFragment(); 
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
		.setTitle(R.string.alert_dialog_checkout_title)
		.setMessage(R.string.alert_dialog_checkout_message)
		.setPositiveButton(R.string.alert_dialog_checkout_ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((SongListActivity)CheckoutAlertDialogFragment.this.getActivity()).checkoutDialogAccept();
			}
		}
				)
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						CheckoutAlertDialogFragment.this.dismiss();
					}
				}
						)
						.create();
	}
}
