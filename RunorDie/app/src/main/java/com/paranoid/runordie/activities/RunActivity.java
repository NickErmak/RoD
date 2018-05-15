package com.paranoid.runordie.activities;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.TimerHelper;
import com.paranoid.runordie.services.LocationService;
import com.paranoid.runordie.services.LocationService.LocationBinder;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.PermissionUtils;
import com.paranoid.runordie.utils.SnackbarUtils;

public class RunActivity extends BaseActivity {

    private LocationService mService;
    boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocationBinder binder = (LocationBinder) service;
            mService = binder.getService();
            if (mService != null) {
                Log.e("TAG", "service is running");
                Log.e("TAG", "service start time = " + mService.getStartTime());
               // run();
            } else {
                Log.e("TAG", "service is null");
            }

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

    private TextView tvTimerResult;
    private TextView tvDistanceResult;


    private TimerHelper timerHelper;

    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setActionBarTitle(R.string.run_title);

        tvTimerResult = (TextView) findViewById(R.id.run_tv_timer_result);
        tvDistanceResult = (TextView) findViewById(R.id.run_tv_distance_result);


        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.run_viewFlipper);
        viewFlipper.setInAnimation(getApplicationContext(), R.anim.in_from_left);
        viewFlipper.setOutAnimation(getApplicationContext(), R.anim.out_to_right);

        mBtnFinish = findViewById(R.id.run_btn_finish);
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), LocationService.class));
                timerHelper.stopTimer();
                long s =  timerHelper.getStartTime();
                tvTimerResult.setText(DateConverter.getTimerTime(s));
                tvDistanceResult.setText(String.format(
                        getString(R.string.distance_format),
                        mService.getDistance()
                ));
                viewFlipper.showNext();
                //TODO: finish for result??
            }
        });
        mTvTimer = findViewById(R.id.run_tv_timer);
        mIbStart = findViewById(R.id.run_ib_start);
        mIbStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewFlipper.showNext();
                checkPermission();
            }
        });

        long startTime = (mService != null) ? mService.getStartTime() : TimerHelper.START_TIME_FIRST_LAUNCH;
        timerHelper = new TimerHelper(mTvTimer, timerHandler, startTime);
    }

    private void checkPermission() {
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            run();
        } else {
            PermissionUtils.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void run() {
       // mIbStart.setVisibility(View.GONE);
        mTvTimer.setVisibility(View.VISIBLE);
        mBtnFinish.setVisibility(View.VISIBLE);
        startService(new Intent(this, LocationService.class));
        timerHelper.startTimer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    run();
                    Log.e("TAG", "getPermissionResult");
                } else {
                    SnackbarUtils.showSnackbar(R.string.permission_write_external_not_granted);
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
        bindService(intent, mConnection, Service.MODE_PRIVATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mService != null) {
            mService.setStartTime(timerHelper.getStartTime());
        }
        unbindService(mConnection);
        mBound = false;
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.run_root_layout);
    }
}
