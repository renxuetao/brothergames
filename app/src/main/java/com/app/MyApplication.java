package com.app;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bean.CloseMsgBean;
import com.brother.games.R;
import com.common.Constant;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.utils.LogUtil;
import com.utils.OnScreenHint;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import de.greenrobot.event.EventBus;

public class MyApplication extends LitePalApplication {

	private static Context mContext;
	private static MyApplication mApplication;

	public static String versionName = "";
	public static String IMEI = "";
	public static String MODEL = "";
	public static String Android_OS = "";

	private ActivityManager mActivityManager;
	public ActivityManagerT1 activityManager = null;
	public SharedPreferences preferences;
	public Editor editor;
	private NotificationManager mNotificationManager;
	public Gson gson = new Gson();
	private OnScreenHint showMsg = null;


	public synchronized static MyApplication getInstance() {
		return mApplication;
	}


	public static boolean  isBackRun         = false; // false 前台  true 后台
	public static long     backRunTime       = 0;
	public static long     foregRunTime      = 0;

	//解决dex报错问题
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		ApplicationInfo appInfo = null;
		try {
			appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
			Constant.pid = appInfo.metaData.getString("channel_key");
			LogUtil.d("获得pid :" + Constant.pid);
		} catch (NameNotFoundException e1) {
			Constant.pid = "h_home";
			e1.printStackTrace();
		}

		//初始化自定义Activity管理器
		activityManager = ActivityManagerT1.getScreenManager();

		if (showMsg != null) {
			showMsg.cancel();
			showMsg = null;
		}
		showMsg = OnScreenHint.makeText(MyApplication.this, "", 8);
				
		mApplication = this;
		IMEI = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();
		if(IMEI == null){
			IMEI = "";
		}
		IMEI = "IMEI-"+IMEI;
		MODEL = Build.MODEL;
		Android_OS = Build.VERSION.RELEASE;
		//初始化数据库
		SQLiteDatabase db = Connector.getDatabase();
		//初始化图片加载器
		initImageLoader(this);


		/**** Beta高级设置*****/
		/**
		 * true表示app启动自动初始化升级模块；
		 * false不好自动初始化
		 * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
		 * 在后面某个时刻手动调用
		 */
		Beta.autoInit = true;

		/**
		 * true表示初始化时自动检查升级
		 * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
		 */
		Beta.autoCheckUpgrade = true;

		/**
		 * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
		 */
		Beta.initDelay = 1 * 1000;

		/**
		 * 设置通知栏大图标，largeIconId为项目中的图片资源；
		 */
		Beta.largeIconId = R.mipmap.ic_launcher;

		/**
		 * 设置状态栏小图标，smallIconId为项目中的图片资源id;
		 */
		Beta.smallIconId = R.mipmap.ic_launcher;


		/**
		 * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
		 * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
		 */
		Beta.defaultBannerId = R.mipmap.ic_launcher;

		/**
		 * 设置sd卡的Download为更新资源保存目录;
		 * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
		 */
		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		/**
		 * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
		 */
		Beta.showInterruptedStrategy = false;

		/**
		 * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
		 * 不设置会默认所有activity都可以显示弹窗;
		 */
//		Beta.canShowUpgradeActs.add(MainActivity.class);


		/**
		 *  设置自定义升级对话框UI布局
		 *  注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
		 *  标题：beta_title，如：android:tag="beta_title"
		 *  升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
		 *  更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
		 *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
		 *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
		 *  详见layout/upgrade_dialog.xml
		 */
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

		/**
		 * 设置自定义tip弹窗UI布局
		 * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
		 *  标题：beta_title，如：android:tag="beta_title"
		 *  提示信息：beta_tip_message 如： android:tag="beta_tip_message"
		 *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
		 *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
		 *  详见layout/tips_dialog.xml
		 */
//        Beta.tipsDialogLayoutId = R.layout.tips_dialog;

		/**
		 *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
		 *  回调参数解释：
		 *  context - 当前弹窗上下文对象
		 *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
		 *  upgradeInfo - 升级信息
		 */
     /*  Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件，并更改imageview图片
                ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
                imageView.setImageResource(R.mipmap.ic_launcher);

                // 通过tag方式获取控件，并更改布局内容
                TextView textView = (TextView) view.findViewWithTag("textview");
                textView.setText("my custom text");

                // 更多的操作：比如设置控件的点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };*/

