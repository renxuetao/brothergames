package com.activty.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activty.BaseActivity;
import com.brother.games.R;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.utils.LogUtil;
import com.widget.XWebView;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class GameWebViewActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back_fl = null;//返回
    private TextView title = null;//中间文字
//	private TextView complete = null;//右边文字

    private XWebView webView = null;
    private int type = -1;
    private String mToken;

//	private XWalkView xWalkWebView;

    @SuppressLint("JavascriptInterface")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_webview);
        findView();
        initView();
    }

    private void findView() {
        back_fl = (RelativeLayout) findViewById(R.id.back_fl);
        back_fl.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        webView = (XWebView) findViewById(R.id.WebView);
    }

    private void initView() {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
        String url = getIntent().getStringExtra("url");
        type = getIntent().getIntExtra("type", -1);
        LogUtil.d("type====" + type);
        if (type == 0) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        String name = getIntent().getStringExtra("name");
        title.setText(name);

        final JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
        webView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.d("WebUrl===" + url);
                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        webView.loadUrl(url);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.saveCookie();
            webView.destroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
        LogUtil.d("onConfigurationChanged===="+message);
    }

    @Override
    public void onClick(View v) {
        if (v == back_fl) {
            closeWebView();
        }
    }


    @Override
    public void onBackPressed() {
        closeWebView();
    }

    private void closeWebView() {
        if (webView != null) {
            webView.saveCookie();
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        }
        finish();
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        public void showToast(String webMessage) {
            final String msgeToast = webMessage;
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    // This gets executed on the UI thread so it can safely modify Views
                    title.setText(msgeToast);
                }
            });
            Toast.makeText(mContext, webMessage, Toast.LENGTH_SHORT).show();
        }
    }

    //清空cookie方法
    public void clearCookies(Context context) {
        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }
}
