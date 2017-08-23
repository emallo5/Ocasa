package com.android.ocasa.util;

import android.content.Context;
import android.view.View;

import com.android.ocasa.R;

/**
 * Created by leandro on 23/8/17.
 */

public class AnimationUtils {

    public static void enterFromLeft(View v, Context context) {
        v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_left_anim));
    }

    public static void enterFromRight(View v, Context context) {
        v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_right_anim));
    }

    public static void enterFromBottom(View v, Context context) {
        v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom_anim));
    }

    public static void enterFromTop(View v, Context context) {
        v.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.slide_in_top_anim));
    }
}
