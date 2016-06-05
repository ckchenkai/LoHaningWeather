package com.ck.weather.cetenquery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.example.lohaningweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class QueryResult extends Activity {
	private Intent intent;
	private TextView name,school,type,id,allgread,tingli,yuedu,xiezuo;
    private String idData="",nameData="";
    private ProgressBar progress_bar;
    private LinearLayout linear_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_result);
		initView();
		//..
		intent=getIntent();
		idData=intent.getStringExtra("id");
        nameData=intent.getStringExtra("name");
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!Thread.interrupted()){
				NameValuePair[] data = {   new NameValuePair("id", "320202151215301"),  
                        new NameValuePair("name","陈凯")}; 
				PostData postData=new PostData("http://cet.99sushe.com/find", data, "GBK", true);
				String result=postData.getData();
				while(result!=null){
				Message msg=new Message();
				msg.what=1;
				msg.obj=result;
				handler.sendMessage(msg);
				}
			}
			}
		}).start();*/
		NameValuePair[] data = {   new NameValuePair("id",idData),  
                new NameValuePair("name",nameData)}; 
		PostData postData=new PostData("http://cet.99sushe.com/find", data, "GBK", true);
		postData.execute();
		
	}
	
	private void initView(){
		name=(TextView) findViewById(R.id.tv_name);
		school=(TextView) findViewById(R.id.tv_school);
		type=(TextView) findViewById(R.id.tv_type);
		id=(TextView) findViewById(R.id.tv_id);
		allgread=(TextView) findViewById(R.id.tv_allgread);
		tingli=(TextView) findViewById(R.id.tv_tingli);
		yuedu=(TextView) findViewById(R.id.tv_yuedu);
		xiezuo=(TextView) findViewById(R.id.tv_xiezuo);
		
		progress_bar=(ProgressBar) findViewById(R.id.progress_bar);
		linear_layout=(LinearLayout) findViewById(R.id.linear_layout);
	 
	}

	  
	
	  public class PostData extends AsyncTask<String, Void, String>{

		 private String url;
		 private NameValuePair[] data;
		 private String charset;
		 private boolean pretty;
		 public  PostData(String url,NameValuePair[] data,String charset,boolean pretty) {
			// TODO Auto-generated constructor stub
			this.url=url;
			this.data=data;
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			 StringBuffer response = new StringBuffer();
	         String inputLine = null ;
	         //这里使用post方法请求服务器
	         PostMethod  method = new PostMethod(url);  
	         method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);  
             //HTTP请求头
	         method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
	         method.setRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8");
	         method.setRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
	         method.setRequestHeader("Accept-Encoding", "gzip, deflate");
	         method.setRequestHeader("Referer", "http://cet.99sushe.com");
	         method.setRequestHeader("Connection", "keep-alive");
	         method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
	         //设置Http Post数据，data是NameValuePair[]类型变量，装载准考证号和姓名
	         method.setRequestBody(data);
	         try {
	         	    HttpClient client = new HttpClient();
	         	    client.setConnectionTimeout(5000);
	         	    int statusCode = client.executeMethod(method);  
	         	    if(statusCode == HttpStatus.SC_OK){  
	         	          InputStream in = method.getResponseBodyAsStream();  
	         	          BufferedReader reader = new BufferedReader(new InputStreamReader(in,method.getResponseCharSet()));  
	         	       
	         	         inputLine = reader.readLine();  
	         	          if(inputLine==null){  
	         	           System.out.println("系统异常");  
	         	           return "";  
	         	          }     
	         	     }
	         	     return inputLine;
	         	  }catch(Exception e){
	         	     e.printStackTrace(); 
	            }
				return "";
		}
		 
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress_bar.setVisibility(View.INVISIBLE);
			linear_layout.setVisibility(View.VISIBLE);
			//如果服务器查不到，就返回4
		
			    String data[]=result.split(",");
			    //如果解析的数组不包含7个数据，就采取方法
			    if(data.length!=7){
			    	Toast.makeText(QueryResult.this, "获取失败，网络异常或输入错误！", Toast.LENGTH_SHORT).show();
			    	for(int i=0;i<7;i++){
					  data=new String[7];
					  data[i]="";
			    	}
			    }
			    	name.setText(data[6]);
			    	school.setText(data[5]);
			    	type.setText("英语 CET-"+data[0]);
			    	id.setText(idData);
			    	allgread.setText("总分    "+data[4]);
			    	tingli.setText("听力   "+data[1]);
			    	yuedu.setText("阅读    "+data[2]);
			    	xiezuo.setText("写作及翻译   "+data[3]);
			    
		}
    }
	  
	  

}
