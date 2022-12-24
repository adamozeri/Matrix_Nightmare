package com.example.android_hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.example.android_hw1.BackgroundSound;
import com.example.android_hw1.DataManager;
import com.example.android_hw1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import im.delight.android.location.SimpleLocation;

public class MenuActivity extends AppCompatActivity {

    private SimpleLocation simpleLocation;

    // view
    private MaterialButton menu_BTN_play;
    private MaterialButton menu_BTN_topGrades;
    private SwitchMaterial  menu_SW_sensor;
    private SwitchMaterial  menu_SW_fast;


    // mode
    private boolean isFast;
    private boolean isSensorMode;

    //sound
    private BackgroundSound scoreSound;
    private BackgroundSound gameStartSound;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.simpleLocation = new SimpleLocation(this);
        locationPermission(simpleLocation);
        findViews();
        switchListener();
        buttonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void buttonListener(){
        menu_BTN_play.setOnClickListener(view -> {
            openGameScreen();
        });

        menu_BTN_topGrades.setOnClickListener(view -> {
            openGradesScreen();
        });
    }

    private void switchListener(){
        menu_SW_fast.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean fast) {
                isFast = fast;
            }
        });
        menu_SW_sensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean sensor) {
                isSensorMode = sensor;
            }
        });
    }

    private void findViews(){
        menu_BTN_play = findViewById(R.id.menu_BTN_play);
        menu_BTN_topGrades = findViewById(R.id.menu_BTN_topGrades);
        menu_SW_sensor = findViewById(R.id.menu_SW_sensor);
        menu_SW_fast = findViewById(R.id.menu_SW_fast);
    }

    private void openGradesScreen() {
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        this.scoreSound = new BackgroundSound(this,R.raw.paper_rip_new);
        scoreSound.execute();
        finish();
    }

    private void openGameScreen() {
        Intent gameIntent = new Intent(this, GameActivity.class);
        gameIntent.putExtra(GameActivity.KEY_SENSOR, isSensorMode);
        gameIntent.putExtra(GameActivity.KEY_SPEED, isFast);
        gameIntent.putExtra(GameActivity.KEY_LATITUDE,latitude);
        gameIntent.putExtra(GameActivity.KEY_LONGITUDE,longitude);
        startActivity(gameIntent);
        this.gameStartSound = new BackgroundSound(this,R.raw.school_bell);
        this.gameStartSound.execute();
        finish();
    }

    private void locationPermission(SimpleLocation location) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        putLatLon(location);
    }

    private void putLatLon(SimpleLocation location){
        location.beginUpdates();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

}