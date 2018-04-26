package com.paranoid.runordie.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.paranoid.runordie.R;
import com.paranoid.runordie.Test;
import com.paranoid.runordie.services.LocationService;
import com.paranoid.runordie.utils.PermissionUtils;
import com.paranoid.runordie.utils.TimerUtil;

public class RunActivity extends BaseActivity {

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
                run();
            }
        });
    }

    private void run() {
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mIbStart.setVisibility(View.GONE);
            mTvTimer.setVisibility(View.VISIBLE);
            mBtnFinish.setVisibility(View.VISIBLE);
            startTimer();
            startService();
            Test.createAlarmNotification();
        }
    }

    private void startTimer() {
        TimerUtil.startTimer(mTvTimer, timerHandler);
    }

    private void startService() {
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startService(new Intent(this, LocationService.class));
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, LocationService.class));
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
}
