package com.example.travelapplication;


import static com.example.travelapplication.SignUpActivity.PHOTO_URL;
import static com.example.travelapplication.SignUpActivity.USEREMAIL;
import static com.example.travelapplication.SignUpActivity.USERNAME;
import static com.example.travelapplication.SignUpActivity.USERPHONE;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    public static final String STARTER_ACTIVITY = "starterActivity";

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
                        mUserId = getUid();

                        // Sign in success, update UI with the signed-in user's information
                        authSuccessLogMessage(task);

                        //fetch data to update sharedPrefs
                        FireStoreUtil.documentImplementation(mUserId);

                        makeToast("Authentication success.");

                        goToDrawerActivity();

                    } else {
                        // If sign in fails, display a message to the user.
                        authFailedLogMessage(task);
                        makeToast("Authentication failed.");
                    }
                });
    }

    private void goToDrawerActivity() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Logging in...");
        pd.show();
        FireStoreUtil.documentImplementation(mUserId).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    mIntent = new Intent(LoginActivity.this, DrawerActivity.class);
                    mIntent.putExtra(SignUpActivity.USERNAME, Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("username")).toString());
                    mIntent.putExtra(SignUpActivity.USEREMAIL, Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("useremail")).toString());
                    mIntent.putExtra(SignUpActivity.PHOTO_URL, Objects.requireNonNull(Objects.requireNonNull(doc.getData()).get("userphoto")).toString());
                    pd.dismiss();
                    startActivity(mIntent);
                    finish();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }


        });

    }

    private void authFailedLogMessage(Task<AuthResult> task) {
        Log.w(TAG, "signInWithEmail:failure", task.getException());
    }

    private void authSuccessLogMessage(Task<AuthResult> task) {
        Log.d(TAG, "signInWithEmail:success", task.getException());
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