package com.example.travelapplication;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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
    public static void getUserDocument(String userID){
        String user = "User-"+userID;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(USERS).document(user);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    Map<String,Object> data = document.getData();
                } else {
                    Log.d(TAG, "No such document");
                }
            }
             else {
                Log.d(TAG, "get failed with ", task.getException());
            }

        });
    }

}
