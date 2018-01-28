package com.rdx.pirate.bluelight_filter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;



/**
 * Created by pirat on 1/26/2018.
 */

public class notoficationService extends Service {
    private static Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


       /* Intent intent = new Intent(this, filterService.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);
*/

        notification = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_pause_white_48dp)

                .setAutoCancel(true)

                //.setSmallIcon(R.drawable.picture)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        System.gc();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(101, notification);

        System.gc();
        return START_NOT_STICKY;


    }
}
