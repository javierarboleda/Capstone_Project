package com.javierarboleda.supercomicreader.app.ui;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.javierarboleda.supercomicreader.R;

/**
 * Created by javierarboleda on 6/27/16.
 *
 */
public class CustomPageTransformer implements ViewPager.PageTransformer {
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);

        if(position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F - Math.abs(position));
        }
    }
}

