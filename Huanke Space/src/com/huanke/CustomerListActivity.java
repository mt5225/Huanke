package com.huanke;

import java.util.ArrayList;
import com.huanke.api.Customer;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CustomerListActivity extends RootActivity implements
		OnItemClickListener {
	private static final String LOG_TAG = CustomerListActivity.class.getName();
	private ListView listView;
	RootActivity rootActivity = null;

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "create customer list view");
		ViewGroup container = (ViewGroup) findViewById(R.id.TitleContent);
		ViewGroup.inflate(this, R.layout.navigation, container);  //load customer list UI
		ImageButton bi = (ImageButton) findViewById(R.id.MainSearchButton); //disable upload button
		bi.setVisibility(View.INVISIBLE);
				
		ArrayList<Customer> customers = new ArrayList<Customer> ();
		listView = (ListView) findViewById(R.id.CustomerList);
		listView.setOnItemClickListener(this);
		CustomerListAdapter customerListAdapter = new CustomerListAdapter(this, customers);
		listView.setAdapter(customerListAdapter);
		customerListAdapter.getCustomerList();
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Customer customer = (Customer) parent.getAdapter().getItem(position);
		Log.d(LOG_TAG, "select customer id = " + customer.getCustomer_id());
		Intent intent = new Intent(parent.getContext(), HuankeSpaceActivity.class); 
		intent.putExtra("CUSTOMER_NAME", customer.getFirstName() + " " + customer.getLastName());
		GoodsListAdapter.current_customer_id =  customer.getCustomer_id();
		startActivity(intent);
	}
}
