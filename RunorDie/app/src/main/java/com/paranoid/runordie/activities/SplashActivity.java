package com.paranoid.runordie.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.network.NetworkUtils;

public class SplashActivity extends BaseActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setActionBarTitle(R.string.splash_title);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User currentUser = App.getInstance().getState().getUser();
                if (currentUser == null) {

                } else {
                    //start main activity
                }
            }
        }, 0);

        ImageView imageView = findViewById(R.id.splach_logo);


        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView, View.ROTATION_Y, 0.0f, 360f);
        animation.setDuration(3000L);
        animation.setRepeatCount(0);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            }
        });

        scheduleSplashScreen();
    }

    private void scheduleSplashScreen() {

    }

    private void routeToAppropriatePage() {

    }

   /* private static class MyHandler extends Handler {}
    private final MyHandler mHandler = new MyHandler();

    public static class MyRunnable implements Runnable {
        private final WeakReference<Activity> mActivity;

        public MyRunnable(Activity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void run() {
            Activity activity = mActivity.get();
            if (activity != null) {
                Button btn = (Button) activity.findViewById(R.id.button);
                btn.setBackgroundResource(R.drawable.defaultcard);
            }
        }
    }

    private MyRunnable mRunnable = new MyRunnable(this);

    public void onClick(View view) {
        my_button.setBackgroundResource(R.drawable.icon);

        // Execute the Runnable in 2 seconds
        mHandler.postDelayed(mRunnable, 2000);
    }*/
}
