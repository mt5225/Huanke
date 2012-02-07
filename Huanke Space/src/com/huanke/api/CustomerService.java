package com.huanke.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.huanke.api.soap.Constants;
import com.huanke.api.soap.KeyValue;
import com.huanke.api.soap.MessageParser;
import com.huanke.api.soap.SoapClient;

public class CustomerService {
	private SoapClient soapClient;

	public CustomerService(SoapClient soapClient) {
		this.soapClient = soapClient;
	}

	public ArrayList<Customer> getAllCustomer() {

		ArrayList<Customer> customers = new ArrayList<Customer>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		try {
			soapClient.clearObjMap();
			@SuppressWarnings("rawtypes")
			List soapResp = (List) soapClient.call(Constants.METHOD_CALL,
					ResourcePath.CustomerList.getPath(), params);
			for (Object line : soapResp) {
				customers.add(makeCustomer(line.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customers;
	}

	public Customer getCustomerDetail(String customer_id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		Vector<Object> args = new Vector<Object>();
		args.add(customer_id);
		params.put("args", args);

		Object soapResp;
		soapClient.clearObjMap();
		soapResp = soapClient.call(Constants.METHOD_CALL,
				ResourcePath.CustomerInfo.getPath(), params);
		String line = soapResp.toString();
		Customer customer = makeCustomer(line);
		makeCustomerDetail(customer, line);
		return customer;
	}

	private Customer makeCustomer(String line) {
		Customer customer = new Customer();
		customer.setCustomer_id(MessageParser.getValueFromString("customer_id",
				line));
		customer.setFirstName(MessageParser.getValueFromString("firstname",
				line));
		customer.setLastName(MessageParser.getValueFromString("lastname", line));
		customer.setMiddleName(MessageParser.getValueFromString("customer_id",
				line));
		customer.setEmail(MessageParser.getValueFromString("email", line));
		customer.setPasswordHash(MessageParser.getValueFromString(
				"password_hash", line));
		return customer;
	}

	private void makeCustomerDetail(Customer customer, String line) {
		customer.setPrefix(MessageParser.getValueFromString("prefix", line));
		customer.setSuffix(MessageParser.getValueFromString("suffix", line));

	}

	public String createCustomerWithProduct(Customer customer) {
		String customer_id = createCustomer(customer);
		String uuid = UUID.randomUUID().toString();
		String product_id = createAssoiateProduct(uuid, customer_id);
		if (product_id != null) {
			updateCustomerPrefix(customer_id, uuid);
			return customer_id;
		}
		return null;
	}

	private String createAssoiateProduct(String uuid, String customer_id) {
		Product p1 = new Product();
		p1.setName(customer_id + ":" + uuid);
		String sku = uuid;
		p1.setSku(sku);
		try {
			ProductService productService = new ProductService(soapClient);
			String product_id = productService.CreateProduct(p1);
			return product_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Boolean updateCustomerPrefix(String customer_id, String uuid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		Vector<Object> args = new Vector<Object>();
		args.add(customer_id);
		SoapObject so = new SoapObject(Constants.NAMESPACE_SOAPXML, "Map");
		KeyValue kv = new KeyValue("prefix", uuid);
		so.addProperty("item", kv);
		args.add(so);
		params.put("args", args);
		try {
			soapClient.clearObjMap();
			soapClient.addMapping(new KeyValue());
			Object soapResp;
			soapResp = soapClient.call(Constants.METHOD_CALL,
					ResourcePath.CustomerUpdate.getPath(), params);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String createCustomer(Customer customer) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		Vector<Object> args = new Vector<Object>();
		SoapObject so = new SoapObject(Constants.NAMESPACE_SOAPXML, "Map");
		KeyValue kv = new KeyValue("firstname", customer.getFirstName());
		so.addProperty("item", kv);
		kv = new KeyValue("lastname", customer.getLastName());
		so.addProperty("item", kv);
		kv = new KeyValue("email", customer.getEmail());
		so.addProperty("item", kv);
		kv = new KeyValue("password_hash", customer.getPasswordHash());
		so.addProperty("item", kv);
		kv = new KeyValue("store_id", "0");
		so.addProperty("item", kv);
		kv = new KeyValue("website_id", "0");
		so.addProperty("item", kv);
		args.add(so);
		params.put("args", args);
		try {
			soapClient.clearObjMap();
			soapClient.addMapping(new KeyValue());
			Object soapResp;
			soapResp = soapClient.call(Constants.METHOD_CALL,
					ResourcePath.CustomerCreate.getPath(), params);
			return (soapResp.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean deleteCustomerWithAssoiatedProduct(Customer customer) {
		ProductService productService = new ProductService(soapClient);
		if (productService.deleteProduct(customer.getPrefix())) {
			return deleteCustomer(customer.getCustomer_id());
		} else {
			return false;
		}
	}

	private Boolean deleteCustomer(String customer_id) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionId", this.soapClient.getSessionID());
			params.put("args", customer_id);
			soapClient.clearObjMap();
			soapClient.call(Constants.METHOD_CALL,
					ResourcePath.CustomerDelete.getPath(), params);
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return false;
	}
}
