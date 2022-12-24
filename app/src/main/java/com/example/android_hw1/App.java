package com.example.android_hw1;

import android.app.Application;

import com.example.android_hw1.Utils.MySP;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.init(this);
        DataManager.init();
    }
}
