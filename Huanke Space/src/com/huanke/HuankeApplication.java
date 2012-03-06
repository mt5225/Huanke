package com.huanke;

import android.app.Application;
import android.util.Log;

public class HuankeApplication extends Application {

	private static final String LOG_TAG = HuankeApplication.class.getName(); 
	public static String customer_id = "1"; //TODO login to get id
	
	@Override
	public void onCreate() {
		Log.d(LOG_TAG, "application startted");
		super.onCreate();
	}
}
