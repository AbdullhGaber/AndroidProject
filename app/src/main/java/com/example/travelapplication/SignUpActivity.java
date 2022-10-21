package com.example.travelapplication;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private static final int PICK_IMAGE = 2 ;

    private FirebaseAuth mAuth;

    Intent mIntent;
    EditText mEmailText;
    EditText mPasswordText;
    EditText mUserNameText;
    EditText mPasswordConfirmationText;
    EditText mPhoneText;
    CircleImageView mCircleImageView;
    TextView mAlreadyHaveAccountText;
    TextView mUploadPicText;

    private String mEmail;
    private String mPassword;
    private String mUser;
    private String mPasswordConfirm;
    private String mPhone;
    private Uri imageUri;

    Button mSignUpBtn;
    private boolean imgIsSet = false;

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
        return mEmail.equals("") || mPassword.equals("") || mUser.equals("") || mPhone.equals("") || !imgIsSet;
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
        uploadPicText();
    }

    private void uploadPicText() {
        mUploadPicText.setOnClickListener(v -> pickImage());
    }

    private void createAccount() {
        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signUpWithEmail:success");
                        //Start uploading the image while naming it with the user ID
                        uploadImage
                                (
                                  Objects.requireNonNull
                                  (
                                  FirebaseAuth.
                                  getInstance().
                                  getCurrentUser()
                                  ).
                                  getUid()
                                );
                        writeDataToSharedPref();

                        Toast.makeText(SignUpActivity.this, "Authentication success.",
                        Toast.LENGTH_SHORT).show();


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signUpWithEmail:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void writeDataToSharedPref() {
        SpUtil.writeStringPref(this,SpUtil.USER_EMAIL,mEmail);
        SpUtil.writeStringPref(this,SpUtil.USER_NAME,mUser);
        SpUtil.writeStringPref(this,SpUtil.USER_PHONE,mPhone);
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
            imageUri = data.getData();
            setImageViewPhoto(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(String userID) {
        //progress dialog to wait for uploading
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading image");
        pd.show();
        if(imageUri!=null){
            //uploading image to DB
            StorageReference fileReference = FirebaseStorage.getInstance().getReference().child("uploads").child(userID+"."+getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnCompleteListener(task -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String url = uri.toString();
                //Waiting for the upload to finish so we can fire the intent to start next activity, while putting the name of the firing activity
                // along with the URL of the photo so we can use it later, we can't do that unless the task is finished, so e cannot get outside the complete listener
                mIntent = new Intent(SignUpActivity.this,DrawerActivity.class);
                mIntent.putExtra("photoUrl",url);
                mIntent.putExtra("username",mUser);
                mIntent.putExtra("useremail",mEmail);
                mIntent.putExtra("userphone",mPhone);
                mIntent.putExtra("starterActivity","SignUpActivity");
                startActivity(mIntent);
                finish();
                //dismissing the progress dialog with try to avoid exceptions
                dismissWithTry(pd);
                Toast.makeText(SignUpActivity.this, "Image upload successful", Toast.LENGTH_SHORT).show();
            }));
        }
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
        imgIsSet = true;
    }
}