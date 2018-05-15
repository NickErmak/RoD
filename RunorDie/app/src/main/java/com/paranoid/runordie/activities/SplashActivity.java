package com.paranoid.runordie.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.PreferenceHelper;
import com.paranoid.runordie.models.Session;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.AnimationUtils;
import com.paranoid.runordie.utils.SnackbarUtils;

public class SplashActivity extends BaseActivity {

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

        setActionBarTitle(R.string.splash_title);
        AnimationUtils.setLogoAnimation(
                findViewById(R.id.splash_logo),
                animFinishListener
        );
    }

    private void route() {
        User user = PreferenceHelper.loadUser();
        if (user == null) {
            routeToAuth();
        } else {
            routeToMain(user);
        }
    }

    private void routeToAuth() {
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }

    private void routeToMain(final User user) {
        ApiClient.getInstance().login(user, new Callback<LoginResponse>() {
            @Override
            public void success(LoginResponse result) {
                Log.d("TAG", "success login");
                Session activeSession = new Session(user, result.getToken());
                App.getInstance().getState().setActiveSession(activeSession);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }

            @Override
            public void failure(NetworkException exception) {
                SnackbarUtils.showSnackbar(exception.getErrorCode());
                routeToAuth();
            }
        });
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.splash_root_layout);
    }
}
