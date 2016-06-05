package com.ck.weather.util;

import com.ck.weather.activity.WeatherFragment;
import com.ck.weather.category.ChuanYi;
import com.ck.weather.category.FangShai;
import com.ck.weather.category.GanMao;
import com.ck.weather.category.History;
import com.ck.weather.category.Today;
import com.ck.weather.category.Weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadWeatherThread extends Thread {
	private Weather weather;
	private Handler handler;
	private String cityName;
	
	public LoadWeatherThread(Handler handler, Weather weather, String cityName) {
		// TODO Auto-generated constructor stub
		this.weather = weather;
		this.handler = handler;
		this.cityName = cityName;
	}
	

	 //检查字符串是否为空
	 public String check(String x){
		 if(x.equals("")||x==null){
			 return "null";
		 }
			return x;
		}
	
	
	 //截取字符串格式
	 public String sub(String x){
		 if(x.equals("")||x==null){
			 return "0";
		 }
			x=x.substring(0,x.indexOf(("\u2103")));
			return x;
		}
	 
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 Message msg=new Message(); 
			try{
				weather.fresh(cityName);
				String []WeatherData=new String[25];
				WeatherData[0]=sub(weather.getWeatherInfo(cityName, Today.CURTEMP));
	            WeatherData[1]=check(weather.getWeatherInfo(cityName, Today.DATE)
						+"  "+ weather.getWeatherInfo(cityName, Today.WEEK));
	            WeatherData[2]=check(weather.getWeatherInfo(cityName, Today.AQI));
	            WeatherData[3]=sub(weather.getWeatherInfo(cityName, Today.HIGHTEMP));
	            WeatherData[4]=sub(weather.getWeatherInfo(cityName, Today.LOWTEMP));
	            WeatherData[5]=check(weather.getWeatherInfo(cityName, Today.TYPE));
	            WeatherData[6]=check(weather.getWeatherInfo(cityName, Today.FENGLI));
	          //未来几天天气情况
	            WeatherData[7]=sub(weather.getWeatherInfo(cityName, History.HIGHTEMP,-1));
	            WeatherData[8]=sub(weather.getWeatherInfo(cityName, History.LOWTEMP,-1));
	            WeatherData[9]=sub(weather.getWeatherInfo(cityName, Today.HIGHTEMP));
	            WeatherData[10]=sub(weather.getWeatherInfo(cityName, Today.LOWTEMP));
	            WeatherData[11]=sub(weather.getWeatherInfo(cityName, History.HIGHTEMP,1));
	            WeatherData[12]=sub(weather.getWeatherInfo(cityName, History.LOWTEMP,1));
	            WeatherData[13]=sub(weather.getWeatherInfo(cityName, History.HIGHTEMP,2));
	            WeatherData[14]=sub(weather.getWeatherInfo(cityName, History.LOWTEMP,2));
	            WeatherData[15]=sub(weather.getWeatherInfo(cityName, History.HIGHTEMP,3));
	            WeatherData[16]=sub(weather.getWeatherInfo(cityName, History.LOWTEMP,3));
	    		//未来几天天气情况所显示的图片  type_image(String type,ImageView imageView)
	            WeatherData[17]=weather.getWeatherInfo(cityName, History.TYPE,-1);
	            WeatherData[18]=weather.getWeatherInfo(cityName, Today.TYPE);
	            WeatherData[19]=weather.getWeatherInfo(cityName, History.TYPE,1);
	            WeatherData[20]=weather.getWeatherInfo(cityName, History.TYPE,2);
	            WeatherData[21]=weather.getWeatherInfo(cityName, History.TYPE,3);

	           WeatherData[22]=weather.getWeatherInfo(cityName,ChuanYi.DETAILS);//穿衣细节
	           WeatherData[23]=weather.getWeatherInfo(cityName,GanMao.DETAILS); //感冒注意
	           WeatherData[24]=weather.getWeatherInfo(cityName,FangShai.DETAILS); //防晒细节    
	            msg.what=WeatherFragment.REFRESH_SUCCESS;
	            msg.obj=WeatherData;
			}catch(Exception e){
				e.printStackTrace();
				msg.what=WeatherFragment.REFRESH_ERROR;
			}
			handler.sendMessage(msg);
	}
}
