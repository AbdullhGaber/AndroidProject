package com.example.travelapplication;

import android.content.Intent;
import android.net.Uri;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity {
    CircleImageView drawerImageView;
    TextView userNameView;
    TextView userEmailView;
    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

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
    }

    private void initializeComponents() {
        View header = navigationView.getHeaderView(0);
        drawerImageView = header.findViewById(R.id.nav_header_profile);
        userNameView = header.findViewById(R.id.tvUserName);
        userEmailView = header.findViewById(R.id.tvUserEmail);
    }

    private void setComponentsValues() {
        drawerImageView.setImageURI(Uri.parse(SpUtil.getPreferenceString(this, SpUtil.USER_PHOTO)));
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

}