package model.util;

import android.app.Activity;
import android.app.Dialog;
import cz.smartfine.R;

public class Login {

	public static boolean loginHard(Activity a) {
		
		
		Dialog dialog = new Dialog(a);
		dialog.setContentView(R.layout.login_hard);
		dialog.setTitle("Pøihlášení:");
		dialog.show();
		
		
//		Builder adb = new Builder();
//		adb.
//		adb.
//		adb.setView();
//		adb.setNegativeButton(context.getText(R.string.no).toString(), null);
//		adb.setPositiveButton(context.getText(R.string.yes).toString(), new AlertDialog.OnClickListener() {
//
//			public void onClick(DialogInterface dialog, int which) {
//
//				// Vymaze polozku s arraylistu a refreshne seznam
//				items.remove(position);
//				notifyDataSetChanged();
//			}
//		});
//		adb.show();
		
		return true;
	}
}
