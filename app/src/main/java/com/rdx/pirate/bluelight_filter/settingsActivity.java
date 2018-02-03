package com.rdx.pirate.bluelight_filter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

/**
 * Created by pirat on 1/19/2018.
 */

public class settingsActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Switch switch_filter_navbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();




        RelativeLayout filter_navbar = (RelativeLayout)findViewById(R.id.filter_navbar);
        //LinearLayout filter_navbar_lnd = (LinearLayout)findViewById(R.id.filter_navbar_landscape);

        switch_filter_navbar = (Switch)findViewById(R.id.switch_filter_navbar);
        //Switch switch_filter_navbar_lnd = (Switch)findViewById(R.id.switch_filter_navbar_landscape);

        loadCond();

        filter_navbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    switch_filter_navbar.toggle();

            }
        });

        switch_filter_navbar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("paramFull", true);
                    editor.apply();

                    stopService(navActivity.filter_intent);

                    startService(navActivity.filter_intent);


                }else{
                    editor.putBoolean("paramFull", false);
                    editor.apply();

                    stopService(navActivity.filter_intent);
                    startService(navActivity.filter_intent);



                }
            }
        });

    }

    private static void loadCond() {

        if(sharedPreferences.getBoolean("paramFull", false)){

            switch_filter_navbar.setChecked(true);
        }


    }
}
