package com.ck.weather.activity;

import com.ck.weather.application.MyApplication;
import com.ck.weather.util.C;
import com.ck.weather.util.LocateUtil;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OutLoginActivity extends Activity {
   private String LoginAction="TO_THE_ME_ACTIVITY";
   private Button outLogin;
   private ActionBar mActionBar;
   private ImageView backIV;
   private TextView tv_location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outlogin);
		((MyApplication)getApplicationContext()).addActivity(this);
		
		//标题栏设置
		mActionBar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
		backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
		tv_location=(TextView) mActionBar.getCustomView().findViewById(R.id.tv_location);
		tv_location.setText("登录");
		backIV.setOnClickListener(backClick);
				
		outLogin=(Button) findViewById(R.id.bt_outlogin);
		outLogin.setOnClickListener(backClick);
	}
	
	
	View.OnClickListener backClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.bt_outlogin:
				//清除登录缓存
				SharedPreferences mySharedPreferences= getSharedPreferences("user",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.clear();
				editor.commit();
				//退出登录,并跳转至我的信息界面
				/*Intent intent=new Intent(OutLogin.this,MainActivity.class);
				intent.setAction(LoginAction);
				sendBroadcast(intent);  //发送广播，跳到tabHost的第二个页面
				startActivity(intent);*/
				OutLoginActivity.this.finish();
			break;
			case R.id.tourism_back:
				OutLoginActivity.this.finish();
			break;
			default:
			break;
			}
		}
	};
}
