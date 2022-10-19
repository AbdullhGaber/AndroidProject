package com.example.travelapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FireStoreUtil {

    public static final String TAG="Documents";
    private static final String USERS="users";


    private FireStoreUtil(){}

    public static void addUserCollection(String userName, String userPhone ,String userID){
        Map<String,String> tempMap = new HashMap<>();
        tempMap.put("username",userName);
        tempMap.put("userphone",userPhone);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

         String user = "User-"+userID;
        db.collection(USERS).document(user).set(tempMap);

    }


    public static void getUserDocument(String userID, Context context){
        String user = "User-"+userID;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(USERS).document(user);
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


}
