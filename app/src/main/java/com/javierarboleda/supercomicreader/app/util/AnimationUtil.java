package com.javierarboleda.supercomicreader.app.util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Javier Arboleda on 5/4/16.
 *
 */
public class AnimationUtil {


    static public ValueAnimator getTopBottomPanelValueAnimator(final View panel, int toHeight) {
        ValueAnimator valueAnimator =
                ValueAnimator.ofInt(panel.getMeasuredHeight(), toHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = panel.getLayoutParams();

                layoutParams.height = val;

                panel.setLayoutParams(layoutParams);

            }
        });

        return valueAnimator;
    }

    static public ValueAnimator getLeftRightPanelValueAnimator(final View panel, int toHeight) {
        ValueAnimator valueAnimator =
                ValueAnimator.ofInt(panel.getMeasuredWidth(), toHeight);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = panel.getLayoutParams();

                layoutParams.width = val;

                panel.setLayoutParams(layoutParams);

            }
        });

        return valueAnimator;
    }




}
