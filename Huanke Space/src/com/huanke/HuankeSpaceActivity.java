package com.huanke;

import com.huanke.RootActivity;
import com.huanke.utils.DisplayUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author qjiang Main activity to show my Goods
 */
public class HuankeSpaceActivity extends RootActivity implements
		OnItemClickListener {

	private static final String LOG_TAG = HuankeSpaceActivity.class.getName();
	private static final int SELECT_IMAGE = 1;
	private ListView listView;
	private GoodsListAdapter goodListAdapter;
	protected MenuDialog menuDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (getIntent() == null) {
			Log.d(LOG_TAG, "set default intent");
			setDefaultIntent();
		}
		super.onCreate(savedInstanceState);
		ViewGroup container = (ViewGroup) findViewById(R.id.TitleContent);
		ViewGroup.inflate(this, R.layout.title, container);

		// set title text
		// TODO move to a layout
		View titleBar = findViewById(R.id.TitleBar);
		TextView titleText = (TextView) findViewById(R.id.TitleText);
		titleText.setText("My Goods");
		titleBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.top_stories_title_background));
		titleBar.getLayoutParams().height = DisplayUtils.convertToDIP(this, 20);
		titleText
				.setTextColor(getResources().getColor(R.color.news_title_text));

		// build the goods list UI and content
		Log.d(LOG_TAG, "Build Goood List UI");
		container = (ViewGroup) findViewById(R.id.Content);
		ViewGroup.inflate(this, R.layout.news, container);
		listView = (ListView) findViewById(R.id.ListView01);
		listView.setOnItemClickListener(this);
		goodListAdapter = new GoodsListAdapter(this);
		listView.setAdapter(goodListAdapter);

		// load Good list
		Log.d(LOG_TAG, "Load Goood List");
		goodListAdapter.getGoodList();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(LOG_TAG, "item clicked =" + position);
	}

	/**
	 * When first starting up
	 */
	private void setDefaultIntent() {
		Intent i = new Intent(this, HuankeSpaceActivity.class);
		setIntent(i);
	}

	/**
	 * app exit
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(LOG_TAG, "keyCode = " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(LOG_TAG, "popup exit dialog");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Intent intent = new Intent(
											Intent.ACTION_MAIN);
									intent.addCategory(Intent.CATEGORY_HOME);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
									// activity.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}
		// Pass other events along their way up the chain
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * menu dialog
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (menuDialog == null) {
				menuDialog = new MenuDialog(this);
			}
			menuDialog.getWindow().setGravity(Gravity.BOTTOM);
			menuDialog.show();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * on refresh
	 */
	public void onRefresh(View v) {
		Log.d(LOG_TAG, "refresh the image list");
		if(goodListAdapter != null) {
			goodListAdapter.clear();
			goodListAdapter.getGoodList();
			listView.invalidateViews();
		}
	}

	/**
	 * on upload new image
	 */
	// Show the Forum Selector
	public void onUploadImage(View v) {
		Log.d(LOG_TAG, "image button clicked");
		startActivityForResult(new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
				SELECT_IMAGE);  //TODO choose existing image or start a camera

	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == SELECT_IMAGE) {
	            Uri selectedImageUri = data.getData();
	            String selectedImagePath = getPath(selectedImageUri);
	            Log.d(LOG_TAG, "user selection = " + selectedImagePath);
	            //start the image upload activity
	    	    Intent i = new Intent(this, UploadImageActivity.class);
	    	    i.putExtra("IMAGE_PATH", selectedImagePath);
	    	    Log.d(LOG_TAG, "start image uploader");
	    	    startActivity(i);
	        }
	    }
	   
	}

	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor
	            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
}