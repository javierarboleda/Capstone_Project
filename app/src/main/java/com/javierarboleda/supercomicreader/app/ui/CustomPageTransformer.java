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
        int pageWidth = view.getWidth();
        View imageView = view.findViewById(R.id.comic_image_view);
        View hiddenImageView = view.findViewById(R.id.comicHiddenImageView);
        View emptyView = view.findViewById(R.id.empty_view);

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left
        } else if (position <= 0) { // [-1,0]
            // This page is moving out to the left

            // Counteract the default swipe
            view.setTranslationX(pageWidth * -position);
            if (imageView != null) {
                // Fade the image in
                imageView.setAlpha(1 + position);
            }
            if (hiddenImageView != null) {
                // Fade the image in
                hiddenImageView.setAlpha(1 + position);
            }
            if (emptyView != null) {
                // Fade the image in
                emptyView.setAlpha(1 + position);
            }
            if (imageView != null) {
                // Fade the image in
                imageView.setAlpha(1 + position);
            }

        } else if (position <= 1) { // (0,1]
            // This page is moving in from the right

            // Counteract the default swipe
            view.setTranslationX(pageWidth * -position);
            if (hiddenImageView != null) {
                // Fade the image out
                hiddenImageView.setAlpha(1 - position);
            }
            if (emptyView != null) {
                // Fade the image out
                emptyView.setAlpha(1 - position);
            }
            if (imageView != null) {
                // Fade the image out
                imageView.setAlpha(1 - position);
            }
        } else { // (1,+Infinity]
            // This page is way off-screen to the right
        }
    }
}

