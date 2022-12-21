package com.example.android_hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.android_hw1.Fragments.ListFragment;
import com.example.android_hw1.Fragments.MapFragment;
import com.example.android_hw1.R;

public class ScoreActivity extends AppCompatActivity {

    private ListFragment listFragment;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        listFragment = new ListFragment();
        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.score_FRAME_map,mapFragment).commit();

    }
}