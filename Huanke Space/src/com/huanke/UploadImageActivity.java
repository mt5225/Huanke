package com.huanke;

import java.util.UUID;

import com.huanke.api.Customer;
import com.huanke.api.CustomerService;
import com.huanke.api.ProductImageService;
import com.huanke.api.soap.SoapClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UploadImageActivity extends RootActivity {
	private static final String LOG_TAG = UploadImageActivity.class.getName();
	private String image_path = "";
	private final Activity activity = this;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "draw the upload image UI");
		super.onCreate(savedInstanceState);
		ViewGroup container = (ViewGroup) findViewById(R.id.TitleContent);
		ViewGroup.inflate(this, R.layout.upload_image, container);  //load upload_image UI
		ImageButton bi = (ImageButton) findViewById(R.id.MainSearchButton); //disable upload button
		bi.setVisibility(View.INVISIBLE);
		

		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			return;
		}
		image_path = extras.getString("IMAGE_PATH");
		ImageView imageView = (ImageView) findViewById(R.id.productImage);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setPadding(8, 8, 8, 8);
		Bitmap bmp = BitmapFactory.decodeFile(image_path);
		imageView.setImageBitmap(bmp);
	}

	/**
	 * Upload image to server
	 * @param view
	 */
	public void onUploadImage(View view) {
		final ProgressDialog dialog = ProgressDialog.show(this, "",
				"Goods Uploading ... ", true);
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				Log.d(LOG_TAG, "upload finished, go back to main UI");
				dialog.dismiss();
				activity.finish();
			}
		};
		Thread checkUpdate = new Thread() {
			public void run() {
				try {
					Log.d(LOG_TAG, "upload image to server");
					TextView titleText = (TextView) findViewById(R.id.good_description);
					SoapClient soapClient = new SoapClient();
					CustomerService customerService = new CustomerService(
							soapClient);
					ProductImageService productImageService = new ProductImageService(
							soapClient);

					Customer customer = customerService
							.getCustomerDetail(HuankeApplication.customer_id);

					productImageService.uploadImageToProduct(
							customer.getPrefix(), image_path, UUID.randomUUID() + "_" + titleText.getText());
					soapClient.endSession();
					
				} catch (Exception e) {
					Log.d(LOG_TAG, e.toString());
				}
				handler.sendEmptyMessage(0);
			}
		};
		checkUpdate.start();
	}
	
	public void onRefresh(View v) {
		Log.d(LOG_TAG, "do nothing");
	}
}
