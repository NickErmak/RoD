package com.paranoid.runordie.utils;

import android.os.Handler;
import android.widget.TextView;

public class TimerUtil {

    //TODO: make this class non static?
    public static long START_TIME_FIRST_LAUNCH = 0;
    private static TextView timerTextView;
    private static long startTime;

    private static Handler timerHandler;

    public static long startTimer(TextView timerTextView, Handler timerHandler, long startTime) {
        TimerUtil.timerHandler = timerHandler;
        TimerUtil.startTime = (startTime == START_TIME_FIRST_LAUNCH)
                ? System.currentTimeMillis()
                : startTime;
        TimerUtil.timerTextView = timerTextView;
        timerHandler.postDelayed(timerRunnable, 0);
        return TimerUtil.startTime;
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
            timerTextView.setText(String.format("%d:%02d,%3d", minutes, seconds, millis));
            timerHandler.postDelayed(this, 10);
        }
    };
}
