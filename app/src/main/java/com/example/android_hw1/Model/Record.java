package com.example.android_hw1.Model;

public class Record {

    private int score;
    private double latitude;
    private double longitude;
    private String name;


    public Record() {
    }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
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

    @Override
    public String toString() {
        return "score=" + score + ", name='" + name;
    }
}

