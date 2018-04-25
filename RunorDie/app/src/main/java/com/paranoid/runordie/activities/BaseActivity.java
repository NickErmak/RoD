package com.paranoid.runordie.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.fragments.AbstractFragment;
import com.paranoid.runordie.models.httpResponses.AbstractResponse;
import com.paranoid.runordie.utils.BroadcastUtils;

public class BaseActivity extends AppCompatActivity implements IActionBarHandler {

    private Toolbar mToolbar;
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
        LocalBroadcastManager.getInstance(App.getInstance()).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "onResume Activity" + ". Thread = " + Thread.currentThread().getName());
        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(
                receiver,
                new IntentFilter(BroadcastUtils.BROADCAST_ACTION)
        );
    }
}
