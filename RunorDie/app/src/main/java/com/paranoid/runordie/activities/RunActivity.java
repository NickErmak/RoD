package com.paranoid.runordie.activities;

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
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.LocationHelper;
import com.paranoid.runordie.helpers.TimerHelper;
import com.paranoid.runordie.services.LocationService;
import com.paranoid.runordie.services.LocationService.LocationBinder;
import com.paranoid.runordie.utils.DateConverter;
import com.paranoid.runordie.utils.DistanceUtils;
import com.paranoid.runordie.utils.PermissionUtils;
import com.paranoid.runordie.utils.SnackbarUtils;


public class RunActivity extends BaseActivity {

    private String jpsAccessNotGrantedMsg;
    private String finishBeforeReturnMsg;

    private LocationService locationService;
    boolean bound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d("TAG", "Run activity: location service connected");
            LocationBinder binder = (LocationBinder) service;
            locationService = binder.getService();
            if (locationService != null) {
                viewFlipper.showNext();
                timerHelper.setStartTime(locationService.getStartTime());
                timerHelper.startTimer();
            }
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("TAG", "Run activity: location service disconnected");
            bound = false;
        }
    };

    private ViewFlipper viewFlipper;
    private TextView tvTimer;
    private TextView tvTimerResult;
    private TextView tvDistanceResult;

    private TimerHelper timerHelper;
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        setActionBarTitle(R.string.run_title);

        initViews();
        initFromResources();
        long startTime = (locationService != null) ? locationService.getStartTime() : TimerHelper.START_TIME_FIRST_LAUNCH;
        timerHelper = new TimerHelper(tvTimer, timerHandler, startTime);
    }

    private void initViews() {
        tvTimerResult = (TextView) findViewById(R.id.run_tv_timer_result);
        tvDistanceResult = (TextView) findViewById(R.id.run_tv_distance_result);
        tvTimer = findViewById(R.id.run_tv_timer);
        viewFlipper = (ViewFlipper) findViewById(R.id.run_viewFlipper);

        viewFlipper.setInAnimation(getApplicationContext(), R.anim.in_from_left);
        viewFlipper.setOutAnimation(getApplicationContext(), R.anim.out_to_right);

        findViewById(R.id.run_ib_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRunning();
            }
        });

        findViewById(R.id.run_btn_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                stopService(new Intent(getApplicationContext(), LocationService.class));
                timerHelper.stopTimer();
                long s = timerHelper.getStartTime();
                tvTimerResult.setText(DateConverter.getCurrentTimerTime(s));
                tvDistanceResult.setText(DistanceUtils.getDistanceFormat(locationService.getDistance()));
                viewFlipper.showNext();
            }
        });
    }

    private void initFromResources() {
        jpsAccessNotGrantedMsg = getString(R.string.permission_access_fine_location_not_granted);
        finishBeforeReturnMsg = getString(R.string.run_finish_error);
    }

    private void startRunning() {
        if (LocationHelper.checkLocationPermission() && LocationHelper.checkLocationAccess()) {
            //todo logic
            startService(new Intent(this, LocationService.class));
            timerHelper.startTimer();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationHelper.LOCATION_REQUEST_CODE:
                startRunning();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", "permission access_fine_location: granted");
                    startRunning();
                } else {
                    Log.d("TAG", "permission access_fine_location: not granted");
                    SnackbarUtils.showSnack(jpsAccessNotGrantedMsg);
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
        if (locationService != null) {
            locationService.setStartTime(timerHelper.getStartTime());
        }
        unbindService(mConnection);
        bound = false;
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.run_root_layout);
    }

    @Override
    public void onBackPressed() {
        if (locationService != null && locationService.isActive()) {
            SnackbarUtils.showSnack(finishBeforeReturnMsg);
        } else {
            super.onBackPressed();
        }
    }
}
