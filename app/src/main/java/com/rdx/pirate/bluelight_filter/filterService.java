package com.rdx.pirate.bluelight_filter;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;



/**
 * Created by pirat on 1/16/2018.
 */

public class filterService extends Service {


    public static LinearLayout linearLayout;

    private int color;

    private static WindowManager window;

    private Notification notification;

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences.Editor editor;

    private static WindowManager.LayoutParams params;

    private int height;
    private int width;


    static {

    }

    public filterService() {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        editor.putBoolean("appCrash", true);
        editor.apply();
        Boolean test = sharedPreferences.getBoolean("test", Boolean.parseBoolean(null));

        Log.d("dd", "oncreate worked" + test);
        if (linearLayout == null) {
            linearLayout = new LinearLayout(getApplicationContext());

        }

        if (window == null) {


            window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();

            params = new WindowManager.LayoutParams(
                    (height + width),
                    (height + width),
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT
            );

        }

        if (notification == null) {

            notification = new Notification.Builder(this)
                    .setContentTitle("BlueLight Filter")
                    .setContentText("Click to stop")
                    .setSmallIcon(R.drawable.ic_pause_white_48dp)

                    .setAutoCancel(true)

                    //.setSmallIcon(R.drawable.picture)
                    .build();

        }
            //       System.gc();
            //   notificationManager.notify(0, notification);

            runner = new Runnable() {
                @Override
                public void run() {
                    Log.d("wait", "pause suucceess");

                    startFilter();
                    if (navActivity.pause_text != null) {

                        navActivity.pause_text.setText("Pause for 1 minute");
                        navActivity.pause_text.setTextColor(Color.BLACK);
                        editor.putBoolean("isPause", false);
                        editor.apply();
                    }



                }
            };

        startForeground(101, notification);

        Log.d("service", "filter service");



    }







    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        editor.putBoolean("isOnNightmode", true);
        editor.apply();
//        Log.d("vlaoe","crash at "+(sharedPreferences.getString("color", "1978150400")));
        linearLayout.setBackgroundColor((sharedPreferences.getInt("color", 1978150400)));

            // Log.d("cjj", "filter created");



                 /*   // is_pause = false;
                    startservice();

                    pause_text.setTextColor(Color.BLACK);
                    pause_text.setText("Pause for 1 minute");
                   */



        //  check();
/*
        navActivity.linearLayout_screenFilter.setClickable(false);
        navActivity.linearLayout_screenFilter.setBackgroundColor(color);*/
            // Log.d("cjj", "filter created11");

            //here is all the science of params


            // l2 = new LinearLayout(this);



/*
        param2 = (new WindowManager.LayoutParams(40, 40,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN ,
                PixelFormat.TRANSLUCENT));*/
            //  Log.d("cjj", "jhjhjhjh");

//LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

//ll.setLayoutParams(llParameters);


            //Log.d("color", "va");
            //  l2.setBackgroundColor(cl);


        /*WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0 | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, PixelFormat.TRANSLUCENT);

    */
        try {
                window.updateViewLayout(linearLayout, params);
            } catch (Exception e) {
                window.addView(linearLayout, params);


            }

            return START_NOT_STICKY;
        }


        public static void pauseFilter(){

            window.removeView(linearLayout);
        handler.postDelayed(runner, 20000);


        }


        public static void stopFilter(){

            if(linearLayout!=null && !sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null))) {
                //linearLayout.setBackgroundColor(Color.TRANSPARENT);
                window.removeView(linearLayout);

            }
            if(editor!=null) {

                editor.putBoolean("isOnNightmode", false);
                editor.putBoolean("isPause", false);
                editor.apply();
                handler.removeCallbacks(runner);


            }
        }

        public static void startFilter(){

            if(editor!=null){

                editor.putBoolean("isOnNightmode", true);
                editor.apply();
                //Log.d("ok","crash"+(sharedPreferences.getInt("color", 1978150400)));
                //linearLayout.setBackgroundColor(Integer.parseInt(sharedPreferences.getString("color", "1978150400")));
                try {
                    window.updateViewLayout(linearLayout, params);
                } catch (Exception e) {
                    window.addView(linearLayout, params);


                }
            }

        }


    public   static Handler handler = new Handler();
    public static Runnable runner;


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("hh","destroy called");

        editor.putBoolean("isOnNightmode", false);
        editor.apply();


        editor.putBoolean("appCrash", false);
        editor.apply();

        if (navActivity.pause_text!=null){

            navActivity.pause_text.setText("Pause for 1 minute");
            navActivity.pause_text.setTextColor(Color.BLACK);
        }
        /*Log.d("service","filter servbice stops");
        if(isOnNightMode)
        stopForeground(true);

        if(is_pause) {
            is_pause = false;
            navActivity.pause_text.setTextColor(Color.BLACK);
            navActivity.pause_text.setText("Pause for 1 minute");

        }*/
    }


}
