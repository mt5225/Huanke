package com.huanke;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuDialog extends AlertDialog {
	private static final String LOG_TAG = MenuDialog.class.getName();

	public MenuDialog(Context context) {
		super(context);
		setTitle("请选择  ");
		View menu = getLayoutInflater().inflate(R.layout.menu, null);
		setView(menu);
		ImageButton uploadImage = (ImageButton) menu.findViewById(R.id.uploadImage);
		uploadImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d(LOG_TAG, v.toString());
				Toast.makeText(getContext(), "You clicked a button!",
						Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			dismiss();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

}
