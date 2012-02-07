package com.huanke;

import android.app.Application;
import android.util.Log;

public class HuankeApplication extends Application {

	private static final String LOG_TAG = HuankeApplication.class.getName();

	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "application startted");
		super.onCreate();
	}
}
