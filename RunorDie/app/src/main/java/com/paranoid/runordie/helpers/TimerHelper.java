package com.paranoid.runordie.helpers;

import android.os.Handler;
import android.widget.TextView;

import com.paranoid.runordie.utils.DateConverter;

import java.lang.ref.WeakReference;

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

    private static long START_TIME_FIRST_LAUNCH = 0L;
    private static long START_TIMER_DELAY_MS = 0L;
    private static long TIMER_REFRESH_INTERVAL_MS = 10L;

    private long startTime;
    private Handler timerHandler;
    private volatile WeakReference<TextView> timerTextView;

    public TimerHelper(TextView timerTextView, Handler timerHandler) {
        this.timerTextView = new WeakReference<>(timerTextView);
        this.timerHandler = timerHandler;
    }

    public void startTimer(long startTime) {
        this.startTime = (startTime == START_TIME_FIRST_LAUNCH)
                ? System.currentTimeMillis()
                : startTime;
        timerHandler.postDelayed(timerRunnable, START_TIMER_DELAY_MS);
    }

    public void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }
}
