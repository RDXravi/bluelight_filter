package com.rdx.pirate.bluelight_filter;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentCallbacks2;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;



public class navActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComponentCallbacks2 {



    private int color;


    private int color_clr_temp;
    private int color_intensity;
    private int color_screendim;


    private NavigationView navigationView;


    public int progress_clr_temp;
    public int progress_intensity;
    public int progress_screendim;

    public boolean ison_fb_on_off = false;

    //Intent for making services

    public Intent intent;

    public static FloatingActionButton fb_on_off;
    private SharedPreferences.Editor editor;

    private SharedPreferences sharedPreferences;

    private int check_press = 0;
    public static TextView pause_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);



        //toolbar functionality
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //taking permission for overlay from android OS
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(navActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())) {
                // Do stuff here
            } else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                // intent.setData(Uri.parse(“package:” + getCallingActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
*/
        //rough value shown
        //tv = (TextView) findViewById(R.id.myview);


       /* notification= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Whip And Weep")
                .setContentText("Whip is On!")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
        nMN.notify(NOTIFICATION_ID, n);*/


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();


        //Floating button for on off
        fb_on_off = (FloatingActionButton) findViewById(R.id.floating_on_off);



        //COLOR TEMPERATURE ELEMENTS
        ImageView img_ColorTemperature = (ImageView) findViewById(R.id.img_color_temp);
        SeekBar seekbar_ColorTemperature = (SeekBar) findViewById(R.id.seekBar_color_temp);
        seekbar_ColorTemperature.setMax(125);

        //Intensity Functionality
        ImageView img_intensity = (ImageView)findViewById(R.id.img_intensity);
        SeekBar seekbar_intensity = (SeekBar) findViewById(R.id.seekBar_intensity);
        seekbar_intensity.setProgress(90);
        seekbar_intensity.setMax(150);

        //Brightness Functionality
        ImageView img_screendim = (ImageView) findViewById(R.id.img_screen_dim);
        SeekBar seekbar_screendim = (SeekBar) findViewById(R.id.seekBar_screen_dim);
        seekbar_screendim.setMax(100);

        //getting image preview circles
        final GradientDrawable preview_colorTemperature = (GradientDrawable)img_ColorTemperature.getBackground();
        final GradientDrawable preview_intensity = (GradientDrawable)img_intensity.getBackground();
        final GradientDrawable preview_screendim = (GradientDrawable)img_screendim.getBackground();


        //load defaults
       loadDefaults();






        seekbar_ColorTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // tv.setText(String.valueOf(progress));

                progress_clr_temp = progress;

                //finding color for values
                color_clr_temp = colorMixer.colorMix(progress, progress_intensity);

                //storing in main color variable
                color = color_clr_temp;
                Log.d("color value","value"+color);
                //filterService.color = color;
                if(sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))) {


                    filterService.linearLayout.setBackgroundColor(color);

                }
                //setting color in color_temperature image
                preview_colorTemperature.setColor(color);
                preview_intensity.setColor(color);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                editor.putInt("color", (color));
                editor.apply();


            }
        });




        seekbar_intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress_intensity = progress;

                // storing intensity to color_intensity variable
                color_intensity = colorMixer.colorMix(progress_clr_temp, progress);

                //storing intensity color to main color variable;
                color = color_intensity;
              //  filterService.color = color;
                if(sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))) {


                    filterService.linearLayout.setBackgroundColor(color);

                }
                //setting image of intensity the actual color
                preview_intensity.setColor(color);
                preview_colorTemperature.setColor(color);
               // filter.color = color;
