package com.amw.mws.myapplication.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Matrix on 12/11/2018.
 */

public class Warnings {

    public static void show(Context context , String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
