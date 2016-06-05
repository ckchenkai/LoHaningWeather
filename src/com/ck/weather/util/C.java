package com.ck.weather.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;


public class C {
	//public static boolean isChange = false;
	//public static List<String> mCityList = new ArrayList<>();
	//public static List<Fragment> mPageList  = new ArrayList<>();
	public static int mPosition = 0;
    public int []typeList; 
	public void getAction_Bar(int layout_id,ActionBar mActionBar){
		 
		//标题栏
	 	//mActionBar=getActionBar();
	 	if(mActionBar!=null){
	 		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	 		mActionBar.setDisplayShowCustomEnabled(true);
	 		mActionBar.setCustomView(layout_id);
	 		/*a=(ImageView) mActionBar.getCustomView().findViewById(a_id);
	 		b=(ImageView) mActionBar.getCustomView().findViewById(b_id);*/
	 	}
	}
	
	/**
	 * 
	 * @param 为不同的天气状况配图
	 * @param context
	 */
	public void imageSet(ImageView imageView,Context context,int i){
		typeList=new int[]{R.drawable.biz_plugin_weather_baoxue,R.drawable.biz_plugin_weather_baoyu, R.drawable.biz_plugin_weather_dabaoyu,
				 R.drawable.biz_plugin_weather_daxue,R.drawable.biz_plugin_weather_dayu,R.drawable.biz_plugin_weather_duoyun,
				 R.drawable.biz_plugin_weather_leizhenyu,R.drawable.biz_plugin_weather_leizhenyubingbao,R.drawable.biz_plugin_weather_qing,
				 R.drawable.biz_plugin_weather_shachenbao,R.drawable.biz_plugin_weather_tedabaoyu,R.drawable.biz_plugin_weather_wu,
				 R.drawable.biz_plugin_weather_xiaoxue,R.drawable.biz_plugin_weather_xiaoyu,R.drawable.biz_plugin_weather_yin,
				 R.drawable.biz_plugin_weather_yujiaxue,R.drawable.biz_plugin_weather_zhenxue,R.drawable.biz_plugin_weather_zhongyu,
				 R.drawable.biz_plugin_weather_zhongxue,R.drawable.biz_plugin_weather_zhongyu,R.drawable.biz_plugin_weather_qing};
		imageView.setImageDrawable(context.getResources().getDrawable( typeList[i]));
	}
	
	/**
	 * 反射方法获取状态栏高度
	 * 
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int statusBarHeight = 20;
		try {
			Class<?> _class = Class.forName("com.android.internal.R$dimen");
			Object object = _class.newInstance();
			Field field = _class.getField("status_bar_height");
			int restult = Integer.parseInt(field.get(object).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(
					restult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Toast.makeText(getActivity(), "StatusBarHeight = " + statusBarHeight,
		// Toast.LENGTH_SHORT).show();
		return statusBarHeight;
	}
	
	/**
	 * 获取手机屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		wm.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	
	/**
	 * 
	 * 将数组转化为json字符串 ，保存在sharedPreferences中，下次从中读取
	 * 
	 */
	public static void saveLastDataAsArray(Context context,String[] WeatherArray,String cityName) {
		if(WeatherArray==null){
			return;
		}
	    SharedPreferences prefs = context.getSharedPreferences(cityName, Context.MODE_PRIVATE);
	    JSONArray jsonArray = new JSONArray();
	    for (String b : WeatherArray) {
	      jsonArray.put(b);
	    }
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString("last_weather",jsonArray.toString());
	    editor.commit();
	  }
	
	/**
	 * 
	 * 将json字符串转化为数组  ，从sharedPreferences中读取，作为缓存
	 * 
	 */
	public static String[] getLastDataArray(Context context,int arrayLength,String cityName)
	  {
	    SharedPreferences prefs = context.getSharedPreferences(cityName, Context.MODE_PRIVATE);
	    String[] resArray=new String[arrayLength]; 
	    
	    try {
	        JSONArray jsonArray = new JSONArray(prefs.getString("last_weather", null));
	        for (int i = 0; i < jsonArray.length(); i++) {
	        	resArray[i] = jsonArray.getString(i);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    	        return resArray;
	  }

}
