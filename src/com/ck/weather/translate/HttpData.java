package com.ck.weather.translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpData {

	String urlString;
	String method;
	String data;
	public HttpData(String urlString,String method,String data){
		this.method=method;
		this.urlString=urlString;
		this.data=data;
	}
	public String getData(){
		
		try{
            // String path = "http://apix.sinaapp.com/postcode/?appkey=trailuser&code=211400";// 实验环境中使用pc的ip，不能用localhost或127.0.0.1 
			 String encodeData=URLEncoder.encode(data,"UTF-8");
			// String path = "http://howtospeak.org:443/api/k2c?user_key=dfcacb6404295f9ed9e430f67b641a8e &notrans=0&text="+encodeData;
			 URL url = new URL(urlString+encodeData);  
		     HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		//      conn.setRequestProperty("Accept-Charset", "GBK");  
		     conn.setRequestProperty("contentType", "GBK");  
	         conn.setConnectTimeout(5 * 1000);  
	         conn.setRequestMethod(method);  
	         InputStream inStream = conn.getInputStream();  
	//      readLesoSysXML(inStream);  
		          
	        BufferedReader in = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));  
	        StringBuffer buffer = new StringBuffer();  
	        String line = "";  
	        while ((line = in.readLine()) != null){  
	          buffer.append(line);  
	          buffer.append("\n");  
	        }  
		   String str = buffer.toString(); 
		   //System.out.print(str);
		   return str;
		}catch(IOException e){
			e.printStackTrace();
		}	
		return null;	
	}
}
