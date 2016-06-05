package com.ck.weather.contact;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import com.ck.weather.activity.FindActivity;
import com.ck.weather.application.MyApplication;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class contactActivity extends Activity implements ListDataListener,OnClickListener{

	private ListView list_view;
	private EditText edit_text;
	private ImageView send;
	private String url="http://www.tuling123.com/openapi/api?key=6af9822f5491fadfc142b53818bbd63a&info=ll";
	private String data;
	private ListData mListData=null;
	private List<MyData> myList;
	//http://dev.skjqr.com/api/weixin.php?email=910663958@qq.com&appkey=ee892a0fab3e9bde200df6530fa597a9&msg=
	private int LEFT_CONTACT=0;
	private int RIGHT_CONTACT=1;
	private ListView lv;
	private MyAdapter myAdapter;
	private Button send_bt2;
	private String [] welcome;
	private double currentTime=0,oldTime=0;
	private TextView edit_line;
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		((MyApplication)getApplicationContext()).addActivity(this);
		 
 		
 		init();
 		lv.setAdapter(myAdapter);
 		
 		 
 		edit_text.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(!edit_text.getText().toString().trim().equals("")){
				    send_bt2.setVisibility(View.VISIBLE);
				    send.setVisibility(View.GONE);
				}else {
			        send_bt2.setVisibility(View.GONE);
					send.setVisibility(View.VISIBLE);
				}
				edit_line.setBackgroundColor(Color.GREEN);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void init(){
		edit_line=(TextView) findViewById(R.id.edit_line);
		edit_text=(EditText) findViewById(R.id.edit_text);
		send=(ImageView) findViewById(R.id.send_bt);
		send_bt2=(Button) findViewById(R.id.send_bt2);
		//System.out.print("data");
		myList=new ArrayList<MyData>();
		lv=(ListView) findViewById(R.id.list_view);
		send_bt2.setOnClickListener(this);
		myAdapter=new MyAdapter(myList, contactActivity.this);
		
		 
 		MyData wel=new MyData(getWelcomeTips()==null?"null":getWelcomeTips(),LEFT_CONTACT,getTime());
 		myList.add(wel);
 		myAdapter.notifyDataSetChanged();
 		
 		if (getActionBar() != null) 
 			getActionBar().setTitle(""); 		
 		//getActionBar().setTitle("图灵机器人");
 		ActionBar actionBar = getActionBar(); 
 		actionBar.setDisplayShowHomeEnabled(false); 
 		actionBar.setDisplayShowCustomEnabled(true);  
 		actionBar.setCustomView(R.layout.contact_actionbar); //设置自定义布局界面                
 		View actionBarView = actionBar.getCustomView();  //获取自定义View进行设置
 		back=(ImageView) actionBarView.findViewById(R.id.back);
 		back.setOnClickListener(backClick);
		 
	}

	@Override
	public void getData(String data) {
		// TODO Auto-generated method stub
	   PgetText(data); 
	}
	private void  PgetText(String str){
		try {
			JSONObject js=new JSONObject(str);
		    MyData myData=new MyData(js.getString("text"),LEFT_CONTACT,getTime());
		    myList.add(myData);
		    //数据更新
		    myAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
			//Log.e("error","出错啦");
		} 
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 //判断输入框不为空

 		if(edit_text.getText().toString().trim().equals("")){
			Toast.makeText(this, "输入不能为空！", Toast.LENGTH_SHORT).show();;
		}
 		
 		 
 		
		String myWord=edit_text.getText().toString();
		MyData myData=new MyData(myWord,RIGHT_CONTACT,getTime());
		myList.add(myData);
		myAdapter.notifyDataSetChanged();
		edit_text.setText(null);
		mListData=(ListData) new ListData(url+myWord, this).execute();
	}


     private String getWelcomeTips(){
    	//欢迎语
 		welcome=new String[]{"主人，图灵机器人想死您了！","官人，奴家在此恭候多时了","只见新人笑，哪闻旧人哭！",
 						"没有你的日子里，我天天打飞机。","WELCOME BACK!萨拉黑！"};
 		//添加欢迎语
  		int index=(int) (Math.random()*welcome.length);
  		return welcome[index];
     }
     
     private String getTime(){
    	 currentTime=System.currentTimeMillis();
    	 SimpleDateFormat format=new SimpleDateFormat("MM月dd日  HH:mm");
    	 Date curDate=new Date();
    	 String str=format.format(curDate);
    	 if(currentTime-oldTime>=2*60*1000){
    		 oldTime=currentTime;
    		 return str;
    	 }
    	 return "";
     }
     
  // 返回键
 	
 	@Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		// TODO Auto-generated method stub
 		this.finish();
 		overridePendingTransition(R.anim.in_leftright, R.anim.out_leftright);
 		return super.onKeyDown(keyCode, event);

 	}
 	
 	/**
	 * 
	 * @param 返回图标
	 * @param event
	 * @return
	 */
	View.OnClickListener backClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			contactActivity.this.finish();
		}
	};
}
