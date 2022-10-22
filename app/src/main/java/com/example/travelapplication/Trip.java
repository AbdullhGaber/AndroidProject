package com.example.travelapplication;

import android.location.Geocoder;

import com.google.firebase.firestore.GeoPoint;

public class Trip {
    private GeoPoint startPoint;
    private GeoPoint endPoint;
    private String id;
    private String name;
    private String date;
    private String time;
    private String tripStatus;
    private String returnDate;
    private String returnTime;
    private String[] notes;

    public Trip(GeoPoint startPoint, GeoPoint endPoint, String name, String date, String time, String tripStatus, String returnDate, String returnTime , String[] notes) {
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

    public void setNotes(String[] notes) {
        this.notes = notes;
    }
    public String[] getNotes() {
      return this.notes ;
    }

}
