package com.example.travelapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.travelapplication.databinding.ActivityDrawerBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    CircleImageView mDrawerImageView;
    Intent mIntent;
    TextView mUserNameText;
    TextView mUserEmailText;

    private NavigationView mNavigationView;

    SharedPreferences mPrefs;

    private String mSharedPhotoString;
    private String mSharedUserNameString;
    private String mSharedEmailString;
    private String mSharedUserPhoneString;
    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //***********************************************************************************************************
        super.onCreate(savedInstanceState);
        com.example.travelapplication.databinding.ActivityDrawerBinding binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarDrawer.toolbar);
        binding.appBarDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        mNavigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_upcoming, R.id.nav_history, R.id.nav_history_map)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
        //**************************************************************************************************************


        initializeComponents();
        setComponentsValues();
        mPrefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> setComponentsValues());
    }

    private void initializeComponents() {
        View header = mNavigationView.getHeaderView(0);
        mDrawerImageView = header.findViewById(R.id.nav_header_profile);
        mUserNameText = header.findViewById(R.id.tvUserName);
        mUserEmailText = header.findViewById(R.id.tvUserEmail);
        mPrefs = SpUtil.getPref(this);
        mIntent = getIntent();
        mUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    private void setComponentsValues() {
        // Checking the firing activity of the intent to do actions based on it
        if(mIntent.getStringExtra("starterActivity").equals("SignUpActivity")){
            // if we sign up user for first time we will add data to DB and we get them  from the intent as we put it there
            mSharedPhotoString = mIntent.getStringExtra("photoUrl");
            mSharedUserPhoneString = mIntent.getStringExtra("userphone");
            mSharedUserNameString = mIntent.getStringExtra("username");
            mSharedEmailString = mIntent.getStringExtra("useremail");
            FireStoreUtil.addUserCollection(mSharedUserNameString,mSharedUserPhoneString,mUserID,mSharedPhotoString,mSharedEmailString);
        }
        else{
            // if any activity fires the intent like login or splash we will get the already registered shared prefs
            getPreferenceString();
        }
        //this will happen in any case

        new FetchImage(mSharedPhotoString, mDrawerImageView , getApplicationContext()).start();
        mUserNameText.setText(mSharedUserNameString);
        mUserEmailText.setText(mSharedEmailString);
    }

    private void getPreferenceString() {
        mSharedPhotoString = SpUtil.getPreferenceString(this,SpUtil.USER_PHOTO);
        mSharedUserNameString = SpUtil.getPreferenceString(this,SpUtil.USER_NAME);
        mSharedEmailString = SpUtil.getPreferenceString(this,SpUtil.USER_EMAIL);
        mSharedUserPhoneString = SpUtil.getPreferenceString(this,SpUtil.USER_PHONE);
    }

    public void signOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        mIntent = new Intent(DrawerActivity.this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}