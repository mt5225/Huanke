package com.huanke.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.huanke.api.soap.Constants;
import com.huanke.api.soap.KeyValue;
import com.huanke.api.soap.MessageParser;
import com.huanke.api.soap.SoapClient;

public class ProductService {
	private SoapClient soapClient;
	private static final Map<String, String> dummyAttr = new HashMap<String, String>() {
		private static final long serialVersionUID = 4083541861317888487L;

		{
			put("cost", "1.0");
			put("status", "1");
			put("price", "1.0");
			put("short_description", "shortDescription");
			put("description", "description");
			put("weight", "1.0");
			put("tax_class_id", "0");
			put("meta_description", "metaDescription");
			put("visibility", "4");
		}
	};

	public ProductService(SoapClient soapClient) {
		this.soapClient = soapClient;
	}

	private ArrayList<Product> getAllProduct() {
		ArrayList<Product> list = new ArrayList<Product>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		try {
			@SuppressWarnings("rawtypes")
			List soapResp = (List) soapClient.call(Constants.METHOD_CALL,
					ResourcePath.ProductList.getPath(), params);
			for (Object line : soapResp) {
				list.add(makeProduct(line.toString()));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Fetch all products
	 * 
	 * @return
	 */
	public ArrayList<Product> getAllProductFull() {
		ArrayList<Product> products = getAllProduct();
		for (Product p : products) {
			try {
				p = getProductDetailsByID(p.getProduct_id());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return products;
	}

	/**
	 * Get product details by id
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */

	public Product getProductDetailsByID(String id) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		params.put("args", id);
		soapClient.clearObjMap();
		Object soapResp = soapClient.call(Constants.METHOD_CALL,
				ResourcePath.ProductInfo.getPath(), params);
		String line = soapResp.toString();
		Product product = makeProduct(line);
		getMoreDetail(product, line);
		return product;
	}

	/**
	 * Parse soap message and construct product object
	 * 
	 * @param line
	 * @return
	 */
	private Product makeProduct(String line) {
		Product p = new Product();
		p.setProduct_id(MessageParser.getValueFromString("product_id", line));
		p.setSku(MessageParser.getValueFromString("sku", line));
		p.setName(MessageParser.getValueFromString("name", line));
		return p;
	}

	private void getMoreDetail(Product product, String line) {
		product.setDescription(MessageParser.getValueFromString("description",
				line));
		product.setMeta_description(MessageParser.getValueFromString(
				"meta_description", line));
		product.setShort_description(MessageParser.getValueFromString(
				"short_description", line));
	}

	/**
	 * Create a new product
	 * 
	 * @param product
	 * @return product_id
	 */
	public String CreateProduct(Product product) {
		try {
			soapClient.clearObjMap();
			soapClient.addMapping(new KeyValue());
			Object soapResp;
			soapResp = soapClient.call(Constants.METHOD_CALL,
					ResourcePath.ProductCreate.getPath(),
					addAttributeHelper(product));
			return (soapResp.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, Object> addAttributeHelper(Product product) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		Vector<Object> args = new Vector<Object>();
		args.add("simple");
		args.add(4);
		args.add(product.getSku());

		SoapObject so = new SoapObject(Constants.NAMESPACE_SOAPXML, "Map");
		KeyValue kv = new KeyValue("name", product.getName());
		so.addProperty("item", kv);

		// TODO add dummy data for future use
		for (String key : dummyAttr.keySet()) {
			kv = new KeyValue(key, dummyAttr.get(key));
			so.addProperty("item", kv);
		}

		args.add(so);
		params.put("args", args);
		return params;
	}

	/**
	 * delete product
	 * 
	 * @param product_id
	 * @return
	 */
	public Boolean deleteProduct(String product_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		params.put("args", product_id);
		try {
			soapClient.clearObjMap();
			soapClient.call(Constants.METHOD_CALL,
					ResourcePath.ProductDelete.getPath(), params);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
