package com.example.travelapplication;

import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Trip implements Parcelable {
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private String id;
    private String name;
    private String date;
    private String time;
    private String tripStatus;
    private String returnDate;
    private String returnTime;
    private List<Note> notes;

    public Trip(GeoPoint startPoint, GeoPoint endPoint, String name, String date, String time, String tripStatus, String returnDate, String returnTime , List<Note> notes)
    {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripStatus = tripStatus;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
        this.notes = notes;
    }

    protected Trip(Parcel in) {
        id = in.readString();
        name = in.readString();
        date = in.readString();
        time = in.readString();
        tripStatus = in.readString();
        returnDate = in.readString();
        returnTime = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public GeoPoint getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(GeoPoint startPoint) {
        this.startPoint = startPoint;
    }

    public GeoPoint getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(GeoPoint endPoint) {
        this.endPoint = endPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
      return this.notes ;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(date);
        parcel.writeString(time);
        parcel.writeString(tripStatus);
        parcel.writeString(returnDate);
        parcel.writeString(returnTime);
    }
}
