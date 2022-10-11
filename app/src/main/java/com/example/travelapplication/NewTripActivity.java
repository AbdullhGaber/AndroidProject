package com.example.travelapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class NewTripActivity extends AppCompatActivity {
    EditText mTime;
    EditText mReturnTime;
    Spinner mSpinner;
    EditText mReturnDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        initializeComponents();
        mTime.setOnClickListener(v -> showTimePicker(mTime));
        mReturnTime.setOnClickListener(v -> showTimePicker(mReturnTime));
        mSpinner.setOnClickListener(v -> {
            if(mSpinner.getSelectedItem().toString().equals("Round Trip")){
                mReturnDate.setVisibility(View.VISIBLE);
                mReturnTime.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showTimePicker(EditText editTime) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewTripActivity.this, (timePicker, selectedHour, selectedMinute) -> editTime.setText( selectedHour + ":" + selectedMinute), hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void initializeComponents() {
        mTime = findViewById(R.id.editTextTime);
        mReturnTime = findViewById(R.id.editTextReturnTime);
        mSpinner = findViewById(R.id.spinner);
        mReturnDate = findViewById(R.id.editTextReturnDate);
    }
}