		/**
		 * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
		 */
       /* Beta.upgradeListener = new UpgradeListener() {

            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };*/


		/**
		* 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
		* init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
		* 参数1： applicationContext
				* 参数2：appId
				* 参数3：是否开启debug
				*/
		Bugly.init(getApplicationContext(), getResources().getString(R.string.bugly_app_id), false);

		/**
		 * 如果想自定义策略，按照如下方式设置
		 */

		/***** Bugly高级设置 *****/
		//        BuglyStrategy strategy = new BuglyStrategy();
		/**
		 * 设置app渠道号
		 */
		//        strategy.setAppChannel(APP_CHANNEL);

		//        Bugly.init(getApplicationContext(), APP_ID, true, strategy);


		//上报bug
//		CrashReport.initCrashReport(getApplicationContext());


		ShareSDK.initSDK(mContext);
	}

	public static Context getContext() {
		return mContext;
	}

	public ActivityManagerT1 getActivityManager() { 
		if(activityManager == null){
			activityManager = ActivityManagerT1.getScreenManager(); 
		}
		return activityManager; 
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constant.MSG_HIDE_HINT_LATER) {
				showMsg.cancel();
			}
		}
	};

	/**
	 *提示框
	 */
	public void showScreenHint(String tip){
		if(showMsg != null){
			mHandler.removeMessages(Constant.MSG_HIDE_HINT_LATER);
			showMsg.cancel();
			showMsg.setText(tip);
			showMsg.show();
			mHandler.sendEmptyMessageDelayed(Constant.MSG_HIDE_HINT_LATER, 1000);
		}
	}

	/**
	 * 提示框 提示时间长
	 * @param tip
	 */
	public void showLongScreenHint(String tip){
		if(showMsg != null){
			mHandler.removeMessages(Constant.MSG_HIDE_HINT_LATER);
			showMsg.cancel();
			showMsg.setText(tip);
			showMsg.show();
			mHandler.sendEmptyMessageDelayed(Constant.MSG_HIDE_HINT_LATER, 3000);
		}
	}
	
	public void exit(){
		EventBus.getDefault().post(new CloseMsgBean(1000));
		activityManager.popAllActivity();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
		System.exit(0);
	}

	/**
	 * 判断应用是否在最前端
	 * @return
	 */
	public boolean isAppOnForeground() {
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		return taskInfos.size() > 0 && TextUtils.equals(getPackageName(), taskInfos.get(0).topActivity.getPackageName());
	}
	
	public boolean isLocked(){
		KeyguardManager mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}

	public NotificationManager getNotificationManager() {
		if (mNotificationManager == null)
			mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		return mNotificationManager;
	}
	
	public String getTopActivity() {
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(1);
		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	public void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by ImageLoaderConfiguration.createDefault(this);
		// method.
		File file = new File(XmppConnectionManager.DOWN_PATH);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 1)//线程优先级
				.threadPoolSize(5)
						//.memoryCacheExtraOptions(640, 960)    // 设定缓存在内存的图片大小最大为200x200
				.denyCacheImageMultipleSizesInMemory()//不在内存中对同一个图片，缓存多个尺寸
						//.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCache(new UnlimitedDiscCache(file))//缓存最大10M
						//new DiskCache(file, //缓存目录
						//new HashCodeFileNameGenerator(), //文件名生成器 10*1024*1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)//后进先出
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCacheSizePercentage(13)
						//.memoryCache(new WeakMemoryCache())
						//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public String getImageUrl(){
		String ser_ip = MyApplication.getInstance().preferences.getString("ImageSrv", "");
		int ser_port = MyApplication.getInstance().preferences.getInt("ImageSrv_Port", 0);
		return "http://" +ser_ip+":"+ser_port;
	}

	public String getResourceUrl(){
		String ser_ip = MyApplication.getInstance().preferences.getString("ResourceSrv","");
		int ser_port = MyApplication.getInstance().preferences.getInt("ResourceSrv_Port",0);
		return "http://" +ser_ip+":"+ser_port;
	}

	public String getWebUrl(){
		String ser_ip = MyApplication.getInstance().preferences.getString("WebSrv","");
		int ser_port = MyApplication.getInstance().preferences.getInt("WebSrv_Port", 0);
		return "http://" +ser_ip+":"+ser_port;
	}
}

