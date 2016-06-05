package com.ck.weather.cetenquery;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ck.weather.activity.FindActivity;
import com.ck.weather.application.MyApplication;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CetQueryActivity extends Activity implements TextWatcher,OnClickListener {

	private Button queryB;
	private ImageView clear_id;
	private ImageView clear_name;
	private EditText id;
	private EditText name;
	private TextView tv_check;
	
	private ActionBar mActionBar;
	private ImageView backIV;
	private TextView tv_location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cetquery);
		((MyApplication)getApplicationContext()).addActivity(this);
		initView();
		id.addTextChangedListener(this);
		name.addTextChangedListener(this);
		clear_id.setOnClickListener(this);
		clear_name.setOnClickListener(this);
		queryB.setOnClickListener(this);
		
		//����������
		mActionBar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
		backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
		tv_location=(TextView) mActionBar.getCustomView().findViewById(R.id.tv_location);
		backIV.setOnClickListener(backClick);
		tv_location.setText("��������ѯ");
		 
		
	}
	private void initView() {
		// TODO Auto-generated method stub
		queryB=(Button) findViewById(R.id.queryB);
		clear_id=(ImageView) findViewById(R.id.clear_id);
		clear_name=(ImageView) findViewById(R.id.clear_name);
		id=(EditText) findViewById(R.id.et_id);
		name=(EditText) findViewById(R.id.et_name);
		tv_check=(TextView) findViewById(R.id.tv_check);
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
        tv_check.setText("");
		
        if(id.getText().toString().trim().equals("")||id.getText().toString().trim()==null){
        	clear_id.setVisibility(View.INVISIBLE);
        }else{
        	clear_id.setVisibility(View.VISIBLE);
        }
        if(name.getText().toString().trim().equals("")||name.getText().toString().trim()==null){
        	clear_name.setVisibility(View.INVISIBLE);
        }else{
        	clear_name.setVisibility(View.VISIBLE);
        }
		 
		 
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		      
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.clear_id:
			id.setText("");
			break;
		case R.id.clear_name:
			name.setText("");
			break;
		case R.id.queryB:
		     
		   //�ж�id������Ƿ�Ϊ15Ϊ׼��֤��
			    if(id.getText().toString().length()!=15){
							tv_check.setText("׼��֤�Ÿ�ʽ���ԣ�");
							id.requestFocus();
			    }else if(!checkNameChese(name.getText().toString())||name.getText().toString().length()<2){     ////�ж�name������Ƿ������Ϊ����
				            tv_check.setText("��������������ǰ�����֣�");
				            name.requestFocus();
			    }else{
				            tv_check.setText("");
				            Intent intent=new Intent(this,QueryResult.class);
				            intent.putExtra("id", id.getText().toString());
				            intent.putExtra("name", name.getText().toString());
			                startActivity(intent);
			}
			
			break;
		}
	}
	
	public boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

		return true;

		}

		return false;

		}

	public boolean checkNameChese(String name) {

		boolean res = true;

		char[] cTemp = name.toCharArray();

		for (int i = 0; i < name.length(); i++) {

		if (!isChinese(cTemp[i])) {

		res = false;

		break;

		}

		}

		return res;

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
			CetQueryActivity.this.finish();
		}
	};
	
	
 // ���ؼ�
 	
 	@Override
 	public boolean onKeyDown(int keyCode, KeyEvent event) {
 		// TODO Auto-generated method stub
 		this.finish();
 		overridePendingTransition(R.anim.in_leftright, R.anim.out_leftright);
 		return super.onKeyDown(keyCode, event);

 	}
}
