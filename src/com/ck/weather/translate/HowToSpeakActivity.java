package com.ck.weather.translate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import com.ck.weather.activity.FindActivity;
import com.ck.weather.application.MyApplication;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.wifi.WifiConfiguration.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class HowToSpeakActivity extends Activity implements android.view.View.OnClickListener,OnItemSelectedListener
,TextWatcher,OnInitListener{

	private static int LV=0;
	private String urlE="http://howtospeak.org:443/api/e2c?user_key=dfcacb6404295f9ed9e430f67b641a8e &notrans=0&text=";
	private String urlJ="http://howtospeak.org:443/api/j2c?user_key=dfcacb6404295f9ed9e430f67b641a8e &notrans=0&text=";
	private String urlK="http://howtospeak.org:443/api/k2c?user_key=dfcacb6404295f9ed9e430f67b641a8e &notrans=0&text=";
	private String url;
	private String GET="GET";
	private String POST="POST";
	private final int DETDATA_SUCCESS = 1;
	private final int DETDATA_ERROR = 2;
	private String data;
	//判断当前spinner项的标志
	private String FLAG="english";
	private String []FLAG_ALL=new String[]{"english","japanese","korean"};
    //spinner图片
	private List<Integer> list=new ArrayList<Integer>();
	private List<Integer> list2=new ArrayList<Integer>();
	
	private EditText cn_speak;
	private TextView foreign_speak;
	private TextView cn_speak_xieyin;
	private Spinner country_spinner;
	private Spinner c_spinner;
	private Button queryB;
	private ImageView refresh_iv;
	private ImageView clear_input;
	
	private ActionBar mActionBar;
    private ImageView backIV;
    private TextView tv_location;
	//语音朗读
	private TextToSpeech mSpeech;
	private ImageView read;
	private ImageView speak_cn;
	private ImageView speak_xieyin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.howtospeak);
		((MyApplication)getApplicationContext()).addActivity(this);
		initView();
		//FLAG_URL();
		 
		//标题栏设置
				mActionBar=getActionBar();
				C c=new C();
				c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
				backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
				tv_location=(TextView) mActionBar.getCustomView().findViewById(R.id.tv_location);
				backIV.setOnClickListener(backClick);
				tv_location.setText("谐音学外语");
		
		queryB.setOnClickListener(this);
		country_spinner.setOnItemSelectedListener(this);
		cn_speak.addTextChangedListener(this);
		clear_input.setOnClickListener(this);
		speak_cn.setOnClickListener(this);
		speak_xieyin.setOnClickListener(this);
		//spinner适配
		list.add(R.drawable.flag_en);
		list.add(R.drawable.flag_jp);
		list.add(R.drawable.flag_ko);
		MySpinnerAdapter myAdapter=new MySpinnerAdapter(list, HowToSpeakActivity.this);
		country_spinner.setAdapter(myAdapter);
		
		
		list2.add(R.drawable.flag_cn);
		list2.add(R.drawable.flag_en);
		MySpinnerAdapter myAdapter2=new MySpinnerAdapter(list2, HowToSpeakActivity.this);
		c_spinner.setAdapter(myAdapter2);
		
	//语音朗读
		 mSpeech=new TextToSpeech(this,this);
		 read.setOnClickListener(this);
	}
	
	
	public void initView(){
		cn_speak=(EditText) findViewById(R.id.cn_speak);
		cn_speak_xieyin=(TextView) findViewById(R.id.cn_speak_xieyin);
		foreign_speak=(TextView) findViewById(R.id.foreign_speak);
		country_spinner=(Spinner) findViewById(R.id.country_spinner);
		c_spinner=(Spinner) findViewById(R.id.c_spinner);
		queryB=(Button) findViewById(R.id.queryB);
		queryB.setEnabled(false);
		refresh_iv=(ImageView) findViewById(R.id.refresh_iv);
		refresh_iv.setVisibility(View.INVISIBLE);
		//清空输入框
		clear_input=(ImageView) findViewById(R.id.clear_input);
		clear_input.setVisibility(View.INVISIBLE);
		
		//语音朗读
		read=(ImageView) findViewById(R.id.read);
		speak_cn=(ImageView) findViewById(R.id.speak_cn);
		speak_xieyin=(ImageView) findViewById(R.id.speak_xieyin);
		read.setEnabled(false);
		speak_cn.setEnabled(false);
		speak_xieyin.setEnabled(false);
	}
	
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int tag=msg.what;
			switch (tag) {
			case DETDATA_SUCCESS:
				//ArrayList<String> a=parseJSON(msg.obj.toString());
				//String a=JString[0];
				foreign_speak.setText(parseJSON(msg.obj.toString())[0]);
				cn_speak_xieyin.setText(parseJSON(msg.obj.toString())[1]);
				//Toast.makeText(MainActivity.this,msg.obj.toString(), Toast.LENGTH_LONG).show();
				
				refresh_iv.clearAnimation();
				refresh_iv.setVisibility(View.INVISIBLE);
				break;
			case DETDATA_ERROR:
				foreign_speak.setText("");
				cn_speak_xieyin.setText("");
				Toast.makeText(HowToSpeakActivity.this,"网络异常！", Toast.LENGTH_SHORT).show();
				refresh_iv.clearAnimation();
				refresh_iv.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
	};
	
	
	//JSON解析数据
	private String [] parseJSON(String str){
		String[] JString ={" "," "};
		try{
			JSONObject js=new JSONObject(str);		
			JString[0]=js.getString(FLAG);
			JString[1]=js.getString("chinglish");
			
		}catch(Exception e){
		   e.printStackTrace();
		}
		return JString;
	}
	
	
	//spinner的适配器
	private class MySpinnerAdapter extends BaseAdapter{
		private List<Integer> list;
		private Context context;
		private ImageView imageV;

	    public MySpinnerAdapter(List<Integer> list,Context  context) {
			// TODO Auto-generated constructor stub
			this.list=list;
			this.context=context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater layoutInflater=LayoutInflater.from(context);
			v=layoutInflater.inflate(R.layout.country_spinner, null);
			imageV=(ImageView)v.findViewById(R.id.imageV);
			
			imageV.setImageResource(list.get(position));
			return v;
		}
		
	}

