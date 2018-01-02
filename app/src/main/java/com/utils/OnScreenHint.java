/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brother.games.R;


/**
 * A on-screen hint is a view containing a little message for the user and will
 * be shown on the screen continuously.  This class helps you create and show
 * those.
 * <p/>
 * <p/>
 * When the view is shown to the user, appears as a floating view over the
 * application.
 * <p/>
 * The easiest way to use this class is to call one of the static methods that
 * constructs everything you need and returns a new {@code OnScreenHint} object.
 */
public class OnScreenHint {
    static final String TAG = "OnScreenHint";
    static final boolean LOCAL_LOGV = false;

    int mGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    int mX, mY;
    float mHorizontalMargin;
    float mVerticalMargin;
    View mView;
    View mNextView;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private final WindowManager mWM;
    private final Handler mHandler = new Handler();

    /**
     * Construct an empty OnScreenHint object.  You must call {@link #setView}
     * before you can call {@link #show}.
     *
     * @param context The context to use.  Usually your
     *                {@link android.app.Application} or
     *                {@link android.app.Activity} object.
     */
    public OnScreenHint(Context context) {
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mY = context.getResources().getDimensionPixelSize(R.dimen.hint_y_offset);
        mParams.height = WindowManager.LayoutParams.FILL_PARENT;
        mParams.width = WindowManager.LayoutParams.FILL_PARENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.Animation_OnScreenHint;
//        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("OnScreenHint");
    }

    /**
     * Show the view on the screen.
     */
    public void show() {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        mHandler.post(mShow);
    }

    //mNextView
    public void show(String str) {
        if (mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        ((TextView) mNextView.findViewById(R.id.message)).setText(str);
        mHandler.post(mShow);
    }


    /**
     * Close the view if it's showing.
     */
    public void cancel() {
        mHandler.post(mHide);
    }

    /**
     * Make a standard hint that just contains a text view.
     *
     * @param context The context to use.  Usually your
     *                {@link android.app.Application} or
     *                {@link android.app.Activity} object.
     * @param text    The text to show.  Can be formatted text.
     */
    public static OnScreenHint makeText(Context context, CharSequence text, int y_precent) {
        OnScreenHint result = new OnScreenHint(context);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();//屏幕宽度
        int height = wm.getDefaultDisplay().getHeight();//屏幕高度
        FrameLayout.LayoutParams params_item = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params_item.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params_item.topMargin = height / y_precent * 5;
        View v = inflate.inflate(R.layout.on_screen_hint, null);
        RelativeLayout tips_bg = (RelativeLayout) v.findViewById(R.id.tips_bg);
        tips_bg.setLayoutParams(params_item);
        TextView tv = (TextView) v.findViewById(R.id.message);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        String s = text.toString();
        if (s.contains("金币") && s.contains("经验")) {
            style.setSpan(new BackgroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            style.setSpan(new ForegroundColorSpan(0xffffff), 2, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        tv.setText(style);

        result.mNextView = v;

        return result;
    }

    /**
     * Update the text in a OnScreenHint that was previously created using one
     * of the makeText() methods.
     *
     * @param s The new text for the OnScreenHint.
     */
    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This OnScreenHint was not "
                    + "created with OnScreenHint.makeText()");
        }
        TextView tv = (TextView) mNextView.findViewById(R.id.message);
        if (tv == null) {
            throw new RuntimeException("This OnScreenHint was not "
                    + "created with OnScreenHint.makeText()");
        }
        SpannableStringBuilder style = new SpannableStringBuilder(s);
        tv.setText(style);
//        ImageView iv = (ImageView)mNextView.findViewById(R.id.tips_icon);
//        if(icon_style == 0){
//        	if(iv.getVisibility() != View.VISIBLE){
//        		iv.setVisibility(View.VISIBLE)	;
//        	}
//        	iv.setBackgroundResource(R.drawable.tips_0);
//        }else if(icon_style == 1){
//        	if(iv.getVisibility() != View.VISIBLE){
//        		iv.setVisibility(View.VISIBLE)	;
//        	}    
//        	iv.setBackgroundResource(R.drawable.tips_1);
//        }else{
//        	if(iv.getVisibility() != View.GONE){
//        		iv.setVisibility(View.GONE)	;	
//        	}
//        }
    }

    private synchronized void handleShow() {
        if (mView != mNextView) {
            // remove the old view if necessary
            handleHide();
            mView = mNextView;
//            final int gravity = mGravity;
//            mParams.gravity = gravity;
//            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
//                    == Gravity.FILL_HORIZONTAL) {
//                mParams.horizontalWeight = 1.0f;
//            }
//            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK)
//                    == Gravity.FILL_VERTICAL) {
//                mParams.verticalWeight = 1.0f;
//            }
//            mParams.x = mX;
//            mParams.y = mY;
//            mParams.verticalMargin = mVerticalMargin;
//            mParams.horizontalMargin = mHorizontalMargin;

//            mParams.gravity = Gravity.CENTER;
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mWM.addView(mView, mParams);
        }
    }

    private synchronized void handleHide() {
        if (mView != null) {
            // note: checking parent() just to make sure the view has
            // been added...  i have seen cases where we get here when
            // the view isn't yet added, so let's try not to crash.
            if (mView.getParent() != null) {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }

    private final Runnable mShow = new Runnable() {
        public void run() {
            handleShow();
        }
    };

    private final Runnable mHide = new Runnable() {
        public void run() {
            handleHide();
        }
    };

}

