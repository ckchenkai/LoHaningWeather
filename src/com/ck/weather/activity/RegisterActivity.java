package com.ck.weather.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.a.a.a.a.b;
import com.ck.weather.application.MyApplication;
import com.ck.weather.sql.Tb_user;
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

public class RegisterActivity extends Activity {
	private ActionBar mActionBar;
	private ImageView backIV;
	private TextView tv_location;
	private EditText et_phone;
	private EditText et_pwd;
	private Button register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		((MyApplication)getApplicationContext()).addActivity(this);
		init();
		//����������
				mActionBar=getActionBar();
				C c=new C();
				c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
				backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
				tv_location=(TextView) mActionBar.getCustomView().findViewById(R.id.tv_location);
				tv_location.setText("ע��");
				backIV.setOnClickListener(backClick);
	}
	

	private void init(){
		et_phone=(EditText) findViewById(R.id.et_phone);
		et_pwd=(EditText) findViewById(R.id.et_pwd);
		register=(Button) findViewById(R.id.bt_register);
		register.setOnClickListener(backClick);
	}

	/**
	 * 
	 * @param ����ͼ��
	 * @param event
	 * @return
	 */
	View.OnClickListener backClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tourism_back:
				RegisterActivity.this.finish();
				break;
			case R.id.bt_register:
				//�ж�ע������ĸ�ʽ�Ƿ���ȷ
				String user=et_phone.getText().toString();
				String pwd=et_pwd.getText().toString();
				UserDAO userDAO=new UserDAO(RegisterActivity.this);
				if(isMobileNO(user)||emailFormat(user)){ 
					//�ж��˻��Ƿ����,���ھͲ�������һ��
					if(userDAO.find(user) != null){ 
						Toast.makeText(RegisterActivity.this, "�û����Ѵ��ڣ�", Toast.LENGTH_SHORT).show();
						return;
					} 
					if(pwd!=null&&!pwd.trim().equals("")){
						userDAO.add(new Tb_user(user, pwd));
						Toast.makeText(RegisterActivity.this, "ע��ɹ���", Toast.LENGTH_SHORT).show();
						//Ȼ����ת����¼ҳ��,�������û���������
						Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
						SharedPreferences mySharedPreferences= getSharedPreferences("JUST_REGISTERED",
								Activity.MODE_PRIVATE);
						SharedPreferences.Editor editor = mySharedPreferences.edit();
						editor.putString("user_register",user);
						editor.putString("pwd_register",pwd);
						editor.commit(); 
						startActivity(intent);
						
					}else{
						Toast.makeText(RegisterActivity.this, "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(RegisterActivity.this, "�û�����ʽ����ȷ��", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	/**
	 * 
	 * @param�ж��Ƿ�Ϊ�ֻ���
	 */
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
		}
	
	 /** 
     * ��֤����������ʽ�Ƿ���� 
     * @param email 
     * @return �Ƿ�Ϸ� 
     */ 
	public static boolean emailFormat(String email) 
    	{ 
			boolean tag = true; 
        	final String pattern1 = "^([a-z0-9A-Z]+[-|//.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?//.)+[a-zA-Z]{2,}$"; 
        	final Pattern pattern = Pattern.compile(pattern1); 
        	final Matcher mat = pattern.matcher(email); 
        	if (!mat.find()) { 
        		tag = false; 
        	} 
        	return tag; 
    	} 
} 

