package com.example.travelapplication;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000;

    private FirebaseAuth mAuth;

    private Intent mIntent;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

        /* New Handler to start the Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser == null)
                mIntent = new Intent(SplashActivity.this, DrawerActivity.class);
            else
                mIntent = new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(mIntent);

        }, SPLASH_DISPLAY_LENGTH);
    }

}