package com.huanke.api.soap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class SoapClient {
	private SoapConfig soapConfig = null;
	private String sessionID = "";
	@SuppressWarnings("rawtypes")
	private ArrayList objMap = new ArrayList();

	public SoapClient() {
		soapConfig = new SoapConfig();

		// do login to sure session valid
		this.login();
	}

	@SuppressWarnings("unchecked")
	public void addMapping(Object obj) {
		objMap.add(obj);
	}

	public void clearObjMap() {
		objMap.clear();
	}

	public String getSessionID() {
		return this.sessionID;
	}

	/**
	 * the method make soap call
	 * 
	 * @param method
	 * @param resourcePath
	 * @param params
	 * @return
	 * @throws Exception
	 * 
	 */
	public Object call(String method, String resourcePath,
			Map<String, Object> params) throws Exception {

		SoapObject request = new SoapObject(Constants.NAMESPACE_MAG, method);
		for (String item : params.keySet()) {
			request.addProperty((item), params.get(item));
		}

		if (method == Constants.METHOD_CALL) {
			request.addProperty("resourcePath", resourcePath);
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		for (Object obj : objMap) {
			envelope.addMapping(Constants.NAMESPACE_SOAPXML, "Map",
					obj.getClass());
		}

		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(
				soapConfig.getRemoteHost());
		androidHttpTransport
				.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		androidHttpTransport.call(method, envelope);
		return envelope.getResponse();

	}

	public void endSession() {
		if (this.sessionID.length() > 1) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionId", this.sessionID);
			try {
				this.call(Constants.METHOD_ENDSESSION,
						Constants.METHOD_ENDSESSION, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void login() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", soapConfig.getApiUser());
		params.put("apiKey", soapConfig.getApiKey());
		try {
			this.sessionID = (String) this.call(Constants.METHOD_LOGIN,
					Constants.METHOD_LOGIN, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
