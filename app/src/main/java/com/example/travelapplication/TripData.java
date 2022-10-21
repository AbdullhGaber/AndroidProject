package com.example.travelapplication;

public class TripData {

    private String tripStart;
    private String tripEnd;
    private String tripName;
    private String tripTime;
    private String tripDate;

    public TripData(String tripStart, String tripEnd, String tripName, String tripDate, String tripTime){
        this.tripStart =tripStart;
        this.tripEnd = tripEnd;
        this.tripName = tripName;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
    }

    public String getTripStart() {
        return tripStart;
    }

    public void setTripStart(String tripStart) {
        this.tripStart = tripStart;
    }

    public String getTripEnd() {
        return tripEnd;
    }

    public void setTripEnd(String tripEnd) {
        this.tripEnd = tripEnd;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripTime() {
        return tripTime;
    }

    public void setTripTime(String tripTime) {
        this.tripTime = tripTime;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
}
