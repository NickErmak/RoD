package com.paranoid.runordie.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.api.activities.IProgressHandler;
import com.paranoid.runordie.api.activities.IRootLayoutHandler;

import static com.paranoid.runordie.utils.broadcastUtils.AppBroadcast.ACTION;
import static com.paranoid.runordie.utils.broadcastUtils.AppBroadcast.BROADCAST_ACTION;
import static com.paranoid.runordie.utils.broadcastUtils.AppBroadcast.EXTRA_ACTION;

public abstract class BaseActivity extends AppCompatActivity implements IActionBarHandler, IProgressHandler, IRootLayoutHandler {

    private Toolbar mToolbar;
    private View mProgressView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "broadcast received (base activity)");
            ACTION action = (ACTION) intent.getSerializableExtra(EXTRA_ACTION);
            switch (action) {
                case SUCCESS_LOGIN:
                    Intent routeMainIntent = new Intent(getApplicationContext(), MainActivity.class);
                    routeMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(routeMainIntent);
                    break;
                case STOP_REFRESHING:
                    showProgress(false);
                    break;

              /*  case ERROR:
                    String errorCode = intent.getStringExtra(EXTRA_ERROR);
                    if (errorCode.equals(AbstractResponse.INVALID_TOKEN)) {
                        //TODO: snack
                        Toast.makeText(getApplicationContext(), "Authorization error", Toast.LENGTH_LONG).show();

                        Intent intent2 = new Intent(getApplicationContext(), AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        finish();
                    }

                    Log.e("TAG", "error_code: " + errorCode);
                    break;*/

                //TODO: test. rebuild for bolts
            }
        }
    };

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mProgressView = findViewById(R.id.progress_bar);
        setupToolbar();
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    public void setActionBarTitle(int titleId) {
        setActionBarTitle(getString(titleId));
    }

    @Override
    public void setActionBarTitle(String title) {
        if (title != null) {
            getSupportActionBar().setTitle(title);
        } else {
            Log.e("TAG", "actionBar title is null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().getState().setCurrentActivity(this);
        if (App.getInstance().getState().isLoading()) {
            showProgress(true);
        }
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(BROADCAST_ACTION)
        );
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
        showProgress(false);
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    @Override
    public void showProgress(boolean isLoading) {
        if (mProgressView != null) {
            mProgressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        } else {
            Log.e("TAG", "progress view is null");
        }
    }

    private void clearReferences() {
        Activity currentActivity = App.getInstance().getState().getCurrentActivity();
        if (this.equals(currentActivity))
            App.getInstance().getState().setCurrentActivity(null);
    }
}
