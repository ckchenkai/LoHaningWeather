package com.ck.weather.activity;

import java.util.ArrayList;
import java.util.List;

import com.ck.weather.activity.SlideCutListView.RemoveDirection;
import com.ck.weather.activity.SlideCutListView.RemoveListener;
import com.ck.weather.application.MyApplication;
import com.ck.weather.sql.CityDAO;
import com.ck.weather.sql.DBOpenHelper;
import com.ck.weather.sql.Tb_city;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LocationManageActivity extends Activity implements OnClickListener,RemoveListener{
	private ImageView addLocation;
	private SlideCutListView listView;
	private List<String> arrayList=null; 
	private ArrayAdapter<String> adapter;
	private ImageView bt_remove;
	private ActionBar mActionBar;
	private ImageView action_back;
	private ImageView action_remove;
	private TextView dingwei_name;
	private TextView text_i;
	private RelativeLayout rlLocate;
	int requestCode=0;


   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationmanage);
		init();
		((MyApplication)getApplicationContext()).addActivity(this);
		
		adapter=new ArrayAdapter(this,R.layout.list_style,R.id.tv_location,arrayList);
		listView.setAdapter(adapter);
		addLocation.setOnClickListener(this);
		listView.setRemoveListener(this);  //删除监听
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//列表点击事件
				 C.mPosition = position+1;
				 Intent i =new Intent(LocationManageActivity.this,MainActivity.class);
				 startActivity(i);
				 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
		
		
		//标题栏设置
		mActionBar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.location_actionbar, mActionBar);
		action_back=(ImageView) mActionBar.getCustomView().findViewById(R.id.action_back);
 		action_remove=(ImageView) mActionBar.getCustomView().findViewById(R.id.action_remove);
		action_back.setOnClickListener(this);
 		action_remove.setOnClickListener(this);
 		
		dingwei_name.setText(getIntent().getStringExtra("address"));
		
		//左右抖动动画
	    TranslateAnimation animation = new TranslateAnimation(-30, 30, 0, 0);  
        animation.setInterpolator(new OvershootInterpolator());  
        animation.setDuration(60);  
        animation.setRepeatCount(4);  
        animation.setRepeatMode(Animation.REVERSE);  
        text_i.startAnimation(animation);  
	}

	private void init(){
		rlLocate = (RelativeLayout) findViewById(R.id.rl_locate);
		listView=(SlideCutListView) findViewById(R.id.listView);
		addLocation=(ImageView) findViewById(R.id.addLocation);
		dingwei_name=(TextView) findViewById(R.id.dingwei_name);
		text_i=(TextView) findViewById(R.id.text_i);
		arrayList=new ArrayList();
		rlLocate.setOnClickListener(this);
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.addLocation:
			Intent intent=new Intent(this,SearchCityActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.action_back:	
			onBackPressed();
			break;
		case R.id.action_remove:
			action_remove.setVisibility(View.INVISIBLE);
			break;
		case R.id.rl_locate:
			C.mPosition = 0;
			 Intent i =new Intent(LocationManageActivity.this,MainActivity.class);
			 startActivity(i);
			 overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		}
		
		 	
	}
	
    //从数据库中获取数据
	private void getData(){
		   CityDAO cityDAO=new CityDAO(LocationManageActivity.this);
		   List<Tb_city> strInfo=cityDAO.getScrollData();
		   for(Tb_city tb_city:strInfo){
			   arrayList.add(String.valueOf(tb_city.getCity()));
		   }
	}
	
	//滑动删除之后的回调方法
		@Override
		public void removeItem(RemoveDirection direction, int position) {
			String cityName = arrayList.get(position);
			CityDAO cityDAO=new CityDAO(LocationManageActivity.this);
			cityDAO.delete(cityName);
			adapter.remove(cityName);
			//C.mCityList.remove(position);
			//C.mPageList.remove(position);
			//C.isChange = true;
			if(position==cityDAO.getMaxId()){
				if((position-1)>=0){
					C.mPosition = position-1;
				}
			}else{
				C.mPosition = position;
			}
		}
		
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			this.finish();
			Intent i =new Intent(this,MainActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.in_leftright, R.anim.out_leftright);
		}

}
