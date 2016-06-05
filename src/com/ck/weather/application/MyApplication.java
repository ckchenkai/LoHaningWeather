package com.ck.weather.application;

import java.util.LinkedList;
import java.util.List;

import com.ck.weather.category.Weather;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;


public class MyApplication extends Application {
	private Weather weather;
	private static MyApplication instance;  
	private List<Activity> activitys = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		weather = new Weather();

		activitys = new LinkedList<Activity>();
	}

	public synchronized static MyApplication getInstance(){      
        return instance;   
    }  
	
	public Weather getWeather() {
		return this.weather;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	// 遍历所有Activity并finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
	}

	


}
