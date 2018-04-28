package com.paranoid.runordie.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.api.activities.IProgressHandler;
import com.paranoid.runordie.models.httpResponses.AbstractResponse;
import com.paranoid.runordie.utils.BroadcastUtils;

public class BaseActivity extends AppCompatActivity implements IActionBarHandler, IProgressHandler {

    private Toolbar mToolbar;
    private View mProgressView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            BroadcastUtils.ACTION action = (BroadcastUtils.ACTION) intent.getSerializableExtra(BroadcastUtils.EXTRA_ACTION);
            switch (action) {
                case ERROR:
                    String errorCode = intent.getStringExtra(BroadcastUtils.EXTRA_ERROR);
                    if (errorCode.equals(AbstractResponse.INVALID_TOKEN)) {
                        Toast.makeText(getApplicationContext(), "Authorization error", Toast.LENGTH_LONG).show();

                        Intent intent2 = new Intent(getApplicationContext(), AuthActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        finish();
                    }

                    Log.e("TAG", "error_code: " + errorCode);
                    break;
                    //TODO: test. rebuild for bolts
                case SUCCESS_LOGIN:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    break;
            }
        }
    };

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mProgressView = findViewById(R.id.progress_bar);
        setupToolbar();
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
    protected void onPause() {
        super.onPause();
        Log.e("TAG", "onPause Activity ");
        showProgress(false);
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.getInstance().getState().isLoading()) {
            showProgress(true);
        }
        Log.e("TAG", "onResume Activity" + ". Thread = " + Thread.currentThread().getName());
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(BroadcastUtils.BROADCAST_ACTION)
        );
    }

    @Override
    public void showProgress(boolean isLoading) {
        if (mProgressView != null) {
            mProgressView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        } else {
            Log.e("TAG", "progress view is null");
        }
    }
}
