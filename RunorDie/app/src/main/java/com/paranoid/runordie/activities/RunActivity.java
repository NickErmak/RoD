package com.paranoid.runordie.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paranoid.runordie.R;
import com.paranoid.runordie.services.LocationService;
import com.paranoid.runordie.services.LocationService.LocationBinder;
import com.paranoid.runordie.utils.PermissionUtils;
import com.paranoid.runordie.utils.TimerUtil;

public class RunActivity extends BaseActivity {

    LocationService mService;
    private long startTime;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationBinder binder = (LocationBinder) service;
            mService = binder.getService();
            if (mService != null) {
                Log.e("TAG", "service is running");
                run(mService.getStartTime());
            } else {
                Log.e("TAG", "service is null");
            }
            Log.e("TAG", "service start time = " + mService.getStartTime());
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.e("TAG", "service disconnected");
            mBound = false;
        }
    };


    private ImageButton mIbStart;
    private TextView mTvTimer;
    private Button mBtnFinish;

    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setActionBarTitle(R.string.run_title);

        mBtnFinish = findViewById(R.id.run_btn_finish);
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), LocationService.class));
                TimerUtil.stopTimer();
                //TODO: finish for result??

                finish();
            }
        });
        mTvTimer = findViewById(R.id.run_tv_timer);
        mIbStart = findViewById(R.id.run_ib_start);
        mIbStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            run(TimerUtil.START_TIME_FIRST_LAUNCH);
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void run(long startTime) {
        mIbStart.setVisibility(View.GONE);
        mTvTimer.setVisibility(View.VISIBLE);
        mBtnFinish.setVisibility(View.VISIBLE);
        startTimer(startTime);
        startService(new Intent(this, LocationService.class));
    }

    private void startTimer(Long startTime) {
        this.startTime = TimerUtil.startTimer(mTvTimer, timerHandler, startTime);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    run(TimerUtil.START_TIME_FIRST_LAUNCH);
                    Log.e("TAG", "getPermissionResult");
                } else {
                    Toast.makeText(
                            this,
                            R.string.toast_permission_write_external_not_granted,
                            Toast.LENGTH_SHORT
                    ).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LocationService.class);
        bindService(intent, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mService.setStartTime(startTime);
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.run_root_layout);
    }
}
