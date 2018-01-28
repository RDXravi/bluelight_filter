package com.rdx.pirate.bluelight_filter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by pirat on 1/23/2018.
 */

public class upCustomTimer extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("www","worked timer");

        //context.startService(new Intent(context,filterService.class));

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.gc();

    }
}
