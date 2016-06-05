package com.ck.weather.activity;

import com.ck.weather.widget.CircleFlowIndicator;
import com.ck.weather.widget.ViewFlow;
import com.example.lohaningweather.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class FirstTimeInActivity extends Activity {
	/** Called when the activity is first created. */
	private ViewFlow viewFlow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first_time_in);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		viewFlow.setAdapter(new DemoPic());
		viewFlow.setmSideBuffer(ids.length); // 瀹為檯鍥剧墖寮犳暟锛�
		// 鎴戠殑ImageAdapter瀹為檯鍥剧墖寮犳暟涓�3

		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewFlow.setFlowIndicator(indic);
		viewFlow.setSelection(0); // 璁剧疆鍒濆浣嶇疆
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_OK);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public int[] ids = { R.drawable.tutorial_back_1, R.drawable.tutorial_back_2, R.drawable.tutorial_back_3, R.drawable.tutorial_back_app };

	class DemoPic extends BaseAdapter {
		private LayoutInflater mInflater;

		public DemoPic() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return ids.length; 
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.image_item, null);
			}

			((ImageView) convertView.findViewById(R.id.imgView)).setImageResource(ids[position % ids.length]);
			if (position == 3) {
				convertView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
						Toast.makeText(FirstTimeInActivity.this, "下滑查看更多哦！", Toast.LENGTH_SHORT).show();
					}
				});
			}
			return convertView;
		}
	}
}
