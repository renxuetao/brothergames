/*
 * Copyright (C) 2015 Two Toasters
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
package com.handmark.pulltorefresh.library.jazzylistview.effects;

import android.view.View;

import com.handmark.pulltorefresh.library.jazzylistview.JazzyEffect;
import com.handmark.pulltorefresh.library.jazzylistview.JazzyHelper;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
//import android.view.ViewPropertyAnimator;

public class TiltEffect implements JazzyEffect {

    private static final float INITIAL_SCALE_FACTOR = 0.7f;

    @Override
    public void initView(View item, int position, int scrollDirection) {
//        item.setPivotX(item.getWidth() / 2);
//        item.setPivotY(item.getHeight() / 2);
//        item.setScaleX(INITIAL_SCALE_FACTOR);
//        item.setScaleY(INITIAL_SCALE_FACTOR);
//        item.setTranslationY(item.getHeight() / 2 * scrollDirection);
//        item.setAlpha(JazzyHelper.OPAQUE / 2);
    	ViewHelper.setPivotX(item, item.getWidth() / 2);
    	ViewHelper.setPivotY(item, item.getHeight() / 2);
    	ViewHelper.setScaleX(item, INITIAL_SCALE_FACTOR);
    	ViewHelper.setScaleY(item, INITIAL_SCALE_FACTOR);
        ViewHelper.setTranslationY(item, item.getHeight() / 2 * scrollDirection);
        ViewHelper.setAlpha(item, JazzyHelper.OPAQUE / 2);
    }

    @Override
    public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
        animator
            .translationYBy(-item.getHeight() / 2 * scrollDirection)
            .scaleX(1)
            .scaleY(1)
            .alpha(JazzyHelper.OPAQUE);
    }
}
