package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.brother.games.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;

public class RedHeartLoadingLayout extends LoadingLayout {

	public AnimationDrawable up_love_ani;
	
	public RedHeartLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs, 1);
		red_heart_iv.setBackgroundResource(R.drawable.red_heart_ani);
		up_love_ani = (AnimationDrawable)red_heart_iv.getBackground();  
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void pullToRefreshImpl() {
		
	}

	@Override
	protected void refreshingImpl() {
		
	}

	@Override
	protected void releaseToRefreshImpl() {
		up_love_ani.start(); 
	}

	@Override
	protected void resetImpl() {
		if(up_love_ani != null){
			up_love_ani.stop();
		}
	}

}
