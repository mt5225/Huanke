package com.huanke.api.soap;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class KeyValue implements KvmSerializable {
	private static final long serialVersionUID = -1106006770093411055L;
	private String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	private String value;
	
	public KeyValue() {
		
	}
	
	public KeyValue (String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public Object getProperty(int index) {
		switch(index){
		case 0:
			return this.key;
		case 1:
			return this.value;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@Override
	public void setProperty(int index, Object value) {
		switch(index){
		case 0:
			value = this.key;
		case 1:
			value = this.value;
		}
	}

	@Override
	public void getPropertyInfo(int index, Hashtable properties,
			PropertyInfo info) {
		switch(index){
		case 0:
			info.name = "key";
			info.type = PropertyInfo.STRING_CLASS;
			break;
		case 1:
		    info.name = "value";
		    info.type = PropertyInfo.STRING_CLASS;
		    break;
		default:break;
		}
		
	}
	

}
