package com.example.travelapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    SharedPreferences mSharedPreferences;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
   

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);


        SharedPreferences.Editor editor = getEditor();
        setSharedPreferencesValues(editor);


        /* New Handler to start the Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent ;
                if("user@example.com".equals(mSharedPreferences.getString(EMAIL, EMAIL)) &&
                   "123123".equals(mSharedPreferences.getString(PASSWORD, PASSWORD)))
                    intent = new Intent(SplashActivity.this, DrawerActivity.class);
                else
                    intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void setSharedPreferencesValues(SharedPreferences.Editor editor) {
        editor.putString(EMAIL,"user@example.com");
        editor.putString(PASSWORD, "123123");
        editor.apply();
    }

    private SharedPreferences.Editor getEditor() {
        mSharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        return editor;
    }

}