package com.ck.weather.util;

import com.ck.weather.application.MyApplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DeviceUtil {

	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return
	 */
	public static int[] getMetrics() {
		int[] info = { 0, 0 };
		Resources resources = MyApplication.getInstance().getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		float density1 = dm.density;
		info[0] = dm.widthPixels;
		info[1] = dm.heightPixels;
		return info;
	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dp(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
    	  int result = 0;
    	  int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
    	  if (resourceId > 0) {
    	      result = context.getResources().getDimensionPixelSize(resourceId);
    	  }
    	  return result;
    	}
}
