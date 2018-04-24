package com.paranoid.runordie.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paranoid.runordie.R;
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
                TimerUtil.stopTimer();
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
        mIbStart.setVisibility(View.GONE);
        mTvTimer.setVisibility(View.VISIBLE);
        mBtnFinish.setVisibility(View.VISIBLE);
        startTimer();
    }

    private void startTimer() {
        TimerUtil.startTimer(mTvTimer, timerHandler);
    }





}
