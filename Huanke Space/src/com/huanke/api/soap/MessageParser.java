package com.huanke.api.soap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageParser {
	public static  String getValueFromString(String propertyName, String line) {
		String s1 = line.substring(line.indexOf(propertyName));
		String s2 = s1.substring(s1.indexOf("value=") - 1);
		String s3 = s2.substring(s2.indexOf("=") + 1, s2.length());
		String s4 = s3.substring(0, s3.indexOf(";"));
		return s4;
	}
	
	public static String md5sum(String password){
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			 md.update(password.getBytes());
			 
		        byte byteData[] = md.digest();
		 
		        //convert the byte to hex format method 1
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }
		 
		        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
       return null;
	}
}
