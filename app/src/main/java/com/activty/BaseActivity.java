package com.activty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.MyApplication;
import com.brother.games.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;

public abstract class BaseActivity extends FragmentActivity {

    private android.support.v7.app.AlertDialog dialog;
    protected Context context = null;
    protected MyApplication myApplication;
    private View allView = null;
    public int mScreenWidth;
    public int mScreenHeight;
    public float mDensity;
    protected int mDpi;
    public ProgressDialog progressDialog = null;
    public boolean isStarted = false;
    public boolean isFinished = false;
    public boolean isStartedNew = false;
    public boolean isFinishedNew = false;

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected static final String TAG = "BaseActivity";

    public ProgressDialog setProgressMessage(String message) {
        progressDialog.setMessage(message);
        return progressDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        myApplication = (MyApplication) getApplication();
        if (savedInstanceState != null) {

        } else {

        }


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;
        mDensity = metric.density;
        mDpi = metric.densityDpi;
//		DebugUtil.print("mScreenWidth:", mScreenWidth);
//		DebugUtil.print("mScreenHeight:", mScreenHeight);
//		DebugUtil.print("mDensity:", mDensity);

        myApplication.getActivityManager().pushActivity(this);
//		getWindow().setBackgroundDrawableResource(R.drawable.main_bg);	
        getWindow().setBackgroundDrawableResource(R.color.man_bg_color);
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(Constant.ACTION);
//		registerReceiver(MsgReceiver, intentFilter);// 注册接受消息广播

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(getResources().getString(R.string.waiting));
        progressDialog.setMessage(getResources().getString(R.string.loding));

        isStarted = false;
        isFinished = false;

        isStartedNew = false;
        isFinishedNew = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
        myApplication.getActivityManager().popActivity(this);
        unregisterReceiver(moreMsgReceiver);
        unregisterReceiver(moreMsgNoDisplayReceiver);
        if (getallView() != null) {
            unbindDrawables(getallView());
        }
        getWindow().setBackgroundDrawable(null);

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        System.gc();
    }

    public void setContentView(int layoutResID) {
        LayoutInflater inflater = getLayoutInflater();
        this.allView = inflater.inflate(layoutResID, null);
        super.setContentView(this.allView);
    }

    public void setContentView(View view) {
        this.allView = view;
        super.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        this.allView = view;
        super.setContentView(view, params);
    }

    public View getallView() {
        return this.allView;
    }


    public void unbindDrawables(View view) {
        if (view == null) {
            return;
        }
        if (view.getBackground() != null) {
            view.setBackgroundDrawable(null);
        }
        if (view instanceof ImageView || view instanceof ImageButton) {
            ((ImageView) view).setImageDrawable(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        }
        try {
            ((ViewGroup) this.getWindow().getDecorView()).removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isExit() {
        try {
            new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.confirm_exit))
                    .setNeutralButton(getResources().getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myApplication.exit();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String text) {
        if (text.contains(getResources().getString(R.string.success))) return;
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        if (!MyApplication.getInstance().isAppOnForeground()) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 18) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            MyApplication.getInstance().showScreenHint(text);
        }
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    public void startActivityBroughtToFront(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(R.anim.new_push_left_in, R.anim.new_push_left_out);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.new_push_left_in, R.anim.new_push_left_out);
    }

    /**
     * 含有Bundle通过Class跳转界面
     *
     * @param cls
     * @param bundle
     * @param b_ani  true 动画 false 无动画
     */
    public void startActivity(Class<?> cls, Bundle bundle, boolean b_ani) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (b_ani) {
            overridePendingTransition(R.anim.new_push_left_in, R.anim.new_push_left_out);
        }
    }


    /**
     * 带有右进右出动画的退出
     **/
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.new_push_right_in, R.anim.new_push_right_out);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public final BaseMyHandler myHandler = new BaseMyHandler(this);

    public static class BaseMyHandler extends Handler {
        public final WeakReference<BaseActivity> mActivityReference;

        public BaseMyHandler(BaseActivity activity) {
            mActivityReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivityReference.get();
            if (activity != null) {

            }
        }
    }

    public String getClassName() {
        String class_name = "BaseActivity";
        try {
            class_name = this.getClass().getSimpleName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return class_name;
    }

    public MoreMsgReceiver moreMsgReceiver = null;

    protected class MoreMsgReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    public MoreMsgNoDisplayReceiver moreMsgNoDisplayReceiver = null;

    protected class MoreMsgNoDisplayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }

}
