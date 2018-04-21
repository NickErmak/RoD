package com.paranoid.runordie.api.activities;

import android.support.v7.widget.Toolbar;

public interface IActionBarHandler {

    void setActionBarTitle(String title);
    void setActionBarTitle(int titleId);
    Toolbar getToolBar();
}
