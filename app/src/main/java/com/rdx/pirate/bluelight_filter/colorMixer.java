package com.rdx.pirate.bluelight_filter;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by pirat on 1/13/2018.
 */

public class colorMixer {

    public static int colorMix(int color_value, int alpha_value){

        int red = 255 - color_value;
        int green = 2*color_value;
        int blue = 0;

        return (Color.argb(alpha_value, red, green, blue));


    }


    public static int screenDim(int color_value, int clr_tmp, int alpha_value){

        int red = (255-clr_tmp)-(2*color_value);
        int green = (2*clr_tmp)-(2*color_value);
        int blue = 0;
        //Log.d("rr,  ggg, b"," = "+red+"   "+green+"  "+blue);
        alpha_value+=(color_value);
        if(green<=0){
            green = 0;
        }
        if(red<=0){
            red=0;
        }
        return (Color.argb(alpha_value, red, green, blue));

    }


}
