package com.ck.weather.contact;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;

public class ListData extends AsyncTask<String, Void, String>{

	private HttpClient mHttpClient;
	private HttpGet mHttpGet;
	private String url;
	private HttpResponse mHttpResponse;
	private HttpEntity mHttpEntity;
	private InputStream in;
	private ListDataListener dataListener;
	
	public ListData(String url,ListDataListener dataListener) {
		// TODO Auto-generated constructor stub
		this.url=url;
		this.dataListener=dataListener;
	}
 
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			mHttpClient=(HttpClient) new DefaultHttpClient();
		mHttpGet=new HttpGet(url);
		mHttpResponse=mHttpClient.execute(mHttpGet);
		mHttpEntity=mHttpResponse.getEntity();
		in=mHttpEntity.getContent();
		BufferedReader br=new BufferedReader(new InputStreamReader(in));
		String line=null;
		StringBuffer sb=new StringBuffer();
		while((line=br.readLine())!=null){
			sb.append(line);
		}
		return sb.toString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		result=(result==null)?"Õ¯¬Á“Ï≥££¨ºÏ≤ÈÕ¯¬Á≈‰÷√≈∂£°":result;
		dataListener.getData(result);
	}
	
	
}
 