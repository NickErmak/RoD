package com.paranoid.runordie.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
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
import com.paranoid.runordie.fragments.AbstractFragment;
import com.paranoid.runordie.fragments.HomeFragment;
import com.paranoid.runordie.fragments.NotificationFragment;
import com.paranoid.runordie.fragments.TrackFragment;
import com.paranoid.runordie.helpers.NavigationHelper;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AbstractFragment.IActivityManager,
        HomeFragment.IOnTrackClickEvent {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawerLayout(drawerLayout);

        if (savedInstanceState == null) {
            showFragment(HomeFragment.newInstance(), false);
        }
    }

    private void setupDrawerLayout(DrawerLayout drawerLayout) {
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                getToolBar(),
                R.string.drawer_open,
                R.string.drawer_close
        );

        final NavigationView navView = (NavigationView) findViewById(R.id.main_nav_view);
        navView.setNavigationItemSelectedListener(this);
        NavigationHelper.refreshHeaderLogin(
                navView,
                App.getInstance().getState().getActiveSession().getUser()
        );
        navView.getMenu().getItem(0).setChecked(true);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                AbstractFragment currentFragment = (AbstractFragment) getCurrentFragment();
                switch (currentFragment.getFragTag()) {
                    case HomeFragment.FRAGMENT_TAG:
                        navView.setCheckedItem(R.id.nav_main);
                        break;
                    case NotificationFragment.FRAGMENT_TAG:
                        navView.setCheckedItem(R.id.nav_notifications);
                }
            }
        });

        findViewById(R.id.drawer_tv_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void showFragment(
            AbstractFragment fragToShow,
            boolean clearBackStack) {

        AbstractFragment currentFrag = getCurrentFragment();
        if (currentFrag != null && fragToShow.getFragTag().equals(currentFrag.getTag())) {
            return;
        }

        Log.d("TAG", "showing fragment tag = " + fragToShow.getFragTag());
        String tag = fragToShow.getFragTag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (clearBackStack) {
            fragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            );
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_frame_fragContainer, fragToShow, tag);
        transaction.addToBackStack(tag);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    private AbstractFragment getCurrentFragment() {
        return (AbstractFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_frame_fragContainer);
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
        item.setChecked(true);

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
        drawerLayout.closeDrawer(Gravity.START, true);
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
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
        return findViewById(getCurrentFragment().getRootLayoutId());
    }
}
