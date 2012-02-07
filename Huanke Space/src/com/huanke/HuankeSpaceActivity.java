package com.huanke;

import com.huanke.RootActivity;
import com.huanke.utils.DisplayUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	private ListView listView;
	private GoodsListAdapter goodListAdapter;

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
		goodListAdapter.getGoodList(0);

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
}