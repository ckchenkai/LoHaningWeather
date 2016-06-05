package com.ck.weather.activity;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ck.weather.application.MyApplication;
import com.ck.weather.sql.CityDAO;
import com.ck.weather.sql.DBOpenHelper;
import com.ck.weather.sql.Tb_city;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchCityActivity extends Activity implements OnItemClickListener,OnClickListener {

	private ImageView iv_search;
	private GridView city_list;
	private String []city;
	private ArrayList<String> arrayList;
	private ArrayAdapter<String> adapter;
	private Intent intent;
	private ActionBar mActionbar;
	private ImageView action_back;
	private AutoCompleteTextView search_location;
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	private String action="TO_THE_SELECTED_VIEWPAG  ER";
	int resultCode=0;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchlocation);
		init();
		((MyApplication)getApplicationContext()).addActivity(this);
		//自动匹配框
		String strName=search_location.getText().toString();
		helper=new DBOpenHelper(SearchCityActivity.this);
		db=helper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select _id,name"
				+ " from city where name like'"+strName+"%'", null);
		if(cursor.moveToNext()){
			strName=cursor.getString(cursor.getColumnIndex("_id"));
		}
		myCursorAdapter cursorAdapter=new myCursorAdapter(SearchCityActivity.this, cursor,false);
		
		search_location.setThreshold(1);
		search_location.setCompletionHint("没有更多数据了");
		search_location.setAdapter(cursorAdapter);

		
		//匹配
		for(int i=0;i<city.length;i++){
		arrayList.add(city[i]);
		}
		adapter=new ArrayAdapter(this,R.layout.gridview_city_style,R.id.city,arrayList);
		city_list.setAdapter(adapter);
		
		//添加网格视图点击事件
		city_list.setOnItemClickListener(this);
		iv_search.setOnClickListener(this);
		//添标题栏设置
		mActionbar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.switch_location_actionbar,mActionbar);
		action_back=(ImageView) mActionbar.getCustomView().findViewById(R.id.action_back_s);
		action_back.setOnClickListener(this);
		
	}
	
	private void init(){
		search_location=(AutoCompleteTextView) findViewById(R.id.search_location);
		iv_search=(ImageView) findViewById(R.id.iv_search);
		city_list=(GridView) findViewById(R.id.city_list);
		arrayList=new ArrayList<String>();
		
		city=new String []{"北京市","天津市","上海市","重庆市","沈阳市","大连市","长春市"
				,"哈尔滨市","郑州市","武汉市","长沙市","广州市","深圳市","南京市","无锡市","常州市"
				,"苏州市","扬州市","杭州市","宁波市","济南市","青岛市","福州市","厦门市","西安市","成都市"
				,"昆明市"};

	}
	
	//发送数据
	private void sendData(String data){
		intent=new Intent(this,LocationManageActivity.class);
		intent.putExtra("city", data);
		this.setResult(resultCode, intent);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		   CityDAO cityDAO=new CityDAO(SearchCityActivity.this);
		   
		   //如果数据库中已添加该城市，则不再添加
		     if( cityDAO.find_city(city[position])!=null){
		    	 Toast.makeText(SearchCityActivity.this, "不能重复添加城市哦！", Toast.LENGTH_SHORT).show();
		     }else{    
		        Tb_city tb_city=new Tb_city(cityDAO.getMaxId()+1,city[position]);
		        cityDAO.add(tb_city);
		        
		        
		       /*//用于跳转到viewPager当前城市页面
				SharedPreferences mySharedPreferences= getSharedPreferences("THIS_PAGER",
						Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				editor.putInt("this_pager",cityDAO.getMaxId());
				editor.commit(); */
				C.mPosition = cityDAO.getMaxId();
		      //跳转
		        Intent i=new Intent(SearchCityActivity.this,MainActivity.class);
		        startActivity(i);  
		        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		        this.finish();
		      }
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.in_leftright, R.anim.out_leftright);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.action_back_s:
			onBackPressed();
		    break;
		case R.id.iv_search:
			CityDAO cityDAO=new CityDAO(SearchCityActivity.this);
			String name=search_location.getText().toString();
			//判断是否是合法的城市名，合法则添加入数据库中
			if(name.equals(cityDAO.cfind(name))){
				 //如果数据库中已添加该城市，则不再添加
			     if( cityDAO.find_city(name)!=null){
			    	 Toast.makeText(SearchCityActivity.this, "不能重复添加城市哦！", Toast.LENGTH_SHORT).show();
			     }else{    
			        Tb_city tb_city=new Tb_city(cityDAO.getMaxId()+1,name);
			        cityDAO.add(tb_city);
			        C.mPosition = cityDAO.getMaxId();
			      //跳转
			        Intent intnet=new Intent(SearchCityActivity.this,MainActivity.class);
			        startActivity(intnet);        
			        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			         this.finish();
			      }
			}
			break;
		}
	}
	
	private class myCursorAdapter extends CursorAdapter{
	 
	private LayoutInflater layoutInflater;
	private TextView auto_name;
	private Cursor cursor;
	public myCursorAdapter(Context context, Cursor c,boolean autoRequery) {
			super(context, c);
			// TODO Auto-generated constructor stub
			layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		}
	@Override
		public CharSequence convertToString(Cursor cursor) {
			// TODO Auto-generated method stub
			return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("name"));
		}

	@Override
	public void bindView(View v, Context arg1, Cursor cursor) {
		// TODO Auto-generated method stub
		((TextView)v).setText(cursor.getString(cursor.getColumnIndex("name")));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater;
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		TextView view=(TextView)layoutInflater.inflate(android.R.layout.simple_list_item_1, null); 
		return view;
	}

    @Override
    	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
    		// TODO Auto-generated method stub
    		//return super.runQueryOnBackgroundThread(constraint);
    		cursor=db.rawQuery("select *"
    				+ " from city where name like'"+constraint+"%'", null);
    		return cursor;
    	}
	
	}
}