//                filter.ll.setBackgroundColor(color);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                editor.putInt("color", (color));
                editor.apply();
            }
        });



        seekbar_screendim.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progress_screendim = progress;

                //storing color value for screen dim
                color_screendim = colorMixer.screenDim(progress, progress_clr_temp, progress_intensity);

                //storing color screen dim to the actual value of color
                color = color_screendim;
               // filterService.color = color;
                if(sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))) {


                    filterService.linearLayout.setBackgroundColor(color);

                }
                //setting color value to its image
                preview_screendim.setColor(color);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {


                editor.putInt("color", (color));
                editor.apply();




            }
        });


        //Pause button functions
        LinearLayout pause = (LinearLayout)findViewById(R.id.pause_min);
        pause_text = (TextView)findViewById(R.id.pause_text);
        ImageView pause_image = (ImageView)findViewById(R.id.pause_image);
        pause.isClickable();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pauseFunction();


            }
        });

        //Custom Timer
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        final TextView upTime = (TextView) findViewById(R.id.timer_uptime);
        final TextView separator = (TextView) findViewById(R.id.timer_separator);
        final TextView downTime = (TextView) findViewById(R.id.timer_downtime);
        final TextView setTimer = (TextView)findViewById(R.id.set_auto_timer);
        final Switch switchSeTimer = (Switch)findViewById(R.id.switch_customTimer);
        final RelativeLayout setTimerLayout = (RelativeLayout)findViewById(R.id.set_timer_layout);


        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent iup = new Intent(this, upCustomTimer.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, 0, iup, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent idown = new Intent(this, downCustomTimer.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, 0, idown, PendingIntent.FLAG_UPDATE_CURRENT);




        setTimerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimerLayout.setClickable(Boolean.parseBoolean("false"));
                setTimer.setText("");
                switchSeTimer.setChecked(true);
                upTime.setText("ON 22:00");
                separator.setText("|");
                downTime.setText("OFF 6:00");


            }
        });

        switchSeTimer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){

                    setTimer.setText("Set Auto Timer");
                    upTime.setText("");
                    downTime.setText("");
                    separator.setText("");
                    setTimerLayout.setClickable(true);
                }else if(isChecked){

                    setTimerLayout.setClickable(Boolean.parseBoolean("false"));
                    setTimer.setText("");
                    switchSeTimer.setChecked(true);
                    upTime.setText("ON  22:00");
                    separator.setText("|");
                    downTime.setText("OFF  6:00");

                }
            }
        });



        final TimePickerDialog tp = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(check_press==0) {
                    upTime.setText("ON  "+hourOfDay + ":" + minute);
                  //  ss = upTime.getText().toString().split("  ");
                  //  aa= (ss[1].split(":"));
               //     startTimer(Integer.parseInt(aa[0]), Integer.parseInt(aa[1]));

                }else if (check_press==1){
                    downTime.setText("OFF  "+hourOfDay+":"+minute);

                }

            }
        },hour, minute, true);

        upTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_press=0;
                tp.show();


            }
        });
        downTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_press=1;
                tp.show();
            }
        });


        //Settings button for showing its activity
        LinearLayout settings_button = (LinearLayout)findViewById(R.id.settings_linear_latout);
        final Intent settings_intent = new Intent(getApplicationContext(), settingsActivity.class);
        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(settings_intent);
            }
        });


        intent = (new Intent(getApplicationContext(), filterService.class));




     //   Log.d("nightmode","  "+ isOnNightMode);
        Log.d("is pause","  ");




       // fb_on_off = (FloatingActionButton)findViewById(R.id.floating_on_off);
        fb_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               activate_deactivate_filter(view);

            }
        });


        //Drawer functionality
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //Navigation Functionality
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu mn = navigationView.getMenu();
        on_off = mn.findItem(R.id.nav_on_off);


       /* if(isOnNightMode){
            on_off.setTitle("ON");
        }*/



    }

    private void activate_deactivate_filter(View view) {
        if(!sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){

            //           startService(intent);
            startservice();
            Snackbar.make(view, "Night mode activated", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }else {

            stopservice();
            Snackbar.make(view, "Night mode Deactivated", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();

        }

    }

    private void activate_deactivate_filter() {
        if(!sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){

            //           startService(intent);
            startservice();

        }else {

            stopservice();

        }

    }


    private void pauseFunction(){


        if(!sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null)) && sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){
            filterService.pauseFilter();
            editor.putBoolean("isPause", true);
            editor.apply();

            pause_text.setText("Cancel Pause");
            pause_text.setTextColor(Color.RED);
            Log.d("1","first");
        }else if(sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null)) && sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){

            pause_text.setText("Pause for 1 minute");
            pause_text.setTextColor(Color.BLACK);
            editor.putBoolean("isPause", false);
            editor.apply();
            filterService.handler.removeCallbacks(filterService.runner);
            filterService.startFilter();
            Log.d("1","second");
        }

    }





    private MenuItem on_off;

    //drawer back button functionality
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //inflating menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }


    //ActionBar elements functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    //Drawer items selected response
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        //on_off = findViewById(R.id.nav_on_off);


        if (id == R.id.nav_on_off) {

            activate_deactivate_filter();
            // Handle the camera action
        } else if (id == R.id.nav_pause) {

            pauseFunction();

        } else if (id == R.id.nav_stop) {

            stopService(intent);
            finish();

        } else if (id == R.id.nav_setting) {

           // startActivity(settings_intent);

        } else if (id == R.id.nav_default) {

        } else if (id == R.id.nav_nightWalk) {

        } else if ( id == R.id.nav_bedReading){

        }

        if (id != R.id.nav_on_off || id!=R.id.nav_pause) {

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }
        return true;
    }


    //Checking whether service is running
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //default values
    private void loadDefaults(){
        progress_clr_temp = 50;
        //seekbar_ColorTemperature.setProgress(progress_clr_temp);
        progress_intensity = 120;
        //seekbar_intensity.setProgress(progress_intensity);
        progress_screendim = 40;
        //seekbar_screendim.setProgress(progress_screendim);

        if(sharedPreferences.getBoolean("appCrash", Boolean.parseBoolean(null)) && !isMyServiceRunning(filterService.class)){
            editor.putBoolean("isOnNightmode", false);
            editor.putBoolean("isPause", false);
            editor.apply();
        }

        //filterService.color= colorMixer.colorMix(progress_clr_temp, progress_intensity);
        Log.d("check","ismnight mode"+sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null)));
        if(sharedPreferences.getBoolean("isOnNightmode", Boolean.parseBoolean(null))){
            fb_on_off.setImageResource(R.drawable.ic_pause_white_48dp);
        }

        if(sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null))){
            pause_text.setText("Cancel Pause");
            pause_text.setTextColor(Color.RED);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDefaults();
    }

    @Override
    protected void onDestroy() {
        //stopService(new Intent(this, filterService.class));
        super.onDestroy();
        finish();
        System.gc();
        Runtime.getRuntime().gc();
    }

    private void startservice(){


        // Log.d("intent","intent check");
        startService(intent);
        on_off.setTitle("ON");

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fb_on_off.setImageResource(R.drawable.ic_pause_white_48dp);

        ison_fb_on_off = true;
        System.gc();

    }

    private void stopservice(){

        if(sharedPreferences.getBoolean("isPause", Boolean.parseBoolean(null))){
            pause_text.setTextColor(Color.BLACK);
            pause_text.setText("Pause for 1 minute");
        }
        filterService.stopFilter();
        on_off.setTitle("OFF");
        //filterService.linearLayout.setBackgroundColor(Color.TRANSPARENT);
        fb_on_off.setImageResource(R.drawable.ic_play_arrow_white_48dp);


        //isOnNightMode = false;
        ison_fb_on_off = false;
        System.gc();

    }




    public void startTimer(int hours, int minutes){


        long time;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);


        time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
        Log.d("efdewf","+"+hours+"  "+minutes+" "+time);


        if(System.currentTimeMillis()>time)
        {
            //   Toast.makeText(getActivity().getApplicationContext(), "ALARM ON kar raha", Toast.LENGTH_SHORT).show();

            time = time + (1000*60*60*24);
        }

       // alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent1);


    }



    public void stopTimer(int hours, int minutes){











    }





}
