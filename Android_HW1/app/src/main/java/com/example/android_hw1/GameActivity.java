package com.example.android_hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private Timer timer;
    private final int DELAY = 500;

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
                .load("https://media.shoesonline.co.il/2020/10/unisex-Palladium-Pampa-Dare-Exchange__76860-008-M-555x555.jpg")
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

    /**
     * sets all attributes to the correct view id
     **/
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
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameManger.hit(v)) {
            toast();
            removeHeart();
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
        int col = 0;
        do {
            Log.d("check","here");
            col = gameManger.randomSpawn(game_LL_obstacleCol.length);
        } while (diagonalCheck(col) || gameManger.sequenceCheck(col));

        game_LL_obstacleCol[col].getChildAt(0).setVisibility(View.VISIBLE); // setting a new obstacle in the first row
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameManger.hit(v)) {
            toast();
            removeHeart();
        }
    }


    /**
     * Checks for potential diagonal traps
     **/
    private boolean diagonalCheck(int col) {
        for (int i = 0; i < game_LL_obstacleCol[1].getChildCount() - 1; i++) {
            if (game_LL_obstacleCol[1].getChildAt(i).getVisibility() == View.VISIBLE) {
                if (col == 0 && game_LL_obstacleCol[game_LL_obstacleCol.length - 1].getChildAt(i + 1).getVisibility() == View.VISIBLE)
                    return true;
                else if (col == game_LL_obstacleCol.length - 1
                        && game_LL_obstacleCol[0].getChildAt(i + 1).getVisibility() == View.VISIBLE)
                    return true;
            }
        }
        return false;
    }

    /**
     * sets hearts invisible
     **/
    private void removeHeart() {
        if (gameManger.getPlayer().getLife() >= 0)
            game_IMG_hearts[gameManger.getPlayer().getLife()].setVisibility(View.INVISIBLE);
    }


    private void refreshUI() {
        playerVisibility();
    }

    private void toast() {
        Toast
                .makeText(this, "Ouch ", Toast.LENGTH_SHORT)
                .show();
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

}