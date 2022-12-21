package com.example.android_hw1.Model;

public class Record {

    private int distance;
    private double latitude;
    private double longitude;
    private String name;


    public Record() {
    }

    public int getDistance() {
        return distance;
    }

    public Record setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Record setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Record setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }
}
