package com.ck.weather.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class LocateUtil {

	/**
	 * 裁剪地址
	 * 
	 * @param address
	 */
	public static String cropAddress(String address) {
		if (address.indexOf("省") != -1) {
			if (address.indexOf("区") != -1) {
				address = address.substring(address.indexOf("省") + 1,
						address.indexOf("区") + 1);
			} else if (address.indexOf("县") != -1) {
				address = address.substring(address.indexOf("省") + 1,
						address.indexOf("县") + 1);
			} else if (address.indexOf("市", 8) != -1) {
				address = address.substring(address.indexOf("省") + 1,
						address.indexOf("市", 8) + 1);
			} else if (address.indexOf("省") != -1) {
				address = address.substring(0, address.indexOf("省") + 1);
			}
		} else {
			if (address.indexOf("市") != -1 && address.indexOf("区") != -1) {
				address = address.substring(0, address.indexOf("区") + 1);
			}
		}
		if (address.length() > 10) {
			address = address.substring(0, 11);
		}
		return address;
	}

	/**
	 * 判断有无定位权限
	 * @param context
	 * @return
	 */
	public static boolean isLocatePermission(Context context) {
		PackageManager pm = context.getPackageManager();
		boolean flag = (PackageManager.PERMISSION_GRANTED == 
				pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION","com.example.lohaningweather"));
		boolean flag2 = (PackageManager.PERMISSION_GRANTED == 
				pm.checkPermission("android.permission.ACCESS_FINE_LOCATION","com.example.lohaningweather"));
		return flag&&flag2;
		
	}
}
