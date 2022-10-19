package com.example.travelapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
    private SpUtil(){}
    public static final String PREF_NAME = "UserPreferences";
    public static final String USER_EMAIL = "email";
    public static final String USER_NAME = "username";
    public static final String USER_PHOTO = "userprofile";
    public static final String USER_PHONE = "phone";



    public static SharedPreferences getPref(Context context){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }

    public static String getPreferenceString(Context context, String key){
        return getPref(context).getString(key,"");
    }



    public static void writeStringPref(Context context, String key, String value){
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(key,value);
        editor.apply();
    }

}
