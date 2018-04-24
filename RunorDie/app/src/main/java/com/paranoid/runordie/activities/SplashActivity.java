package com.paranoid.runordie.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.utils.AnimationUtils;

public class SplashActivity extends BaseActivity {

    private AnimatorListenerAdapter endListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            route();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setActionBarTitle(R.string.splash_title);
        AnimationUtils.setLogoAnimation(
                findViewById(R.id.splach_logo),
                endListener
        );
    }

    private void route() {
        Class<? extends BaseActivity> nextActivity;
        Log.e("TAG", "token = " + App.getInstance().getState().getToken());
        if (App.getInstance().getState().getToken() == null) {
            nextActivity = AuthActivity.class;
        } else {
            nextActivity = MainActivity.class;
        }
        startActivity(new Intent(this, nextActivity));
    }
}