//查询按钮
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.queryB:
			
			refresh_iv.setVisibility(View.VISIBLE);  //设置刷新图标可见
			//设置refresh_iv动画效果
			RotateAnimation animation =new RotateAnimation(0f,360f,Animation.RELATIVE_TO_SELF,
					0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
	        animation.setInterpolator(new LinearInterpolator());  
	        animation.setDuration(500);  
	        animation.setRepeatCount(Animation.INFINITE);;  
	        animation.setRepeatMode(Animation.RESTART);  
	        refresh_iv.startAnimation(animation);  
	        
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
				    Message msg=new Message();
					try{
						//从网上获取数据
						FLAG_URL();
						HttpData httpData=new HttpData(url,GET,cn_speak.getText().toString());
					    data=httpData.getData();
					    msg.what=DETDATA_SUCCESS;
					    msg.obj= data==null?" ":data;
					}catch(Exception e){
						//Toast.makeText(MainActivity.this,"网络异常！", Toast.LENGTH_SHORT).show();
						msg.what=DETDATA_ERROR;
					}
					handler.sendMessage(msg);
				
				}
			}).start();
			
			break;
		
			//清空输入框
		case R.id.clear_input:
			cn_speak.setText(null);
			//语音朗读
			break;
		case R.id.read:
			LV=1;
			 if (mSpeech != null && !mSpeech.isSpeaking()) {  
				 mSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规  
				 mSpeech.speak(foreign_speak.getText().toString(),  
				               TextToSpeech.QUEUE_FLUSH, null);  
		     } 
			 break;
		case R.id.speak_cn:
			 LV=2;
			if (mSpeech != null && !mSpeech.isSpeaking()) {  
				 mSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规  
				 mSpeech.speak(cn_speak.getText().toString(),  
				               TextToSpeech.QUEUE_FLUSH, null);  
			}
			break;
			
		case R.id.speak_xieyin:
			 LV=2;
			if (mSpeech != null && !mSpeech.isSpeaking()) {  
				 mSpeech.setPitch(1.0f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规  
				 mSpeech.speak(cn_speak_xieyin.getText().toString(),  
				               TextToSpeech.QUEUE_FLUSH, null);  
			}
			break;
		}
	}
	
	//判断该放那个接口地址	
	public void FLAG_URL(){ 
	if(FLAG==FLAG_ALL[0]) url=urlE;
	else if(FLAG==FLAG_ALL[1])  url=urlJ;
	else if(FLAG==FLAG_ALL[2])  url=urlK;
	}
	

//spinnner点击事件
@Override
public void onItemSelected(AdapterView<?> parent, View view, int position,
		long id) {
	// TODO Auto-generated method stub
	  FLAG=FLAG_ALL[position];
}


@Override
public void onNothingSelected(AdapterView<?> parent) {
	// TODO Auto-generated method stub
	
}



//editText 监测
@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	// TODO Auto-generated method stub
	
}


@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	//当输入为空时，其余TextView都为空,语音朗读功能失效
	 if(cn_speak.getText().toString()==null||cn_speak.getText().toString().trim().equals("")){
		 cn_speak_xieyin.setText("");
		 foreign_speak.setText("");
		 read.setEnabled(false);
		 speak_cn.setEnabled(false);
		 speak_xieyin.setEnabled(false);
		 clear_input.setVisibility(View.INVISIBLE);
		 queryB.setEnabled(false);
	 }else{
		 clear_input.setVisibility(View.VISIBLE);
		 read.setEnabled(true);
		 speak_cn.setEnabled(true);
		 speak_xieyin.setEnabled(true);
		 queryB.setEnabled(true);
	 }
}


@Override
public void afterTextChanged(Editable s) {
	// TODO Auto-generated method stub
	
}

//语音朗读
@Override
public void onInit(int status) {
	// TODO Auto-generated method stub
	if(status==TextToSpeech.SUCCESS){
		//根据情况选择支持不同的语言语音
		int result = mSpeech.setLanguage(Locale.ENGLISH);

		switch (FLAG) {
		case "english":
			    result=mSpeech.setLanguage(Locale.ENGLISH);
			break;
		case "japanese":
			    result=mSpeech.setLanguage(Locale.JAPANESE);
			break;
		case "korean":
			    result=mSpeech.setLanguage(Locale.KOREAN);
			break;
		default:
			break;
		}
		if(LV==2){
			result=mSpeech.setLanguage(Locale.CHINA);
		}
		
		if(result==mSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED){
			Log.e("lanageTag", "not use");
		}else{
			read.setEnabled(true);
			//mSpeech.speak("i dont know", TextToSpeech.QUEUE_FLUSH,
                    //null);
		}
	}
}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mSpeech.stop();
		mSpeech.shutdown();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		 mSpeech=new TextToSpeech(this,this);
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
			HowToSpeakActivity.this.finish();
		}
	};
}
