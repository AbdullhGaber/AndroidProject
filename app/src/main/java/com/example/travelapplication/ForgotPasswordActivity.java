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

        onRestClick();
    }

    private void onRestClick() {
        mResetButton.setOnClickListener(v -> {
            if (!emailIsEmpty())
                FirebaseAuth.getInstance().sendPasswordResetEmail(mEtEmail.getText().toString());
            else
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean emailIsEmpty() {
        return mEtEmail.getText().toString().isEmpty();
    }

    private void initializeComponents() {
        mResetButton = findViewById(R.id.resetButton);
        mEtEmail = findViewById(R.id.reset_email_text);
    }
}