package com.ck.weather.activity;

import com.example.lohaningweather.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2250);
				} catch (InterruptedException e) {
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						startActivity(new Intent(SplashActivity.this,
								MainActivity.class));
						overridePendingTransition(R.anim.scale_in,
								R.anim.scale_out);
						finish();
					}
				});
			}
		}).start();

	}
}
