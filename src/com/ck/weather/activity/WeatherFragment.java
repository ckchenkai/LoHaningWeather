package com.ck.weather.activity;
import com.ck.weather.application.MyApplication;
import com.ck.weather.category.Weather;
import com.ck.weather.util.C;
import com.ck.weather.util.DeviceUtil;
import com.ck.weather.util.LoadWeatherThread;
import com.ck.weather.util.SPTool;
import com.ck.weather.widget.TouchDispatchView;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.example.lohaningweather.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherFragment extends Fragment implements OnRefreshListener<ScrollView>{
	public static final int REFRESH_SUCCESS = 0;
	public static final int REFRESH_ERROR = 1;
	//public static final int AUTO_REFRESH_AFTER_CACHE = 2;
	private MyApplication app;
	private Weather weather = null;
	/**
	 * �ж�fragment�Ƿ�ɹ�������־λ
	 */
	protected boolean isCreaded = false;
	/**
	 * �ж�fragment�Ƿ�λ��viewPager��ǰ�����־λ
	 */
	protected boolean isVisible = false;
	private RelativeLayout main_bg;
	private TouchDispatchView touchView;
	private TextView current_temp;
	private ImageView ivQuality;
	private TextView current_time;
	private TextView temp_quality_number;
	private TextView temp_quality;
	private TextView high_temp;
	private TextView low_temp;
	private TextView temp_condition;
	private TextView wind_direction;
	private ImageView temp_picture;
	private TextView Location_name;
	private ImageView iv_location_right;
	private ImageView more_city;

	private ImageView zuori_image;
	private ImageView jinri_image;
	private ImageView mingri_image;
	private ImageView houri_image;
	private ImageView weilai_image;

	private TextView zuori_temp;
	private TextView jinri_temp;
	private TextView mingri_temp;
	private TextView houri_temp;
	private TextView weilai_temp;

	private String GMdetail;
	private String CYdetail;
	private String FSdetail;

	private TextView gm_detail;
	private TextView cy_detail;
	private TextView fs_detail;

	private String cityName = "";
	private String[] WeatherData = null;
	// ���߱�����
	private float[][] mValuesThree = { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };
	private LineChartView mChartThree;
	private boolean mUpdateThree;
	private final String[] mLabelsThree = { "����", "����", "����", "����", "δ��" };
	// ����ˢ��
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private LinearLayout layout_weather;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (MyApplication) getActivity().getApplication();
		weather = app.getWeather();
		cityName = getArguments().getString("city");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.weather, container, false);
		initView(view);
		// ʹ���ϴλ��������
		String[] lastData = C.getLastDataArray(getActivity(), 25, cityName);
		if (!NullWeatherData(lastData)) {
			refreshUI(lastData);
			//handler.sendEmptyMessage(AUTO_REFRESH_AFTER_CACHE);
		}
		isCreaded = true; // �Ѵ�����־λ
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				/**
				 * <p> ��ʱviewPagerÿ��fragment����ִ�У�����������fragment���ɼ���
				 * <p>����ֻ��ִ�е�һ��fragment����������,�˱�־λ��onLoad������
				 */
				onLoad(cityName); 
			}
		}, 1500);
		
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param cityName
	 */
	private void onLoad(final String cityName) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(cityName)||TextUtils.equals(cityName, "δ֪"))
			return;
		// ��ȡ�ϴ�ˢ��ʱ��
		long lastTime = SPTool.getLong(getActivity(),cityName+ "last_time", 0);
		long thisTime = System.currentTimeMillis();
		if (isCreaded && isVisible) {
			// ����ϴ�ˢ��ʱ�䳬��5���ӣ����Զ�ˢ��
			if (thisTime - lastTime > 5 *60 * 1000) {
				// �ӳٴ�����������
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (mPullRefreshScrollView != null)
							mPullRefreshScrollView.setRefreshing();
						new LoadWeatherThread(handler, weather, cityName).start();
					}
				}, 200);
				SPTool.putLong(getActivity(), cityName+"last_time", System.currentTimeMillis());
			}
		}
	}

	// ��ʼ��
	private void initView(View v) {
		// ����ˢ��
		mPullRefreshScrollView = (PullToRefreshScrollView) v.findViewById(R.id.mPullRefreshScrollView);
		mPullRefreshScrollView.setOnRefreshListener(this);
		mScrollView = mPullRefreshScrollView.getRefreshableView();
		// ��ȡinclude�ؼ�
		layout_weather = (LinearLayout) v.findViewById(R.id.layout_weather);
		// HeaderView�߶�=��Ļ�߶�-�������߶�-tabHost�߶�-״̬���߶�
		final float scale = getActivity().getResources().getDisplayMetrics().density;
		int mHeaderHeight = DeviceUtil.getMetrics()[1] - DeviceUtil.dp2px(getActivity(), 47)
				- DeviceUtil.dp2px(getActivity(), 54) - DeviceUtil.getStatusBarHeight(getActivity());
		ViewGroup.LayoutParams lp = layout_weather.getLayoutParams();
		lp.height = mHeaderHeight;

		main_bg = (RelativeLayout) v.findViewById(R.id.main_bg);// ������
		touchView = (TouchDispatchView) v.findViewById(R.id.touch_intercept_layer);
		touchView.setPadding(0, DeviceUtil.getStatusBarHeight(getActivity()) + DeviceUtil.dp2px(getActivity(), 47), 0,
				0);
		mUpdateThree = true;
		mChartThree = (LineChartView) v.findViewById(R.id.linechart3);

		current_temp = (TextView) v.findViewById(R.id.current_temp);
		current_time = (TextView) v.findViewById(R.id.current_time);
		ivQuality = (ImageView) v.findViewById(R.id.iv_quality);
		temp_quality_number = (TextView) v.findViewById(R.id.temp_quality_number);
		temp_quality = (TextView) v.findViewById(R.id.temp_quality);
		high_temp = (TextView) v.findViewById(R.id.high_temp);
		low_temp = (TextView) v.findViewById(R.id.low_temp);
		temp_condition = (TextView) v.findViewById(R.id.temp_condition);
		wind_direction = (TextView) v.findViewById(R.id.wind_direction);
		temp_picture = (ImageView) v.findViewById(R.id.temp_picture);
		// ��ȡ����
		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
		// ���ü�ϸ����
		current_temp.setTypeface(typeFace);
		current_time.setTypeface(typeFace);
		temp_quality_number.setTypeface(typeFace);
		temp_quality.setTypeface(typeFace);
		wind_direction.setTypeface(typeFace);
		// δ�������������
		zuori_image = (ImageView) v.findViewById(R.id.zuori_image);
		jinri_image = (ImageView) v.findViewById(R.id.jinri_image);
		mingri_image = (ImageView) v.findViewById(R.id.mingri_image);
		houri_image = (ImageView) v.findViewById(R.id.houri_image);
		weilai_image = (ImageView) v.findViewById(R.id.weilai_image);

		zuori_temp = (TextView) v.findViewById(R.id.zuori_temp);
		jinri_temp = (TextView) v.findViewById(R.id.jinri_temp);
		mingri_temp = (TextView) v.findViewById(R.id.mingri_temp);
		houri_temp = (TextView) v.findViewById(R.id.houri_temp);
		weilai_temp = (TextView) v.findViewById(R.id.weilai_temp);

		gm_detail = (TextView) v.findViewById(R.id.gm_detail);
		cy_detail = (TextView) v.findViewById(R.id.cy_detail);
		fs_detail = (TextView) v.findViewById(R.id.fs_detail);

	}
	
	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		// TODO Auto-generated method stub
		new LoadWeatherThread(handler, weather, cityName).start();
	}

	// �����������ݣ�����UI
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_SUCCESS:
				if (mPullRefreshScrollView != null)
					mPullRefreshScrollView.onRefreshComplete();
				WeatherData = (String[]) msg.obj;
				if (isAdded()) {
					if (isWeatherData(WeatherData)) {
						Toast.makeText(getActivity(), "�������ݣ�", Toast.LENGTH_SHORT).show();
						return;
					}
					// ��������
					C.saveLastDataAsArray(getActivity(), WeatherData, cityName);
					// ����UI
					refreshUI(WeatherData);
				}
				break;
			case REFRESH_ERROR:
				if (isAdded()) {
					Toast.makeText(getActivity(), "�����쳣��", Toast.LENGTH_SHORT).show();
					// �������ˢ��
					if (mPullRefreshScrollView != null)
						mPullRefreshScrollView.onRefreshComplete();
				}
				break;
			/*case AUTO_REFRESH_AFTER_CACHE:
				//onLoad(cityName); // ����ҳ�����
				break;*/
			}

		}
	};

	/**
	 * 
	 * @param �ж��������������ͼƬ
	 * @param imageView
	 */
	public void type_image(String type, ImageView imageView) {

		C c = new C();
		switch (type) {
		case "��ѩ":
			c.imageSet(imageView, this.getActivity(), 0);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 1);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 2);
			break;
		case "��ѩ":
			c.imageSet(imageView, this.getActivity(), 3);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 4);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 5);
			break;
		case "������":
			c.imageSet(imageView, this.getActivity(), 6);
			break;
		case "���������":
			c.imageSet(imageView, this.getActivity(), 7);
			break;
		case "��":
			c.imageSet(imageView, this.getActivity(), 8);
			break;
		case "ɳ����":
			c.imageSet(imageView, this.getActivity(), 9);
			break;

		case "�ش���":
			c.imageSet(imageView, this.getActivity(), 10);
			break;
		case "��":
			c.imageSet(imageView, this.getActivity(), 11);
			break;
		case "Сѩ":
			c.imageSet(imageView, this.getActivity(), 12);
			break;
		case "С��":
			c.imageSet(imageView, this.getActivity(), 13);
			break;
		case "��":
			c.imageSet(imageView, this.getActivity(), 14);
			break;
		case "���ѩ":
			c.imageSet(imageView, this.getActivity(), 15);
			break;
		case "��ѩ":
			c.imageSet(imageView, this.getActivity(), 16);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 17);
			break;
		case "��ѩ":
			c.imageSet(imageView, this.getActivity(), 18);
			break;
		case "����":
			c.imageSet(imageView, this.getActivity(), 19);
			break;
		default:
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
			break;
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isVisible = true;
			onLoad(cityName);
		} else {
			isVisible = false;
		}
	}

	/**
	 * 
	 * ����UI
	 * 
	 */
	private void refreshUI(String[] WeatherData) {
		current_temp.setText(WeatherData[0] + "��");
		current_time.setText(WeatherData[1]);
		temp_quality_number.setText(WeatherData[2]);
		high_temp.setText(WeatherData[3] + "��");
		low_temp.setText(WeatherData[4] + "��");
		temp_condition.setText(WeatherData[5]);
		wind_direction.setText(WeatherData[6]);
		// ����AQI�жϿ�������
		int aNumber = -1;
		if (WeatherData[2].equals("") || WeatherData[2].equals("null")) {
			aNumber = -1;
		} else {
			aNumber = Integer.parseInt(temp_quality_number.getText().toString());
		}
		if (aNumber < 0) {
			temp_quality.setText("��");
			ivQuality.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.leaf1));
		} else if (aNumber >= 0 && aNumber <= 50) {
			temp_quality.setText("��");
			temp_quality.setTextColor(getResources().getColorStateList(R.color.hao));
			ivQuality.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.leaf1));
		} else if (aNumber > 50 && aNumber <= 100) {
			temp_quality.setText("��");
			temp_quality.setTextColor(getResources().getColorStateList(R.color.liang));
			ivQuality.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.leaf2));
		} else {
			temp_quality.setText("��");
			temp_quality.setTextColor(getResources().getColorStateList(R.color.cha));
			ivQuality.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.leaf3));
		}
		// ��������״����ͼƬ
		String type = WeatherData[5];
		type_image(type, temp_picture);
		// ��������
		bg_replace(type);
		// δ�������������
		zuori_temp.setText(WeatherData[7] + "/" + WeatherData[8]);
		jinri_temp.setText(WeatherData[9] + "/" + WeatherData[10]);
		mingri_temp.setText(WeatherData[11] + "/" + WeatherData[12]);
		houri_temp.setText(WeatherData[13] + "/" + WeatherData[14]);
		weilai_temp.setText(WeatherData[15] + "/" + WeatherData[16]);

		// ���߱�����
		mValuesThree = new float[][] {
				{ Float.parseFloat(WeatherData[7]), Float.parseFloat(WeatherData[9]), Float.parseFloat(WeatherData[11]),
						Float.parseFloat(WeatherData[13]), Float.parseFloat(WeatherData[15]) },
				{ Float.parseFloat(WeatherData[8]), Float.parseFloat(WeatherData[10]),
						Float.parseFloat(WeatherData[12]), Float.parseFloat(WeatherData[14]),
						Float.parseFloat(WeatherData[16]) } };
		mChartThree.dismiss();
		mChartThree.removeAllViews();
		// ��ʾ���߱�
		showChart(2, mChartThree);
		// δ�����������������ʾ��ͼƬ type_image(String type,ImageView imageView)
		type_image(WeatherData[17], zuori_image);
		type_image(WeatherData[18], jinri_image);
		type_image(WeatherData[19], mingri_image);
		type_image(WeatherData[20], houri_image);
		type_image(WeatherData[21], weilai_image);
		CYdetail = WeatherData[22];// ����ϸ��
		GMdetail = WeatherData[23]; // ��ðע��
		FSdetail = WeatherData[24]; // ��ɹϸ��
		cy_detail.setText(CYdetail == null ? "����" : CYdetail);
		gm_detail.setText(GMdetail == null ? "����" : GMdetail);
		fs_detail.setText(FSdetail == null ? "����" : FSdetail);
	}

	/*
	 *
	 * ��������
	 * 
	 */private void bg_replace(String type) {
		int type_image[] = { 
				R.drawable.bg_fine_night, R.drawable.bg_haze, R.drawable.bg_overcast,
				R.drawable.bg_thunder_storm, R.drawable.bg_sand_storm, R.drawable.bg_rain, 
				R.drawable.bg_fog,R.drawable.bg_na,R.drawable.bg_moderate_rain_day,
				R.drawable.bg_heavy_rain_night,R.drawable.bg_snow_night};
		switch (type) {
		case "��":
			main_bg.setBackgroundResource(type_image[0]);
			break;
		case "ѩ":
			main_bg.setBackgroundResource(type_image[10]);
			break;
		case "��":
		case "����":
			main_bg.setBackgroundResource(type_image[7]);
			break;
		case "��":
			main_bg.setBackgroundResource(type_image[6]);
			break;
		case "С��":
		case "����":
			main_bg.setBackgroundResource(type_image[8]);
			break;
		case "����":
		case "����":
			main_bg.setBackgroundResource(type_image[9]);
			break;
		case "����":
		case "����":
		case "�ش���":
			main_bg.setBackgroundResource(type_image[5]);
			break;
		case "ɳ����":
			main_bg.setBackgroundResource(type_image[4]);
			break;
		case "������":
		case "���������":
			main_bg.setBackgroundResource(type_image[3]);
			break;
		default:
			main_bg.setBackgroundResource(type_image[0]);
			break;

		}
	}

	/**
	 * 
	 * ���߱�
	 * 
	 */
	/**
	 * Show a CardView chart
	 * 
	 * @param tag
	 *            Tag specifying which chart should be dismissed
	 * @param chart
	 *            Chart view
	 * @param btn
	 *            Play button
	 */
	private void showChart(final int tag, final LineChartView chart) {

		Runnable action = new Runnable() {
			@Override
			public void run() {
				new Handler().postDelayed(new Runnable() {
					public void run() {

					}
				}, 500);
			}
		};

		switch (tag) {
		case 2:
			produceThree(chart, action);
			break;
		default:
		}
	}

	/**
	 * Update the values of a CardView chart
	 * 
	 * @param tag
	 *            Tag specifying which chart should be dismissed
	 * @param chart
	 *            Chart view
	 * @param btn
	 *            Play button
	 */
	private void updateChart(final int tag, final LineChartView chart, ImageButton btn) {

		dismissPlay(btn);

		switch (tag) {
		case 2:
			updateThree(chart);
			break;
		default:
		}
	}

	/**
	 * Dismiss a CardView chart
	 * 
	 * @param tag
	 *            Tag specifying which chart should be dismissed
	 * @param chart
	 *            Chart view
	 * @param btn
	 *            Play button
	 */
	private void dismissChart(final int tag, final LineChartView chart, final ImageButton btn) {

		dismissPlay(btn);

		Runnable action = new Runnable() {
			@Override
			public void run() {
				new Handler().postDelayed(new Runnable() {
					public void run() {

						showChart(tag, chart);
					}
				}, 500);
			}
		};

		switch (tag) {
		case 2:
			dismissThree(chart, action);
			break;
		default:
		}
	}

	/**
	 * Show CardView play button
	 * 
	 * @param btn
	 *            Play button
	 */
	private void showPlay(ImageButton btn) {
		btn.setEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			btn.animate().alpha(1).scaleX(1).scaleY(1);
		else
			btn.setVisibility(View.VISIBLE);
	}

	/**
	 * Dismiss CardView play button
	 * 
	 * @param btn
	 *            Play button
	 */
	private void dismissPlay(ImageButton btn) {
		btn.setEnabled(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			btn.animate().alpha(0).scaleX(0).scaleY(0);
		else
			btn.setVisibility(View.INVISIBLE);
	}

	/**
	 *
	 * Chart
	 *
	 */

	public void produceThree(LineChartView chart, Runnable action) {

		Tooltip tip = new Tooltip(getActivity(), R.layout.linechart_three_tooltip, R.id.value);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

			tip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
					PropertyValuesHolder.ofFloat(View.SCALE_X, 1f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f));

			tip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
					PropertyValuesHolder.ofFloat(View.SCALE_X, 0f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f));
		}

		chart.setTooltips(tip);

		LineSet dataset = new LineSet(mLabelsThree, mValuesThree[0]);
		dataset.setColor(Color.parseColor("#FF58C674")).setDotsStrokeThickness(Tools.fromDpToPx(2))
				.setDotsStrokeColor(Color.parseColor("#FF58C674")).setDotsColor(Color.parseColor("#eef1f6"));
		chart.addData(dataset);

		dataset = new LineSet(mLabelsThree, mValuesThree[1]);
		dataset.setColor(Color.parseColor("#FFA03436")).setDotsStrokeThickness(Tools.fromDpToPx(2))
				.setDotsStrokeColor(Color.parseColor("#FFA03436")).setDotsColor(Color.parseColor("#eef1f6"));
		chart.addData(dataset);

		Paint gridPaint = new Paint();
		gridPaint.setColor(Color.parseColor("#308E9196"));
		gridPaint.setStyle(Paint.Style.STROKE);
		gridPaint.setAntiAlias(true);
		gridPaint.setStrokeWidth(Tools.fromDpToPx(1f));

		chart.setBorderSpacing(1)
				// .setAxisBorderValues(0, 10, 1)
				.setXLabels(AxisController.LabelPosition.OUTSIDE).setYLabels(AxisController.LabelPosition.OUTSIDE)
				.setLabelsColor(Color.parseColor("#FF8E9196")).setXAxis(false).setYAxis(false).setStep(2)
				.setBorderSpacing(Tools.fromDpToPx(5)).setGrid(ChartView.GridType.VERTICAL, gridPaint);

		if (isSmallToZero(mValuesThree))
			chart.setAxisBorderValues(-20, 20, 8);
		else
			chart.setAxisBorderValues(0, 40, 8);

		Animation anim = new Animation().setEndAction(action);

		// chart.show(anim);
		chart.show();

	}

	public void updateThree(LineChartView chart) {
		chart.dismissAllTooltips();

		chart.updateValues(1, mValuesThree[0]);
		chart.updateValues(0, mValuesThree[1]);
		chart.notifyDataUpdate();
	}

	public static void dismissThree(LineChartView chart, Runnable action) {
		chart.dismissAllTooltips();
		// chart.dismiss(new Animation().setEndAction(action));
		chart.dismiss();
	}

	/**
	 * 
	 * @param �ж����߱��Ƿ����С��0�ĵ�����
	 * @return
	 */
	public boolean isSmallToZero(float[][] value) {
		for (int i = 0; i < value.length; i++) {
			for (int j = 0; j < value[i].length; j++) {
				if (value[i][j] < 0f)
					return true;
				else
					return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * �ж�WeatherData�Ƿ�Ϊ��null���͡�0��
	 * 
	 */
	private boolean isWeatherData(String[] value) {
		for (int i = 0; i < value.length; i++) {
			if (value[i].equals("null") || value[i].equals("0"))
				return true;
		}
		return false;
	}

	private boolean NullWeatherData(String[] value) {
		for (int i = 0; i < value.length; i++) {
			if (value[i] == null || value[i].equals(""))
				return true;
		}
		return false;
	}
}
