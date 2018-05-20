package com.paranoid.runordie.utils;

import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class AnimationUtils {

    public static void setLogoAnimation(View logo, AnimatorListenerAdapter listener) {
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(
                App.getInstance(),
                R.animator.logo_rotation
        );
        anim.setTarget(logo);
        anim.start();
        anim.addListener(listener);
    }
}
