package com.huanke.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.huanke.api.soap.Constants;
import com.huanke.api.soap.ImageHelper;
import com.huanke.api.soap.KeyImage;
import com.huanke.api.soap.KeyValue;
import com.huanke.api.soap.MessageParser;
import com.huanke.api.soap.SoapClient;

public class ProductImageService {

	private com.huanke.api.soap.SoapClient soapClient;
	private static final String LOG_TAG = ProductImageService.class.getName();

	public ProductImageService(SoapClient soapClient) {
		this.soapClient = soapClient;
	}

	/**
	 * upload image for product
	 * 
	 * @throws IOException
	 */
	public ProductImage uploadImageToProduct(String product_id, InputStream io) {
		try {
			soapClient.clearObjMap();
			soapClient.addMapping(new KeyValue());
			soapClient.addMapping(new KeyImage());
			Object soapResp;
			soapResp = soapClient.call(Constants.METHOD_CALL,
					ResourcePath.ProductAttributeMediaCreate.getPath(),
					uploadImageHelper(product_id, io));
			String file = soapResp.toString();
			ArrayList<ProductImage> imageList = listProductImage(product_id);
			for (ProductImage pimg : imageList) {
				if (pimg.getFile().equalsIgnoreCase(file)) {
					return pimg;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, Object> uploadImageHelper(String product_id,
			InputStream io) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		Vector<Object> args = new Vector<Object>();
		args.add(product_id);
		SoapObject so = new SoapObject(Constants.NAMESPACE_SOAPXML, "Map");
		SoapObject image = new SoapObject(Constants.NAMESPACE_SOAPXML, "Map");
		KeyImage ki = new KeyImage("file", image);
		KeyValue name = new KeyValue("name", "v2");
		KeyValue mime = new KeyValue("mime", "image/jpeg");
		KeyValue content = new KeyValue("content",
				ImageHelper.imageToBase64(io));
		image.addProperty("item", name);
		image.addProperty("item", content);
		image.addProperty("item", mime);
		so.addProperty("item", ki);
		KeyValue label = new KeyValue("label", "v2");
		so.addProperty("item", label);
		KeyValue exclude = new KeyValue("exclude", "0");
		so.addProperty("item", exclude);
		args.add(so);
		params.put("args", args);
		return params;

	}

	public ArrayList<ProductImage> listProductImage(String product_id) {
		ArrayList<ProductImage> list = new ArrayList<ProductImage>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sessionId", this.soapClient.getSessionID());
		params.put("args", product_id);
		try {
			soapClient.clearObjMap();
			@SuppressWarnings("unchecked")
			List<Object> soapResp = (List<Object>) soapClient.call(
					Constants.METHOD_CALL,
					ResourcePath.ProductAttributeMediaList.getPath(), params);
			for (Object line : soapResp) {
				list.add(parse(line.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(LOG_TAG, "fetch good image size = " + list.size());
		return list;
	}

	private ProductImage parse(String line) {
		ProductImage pi = new ProductImage();
		pi.setLabel(MessageParser.getValueFromString("label", line));
		pi.setPosition(MessageParser.getValueFromString("position", line));
		pi.setUrl(MessageParser.getValueFromString("url", line));
		pi.setFile(MessageParser.getValueFromString("file", line));
		return pi;
	}

	public Boolean deleteProductImage(String product_id, String position) {
		String file = "";
		ArrayList<ProductImage> imageList = listProductImage(product_id);
		for (ProductImage pi : imageList) {
			if (pi.getPosition().equalsIgnoreCase(position)) {
				file = pi.getFile();
			}
		}
		if (file.isEmpty()) {
			return false;
		} else {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionId", this.soapClient.getSessionID());
			Vector<Object> args = new Vector<Object>();
			args.add(product_id);
			args.add(file);
			params.put("args", args);
			try {
				soapClient.clearObjMap();
				soapClient.call(Constants.METHOD_CALL,
						ResourcePath.ProductAttributeMediaRemove.getPath(),
						params);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public ProductImage getProductImageByPosition(String product_id,
			String position) throws Exception {
		ArrayList<ProductImage> imageList = listProductImage(product_id);
		for (ProductImage pimg : imageList) {
			if (pimg.getPosition().equalsIgnoreCase(position)) {
				return pimg;
			}
		}
		return null;

	}

}
