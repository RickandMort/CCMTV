package com.linlic.ccmtv.yx.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * name: base64加解密 author:Tom 2016-2-3下午5:43:41
 */
public class Base64utils {

	/**
	 * 
	 * name:加密 author:Tom 2016-2-3下午5:44:11
	 * 
	 * @param str 需要加密字段
	 *            
	 * @return 返回加密后的字符串
	 */
	public static String getBase64(String str) {
		byte[] bytes = null;
		String strBase64 = null;
		try {
			bytes = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes != null) {
			strBase64 = new String(Base64.encode(bytes, Base64.DEFAULT));
		}
//		Log.i("Test", "encode >>>" + strBase64);
		return strBase64;
	}

	/**
	 * name:解密 author:Tom 2016-2-3下午5:44:31
	 * 
	 * @param s 需要解密字段
	 *            
	 * @return 解密后的字符串
	 */
	public static String getFromBase64(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			try {
				result = new String(Base64.decode(s.getBytes(), Base64.DEFAULT));
			} catch (Exception e) {
				e.printStackTrace();
				result="{\"status\":0,\"errorMessage\":\"Base64解析失败\"}";
			}
		}
//		Log.i("Test", "decode >>>" + result);
		return result;
	}

}
