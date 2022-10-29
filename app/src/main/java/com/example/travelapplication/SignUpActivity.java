package com.example.travelapplication;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int PICK_IMAGE = 2 ;
    public static final String PHOTO_URL = "photoUrl";
    public static final String USERNAME = "username";
    public static final String USEREMAIL = "useremail";
    public static final String USERPHONE = "userphone";
    public static final String STARTER_ACTIVITY = "starterActivity";

    private FirebaseAuth mAuth;

    Intent mIntent;

    EditText mEmailText;
    EditText mPasswordText;
    EditText mUserNameText;
    EditText mPasswordConfirmationText;
    EditText mPhoneText;

    TextView mAlreadyHaveAccountText;
    TextView mUploadPicText;

    Button mSignUpBtn;

    CircleImageView mCircleImageView;

    private String mEmail;
    private String mPassword;
    private String mUser;
    private String mPasswordConfirm;
    private String mPhone;
    private Uri mImageUri;

    private String mUserId;

    private boolean mImgIsSet = false;
    private String mRefernceUrl;

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
        mUploadPicText = findViewById(R.id.tvUploadPic);
        mUserNameText = findViewById(R.id.user_name_edit);
        mPasswordConfirmationText = findViewById(R.id.password_confirm_edit);
        mPhoneText = findViewById(R.id.phone_number_edit);
        mSignUpBtn = findViewById(R.id.signup_button);
        mCircleImageView = findViewById(R.id.nav_header_profile);
    }

    private void setComponentValues() {
        mEmail = mEmailText.getText().toString();
        mPassword = mPasswordText.getText().toString();
        mPhone = mPhoneText.getText().toString();
        mUser = mUserNameText.getText().toString();
        mPasswordConfirm = mPasswordConfirmationText.getText().toString();
    }

    private void clickListeners() {
        alreadyHaveAccountText();
        signUpButton();
        uploadPicText();
    }

    private void alreadyHaveAccountText() {
        mAlreadyHaveAccountText.setOnClickListener(view -> {
            mIntent = new Intent(SignUpActivity.this , LoginActivity.class);
            startActivity(mIntent);
        });
    }

    private void signUpButton() {
        mSignUpBtn.setOnClickListener(view -> {
            setComponentValues();
            dataValidation();
        });
    }

    private void uploadPicText() {
        mUploadPicText.setOnClickListener(v -> pickImage());
    }

    private void dataValidation() {
        if(fieldIsEmpty())
           makeToast("empty fields not allowed");

        else if (!passwordMatch())
            makeToast("passwords don't match");

        else if (passwordLessThan())
           makeToast("password must have 8 characters");

        else
            createAccount();
    }

    private boolean fieldIsEmpty() {
        return mEmail.equals("") || mPassword.equals("") || mUser.equals("") || mPhone.equals("") || !mImgIsSet;
    }

    private boolean passwordMatch() {
        return mPassword.equals(mPasswordConfirm);
    }

    private boolean passwordLessThan() {
        return mPassword.length() < 8;
    }

    private void makeToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        mUserId = getUid();

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signUpWithEmail:success");

                        //Start uploading the image while naming it with the user ID
                        uploadImage();

                        writeDataToSharedPref();

                        goToDrawerActivity();

                        makeToast("Authentication success.");
                    } else {
                        // If sign in fails, display a message to the user.
                        authFailedLogMessage(task);
                        makeToast("Authentication failed.");
                    }
                });
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    private String getUid() {
        return Objects.requireNonNull(getCurrentUser()).getUid();
    }

    private void authFailedLogMessage(Task<AuthResult> task) {
        Log.w(TAG, "signInWithEmail:failure", task.getException());
    }

    private void writeDataToSharedPref() {
        SpUtil.writeStringPref(this,SpUtil.USER_EMAIL,mEmail);
        SpUtil.writeStringPref(this,SpUtil.USER_NAME,mUser);
        SpUtil.writeStringPref(this,SpUtil.USER_PHONE,mPhone);
        SpUtil.writeStringPref(this,SpUtil.USER_PHOTO,mRefernceUrl);
    }

    private void pickImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            assert data != null;
            mImageUri = data.getData();
            setImageViewPhoto(mImageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        return  MimeTypeMap.
                getSingleton().
                getExtensionFromMimeType
                (
                        getContentResolver().
                        getType(uri)
                );
    }

    private void uploadImage() {
        //progress dialog to wait for uploading
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading image");
        pd.show();

        if(mImageUri !=null){
            //uploading image to DB
            StorageReference fileReference = createFileReference(mUserId);

            fileReference.putFile(mImageUri).addOnCompleteListener
                    (
                     /*
                     Waiting for the upload to finish so we can fire the intent to start next activity,
                     while putting the name of the firing activity
                     along with the URL of the photo so we can use it later,
                     we can't do that unless the task is finished, so e cannot get outside the complete listener
                     */

                       task -> fileReference.
                       getDownloadUrl().
                       addOnSuccessListener(uri -> {
                       mRefernceUrl = uri.toString();

                       //dismissing the progress dialog with try to avoid exceptions
                       dismissWithTry(pd);

                       makeToast("Image upload successful");
                       })
                    );
        }

    }

    private void goToDrawerActivity() {
        mIntent = new Intent(SignUpActivity.this, DrawerActivity.class);
        mIntent.putExtra(PHOTO_URL, mRefernceUrl);
        mIntent.putExtra(USERNAME, mUser);
        mIntent.putExtra(USEREMAIL, mEmail);
        mIntent.putExtra(USERPHONE, mPhone);
        startActivity(mIntent);
        finish();
    }

    @NonNull
    private StorageReference createFileReference(String userID) {
        return FirebaseStorage.
                getInstance().
                getReference().
                child("uploads").
                child(userID + "." + getFileExtension(mImageUri));
    }

    public static void dismissWithTry(ProgressDialog pd) {
        try {
            pd.dismiss();
        } catch (final Exception e) {
            // Do nothing.
        }
    }

    private void setImageViewPhoto(Uri uri) {
        mCircleImageView.setImageURI(uri);
        mImgIsSet = true;
    }
}