package com.ck.weather.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class SPTool {

	private static final String SP_NAME = "yunqi_user_info";
	private static SharedPreferences.Editor editor;
	private static SharedPreferences sp;

	private SPTool() {

	}

	private static void init(Context context) {
		if (editor == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
			editor = sp.edit();
		}
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @param defualt
	 */
	public static int getInt(Context context, String key, int defValue) {
		init(context);
		return sp.getInt(key, defValue);
	}

	public static String getString(Context context, String key, String defValue) {
		init(context);
		return sp.getString(key, defValue);
	}

	public static boolean getBoolean(Context context, String key, boolean defValue) {
		init(context);
		return sp.getBoolean(key, defValue);
	}

	public static long getLong(Context context, String key, long defValue) {
		init(context);
		return sp.getLong(key, defValue);
	}

	public static float getFloat(Context context, String key, float defValue) {
		init(context);
		return sp.getFloat(key, defValue);
	}
	
	public static void putInt(Context context, String key, int value) {
		init(context);
		editor.putInt(key, value);
		editor.commit();
	}

	public static void putString(Context context, String key, String value) {
		init(context);
		editor.putString(key, value);
		editor.commit();
	}

	public static void putBoolean(Context context, String key, boolean value) {
		init(context);
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void putFloat(Context context, String key, float value) {
		init(context);
		editor.putFloat(key, value);
		editor.commit();
	}

	public static void putLong(Context context, String key, long value) {
		init(context);
		editor.putLong(key, value);
		editor.commit();
	}

	public static void clear(Context context) {
		init(context);
		editor.clear();
		editor.commit();
	}

	public static void remove(Context context, String key) {
		/*init(context);
		editor.remove(key);
		editor.commit();*/
		SharedPreferences sp = context.getSharedPreferences(SP_NAME, Activity.MODE_PRIVATE);  
		// »ñÈ¡Editor¶ÔÏó  
		Editor editor = sp.edit();  
		editor.remove(key);
		editor.commit();
	}

	/**
	 * è¯»å–å·²é?‰å›¾ç‰‡è·¯å¾?
	 */
	public static List<String> getSeletedImages(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("yunqi_image_path", Context.MODE_PRIVATE);
		String images = sharedPreferences.getString(key, "");
		List<String> list = new ArrayList<String>();
		if (TextUtils.isEmpty(images)) {
			return list;
		}
		String[] imgArr = images.split(":");
		for (String item : imgArr) {
			if (TextUtils.isEmpty(item)) {
				continue;
			}
			list.add(item);
		}
		return list;
	}

	/**
	 * è¯»å–å·²é?‰ç½‘ç»œå›¾ç‰‡è·¯å¾?
	 */
	public static List<String> getSeletedNetImages(Context context, String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("yunqi_trial_image_path", Context.MODE_PRIVATE);
		String images = sharedPreferences.getString(key, "");
		List<String> list = new ArrayList<String>();
		if (TextUtils.isEmpty(images)) {
			return list;
		}
		String[] imgArr = images.split(",");
		for (String item : imgArr) {
			if (TextUtils.isEmpty(item)) {
				continue;
			}
			list.add(item);
		}
		return list;
	}

	/**
	 * ä¿å­˜å·²é?‰å›¾ç‰‡è·¯å¾?
	 */
	public static void saveSelectedImags(Context context, String key, List<String> imgList) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("yunqi_image_path", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (imgList == null) {
			return;
		}
		StringBuffer result = new StringBuffer("");
		for (String item : imgList) {
			result.append(item).append(":");
		}
		editor.putString(key, result.toString());
		editor.commit();
	}
}
