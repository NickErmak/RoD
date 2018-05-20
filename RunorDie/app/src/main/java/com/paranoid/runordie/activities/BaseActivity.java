package com.paranoid.runordie.activities;

import android.app.Activity;
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

public abstract class BaseActivity extends AppCompatActivity implements IActionBarHandler, IProgressHandler, IRootLayoutHandler {

    private Toolbar toolbar;
    private View progressView;

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
    }

    @Override
    protected void onStop() {
        clearReferences();
        super.onStop();
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
