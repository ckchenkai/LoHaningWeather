package com.ck.weather.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ck.weather.application.MyApplication;
import com.ck.weather.category.Weather;
import com.ck.weather.sql.CityDAO;
import com.ck.weather.sql.Tb_city;
import com.ck.weather.util.C;
import com.ck.weather.util.DeviceUtil;
import com.ck.weather.util.Locate;
import com.ck.weather.util.LocateUtil;
import com.ck.weather.util.SPTool;
import com.example.lohaningweather.R;

public class WeatherActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener {
	private Activity activity;
	private TextView tvLocate;
	private ImageView ivLocate;
	private ImageView more_city;
	private ActionBar mActionBar;
	private ImageView exit;
	// ��Ӧ�õ�������LinearLayout
	private ViewGroup mainViewGroup;
	// �����ֵײ�ָʾ��ǰҳ���СԲ����ͼ��LinearLayout
	private ViewGroup indicatorViewGroup;
	// ����LayoutInflater
	LayoutInflater mInflater;
	private ViewGroup headViewGroup;
	private View statusView;
	/**
	 * viewpager���
	 * 
	 */
	private ViewPager mViewPager;
	private MyPagerAdapter adapter;
	// ָʾ��СԲ��
	private ImageView mImageView;
	private ImageView[] mImageViews;

	private LinearLayout mybottomviewgroup;
	private Weather weather = null;
	private MyApplication app;
	private List<Fragment> mPageList = new ArrayList<>();
	// ����ӳ����б�
	private List<String> mCityList = new ArrayList<>();

	private Locate locate;
	private String current_location_detail = ""; // ��λ����������
	private String address = ""; // ��ϸ��λ��ַ
	// ɾ�����У�ˢ��viewPager
	public static String ACTION_DELETE_CITY = "action_delete_city";
	private int delPosition = -1;

