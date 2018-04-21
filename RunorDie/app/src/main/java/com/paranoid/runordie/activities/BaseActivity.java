package com.paranoid.runordie.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.api.activities.IActionBarHandler;
import com.paranoid.runordie.fragments.AbstractFragment;

public class BaseActivity extends AppCompatActivity implements IActionBarHandler {

    private Toolbar mToolbar;

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
}
