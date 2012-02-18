package com.huanke;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.huanke.api.Customer;
import com.huanke.api.CustomerService;
import com.huanke.api.soap.SoapClient;
import com.huanke.utils.DisplayUtils;


public class CustomerListAdapter extends ArrayAdapter<Customer> {
	private RootActivity rootActivity = null;
	private final LayoutInflater inflater;
	private List<Customer> customerList = null;
	private static final String LOG_TAG = CustomerListAdapter.class.getName();
			
	public CustomerListAdapter(Context context, ArrayList<Customer> items) {
		// Call through to ArrayAdapter implementation
		super(context, android.R.layout.simple_list_item_1,
				android.R.id.text1, items);
		if (context instanceof RootActivity) {
			rootActivity = (RootActivity) context;
		}
		inflater = LayoutInflater.from(getContext());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Inflate a new row if one isn’t recycled
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.customer_item, parent, false);
		}

		Customer customer = getItem(position);

		TextView textView = (TextView) convertView
				.findViewById(R.id.CustomerTitle);

		convertView.setBackgroundDrawable(null);
		convertView.getLayoutParams().height = DisplayUtils.convertToDIP(
				getContext(), 50);
		textView.setTextColor(R.color.black);
		textView.setText(customer.getFirstName() + " "
				+ customer.getLastName());
		return convertView;
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) {
				if (customerList != null) {
					for (Customer c : customerList) {
						add(c);
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
	
	
	public void getCustomerList() {
		if (rootActivity != null) {
			rootActivity.startIndeterminateProgressIndicator();
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(LOG_TAG, "load good list");
				fetchCustomers();
				if (customerList != null) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(-1);
				}
			}  
		}).start();
	}
	
	private void fetchCustomers () {
		Log.d(LOG_TAG, "loading customer list from server");
		SoapClient soapClient = new SoapClient();
		CustomerService customerService = new CustomerService(soapClient);
		customerList = customerService.getAllCustomer();
		soapClient.endSession();
	}
}