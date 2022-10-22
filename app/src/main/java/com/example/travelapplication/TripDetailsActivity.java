package com.example.travelapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import com.example.travelapplication.databinding.ActivityTripDetailsBinding;

public class TripDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);


        Trip trip = getIntent().getParcelableExtra(TravelDataAdapter.ViewHolder.TRIP);

        ActivityTripDetailsBinding binding = DataBindingUtil.
                setContentView(this,R.layout.activity_trip_details);

        binding.setTrip(trip);
    }
}