package com.huanke;

import java.util.List;
import com.huanke.ImageThreadLoader;
import com.huanke.api.CustomerService;
import com.huanke.api.Customer;
import com.huanke.api.ProductImage;
import com.huanke.api.ProductImageService;
import com.huanke.api.soap.SoapClient;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsListAdapter extends ArrayAdapter<ProductImage> {
	private static final String LOG_TAG = GoodsListAdapter.class.getName();
	private final LayoutInflater inflater;
	private RootActivity rootActivity = null;
	private List<ProductImage> goodList = null;
	private final ImageThreadLoader imageLoader;

	public GoodsListAdapter(Context context) {
		super(context, R.layout.news_item);
		if (context instanceof RootActivity) {
			rootActivity = (RootActivity) context;
		}
		inflater = LayoutInflater.from(getContext());
		imageLoader = ImageThreadLoader.getOnDiskInstance(context);
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				if (goodList != null) {
					for (ProductImage s : goodList) {
						add(s);
					}
				}
			} else {
				Toast.makeText(
						rootActivity,
						rootActivity.getResources().getText(
								R.string.msg_check_connection),
						Toast.LENGTH_LONG).show();
			}
			if (rootActivity != null) {
				rootActivity.stopIndeterminateProgressIndicator();
			}

		}
	};

	public void getGoodList() {
		if (rootActivity != null) {
			rootActivity.startIndeterminateProgressIndicator();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(LOG_TAG, "load good list");
				fetchGoods();
				if (goodList != null) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(-1);
				}
			}  
		}).start();
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.news_item, parent, false);
		}

		ProductImage good = getItem(position);
		Log.d(LOG_TAG, "current good = " + good.toString());
		if (good != null) {
			// load text
			TextView topic = (TextView) convertView
					.findViewById(R.id.NewsItemTopicText);
			TextView name = (TextView) convertView
					.findViewById(R.id.NewsItemNameText);
			name.setText(Html.fromHtml(good.getLabel()));
			name.setTypeface(name.getTypeface(), Typeface.BOLD);
			name.setVisibility(View.VISIBLE);
			topic.setText(Html.fromHtml(good.getUrl()));
			topic.setVisibility(View.VISIBLE);

			// load image
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.NewsItemIcon);
			final ImageView image = (ImageView) convertView
					.findViewById(R.id.NewsItemImage);

			String imageUrl = good.getUrl();
			if (imageUrl != null) {
				Drawable cachedImage = imageLoader.loadImage(imageUrl,
						new ImageLoadListener(position, (ListView) parent));

				image.setImageDrawable(cachedImage);
				image.setVisibility(View.VISIBLE);
			} else {
				image.setVisibility(View.GONE);
			}
		}
		return convertView;

	}

	private void fetchGoods() {
		Log.d(LOG_TAG, "fetch Goods");
		SoapClient soapClient = new SoapClient();
		CustomerService customerService = new CustomerService(soapClient);
		ProductImageService productImageService = new ProductImageService(
				soapClient);
		try {
			Customer customer = customerService
					.getCustomerDetail(HuankeApplication.customer_id);
			goodList = productImageService.listProductImage(customer
					.getPrefix());

		} catch (Exception e) {
			e.printStackTrace();
		}
		soapClient.endSession();
	}

	/**
	 * private class implements load image listener
	 * 
	 * @author qjiang
	 * 
	 */
	private class ImageLoadListener implements
			ImageThreadLoader.ImageLoadedListener {

		private int position;
		private ListView parent;

		public ImageLoadListener(int position, ListView parent) {
			this.position = position;
			this.parent = parent;
		}

		public void imageLoaded(Drawable imageBitmap) {
			View itemView = parent.getChildAt(position
					- parent.getFirstVisiblePosition());
			if (itemView == null) {
				Log.w(LOG_TAG, "Could not find list item at position "
						+ position);
				return;
			}
			ImageView img = (ImageView) itemView
					.findViewById(R.id.NewsItemImage);
			if (img == null) {
				Log.w(LOG_TAG, "Could not find image for list item at "
						+ "position " + position);
				return;
			}
			Log.d(LOG_TAG, "Drawing image at position " + position);
			img.setImageDrawable(imageBitmap);
		}
	}
}
