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

    public static final String TAG="Documents";
    private static final String USERS="users";
    private static String mUser;
    private static FirebaseFirestore mDb;

    private FireStoreUtil(){}

    public static void addUserCollection(String userName, String userPhone ,String userID){
        Map<String,String> tempMap = new HashMap<>();
        tempMap.put("username",userName);
        tempMap.put("userphone",userPhone);
         mDb = FirebaseFirestore.getInstance();

          mUser = "User-"+userID;
        mDb.collection(USERS).document(mUser).set(tempMap);

    }

    public static void documentImplementation(String userID, Context context){
        DocumentReference docRef = getDocRef(userID);
        writePrefFromDoc(context, docRef);
    }

    private static void writePrefFromDoc(Context context, DocumentReference docRef) {
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                Log.d(TAG, "Cached document data: " + document.getData());
                Map<String,Object> myMap = document.getData();
                assert myMap != null;
                SpUtil.writeStringPref(context,SpUtil.USER_NAME, Objects.requireNonNull(myMap.get("username")).toString());
                SpUtil.writeStringPref(context,SpUtil.USER_PHONE, Objects.requireNonNull(myMap.get("userphone")).toString());
            } else {
                Log.d(TAG, "Cached get failed: ", task.getException());
            }
        });
    }

    @NonNull
    private static DocumentReference getDocRef(String userID) {
        mUser = "User-"+ userID;
        mDb = FirebaseFirestore.getInstance();
        return mDb.collection(USERS).document(mUser);
    }


}
