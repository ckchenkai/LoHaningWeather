package com.ck.weather.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ck.weather.application.MyApplication;
import com.ck.weather.cetenquery.CetQueryActivity;
import com.ck.weather.cetenquery.QueryResult;
import com.ck.weather.contact.contactActivity;
import com.ck.weather.translate.HowToSpeakActivity;
import com.ck.weather.util.C;
import com.example.lohaningweather.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FindActivity extends Activity {

	private  GridView mGridView;
	private int []image={R.drawable.dw,R.drawable.xy,
			             R.drawable.lt,R.drawable.sl};
	private String []title={"定位导航","谐音学外语","聊天机器人","四六级查询"};
	private ActionBar mActionBar;
	private ImageView backIV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toursit);
		((MyApplication)getApplicationContext()).addActivity(this);
		
		
		//标题栏设置
		mActionBar=getActionBar();
		C c=new C();
		c.getAction_Bar(R.layout.tourism_actionbar, mActionBar);
		backIV=(ImageView)mActionBar.getCustomView(). findViewById(R.id.tourism_back);
		backIV.setOnClickListener(backClick);
	
		mGridView=(GridView) findViewById(R.id.mGridView);
		List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<4;i++){
			Map<String,Object> item=new HashMap<String,Object>();
			item.put("image", image[i]);
			item.put("title", title[i]);
			items.add(item);
		}
		
		SimpleAdapter adapter=new SimpleAdapter(this, items, R.layout.gridview_list, 
         new String[]{"image","title"}, new int[]{R.id.imageId,R.id.titleId});
		
		mGridView.setAdapter(adapter);
		mGridView.setLayoutAnimation(getAnimationController());
		mGridView.setOnItemClickListener(aListener);
  //屏蔽默认点击效果
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}
	
	
	
	 protected LayoutAnimationController getAnimationController(){
         LayoutAnimationController controller;
         Animation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                 Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//从0.5倍放大到1倍
         anim.setDuration(1000);
         controller=new LayoutAnimationController(anim,0.1f);
         controller.setOrder(LayoutAnimationController.ORDER_NORMAL); 
         return controller; 
	 } 

	 private AdapterView.OnItemClickListener aListener=new OnItemClickListener() {

		 @Override
		 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			 // TODO Auto-generated method stub
			 switch (position) {
			 case 0:
				 Intent intent=new Intent(FindActivity.this,HowToSpeakActivity.class);
				 startActivity(intent);
				 overridePendingTransition(R.anim.in_rightleft, R.anim.out_rightleft);
		
				 break;
			 case 1:
				 Intent intent2=new Intent(FindActivity.this,HowToSpeakActivity.class);
				 startActivity(intent2);
				 overridePendingTransition(R.anim.in_rightleft, R.anim.out_rightleft);
				 break;
			 case 2:
				 Intent intent3=new Intent(FindActivity.this,contactActivity.class);
				 startActivity(intent3);
				 overridePendingTransition(R.anim.in_rightleft, R.anim.out_rightleft);
				 break;
			 case 3:
				 Intent intent4=new Intent(FindActivity.this,CetQueryActivity.class);
				 startActivity(intent4);
				 overridePendingTransition(R.anim.in_rightleft, R.anim.out_rightleft);
				 break;
			 default:
				 break;
			 }
		 }
};
	
	
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
			FindActivity.this.finish();
		}
	};
	
	
		// 返回键
	
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			this.finish();
			return super.onKeyDown(keyCode, event);

		}
		
}
