package com.ck.weather.activity;

import com.ck.weather.application.MyApplication;
import com.ck.weather.sql.UserDAO;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private String LoginAction="I_HAVE_LOGINED";
	private ActionBar mActionBar;
	private ImageView backIV;
	private TextView tv_location;
	private EditText login_phone;
	private EditText login_pwd;
	private String username;
	private String userpwd;
	private Button login;
	private Boolean Flag=false;
	private TextView go_register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		((MyApplication)getApplicationContext()).addActivity(this);
		
		/*//从注册的页面获取用户名和密码
		SharedPreferences sharedPreferences= getSharedPreferences("user",
				Activity.MODE_PRIVATE); 
		username=sharedPreferences.getString("username", "");
		userpwd=sharedPreferences.getString("userpwd","");*/
		init();
		
	}
	
	/***
	 * 
	 * @param进行登陆验证
	 */
	private void login() {
		// TODO Auto-generated method stub
		String user=login_phone.getText().toString();
		String pwd=login_pwd.getText().toString();
		UserDAO userDAO=new UserDAO(LoginActivity.this);
		if(userDAO.find(user) != null){
			if(pwd.trim().equals("")||pwd==null){
				Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			if(userDAO.find(user).getUser().equals(user)&&userDAO.find(user).getPwd().equals(pwd)){
				Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
				//用于缓存
				SharedPreferences mySharedPreferences= getSharedPreferences("user",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putString("username",user);
				editor.putString("userpwd",pwd);
				editor.commit(); 
	
				//下次登陆时，不用手动输入用户名和密码
				SharedPreferences sharedPreferences= getSharedPreferences("JUST_REGISTERED",
						Activity.MODE_PRIVATE); 
				SharedPreferences.Editor editor2 = sharedPreferences.edit();
				editor2.putString("user_register",user);
				editor2.putString("pwd_register",pwd);
				editor2.commit(); 
				//跳转至登录后页面
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}else{
				Toast.makeText(LoginActivity.this, "密码不正确！", Toast.LENGTH_SHORT).show();
				login_pwd.setText(null);
			}
		}else{
			Toast.makeText(LoginActivity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
			login_phone.setText(null);
		}
	}

	private void init(){
		login_phone=(EditText) findViewById(R.id.login_phone);
		login_pwd=(EditText) findViewById(R.id.login_pwd);
		login=(Button) findViewById(R.id.bt_login);
		login.setOnClickListener(backClick);
		go_register=(TextView) findViewById(R.id.go_register);
		go_register.setOnClickListener(backClick);
		
		//如果是从注册界面跳转而来，不用手动输入用户名和密码
		SharedPreferences sharedPreferences= getSharedPreferences("JUST_REGISTERED",
				Activity.MODE_PRIVATE); 
		String check_u=sharedPreferences.getString("user_register",null);
		String check_p=sharedPreferences.getString("pwd_register", null);
		if(check_u!=null){
			login_phone.setText(check_u);
			login_pwd.setText(check_p);
		}
		
		
		//标题栏设置
		mActionBar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
		backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
		tv_location=(TextView) mActionBar.getCustomView().findViewById(R.id.tv_location);
		tv_location.setText("登录");
		backIV.setOnClickListener(backClick);
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
			switch (v.getId()) {
			case R.id.tourism_back:
				onBackPressed();
				break;
			case R.id.bt_login:
				login();
				break;
			case R.id.go_register:
				Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			default:
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.in_leftright, R.anim.out_leftright);
	};
}
