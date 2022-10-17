package com.example.travelapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private FirebaseAuth mAuth;

    Intent mIntent;

    EditText mEmailText;
    EditText mPasswordText;
    TextView mNoAccountText;

    private String mEmail;
    private String mPassword;

    Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();

        clickListeners();

    }
    private void initializeComponents() {
        mAuth = FirebaseAuth.getInstance();
        mEmailText = findViewById(R.id.email_text);
        mPasswordText = findViewById(R.id.password_text);
        mNoAccountText = findViewById(R.id.no_account_text);
        mLoginBtn = findViewById(R.id.login_button);
    }

    private void setComponentValues() {
        mEmail = mEmailText.getText().toString();
        mPassword = mPasswordText.getText().toString();
    }

    private void clickListeners() {
        noAccountText();
        loginButton();
    }

    private void noAccountText() {
        mNoAccountText.setOnClickListener(view -> {
            mIntent = new Intent(LoginActivity.this , SignUpActivity.class);
            startActivity(mIntent);
        });
    }

    private void loginButton() {
        mLoginBtn.setOnClickListener(view -> {
            setComponentValues();
            signIn();
        });
    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        mIntent = new Intent(LoginActivity.this,DrawerActivity.class);
                        startActivity(mIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}