	private int currentItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (MyApplication) getApplication();
		app.addActivity(this);
		weather = app.getWeather();
		activity = WeatherActivity.this;
		getSQLData();
		initView();
		setContentView(mainViewGroup);
		// ��λ
		address = SPTool.getString(activity, "location_name", "");
		if (TextUtils.isEmpty(address)) {
			locate = new Locate(this, handler);
			locate.locate();
		} else {
			handler.sendEmptyMessage(2);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				// �����صĵ�ַ��Ϣ
				address = msg.getData().getString("address");
				if (TextUtils.isEmpty(address)) {
					address = "δ֪";
				} else {
					address = LocateUtil.cropAddress(address);
					SPTool.putString(activity, "location_name", address);
				}
				handler.sendEmptyMessage(2);
				break;
			case 2:
				// ���Ĭ�϶�λ�ĳ���
				mCityList.add(0, address);
				// ��̬��ӳ���
				for (int i = 0; i < mCityList.size(); i++) {
					mPageList.add(new WeatherFragment());
				}
				adapter = new MyPagerAdapter(getSupportFragmentManager(), (ArrayList<Fragment>) mPageList);
				mViewPager.setAdapter(adapter);
				// ���Բ��
				indicatorViewGroup.removeAllViews();
				mImageViews = new ImageView[mPageList.size()];
				for (int i = 0; i < mImageViews.length; i++) {
					mImageView = new ImageView(activity);
					LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(DeviceUtil.dp2px(activity, 5),
							DeviceUtil.dp2px(activity, 5));
					lp2.setMargins(0, 0, DeviceUtil.dp2px(activity, 3), DeviceUtil.dp2px(activity, 2));
					mImageView.setLayoutParams(lp2);
					mImageView.setScaleType(ScaleType.CENTER_INSIDE);
					mImageViews[i] = mImageView;
					indicatorViewGroup.addView(mImageViews[i]);
				}
				setCurrentPage(C.mPosition);
				break;
			}
		}
	};

	/**
	 * �����ݿ��л�ȡ��������
	 */
	private void getSQLData() {
		CityDAO cityDAO = new CityDAO(WeatherActivity.this);
		List<Tb_city> strInfo = cityDAO.getScrollData();
		for (Tb_city tb_city : strInfo) {
			if (String.valueOf(tb_city.getCity()) != null) {
				String city = String.valueOf(tb_city.getCity());
				if (city.indexOf('��') != -1) {
					mCityList.add(city.substring(0, city.indexOf('��')));
				} else {
					mCityList.add(city);
				}
			}
		}
	}

	// ��ʼ��
	private void initView() {
		mInflater = getLayoutInflater();
		mainViewGroup = (ViewGroup) mInflater.inflate(R.layout.weather_viewpager, null);
		mViewPager = (ViewPager) mainViewGroup.findViewById(R.id.myviewpager);
		indicatorViewGroup = (ViewGroup) mainViewGroup.findViewById(R.id.mybottomviewgroup);
		headViewGroup = (ViewGroup) mainViewGroup.findViewById(R.id.headViewGroup);
		tvLocate = (TextView) headViewGroup.findViewById(R.id.location_name);
		ivLocate = (ImageView) headViewGroup.findViewById(R.id.iv_location_right);
		more_city = (ImageView) headViewGroup.findViewById(R.id.more_city);
		exit = (ImageView) headViewGroup.findViewById(R.id.exit);
		statusView = headViewGroup.findViewById(R.id.status_bar);
		more_city.setOnClickListener(this);
		exit.setOnClickListener(this);
		mViewPager.addOnPageChangeListener(this);
		// ����״̬���߶ȵļ��
		int statusHeight = DeviceUtil.getStatusBarHeight(this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT, statusHeight);
		statusView.setLayoutParams(lp);
	}

	/**
	 * ������ʾΪ��ǰҳ��
	 * 
	 * @param position
	 */
	private void setCurrentPage(int position) {
		currentItem = position;
		if (position >= 0 && position < mPageList.size()) {
			mViewPager.setCurrentItem(position);
			tvLocate.setText(mCityList.get(position));
			if (position == 0) {
				ivLocate.setVisibility(View.VISIBLE);
			} else {
				ivLocate.setVisibility(View.GONE);
			}
			// ���û���ʱ��СԲ��
			for (int i = 0; i < mImageViews.length; i++) {
				if (i == position) {
					mImageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
				} else {
					mImageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
				}
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentPage(arg0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.more_city:
			Intent intent = new Intent(WeatherActivity.this, LocationManageActivity.class);
			intent.putExtra("address", address);
			startActivity(intent);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.exit:
			new AlertDialog.Builder(WeatherActivity.this).setTitle("��ȷ��Ҫ�˳���")
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							((MyApplication) getApplicationContext()).exit();
						}
					}).setNegativeButton("ȡ��", null).create().show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ���ڻ�����ʷ״̬
		//SPTool.remove(activity, "location_name");
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
				((MyApplication) getApplicationContext()).exit();

			}
		}
		return super.onKeyDown(keyCode, event);

	}

	/*
	 * // �ж϶�λ���ĵص� private String getLocate(String address) { String a = null;
	 * if (address == null) { return "�Ͼ�"; } if (address.indexOf("����") == -1 &&
	 * address.indexOf("����") == -1 && address.indexOf("���") == -1 &&
	 * address.indexOf("�Ϻ�") == -1) { if (address.indexOf("���") == -1 &&
	 * address.indexOf("����") == -1) { // ��ʡ try { a =
	 * address.substring(address.indexOf("ʡ") + 1, address.indexOf("��")); }
	 * catch (Exception e) { e.printStackTrace(); } return a; } else { // �۰� a =
	 * address.substring(0, 2); return a; } } else { return "�Ͼ�"; } }
	 */

	// viewPager������
	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<Fragment> fragmentArrayList;

		public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentArrayList) {

			super(fm);
			// TODO Auto-generated constructor stub
			this.fragmentArrayList = fragmentArrayList;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Fragment fragment = new WeatherFragment();
			Bundle args = new Bundle();
			String city = mCityList.get(arg0);
			if (city.indexOf("��") != -1) {
				city = city.substring(0, city.indexOf("��"));
			}
			args.putString("city", city);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCityList.size();
		}

		/*
		 * @Override public void destroyItem(View collection, int position,
		 * Object o) { View view = (View) o; ((ViewPager)
		 * collection).removeView(view); view = null; }
		 */

		/*
		 * @Override public int getItemPosition(Object object) { // TODO
		 * Auto-generated method stub return POSITION_NONE; }
		 */
	}
}
