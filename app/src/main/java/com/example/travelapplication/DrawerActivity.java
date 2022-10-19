package com.example.travelapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity {
    CircleImageView drawerImageView;
    TextView userNameView;
    TextView userEmailView;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    SharedPreferences prefs;
    Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.travelapplication.databinding.ActivityDrawerBinding binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        binding.appBarDrawer.fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = binding.drawerLayout;
        navigationView = binding.navView;
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
        NavigationUI.setupWithNavController(navigationView, navController);
        initializeComponents();
        setComponentsValues();
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> setComponentsValues());
    }



    private void initializeComponents() {
        View header = navigationView.getHeaderView(0);
        drawerImageView = header.findViewById(R.id.nav_header_profile);
        userNameView = header.findViewById(R.id.tvUserName);
        userEmailView = header.findViewById(R.id.tvUserEmail);
        prefs = SpUtil.getPref(this);
    }

    private void setComponentsValues() {
        new FetchImage(SpUtil.getPreferenceString(this,SpUtil.USER_PHOTO)).start();
        userNameView.setText(SpUtil.getPreferenceString(this, SpUtil.USER_NAME));
        userEmailView.setText(SpUtil.getPreferenceString(this, SpUtil.USER_EMAIL));
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

    public void signOut(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        Intent mIntent = new Intent(DrawerActivity.this, LoginActivity.class);
        startActivity(mIntent);
        finish();
    }


    class FetchImage extends Thread{
        String URL;
        Bitmap bitmap;
        FetchImage(String URL){
            this.URL=URL;
        }

        @Override
        public void run() {
            InputStream inputStream = null;
            try {
                inputStream = new URL(URL).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            myHandler.post(() -> {
                if(bitmap!=null) {
                    drawerImageView.setImageBitmap(bitmap);
                    SpUtil.writeStringPref(getApplicationContext(), SpUtil.USER_PHOTO, URL);
                }
            });
        }
    }
}