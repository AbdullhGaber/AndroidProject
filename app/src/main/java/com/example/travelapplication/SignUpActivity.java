package com.example.travelapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    private FirebaseAuth mAuth;

    Intent mIntent;

    EditText mEmailText;
    EditText mPasswordText;
    EditText mUserNameText;
    EditText mPasswordConfirmationText;
    EditText mPhoneText;

    TextView mAlreadyHaveAccountText;

    private String mEmail;
    private String mPassword;
    private String mUser;
    private String mPasswordConfirm;
    private String mPhone;

    Button mSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeComponents();

        clickListeners();

    }


    private void initializeComponents() {
        mAuth = FirebaseAuth.getInstance();
        mEmailText = findViewById(R.id.email_edit);
        mPasswordText = findViewById(R.id.password_edit);
        mAlreadyHaveAccountText = findViewById(R.id.already_have_account);
        mUserNameText = findViewById(R.id.user_name_edit);
        mPasswordConfirmationText = findViewById(R.id.password_confirm_edit);
        mPhoneText = findViewById(R.id.phone_number_edit);
        mSignUpBtn = findViewById(R.id.signup_button);
    }

    private void setComponentValues() {
        mEmail = mEmailText.getText().toString();
        mPassword = mPasswordText.getText().toString();
        mPhone = mPhoneText.getText().toString();
        mUser = mUserNameText.getText().toString();
        mPasswordConfirm = mPasswordConfirmationText.getText().toString();
    }

    private void alreadyHaveAccountText() {
        mAlreadyHaveAccountText.setOnClickListener(view -> {
            mIntent = new Intent(SignUpActivity.this , LoginActivity.class);
            startActivity(mIntent);
        });
    }

    private boolean passwordMatch() {
        return mPassword.equals(mPasswordConfirm);
    }

    private boolean fieldIsEmpty() {
        return mEmail.equals("") || mPassword.equals("") || mUser.equals("") || mPhone.equals("");
    }

    private void dataValidation() {
        if(fieldIsEmpty())

            Toast.makeText(SignUpActivity.this, "empty fields not allowed",
                    Toast.LENGTH_SHORT).show();

        else if (!passwordMatch())

            Toast.makeText(SignUpActivity.this, "passwords don't match",
                    Toast.LENGTH_SHORT).show();

        else if (mPassword.length() < 8)

            Toast.makeText(SignUpActivity.this, "password must have 8 characters",
                    Toast.LENGTH_SHORT).show();

        else
            createAccount();
    }

    private void signUpButton() {
        mSignUpBtn.setOnClickListener(view -> {
            setComponentValues();
            dataValidation();
        });
    }

    private void clickListeners() {
        alreadyHaveAccountText();
        signUpButton();
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signUpWithEmail:success");
                        Toast.makeText(SignUpActivity.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();
                        mIntent = new Intent(SignUpActivity.this,DrawerActivity.class);
                        startActivity(mIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signUpWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}