package com.example.travelapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.Objects;

public class NewTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText mTime;
    EditText mReturnTime;
    Spinner mSpinner;
    EditText mDate;
    EditText mReturnDate;
    Button addButton;
    EditText etStartLoc;
    EditText etEndLoc;
    EditText mName;
    private EditText chosenEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        initializeComponents();

        mTime.setOnClickListener(v -> showTimePicker(mTime));
        mReturnTime.setOnClickListener(v -> showTimePicker(mReturnTime));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mSpinner.getSelectedItem().toString().equals("Round Trip")){
                    mReturnDate.setVisibility(View.VISIBLE);
                    mReturnTime.setVisibility(View.VISIBLE);
                }
                else{
                    mReturnDate.setVisibility(View.INVISIBLE);
                    mReturnTime.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSpinner.setSelection(0);
            }
        });
        mDate.setOnClickListener(v -> showDatePicker(mDate));
        mReturnDate.setOnClickListener(v -> showDatePicker(mReturnDate));
        addButton.setOnClickListener(v -> {
            Trip trip = new Trip(GeoPoint.class.cast(etEndLoc.getText().toString()) ,
                    GeoPoint.class.cast(etStartLoc.getText().toString()),
                    mName.getText().toString(),
                    mDate.getText().toString(),
                    mTime.getText().toString(),
                    chosenEt.getText().toString(),
                    chosenEt.getText().toString().equals("One Direction")? null:mReturnDate.getText().toString(),
                    chosenEt.getText().toString().equals("One Direction")? null:mReturnTime.getText().toString(),
                    null
                    );
            FireStoreUtil.TripFireStore.addDocumentToTripCollection(trip, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        });
    }

    private void showDatePicker(EditText editText) {
        chosenEt = editText;
        DatePickerDialog datePickerDialog = new DatePickerDialog(this
                ,this
                ,Calendar.getInstance().get(Calendar.YEAR)-1
                ,Calendar.getInstance().get(Calendar.MONTH)
                ,Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(EditText editTime) {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        String amPm ;
        if(hour < 12) {
            amPm = "AM";
        } else {
            amPm = "PM";
        }
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(NewTripActivity.this, (timePicker, selectedHour, selectedMinute) -> editTime.setText( selectedHour + ":" + selectedMinute+ " " + amPm), hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void initializeComponents() {
        mTime = findViewById(R.id.editTextTime);
        mReturnTime = findViewById(R.id.editTextReturnTime);
        mSpinner = findViewById(R.id.spinner);
        mReturnDate = findViewById(R.id.editTextReturnDate);
        mDate = findViewById(R.id.editTextDate);
        addButton = findViewById(R.id.addButton);
        etEndLoc = findViewById(R.id.edtEndLoc);
        etStartLoc = findViewById(R.id.edtStartLoc);
        mName = findViewById(R.id.edtName);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + (year);
        chosenEt.setText(date);
    }
}