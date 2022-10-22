package com.example.travelapplication;



import android.content.Context;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireStoreUtil {

    private static final String USERS="users";
    public static final  String  TAG ="Documents";
    public static final  String USERNAME = "username";
    public static final  String USEREMAIL = "useremail";
    public static final  String USERPHONE = "userphone";
    public static final  String PHOTO_URL = "userphoto";

    private static String mUserHashDocument;

    private static FirebaseFirestore mDb =  FirebaseFirestore.getInstance();


    private FireStoreUtil(){}

    public static void addDocumentToUserCollection(String userName, String userPhone , String userID, String photoUrl, String email){
        // this function puts user data into a map and uploads it to database, no need to wait for it
        Map<String, String> userDataMap =
                createUserDocument(userName, userPhone, userID, photoUrl, email);

        mDb.collection(USERS).
        document(mUserHashDocument).
        set(userDataMap);

    }

    @NonNull
    private static Map<String, String> createUserDocument(String userName, String userPhone, String userID, String photoUrl, String email) {
        Map<String, String> userDataMap = new HashMap<>();

        userDataMap.put(USERNAME, userName);
        userDataMap.put(USEREMAIL, email);
        userDataMap.put(USERPHONE, userPhone);
        userDataMap.put(PHOTO_URL, photoUrl);

        mUserHashDocument = "User-"+ userID;

        return userDataMap;
    }

    public static void documentImplementation(String userID, Context context){
        writePrefFromDoc(context, getDocRef(userID));
    }

    private static void writePrefFromDoc(Context context, DocumentReference docRef) {
        /*
           this function waits until fetching data is complete, then writes changes to sharedPrefs
           which is in the completeListener , which then fires the sharedPref onChangeListener
        */

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                Log.d(TAG, "Cached document data: " + document.getData());

                Map<String,Object> userMap = document.getData();

                assert userMap != null;

                writeToSharedPreferences(context, userMap);
            } else {
                Log.d(TAG, "Cached get failed: ", task.getException());
            }
        });
    }

    private static void writeToSharedPreferences(Context context, Map<String, Object> map) {
        SpUtil.writeStringPref(context,SpUtil.USER_NAME, Objects.requireNonNull(map.get(USERNAME)).toString());
        SpUtil.writeStringPref(context,SpUtil.USER_PHONE, Objects.requireNonNull(map.get(USERPHONE)).toString());
        SpUtil.writeStringPref(context,SpUtil.USER_PHOTO, Objects.requireNonNull(map.get(PHOTO_URL)).toString());
        SpUtil.writeStringPref(context,SpUtil.USER_EMAIL, Objects.requireNonNull(map.get(USEREMAIL)).toString());
    }

    @NonNull
    private static DocumentReference getDocRef(String userID) {
        mUserHashDocument = "User-"+ userID;
        return mDb.collection(USERS).document(mUserHashDocument);
    }

}
