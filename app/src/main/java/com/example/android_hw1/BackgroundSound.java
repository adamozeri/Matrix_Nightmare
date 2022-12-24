package com.example.android_hw1;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class BackgroundSound extends AsyncTask<Void, Void, Void> {

    private Context context;
    private int sound;

    public BackgroundSound(Context context, int sound) {
        this.context = context;
        this.sound = sound;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MediaPlayer player = MediaPlayer.create(this.context, sound);
        player.setVolume(0.2f, 0.2f);
        player.start();
        return null;
    }

}
