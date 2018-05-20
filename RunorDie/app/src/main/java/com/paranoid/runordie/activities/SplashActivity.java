package com.paranoid.runordie.activities;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.PreferenceHelper;
import com.paranoid.runordie.models.Session;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.SnackbarUtils;

public class SplashActivity extends BaseActivity {
    private static ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(
            App.getInstance(),
            R.animator.logo_rotation
    );

    public enum ROUTE {
        RUN_ACTIVITY
    }

    public static String ROUTE_KEY = "ROUTE_KEY";

    private ROUTE routeActivity;
    private AnimatorListenerAdapter animFinishListener = new AnimatorListenerAdapter() {
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

        setupActionBar();

        Bundle extras = getIntent().getExtras();
        routeActivity = (extras != null) ? (ROUTE) extras.getSerializable(ROUTE_KEY) : null;

        startAnimation(findViewById(R.id.splash_logo));
    }

    @Override
    protected void onResume() {
        super.onResume();
        anim.addListener(animFinishListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        anim.removeAllListeners();
    }

    private void setupActionBar() {
        setActionBarTitle(R.string.splash_title);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void startAnimation(View logo) {
        logo.removeCallbacks(Thread.currentThread());
        anim.setTarget(logo);
        anim.start();
    }

    private void route() {
        User user = PreferenceHelper.loadUser();
        if (user == null) {
            routeToAuth();
        } else {
            routeNext(user);
        }
    }

    private void routeToAuth() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    private void routeNext(final User user) {
        ApiClient.getInstance().login(user, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse result) {
                Log.d("TAG", "success login");
                Session activeSession = new Session(user, result.getToken());
                App.getInstance().getState().setActiveSession(activeSession);

                if (routeActivity != null) {
                    switch (routeActivity) {
                        case RUN_ACTIVITY:
                            TaskStackBuilder.create(getApplicationContext())
                                    .addNextIntentWithParentStack(
                                            new Intent(getApplicationContext(),
                                                    RunActivity.class)
                                    )
                                    .startActivities();
                            break;
                    }
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                finish();
            }

            @Override
            public void failure(NetworkException exception) {
                SnackbarUtils.showSnack(exception.getErrorCode());
                routeToAuth();
            }
        });
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.splash_root_layout);
    }
}
