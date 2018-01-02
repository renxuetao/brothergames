package com.activty.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.activty.BaseActivity;
import com.activty.MainActivity;
import com.brother.games.R;

import java.lang.ref.WeakReference;

public class SplashActivity extends BaseActivity {

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private static final long SPLASH_DELAY_MILLIS = 3000;

	private final MyHandler mHandler = new MyHandler(this);

	private static class MyHandler extends Handler {
		private final WeakReference<SplashActivity> mActivityReference;
		
		public MyHandler(SplashActivity activity) {
			mActivityReference = new WeakReference<SplashActivity>(activity);
		}
		
		@Override
		public void handleMessage(Message msg) {
			SplashActivity activity = mActivityReference.get();
			if (activity != null) {
				switch (msg.what) {
				case GO_HOME:
					break;
				case GO_GUIDE:
					activity.startActivity(MainActivity.class);
					break;
				default:
					break;
				}
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){  
			finish();
			return;
		}
		//设置界面
		setContentView(R.layout.splash);
		init();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){

		}
		return super.onTouchEvent(event);
	}

	private void init() {
		mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
	}

	@Override
	public void onBackPressed() {
		isExit();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
