package com.example.android_hw1;

import com.example.android_hw1.Model.Record;
import com.example.android_hw1.Utils.MySP;
import com.google.gson.Gson;

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

    public boolean addRecord(Record newRecord){
        if(topRecords.size() < MAX_RECORDS) {
            topRecords.add(newRecord);
            saveData();
            return true;
        }
        Record bottomRecord = topRecords.get(topRecords.size()-1);
        if (bottomRecord.getScore() < newRecord.getScore()){
            topRecords.remove(bottomRecord);
            topRecords.add(newRecord);
            saveData();
            return true;
        }
        return false;
    }

    public ArrayList<Record> loadData() {
        String recordsJson = MySP.getInstance().getString(SP_RECORDS, "");
        ArrayList<Record> topRecords = new Gson().fromJson(recordsJson, ArrayList.class);
        if(topRecords != null)
            topRecords.sort((r1, r2) -> r2.getScore() - r1.getScore());
        return topRecords;
    }

    private void saveData() {
        String recordsJson = new Gson().toJson(topRecords);
        MySP.getInstance().putString(SP_RECORDS, recordsJson);
    }
}
