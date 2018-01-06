package com.activty;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bean.MsgBean;
import com.brother.games.R;
import com.common.EventBusConstant;
import com.fragment.GameFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.lang.ref.WeakReference;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements OnClickListener {

	public static boolean requestUserDataFromMain = false;

	private View tab_view_1;
	public  View tab_view_2;
	private View tab_view_3;
	private View tab_view_4;
	private View tab_view_5 = null;

	private ImageView game_icon = null;
	private ImageView game_red_dot = null;

	/**按钮图片*/
	private ImageView tab_icon_1, tab_icon_2, tab_icon_3, tab_icon_4;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager = null;

	private FragmentTransaction transaction = null;
//	/**消息**/
//	private MessageFragment mMessageFragment = null;
//	/**群组(发现)**/
//	public GroupFragment mGroupsFragment = null;
//	/**个人**/
//	private MyFragment mMyFragment = null;
//	/**邂逅**/
//	public NewEncounterFragment mNewEncounterFragment = null;
	/**邂逅**/
	public GameFragment mGameFragment = null;

	public TextView  encounter_msg = null;//邂逅红点
	public TextView  message_msg = null;//消息红点
	public TextView  emotion_msg = null;//发现红点
	public static TextView  my_msg = null;//我的红点

	public int tab_index = -1;

	private DisplayImageOptions options;
	public DisplayImageOptions options_touxiang_male;

	/**
	 * UI Handler
	 */
	public final UiHandler uiHandler = new UiHandler(this);
	public static class UiHandler extends Handler {
		private final WeakReference<MainActivity> mActivityReference;

		public UiHandler(MainActivity activity) {
			mActivityReference = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = mActivityReference.get();
			switch (msg.what) {

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById();
		fragmentManager = getSupportFragmentManager();
		if(savedInstanceState != null){
//			Fragment f1 = fragmentManager.getFragment(savedInstanceState, MessageFragment.class.getName());
//			if(f1 != null){
//				mMessageFragment = (MessageFragment)f1;
//			}
//
//			Fragment f2 = fragmentManager.getFragment(savedInstanceState, NewEncounterFragment.class.getName());
//			if(f2 != null){
//				mNewEncounterFragment = (NewEncounterFragment)f2;
//			}
//
//			Fragment f3 = fragmentManager.getFragment(savedInstanceState, EmotionFragment.class.getName());
//			if(f3 != null){
//				mGroupsFragment = (GroupFragment)f3;
//			}
//			Fragment f4 = fragmentManager.getFragment(savedInstanceState, MyFragment.class.getName());
//			if(f4 != null){
//				mMyFragment = (MyFragment)f4;
//			}
			Fragment f5 = fragmentManager.getFragment(savedInstanceState, GameFragment.class.getName());
			if(f5 != null){
				mGameFragment = (GameFragment)f5;
			}
		}else{
		}
		/**
		 * 启动直接跳转
		 */
		setTabSelection(4);

		options_touxiang_male = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon72)//下载中显示
		.showImageForEmptyUri(R.drawable.icon72)//下载失败
		.showImageOnFail(R.drawable.icon72)//解码失败
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.build();

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon72)
				.showImageForEmptyUri(R.drawable.icon72)//下载失败
				.showImageOnFail(R.drawable.icon72)//解码失败
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
						//.delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
		return super.dispatchTouchEvent(ev);
	}

	/**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

	public void setTabSelection(int index) {
		// 开启一个Fragment事务
		transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		//tab_index 默认值改为-1， 因为在比系统杀死，重新回到此界面的时候，tabFragment_1.reset()里面的auto为空，会崩掉，
		tab_index = index;
		switch (index) {
//		case 1:
//			tab_icon_2.setBackgroundResource(R.drawable.friends_find_hl); //refresh
//			if (mNewEncounterFragment == null) {
//				// 如果Fragment为空，则创建一个并添加到界面上
//                mNewEncounterFragment = new NewEncounterFragment();
//				transaction.add(R.id.content, mNewEncounterFragment);
//			} else {
//				// 如果Fragment不为空，则直接将它显示出来
//				transaction.show(mNewEncounterFragment);
//			}
//			break;
//		case 2:
//			tab_icon_3.setBackgroundResource(R.drawable.friends_message_hl); //Groups
//			if (mGroupsFragment == null) {
//				// 如果Fragment为空，则创建一个并添加到界面上
//				mGroupsFragment = new GroupFragment();
//				transaction.add(R.id.content, mGroupsFragment);
//			} else {
//				// 如果Fragment不为空，则直接将它显示出来
//				transaction.show(mGroupsFragment);
//			}
//			break;
//		case 0:
//				tab_icon_1.setBackgroundResource(R.drawable.friends_flipped_hl); //message
//				if (mMessageFragment == null) {
//					// 如果Fragment为空，则创建一个并添加到界面上
//					mMessageFragment = new MessageFragment();
//					transaction.add(R.id.content, mMessageFragment);
//				} else {
//					// 如果Fragment不为空，则直接将它显示出来
//					transaction.show(mMessageFragment);
//				}
//				break;
//		case 3:
//			tab_icon_4.setBackgroundResource(R.drawable.friends_me_hl); //me
//			if (mMyFragment == null) {
//				// 如果Fragment为空，则创建一个并添加到界面上
//				mMyFragment = new MyFragment();
//				transaction.add(R.id.content, mMyFragment);
//			} else {
//				// 如果Fragment不为空，则直接将它显示出来
//				transaction.show(mMyFragment);
//			}
//
//			break;
		case 4://Game
			if (mGameFragment == null) {
				// 如果Fragment为空，则创建一个并添加到界面上
				mGameFragment = new GameFragment();
				transaction.add(R.id.content, mGameFragment);
			} else {
				// 如果Fragment不为空，则直接将它显示出来
				transaction.show(mGameFragment);
			}
			break;
		default:
			break;
		}
		transaction.commitAllowingStateLoss/*commit*/();
	}

	/**
	 * 隐藏Fragment
	 * @param transaction
	 */
	private void hideFragments(FragmentTransaction transaction) {
//		if (mMessageFragment != null) {
//			transaction.hide(mMessageFragment);
//		}
//		if (mNewEncounterFragment != null) {
//			transaction.hide(mNewEncounterFragment);
//		}
//		if (mGroupsFragment != null) {
//			transaction.hide(mGroupsFragment);
//		}
//		if (mMyFragment != null) {
//			transaction.hide(mMyFragment);
//		}
		if (mGameFragment != null) {
			transaction.hide(mGameFragment);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.linearLayout_tab_1:
//			tab_menu.clearAnimation();
//			tab_menu.setVisibility(View.VISIBLE);
//			setTabSelection(0);
//			mMessageFragment.setUserVisibleHint(true);
//			break;
//		case R.id.linearLayout_tab_2:
//			tab_menu.clearAnimation();
//			tab_menu.setVisibility(View.VISIBLE);
//			if(tab_index == 1 && mNewEncounterFragment != null){
//				mNewEncounterFragment.encountFrag.requestEncounter2(1);
//			}
//			setTabSelection(1);
//			mNewEncounterFragment.setUserVisibleHint(true);
//			break;
//		case R.id.linearLayout_tab_3:
//			tab_menu.clearAnimation();
//			tab_menu.setVisibility(View.VISIBLE);
//			setTabSelection(2);
//			mGroupsFragment.setUserVisibleHint(true);
//			break;
//		case R.id.linearLayout_tab_4:
//			tab_menu.clearAnimation();
//			tab_menu.setVisibility(View.VISIBLE);
//			setTabSelection(3);
//			//请求个人信息
//			getUserInfo(MyApplication.getInstance().interaction.user.account);
//			requestUserDataFromMain = true;
//			//更新数据库 为已操作状态
//			String newApp =  MyApplication.getInstance().preferences.getString(Constant.newAppKey, AnalyticsUtil.getAppVersion(getApplicationContext()));
//			MyApplication.getInstance().dbHelper.updateSystemOperationByVersionName(newApp, MainActivity.class.getName());
//			break;
		case R.id.linearLayout_tab_5:
			if(tab_index == 4 && mGameFragment != null){
				mGameFragment.initPage(1, 0);
			}
			setTabSelection(4);
			requestUserDataFromMain = true;
			game_red_dot.setVisibility(View.GONE);
			game_icon.setImageResource(R.drawable.game_icon);
			break;
		default:
			break;
		}
	}

	private void findViewById() {
		tab_view_1 = findViewById(R.id.linearLayout_tab_1);
		tab_view_2 = findViewById(R.id.linearLayout_tab_2);
		tab_view_3 = findViewById(R.id.linearLayout_tab_3);
		tab_view_4 = findViewById(R.id.linearLayout_tab_4);
		tab_view_5 = findViewById(R.id.linearLayout_tab_5);
		game_icon = (ImageView)findViewById(R.id.game_icon);
		game_red_dot = (ImageView)findViewById(R.id.game_red_dot);

		tab_icon_1 = (ImageView) findViewById(R.id.tab_icon_1);
		tab_icon_2 = (ImageView) findViewById(R.id.tab_icon_2);
		tab_icon_3 = (ImageView) findViewById(R.id.tab_icon_3);
		tab_icon_4 = (ImageView) findViewById(R.id.tab_icon_4);

		tab_view_1.setOnClickListener(this);
		tab_view_2.setOnClickListener(this);
		tab_view_3.setOnClickListener(this);
		tab_view_4.setOnClickListener(this);
		tab_view_5.setOnClickListener(this);

		encounter_msg = (TextView) findViewById(R.id.encounter_msg);//邂逅消息
		message_msg =  (TextView) findViewById(R.id.message_msg);//朋友消息
		emotion_msg =  (TextView) findViewById(R.id.emotion_msg);//发现消息
		my_msg = (TextView) findViewById(R.id.my_msg);//我的消息
	}

	@Override
	public void onBackPressed() {
		isExit();
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public final MyHandler protocolHandler = new MyHandler(this);

	public static class MyHandler extends Handler {
		private final WeakReference<MainActivity> mActivityReference;

		public MyHandler(MainActivity activity) {
			mActivityReference = new WeakReference<MainActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = mActivityReference.get();
			switch(msg.what){

			}
		}
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {//防止重叠
		super.onSaveInstanceState(outState);
//		if(mMessageFragment != null){
//			getSupportFragmentManager().putFragment(outState,MessageFragment.class.getName(), mMessageFragment);
//		}
//		if(mNewEncounterFragment != null){
//			getSupportFragmentManager().putFragment(outState, NewEncounterFragment.class.getName(), mNewEncounterFragment);
//		}
//		if(mGroupsFragment != null){
//			getSupportFragmentManager().putFragment(outState, EmotionFragment.class.getName(), mGroupsFragment);
//		}
//		if(mMyFragment != null){
//			getSupportFragmentManager().putFragment(outState, MyFragment.class.getName(), mMyFragment);
//		}
		if(mGameFragment != null){
			getSupportFragmentManager().putFragment(outState, GameFragment.class.getName(), mGameFragment);
		}
	}


	@Override
    public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus){

		}
	}

	public void onEventMainThread(MsgBean msg) {
		//消息推送
		if(TextUtils.equals(msg.getType(), EventBusConstant.PUSH_MSG)){

		}
	}
}
