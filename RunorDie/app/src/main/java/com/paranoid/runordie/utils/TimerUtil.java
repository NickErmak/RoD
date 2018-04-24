package com.paranoid.runordie.utils;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;

public class TimerUtil {

    private static TextView timerTextView;
    private static long startTime = 0;

    private static Handler timerHandler;

    public static void startTimer(TextView timerTextView, Handler timerHandler) {
        TimerUtil.timerHandler = timerHandler;
        TimerUtil.startTime = System.currentTimeMillis();
        TimerUtil.timerTextView = timerTextView;
        timerHandler.postDelayed(timerRunnable, 0);

    }

    public static void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    static Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;

            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            millis = (millis % 1000) / 10;

            seconds = seconds % 60;
            String time = String.format("%d:%02d,%3d", minutes, seconds, millis);
            Log.e("TAG", time);
            timerTextView.setText(time);
            timerHandler.postDelayed(this, 10);
        }
    };



}
