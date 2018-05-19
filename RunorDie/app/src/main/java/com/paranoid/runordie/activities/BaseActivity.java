package com.paranoid.runordie.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
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

    private Toolbar toolbar;
    private View progressView;
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
            }
        }
    };

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        progressView = findViewById(R.id.progress_bar);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        App.getInstance().getState().setCurrentActivity(this);
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(BROADCAST_ACTION)
        );
    }

    @Override
    protected void onStop() {
        clearReferences();
        super.onStop();
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    @Override
    public Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void setActionBarTitle(int titleId) {
        setActionBarTitle(getString(titleId));
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (title != null && actionBar != null) {
            actionBar.setTitle(title);
        } else {
            Log.e("TAG", "can't set action bar title");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showProgress(boolean isLoading) {
        progressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void clearReferences() {
        Activity currentActivity = App.getInstance().getState().getCurrentActivity();
        if (this.equals(currentActivity))
            App.getInstance().getState().setCurrentActivity(null);
    }
}
