package com.example.android_hw1;

import android.app.Application;

import com.example.android_hw1.Utils.MySP;
import com.example.android_hw1.Utils.SignalGenerator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySP.init(this);
        DataManager.init();
        SignalGenerator.init(this);
    }
}
