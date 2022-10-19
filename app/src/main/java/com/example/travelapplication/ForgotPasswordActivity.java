package com.example.travelapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button mResetButton;
    EditText mEtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeComponents();
        mResetButton.setOnClickListener(v -> {
            if (mEtEmail.getText().toString().isEmpty()) {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(mEtEmail.getText().toString());
            }
        });
    }

    private void initializeComponents() {
        mResetButton = findViewById(R.id.resetButton);
        mEtEmail = findViewById(R.id.reset_email_text);
    }
}