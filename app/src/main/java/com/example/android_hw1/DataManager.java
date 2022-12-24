package com.example.android_hw1;

import android.util.Log;

import com.example.android_hw1.Model.Record;
import com.example.android_hw1.Utils.MySP;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataManager {
    private final String SP_RECORDS = "SP_RECORDS";
    private static DataManager instance = null;
    private final int MAX_RECORDS = 10;
    private ArrayList<Record> topRecords;

    public DataManager() {
        this.topRecords = loadData();
        if (topRecords == null)
            this.topRecords = new ArrayList<>();
    }

    public static void init() {
        if (instance == null)
            instance = new DataManager();
    }

    public static DataManager getInstance() {
        return instance;
    }

    public ArrayList<Record> getTopRecords() {
        return topRecords;
    }

    public void addRecord(Record newRecord){
        if(topRecords.size() < MAX_RECORDS) {
            topRecords.add(newRecord);
            saveData();
        }
        Record bottomRecord = topRecords.get(topRecords.size()-1);
        if (bottomRecord.getScore() < newRecord.getScore()){
            topRecords.remove(bottomRecord);
            topRecords.add(newRecord);
            saveData();
        }
        topRecords.sort((r1, r2) -> r2.getScore() - r1.getScore());
    }

    public ArrayList<Record> loadData() {
        Type typeMyType = new TypeToken<ArrayList<Record>>(){}.getType();
        String recordsJson = MySP.getInstance().getString(SP_RECORDS, "");
        ArrayList<Record> topRecords = new Gson().fromJson(recordsJson, typeMyType);
        return topRecords;
    }

    private void saveData() {
        String recordsJson = new Gson().toJson(topRecords);
        MySP.getInstance().putString(SP_RECORDS, recordsJson);
    }

}
