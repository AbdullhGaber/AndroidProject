package com.example.travelapplication;


import android.content.Context;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireStoreUtil {

    private static final String USERS = "users";
    public static final String TAG = "Documents";
    public static final String USERNAME = "username";
    public static final String USEREMAIL = "useremail";
    public static final String USERPHONE = "userphone";
    public static final String PHOTO_URL = "userphoto";
    public static final String TRIP = "trip";
    private static String mUserHashDocument;
    private static String mTripHashDocument;

    private static FirebaseFirestore mDb = FirebaseFirestore.getInstance();


    private FireStoreUtil() {
    }


    public static class TripFireStore {

        public static final String ID = "id";
        public final static String START_POINT = "startpoint";
        private final static String END_POINT = "endpoint";
        private final static String NAME = "name";
        private final static String DATE = "date";
        private final static String TIME = "time";
        private final static String TRIP_STATUS = "tripstatus";
        private final static String RETURN_DATE = "returndate";
        private final static String RETURN_TIME = "returntime";
        private final static String NOTES = "notes";


        public static void addDocumentToTripCollection(Trip trip, String userID) {
            Map<String, Object> tripDataMap =
                    FireStoreUtil.TripFireStore.createTripDocumentData(trip, userID);

            getTripDocument().set(tripDataMap);

            Map<String, Object> tripIdMap = getIdMap();
            getTripDocument().update(tripIdMap);

            addNotesToDocument(trip);

        }

        private static void addNotesToDocument(Trip trip) {
            mDb.collection(USERS).
            document(mUserHashDocument).collection(mTripHashDocument).
            document().collection(NOTES).document().set(trip.getNotes());
        }

        @NonNull
        private static Map<String, Object> getIdMap() {
            Map<String, Object> tripIdMap = new HashMap<>();
            tripIdMap.put(ID, getTripDocument().getId());
            return tripIdMap;
        }


        private static DocumentReference getTripDocument() {
            return mDb.collection(USERS).
                    document(mUserHashDocument).collection(mTripHashDocument).
                    document();
        }

        private static Map<String, Object> createTripDocumentData(Trip trip, String userID) {
            mTripHashDocument = "Trips-" + userID;
            mUserHashDocument = "User-" + userID;

            Map<String, Object> tripDataMap = new HashMap<>();

            tripDataMap.put(NAME, trip.getName());
            tripDataMap.put(START_POINT, trip.getStartPoint());
            tripDataMap.put(END_POINT, trip.getStartPoint());
            tripDataMap.put(TIME, trip.getTime());
            tripDataMap.put(DATE, trip.getDate());
            tripDataMap.put(TRIP_STATUS, trip.getTripStatus());
            tripDataMap.put(RETURN_DATE, trip.getReturnDate());
            tripDataMap.put(RETURN_TIME, trip.getReturnTime());

            return tripDataMap;
        }

    }

    public static void addDocumentToUserCollection(String userName, String userPhone, String userID, String photoUrl, String email, Trip trip) {
        // this function puts user data into a map and uploads it to database, no need to wait for it
        Map<String, String> userDataMap =
                createTripDocument(userName, userPhone, userID, photoUrl, email, trip);

                mDb.collection(USERS).
                document(mUserHashDocument).
                set(userDataMap);

    }

    @NonNull
    private static Map<String, String> createTripDocument(String userName, String userPhone, String userID, String photoUrl, String email, Trip trip) {
        Map<String, String> userDataMap = new HashMap<>();

        userDataMap.put(USERNAME, userName);
        userDataMap.put(USEREMAIL, email);
        userDataMap.put(USERPHONE, userPhone);
        userDataMap.put(PHOTO_URL, photoUrl);


        mUserHashDocument = "User-" + userID;

        return userDataMap;
    }

    public static void documentImplementation(String userID, Context context) {
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

                Map<String, Object> userMap = document.getData();

                assert userMap != null;

                writeToSharedPreferences(context, userMap);
            } else {
                Log.d(TAG, "Cached get failed: ", task.getException());
            }
        });
    }

    private static void writeToSharedPreferences(Context context, Map<String, Object> map) {
        SpUtil.writeStringPref(context, SpUtil.USER_NAME, Objects.requireNonNull(map.get(USERNAME)).toString());
        SpUtil.writeStringPref(context, SpUtil.USER_PHONE, Objects.requireNonNull(map.get(USERPHONE)).toString());
        SpUtil.writeStringPref(context, SpUtil.USER_PHOTO, Objects.requireNonNull(map.get(PHOTO_URL)).toString());
        SpUtil.writeStringPref(context, SpUtil.USER_EMAIL, Objects.requireNonNull(map.get(USEREMAIL)).toString());
    }

    @NonNull
    private static DocumentReference getDocRef(String userID) {
        mUserHashDocument = "User-" + userID;
        return mDb.collection(USERS).document(mUserHashDocument);
    }

}
