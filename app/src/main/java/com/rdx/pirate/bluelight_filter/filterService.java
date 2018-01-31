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
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DebugUtils;
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

        Intent action1Intent = new Intent(this, NotificationActionService.class)
                .setAction(ACTION_1);

        PendingIntent action1PendingIntent = PendingIntent.getService(this, 0,
                action1Intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent action1Intent2 = new Intent(this, NotificationActionService.class)
                .setAction(ACTION_2);

        PendingIntent action1PendingIntent2 = PendingIntent.getService(this, 0,
                action1Intent2, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent action1Intent3 = new Intent(this, NotificationActionService.class)
                .setAction(ACTION_3);

        PendingIntent action1PendingIntent3 = PendingIntent.getService(this, 0,
                action1Intent3, PendingIntent.FLAG_UPDATE_CURRENT);


        if (notification == null) {

            notification = new Notification.Builder(this)
                    .setContentTitle("BlueLight Filter")
                    .setContentText("Click to stop")
                    .setSmallIcon(R.drawable.ic_pause_white_48dp)
                    .addAction(R.drawable.no_icon, "ON/OFF", action1PendingIntent)
                    .addAction(R.drawable.no_icon, "Pause", action1PendingIntent2)
                    .addAction(R.drawable.no_icon, "Settings", action1PendingIntent3)
                    .build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;


        }
            //       System.gc();
            //   notificationManager.notify(0, notification);



        startForeground(101, notification);

        Log.d("service", "filter service");



        pausefilter_runnable = new Runnable() {
            @Override
            public void run() {

                if(!sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null)) && sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){



                    pauseFilter();

                    try{
                        navActivity.pause_text.setText("Cancel Pause");
                        navActivity.pause_text.setTextColor(Color.RED);
                    }catch(Exception e){

                    }

                } else if(sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null)) && sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){


                    Log.d("dd","dekh lia");
                    stopFilter();
                    startFilter();
                    navActivity.pause_text.setText("Pause for 1 minute");
                    navActivity.pause_text.setTextColor(Color.BLACK);


                }
            }
        };

        start_stop_filter_runnable = new Runnable() {
            @Override
            public void run() {


                if (!sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))) {
                    startFilter();

                    try {
                        navActivity.fb_on_off.setImageResource(R.drawable.ic_pause_white_48dp);
                    }catch (Exception e){

                    }

                } else {

                    stopFilter();
                    try {
                        navActivity.fb_on_off.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                        navActivity.pause_text.setTextColor(Color.BLACK);
                        navActivity.pause_text.setText("Pause for 1 minute");
                    }catch (Exception e){

                    }


                    }


            }
        };


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



        settings_intent = new Intent(this, navActivity.class);
        settings_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    }


    private static Runnable start_stop_filter_runnable;


    private static Intent settings_intent;




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

        editor.putBoolean("isPause", true);
        editor.apply();
            window.removeView(linearLayout);
        handler.postDelayed(runner, 20000);


        }


        public static void stopFilter(){

            if(linearLayout!=null && !sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null))) {
                //linearLayout.setBackgroundColor(Color.TRANSPARENT);

                window.removeView(linearLayout);

            }
            if(editor!=null) {

                Log.d("ok","stop filter 1111");

                editor.putBoolean("isOnNightmode", false);
                editor.putBoolean("isPause", false);
                editor.apply();

                try{
                    handler.removeCallbacks(runner);
                }catch (Exception e){

                }

            }

        }

        public static void startFilter(){

            if(editor!=null){

                editor.putBoolean("isOnNightmode", true);
                editor.apply();






                linearLayout.setBackgroundColor((sharedPreferences.getInt("color", 1978150400)));

                //Log.d("ok","crash"+(sharedPreferences.getInt("color", 1978150400)));
                //linearLayout.setBackgroundColor(Integer.parseInt(sharedPreferences.getString("color", "1978150400")));




                try { window.updateViewLayout(linearLayout, params);


                } catch (Exception e) {
                    window.addView(linearLayout, params);

                    Log.d("ok","start filter inner body");
                }
            }

        }

    public static Runnable pausefilter_runnable;
    public static String ACTION_1 = "action_1";
    public static String ACTION_2 = "action_2";
    public static String ACTION_3 = "action_3";

    private static Handler handler_inner = new Handler(Looper.getMainLooper());


    public static class NotificationActionService extends IntentService {


        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }



        @Override
        protected void onHandleIntent(Intent intent) {
            String action = intent.getAction();
            // DebugUtils.log("Received notification action: " + action);
            if (ACTION_1.equals(action)) {
                // TODO: handle action 1.


                handler_inner.post(start_stop_filter_runnable);


                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            }else if(ACTION_2.equals(action)){

                handler_inner.post(pausefilter_runnable);


            }else if(ACTION_3.equals(action)){


                startActivity(settings_intent);


            }



        }

    }


    public static Handler handler = new Handler();
    public static Runnable runner;


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("hh","destroy called");

        if(sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){




            window.removeView(linearLayout);
            stopSelf();

        }

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
