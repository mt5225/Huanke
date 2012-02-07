package com.huanke.api.soap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


public class ImageHelper {
	public static final String imageToBase64(InputStream fis){
		Bitmap bi = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] byteArrayImage = baos.toByteArray();
		String encodeImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		return encodeImage;
	}
}
