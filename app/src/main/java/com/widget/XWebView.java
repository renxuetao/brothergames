package com.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class XWebView extends WebView {
    private Context context;
    private ProgressDialog dialog;
    private WVReceivedTitle mWVReceivedTitle;
    private String mWebUrl;

    public XWebView(Context context) {
        super(context);
        initWebView(context);
    }

    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebView(context);
    }

    public XWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWebView(context);
    }

    private void initWebView(final Context context) {
        this.context = context;
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setAllowFileAccess(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        // 设置可以支持缩放
        webSetting.setSupportZoom(true);
        // 设置出现缩放工具
        webSetting.setBuiltInZoomControls(true);
        //扩大比例的缩放
        webSetting.setUseWideViewPort(true);
        //自适应屏幕
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(context.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0).getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebUrl = url;
                setBackCookie();
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                if (mWVReceivedTitle != null) {
                    mWVReceivedTitle.onReceivedTitle(webView, title);
                }
                super.onReceivedTitle(webView, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                // newProgress 1-100之间的整数
                /*if (newProgress == 100) {
					// 网页加载完毕，关闭ProgressDialog
					closeDialog();
				} else {
					// 网页正在加载,打开ProgressDialog
					openDialog(newProgress);
				}*/
                if(newProgress == 100){
                    saveCookie();
                }
            }

            private void closeDialog() {
                // TODO Auto-generated method stub
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                saveCookie();
            }

            private void openDialog(int newProgress) {
                // TODO Auto-generated method stub
                if (dialog == null) {
                    dialog = new ProgressDialog(context);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }
        });
    }

    @Override
    public void loadUrl(String url) {
        mWebUrl = url;
        setBackCookie();
        super.loadUrl(url);
    }

    private void setBackCookie() {
        SharedPreferences preferences = context.getSharedPreferences("CookieSP", Context.MODE_PRIVATE);
        String cookieString = preferences.getString(Uri.parse(mWebUrl).getSchemeSpecificPart(), "");
        Log.d("yjw", "CookieSP==" + cookieString);

        CookieManager cookieManager = CookieManager.getInstance();
        CookieSyncManager.createInstance(context);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context).sync();
        }else{
            cookieManager.flush();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(mWebUrl, cookieString);
        Log.d("yjw", "setCookie之后==" + cookieManager.getCookie(mWebUrl));
        CookieSyncManager.getInstance().sync();
    }

    public void saveCookie() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CookieSP", Context.MODE_PRIVATE); // 私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString(Uri.parse(mWebUrl).getSchemeSpecificPart(), CookieManager.getInstance().getCookie(mWebUrl));
        editor.commit();// 提交修改
        Log.d("yjw", "Cookie保存成功==" + CookieManager.getInstance().getCookie(mWebUrl));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (this.canGoBack()) {
                this.goBack();// 返回上一页面
                return true;
            } else {
                System.exit(0);// 退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setWVReceivedTitle(WVReceivedTitle receivedTitle) {
        this.mWVReceivedTitle = receivedTitle;
    }

    public interface WVReceivedTitle {
        void onReceivedTitle(WebView webView, String s);
    }
}
