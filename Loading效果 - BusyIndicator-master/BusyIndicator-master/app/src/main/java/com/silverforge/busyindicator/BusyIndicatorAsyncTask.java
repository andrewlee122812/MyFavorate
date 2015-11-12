package com.silverforge.busyindicator;

import android.app.Activity;
import android.os.AsyncTask;

import com.silverforge.controls.BusyIndicator;

import java.util.Random;

public class BusyIndicatorAsyncTask extends AsyncTask {

    private Activity activity;
    private BusyIndicator busyIndicator;

    public BusyIndicatorAsyncTask(Activity activity, BusyIndicator busyIndicator) {
        this.activity = activity;

        this.busyIndicator = busyIndicator;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        final Random rand = new Random();
        int i = 0;

        while (i < 103) {
            final int a = i;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    busyIndicator.setValue(a);
                }
            });

            i = i + rand.nextInt(20) + 1;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                busyIndicator.setValue(103);
            }
        });

        return null;
    }
}
