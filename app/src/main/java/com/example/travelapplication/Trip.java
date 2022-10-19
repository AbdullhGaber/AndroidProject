package com.example.travelapplication;

import android.location.Geocoder;

public class Trip {
    private Geocoder startPoint;
    private Geocoder endPoint;
    private String name;
    private String date;
    private String time;
    private String tripStatus;
    private String returnDate;
    private String returnTime;

    public Trip(Geocoder startPoint, Geocoder endPoint, String name, String date, String time, String tripStatus, String returnDate, String returnTime) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripStatus = tripStatus;
        this.returnDate = returnDate;
        this.returnTime = returnTime;
    }

    public Geocoder getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Geocoder startPoint) {
        this.startPoint = startPoint;
    }

    public Geocoder getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Geocoder endPoint) {
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

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }
}
