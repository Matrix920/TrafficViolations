package com.amw.mws.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.amw.mws.myapplication.activity.LoginActivity;
import com.amw.mws.myapplication.activity.MainActivity;
import com.amw.mws.myapplication.member.VehicleLog;

/**
 * Created by Matrix on 12/11/2018.
 */

public class LoginManager {

    private Context context;

    private SharedPreferences sharedPref;
    public static final String SHARED_PREF_NAME="e-traffic";
    private static final int PRIVATE_MODE=0;

    public static final String IS_ADMIN="is admin";

    private static final String IS_LOGIN="login";

    SharedPreferences.Editor editor;

    private static LoginManager loginManager;

    private LoginManager(Context context){
        this.context=context;
        sharedPref=context.getSharedPreferences(SHARED_PREF_NAME,PRIVATE_MODE);
        editor=sharedPref.edit();
    }

    public static LoginManager getInstance(Context context){
        if(loginManager==null){
            loginManager=new LoginManager(context);
        }

        return loginManager;
    }

    //logout
    public void clearAndLogout(){
        editor.clear();
        editor.commit();

        //go to lgoin
        startLoginActivity();
    }

    public String getPlugedNumber(){
        return sharedPref.getString(VehicleLog.PLUGED_NUMBER,"");
    }


    public void login(String plugedNumber,boolean isAdmin){
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(VehicleLog.PLUGED_NUMBER,plugedNumber);
        editor.putBoolean(IS_ADMIN,isAdmin);
        editor.commit();
        startMainActivity();
    }

    private void startLoginActivity(){
        Intent i=new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void startMainActivity(){

        Intent i=new Intent(context, MainActivity.class);
        i.putExtra(VehicleLog.PLUGED_NUMBER,getPlugedNumber());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public  boolean isAdmin() {
        boolean isAdmin = sharedPref.getBoolean(IS_ADMIN, false);
        return isAdmin;
    }


    public void ifUserLoggedIng(){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(isLogin){
            startMainActivity();
        }
    }

    public void ifUserLoggedOut(){
        boolean isLogin= sharedPref.getBoolean(IS_LOGIN,false);

        if(! isLogin){
            startLoginActivity();
        }
    }
}
