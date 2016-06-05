package com.ck.weather.util;

import com.ck.weather.application.MyApplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DeviceUtil {

	/**
	 * ��ȡ��Ļ���
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
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dp(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**
     * ��pxֵת��Ϊspֵ����֤���ִ�С����
     * 
     * @param pxValue
     * @param fontScale
     *            ��DisplayMetrics��������scaledDensity��
     * @return
     */ 
    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
   
    /**
     * ��spֵת��Ϊpxֵ����֤���ִ�С����
     * 
     * @param spValue
     * @param fontScale
     *            ��DisplayMetrics��������scaledDensity��
     * @return
     */ 
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
    
    /**
     * ��ȡ״̬���߶�
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
