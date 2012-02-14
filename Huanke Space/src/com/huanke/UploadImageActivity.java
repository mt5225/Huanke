package com.huanke;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.huanke.api.Customer;
import com.huanke.api.CustomerService;
import com.huanke.api.ProductImageService;
import com.huanke.api.soap.SoapClient;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UploadImageActivity extends RootActivity{
	private static final String LOG_TAG = UploadImageActivity.class.getName();
	private String image_path = "";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "draw the upload image UI");
		super.onCreate(savedInstanceState);
		ViewGroup container = (ViewGroup) findViewById(R.id.TitleContent);
		ViewGroup.inflate(this, R.layout.upload_image, container);
		
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		image_path = extras.getString("IMAGE_PATH");
		ImageView imageView = (ImageView) findViewById (R.id.productImage);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(8, 8, 8, 8);      
		Bitmap bmp = BitmapFactory.decodeFile(image_path);
		imageView.setImageBitmap(bmp);
	}
	
	public void onUploadImage(View view) {
		try {			
			SoapClient soapClient = new SoapClient();
			CustomerService customerService = new CustomerService(soapClient);
			ProductImageService productImageService = new ProductImageService(
					soapClient);
			
			Customer customer = customerService
					.getCustomerDetail(HuankeApplication.customer_id);
			
			productImageService.uploadImageToProduct(
					customer.getPrefix(), image_path, "desc");
			soapClient.endSession();
		} catch (Exception e) {
			Log.d(LOG_TAG, e.toString());
		}
		
	}
}
