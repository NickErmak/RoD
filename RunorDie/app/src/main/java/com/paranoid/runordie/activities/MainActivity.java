package com.paranoid.runordie.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.Test;
import com.paranoid.runordie.fragments.AbstractFragment;
import com.paranoid.runordie.fragments.HomeFragment;
import com.paranoid.runordie.fragments.NotificationFragment;
import com.paranoid.runordie.fragments.TrackFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AbstractFragment.IActivityManager,
        HomeFragment.IOnTrackClickEvent {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDrawerLayout();
        findViewById(R.id.drawer_tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        if (savedInstanceState == null) {
            showFragment(HomeFragment.newInstance(), false);
        }
    }

    public void showFragment(
            AbstractFragment frag,
            boolean clearBackStack) {

        Log.d("TAG", "showing fragment:  " + frag.getFragTag());
        String tag = frag.getFragTag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearBackStack) {
            fragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            );
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_frame_fragContainer, frag, tag);
        transaction.addToBackStack(tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private void setupDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolBar(),
                R.string.drawer_open,
                R.string.drawer_close
        );
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
    }

    private void logout() {
        Log.d("TAG", "log out_to_right");
        App.getInstance().getState().setActiveSession(null);
        Intent logoutIntent = new Intent(this, AuthActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(logoutIntent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        AbstractFragment frag = null;
        boolean clearBackStack = false;
        FragmentManager fragManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.nav_notifications:
                clearBackStack = false;
                frag = (AbstractFragment) fragManager.findFragmentByTag(NotificationFragment.FRAGMENT_TAG);
                if (frag == null) {
                    frag = NotificationFragment.newInstance();
                }
                break;
            case R.id.nav_main:
                clearBackStack = false;
                frag = (AbstractFragment) fragManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG);
                if (frag == null) {
                    frag = HomeFragment.newInstance();
                }
                break;
        }

        if (frag != null) {
            showFragment(
                    frag,
                    clearBackStack
            );
        }
        //TODO: remain closing animation?
        mDrawerLayout.closeDrawer(Gravity.START, true);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onTrackClick(long trackId) {
        showFragment(TrackFragment.newInstance(trackId), false);
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.main_root_layout);
    }
}
