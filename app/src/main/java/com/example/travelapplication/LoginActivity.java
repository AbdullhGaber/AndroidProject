package com.example.travelapplication;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private FirebaseAuth mAuth;

    Intent mIntent;

    EditText mEmailText;
    EditText mPasswordText;
    TextView mNoAccountText;
    
    Button mLoginBtn;
    
    private String mEmail;
    private String mPassword;
    private String mUserId;





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
        mNoAccountText.setOnClickListener( view -> {
            mIntent = new Intent(LoginActivity.this, SignUpActivity.class);
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

                        mUserId = getUid();
                        //fetch data to update sharedPrefs
                        FireStoreUtil.documentImplementation(mUserId,getApplicationContext());



                        makeToast("Authentication success.");

                        mIntent = new Intent(LoginActivity.this, DrawerActivity.class);
                        mIntent.putExtra("starterActivity","LoginActivity");
                        startActivity(mIntent);
                        finish();

                    } else {
                        // If sign in fails, display a message to the user.
                        authFailedLogMessage(task);
                        makeToast("Authentication failed.");
                    }
                });
    }

    private void authFailedLogMessage(Task<AuthResult> task) {
        Log.w(TAG, "signInWithEmail:failure", task.getException());
    }



    private void makeToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
    }



    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    private String getUid() {
        return Objects.requireNonNull(getCurrentUser()).getUid();
    }

}