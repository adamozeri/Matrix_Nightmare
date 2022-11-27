package com.example.android_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private Timer timer;
    private final int DELAY = 750;

    private ExtendedFloatingActionButton game_FAB_leftArrow;
    private ExtendedFloatingActionButton game_FAB_rightArrow;
    private LinearLayout[] game_LL_obstacleCol;
    private LinearLayout game_LL_player;
    private ShapeableImageView[] game_IMG_hearts;

    private ShapeableImageView game_IMG_background;

    private GameManger gameManger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        Glide
                .with(this)
                .load("https://img.freepik.com/free-vector/chalkboard-texture_1048-1125.jpg?w=826&t=st=1669559891~exp=1669560491~hmac=fa28ced811066e461dbd16b6075c6c8178fd0077b64ef1f86f8aac74e1bcfa93")
                .into(game_IMG_background);
        this.gameManger = new GameManger(game_LL_obstacleCol, new Player()
                .setCurrentPos(game_LL_player.getChildCount() / 2)
                .setLife(game_IMG_hearts.length)
                .setMaxIndex(game_LL_player.getChildCount() - 1));

        initGameUI();
        refreshUI();
        startTimer();

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
                findViewById(R.id.game_IMG_heart3)
        };
        this.game_IMG_background = findViewById(R.id.game_IMG_background);
    }


    private void clicked(String direction) {
        gameManger.movePlayer(direction);
        refreshUI();
    }

    private void initGameUI() {
        initObstacles();
        playerVisibility();
    }

    /**
     * Sets all obstacles to invisible (we use it at the beginning of the game)
     **/
    private void initObstacles() {
        for (int i = 0; i < game_LL_obstacleCol.length; i++) {
            for (int j = 0; j < game_LL_obstacleCol[i].getChildCount(); j++) {
                game_LL_obstacleCol[i].getChildAt(j).setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * sets player visibility in the current position
     **/
    private void playerVisibility() {
        for (int i = 0; i < game_LL_player.getChildCount(); i++) {
            if (i != gameManger.getPlayer().getCurrentPos())
                game_LL_player.getChildAt(i).setVisibility(View.INVISIBLE);
            else
                game_LL_player.getChildAt(i).setVisibility(View.VISIBLE);
        }
    }

    /**
     * moves the obstacles on the screen with random spawns
     **/
    public void updateObstacles() {
        int numRows = game_LL_obstacleCol[0].getChildCount() - 1;
        for (int i = 0; i < game_LL_obstacleCol.length; i++) {
            if (game_LL_obstacleCol[i].getChildAt(numRows).getVisibility() == View.VISIBLE) // checks the last obstacle
                game_LL_obstacleCol[i].getChildAt(numRows).setVisibility(View.INVISIBLE);
            for (int j = numRows; j > 0; j--) {
                if (game_LL_obstacleCol[i].getChildAt(j - 1).getVisibility() == View.VISIBLE) { // Moves the obstacle one spot down
                    game_LL_obstacleCol[i].getChildAt(j - 1).setVisibility(View.INVISIBLE);
                    game_LL_obstacleCol[i].getChildAt(j).setVisibility(View.VISIBLE);
                }
            }
        }
        game_LL_obstacleCol[gameManger.randomSpawn()].getChildAt(0).setVisibility(View.VISIBLE); // setting a new obstacle in the first row
        gameManger.hit();
    }


    private void refreshUI() {
        playerVisibility();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    updateObstacles();
                });
            }
        }, DELAY, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }


}