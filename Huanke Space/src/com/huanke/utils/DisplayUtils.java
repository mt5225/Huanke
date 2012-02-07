package com.huanke.utils;

import android.content.Context;

public class DisplayUtils {
	 public static int convertToDIP(Context context, int pixels) {
		    return Math.round(pixels * context.getResources().getDisplayMetrics()
		      .density);
		    // DIP
		  }
}
