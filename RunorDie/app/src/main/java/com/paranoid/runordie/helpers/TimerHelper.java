package com.paranoid.runordie.helpers;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.paranoid.runordie.utils.DateConverter;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class TimerHelper {

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (timerTextView != null) {
                timerTextView.get().setText(DateConverter.getCurrentTimerTime(startTime));
            }
            timerHandler.postDelayed(this, TIMER_REFRESH_INTERVAL_MS);
        }
    };

    public static long START_TIME_FIRST_LAUNCH = 0L;
    private static long START_TIMER_DELAY_MS = 0L;
    private static long TIMER_REFRESH_INTERVAL_MS = 10L;

    private long startTime;
    private Handler timerHandler;
    private WeakReference<TextView> timerTextView;

    public TimerHelper(TextView timerTextView, Handler timerHandler, long startTime) {
        this.timerTextView = new WeakReference<>(timerTextView);
        this.timerHandler = timerHandler;
        this.startTime = startTime;
    }

    public void startTimer() {
        if (startTime == START_TIME_FIRST_LAUNCH) {
            startTime = System.currentTimeMillis();
        }
        timerHandler.postDelayed(timerRunnable, START_TIMER_DELAY_MS);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
