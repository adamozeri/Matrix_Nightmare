package com.example.android_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class GameActivity extends AppCompatActivity {

    private FloatingActionButton game_FAB_leftArrow;
    private FloatingActionButton game_FAB_rightArrow;
    private LinearLayout[] game_LL_obstacleCol;
    private LinearLayout game_LL_player;
    private ShapeableImageView[] game_IMG_hearts;

    private GameManger gameManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        this.gameManger = new GameManger(game_LL_obstacleCol, new Player()
                .setIndex(game_LL_player.getChildCount()/2+1)
                .setLife(game_IMG_hearts.length)
                .setMaxIndex(game_LL_player.getChildCount()-1));

        refreshUI();

        game_FAB_leftArrow.setOnClickListener(view -> {
            clicked("left");
        });
        game_FAB_rightArrow.setOnClickListener(view -> {
            clicked("right");
        });

    }


    private void findViews() {
        this.game_FAB_leftArrow = findViewById(R.id.game_FAB_leftArrow);
        this.game_FAB_rightArrow = findViewById(R.id.game_FAB_rightArrow);
        this.game_LL_player = findViewById(R.id.game_LL_player);
        this.game_LL_obstacleCol = new LinearLayout[]{
                findViewById(R.id.game_LL_obstacleCol1),
                findViewById(R.id.game_LL_obstacleCol2),
                findViewById(R.id.game_LL_obstacleCol3)
        };
        this.game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
                findViewById(R.id.game_IMG_heart4)
        };
    }


    private void clicked(String direction) {
        gameManger.movePlayer(direction);
        refreshUI();
    }

    private void refreshUI() {
        if(gameManger.isEnded()){

        }

    }


}