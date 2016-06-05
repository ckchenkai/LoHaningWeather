package com.ck.weather.activity;

import java.lang.reflect.Field;

import com.ck.weather.application.MyApplication;
import com.ck.weather.sql.UserDAO;
import com.ck.weather.util.DeviceUtil;
import com.ck.weather.widget.CircleImageView;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MeActivity extends Activity {

	//private String LoginAction="I_HAVE_LOGINED";
	private RelativeLayout meBox;
	private View statusBar;
	private ActionBar mActionBar;
	private ImageView action_back;
	private ImageView action_remove;
	private TextView register;
	private TextView headName;
	private CircleImageView headImage;
	private int Flag=0;
	private String username;
	private LinearLayout layout_editpwd;
	private LinearLayout layout_outlogin;
	private SharedPreferences sharedPreferences=null;
	private String check_history=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me);
		((MyApplication)getApplicationContext()).addActivity(this);
		init();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 //��ȡ�����¼��Ϣ
		sharedPreferences= getSharedPreferences("user",
				Activity.MODE_PRIVATE); 
	    check_history=sharedPreferences.getString("username",null);
		if(check_history!=null){
			headName.setText( check_history+"�����ã�");
			headName.setEnabled(false);  //���ò��ɵ��
			headName.setTextColor(R.color.location);
			headImage.setImageDrawable(getResources().getDrawable(R.drawable.server));
		}else{
			headName.setText("��¼");
			headName.setEnabled(true);  //���ò��ɵ��
			headName.setTextColor(R.color.dark_blue);
			headImage.setImageDrawable(getResources().getDrawable(R.drawable.rs));
		}
	}
	/*//�㲥����
	private BroadcastReceiver LoginReceiver=new  BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if(action.equals(LoginAction)){
				headName.setText(intent.getExtras().getString("user")+"�����ã�");
				headName.setEnabled(false);  //���ò��ɵ��
				headName.setTextColor(R.color.location);
				headImage.setImageDrawable(getResources().getDrawable(R.drawable.server));
			}
			
		}
	};*/
	
	private void init(){
		meBox = (RelativeLayout) findViewById(R.id.me_box);
		statusBar = findViewById(R.id.status_bar);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,DeviceUtil.getStatusBarHeight(this));
		statusBar.setLayoutParams(lp);
		register=(TextView) findViewById(R.id.register);
		register.setOnClickListener(myOnClick);
		headName=(TextView) findViewById(R.id.headName);
		headName.setOnClickListener(myOnClick);
		headImage=(CircleImageView) findViewById(R.id.headImage);
		headImage.setOnClickListener(myOnClick);
		layout_editpwd=(LinearLayout) findViewById(R.id.layout_editpwd);
		layout_editpwd.setOnClickListener(myOnClick);
		layout_outlogin=(LinearLayout) findViewById(R.id.layout_outlogin);
		layout_outlogin.setOnClickListener(myOnClick);
		
	}
	
	View.OnClickListener myOnClick=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.register:
				Intent intentR=new Intent(MeActivity.this,RegisterActivity.class);
				startActivity(intentR);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			case R.id.headImage:
			case R.id.headName: 
				Intent intentL=new Intent(MeActivity.this,LoginActivity.class);
				startActivity(intentL);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			case R.id.layout_editpwd:
				final EditText et = new EditText(MeActivity.this); 
				et.setBackground(null);
				et.setGravity(Gravity.CENTER);
				new AlertDialog.Builder(MeActivity.this).setIcon(R.drawable.xiugai).setView(et).setTitle("�޸�����")
				.setPositiveButton("ȷ��", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						UserDAO userDAO=new UserDAO(MeActivity.this); 
						try  
					    {  
                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
					        field.setAccessible(true);  
                            //����mShowingֵ����ƭandroidϵͳ  
					        field.set(dialog, false);  //��Ҫ�رյ�ʱ�� �������������Ϊtrue �Ի���ͻ��Զ��ر���
					        
					        if(check_history==null){
					        	Toast.makeText(MeActivity.this, "���ȵ�¼��", Toast.LENGTH_SHORT).show();
					        	field.set(dialog, true);
					        	dialog.dismiss();
					        	return;
					        }
					        if(et.getText()!=null&&!et.getText().toString().trim().equals("")){
								userDAO.updatePwd(check_history,et.getText().toString());
								Toast.makeText(MeActivity.this, "�����޸ĳɹ���", Toast.LENGTH_SHORT).show();
								field.set(dialog, true);
								return;
							}
                         }catch(Exception e) {  
                             e.printStackTrace();  
					     }  		
					}
				})
				.setNegativeButton("ȡ��", null).show();
				break;
			case R.id.layout_outlogin:
				Intent intent=new Intent(MeActivity.this,OutLoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
				break;
			default:
				break;
			}
		}
	};
	
	// ���ؼ��˳�
		// ���ؼ�
		private long mExitTime;

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				if (System.currentTimeMillis() - mExitTime > 1000) {
					Toast.makeText(this, "�ٰ�һ���˳���", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
					return true;
				} else {

					((MyApplication)getApplicationContext()).exit();
				
				}
			}
			return super.onKeyDown(keyCode, event);

		}
		
		/*@Override
		protected void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			//��ע���ҳ���ȡ�û���������  �ж��Ƿ��ѵ�¼
			SharedPreferences sharedPreferences= getSharedPreferences("user",
							Activity.MODE_PRIVATE); 
		    username=sharedPreferences.getString("username", "");
			Intent intent=getIntent();
			Flag=(int) intent.getExtras().get("JUST_REGISTERED");
			if(Flag==1){
				headName.setText(username+",����");
			}else{
				headName.setText("��¼");
				headName.setTextColor(getResources().getColor(R.color.dark_blue));
				headImage.setImageDrawable(getResources().getDrawable(R.drawable.server));
			}
		}*/
		
}
