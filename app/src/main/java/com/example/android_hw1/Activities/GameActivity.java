package com.example.android_hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.android_hw1.GameManger;
import com.example.android_hw1.Sensor.MovementCallback;
import com.example.android_hw1.Sensor.MovementSensor;
import com.example.android_hw1.Model.Player;
import com.example.android_hw1.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    // Intent values
    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_SENSOR = "KEY_SENSOR";


    private Timer timer;
    private GameManger gameManger;
    private MovementSensor movementSensor;

    // game settings
    private int delay;
    private boolean isSensor;

    private int Odometer = 0; // every even number new obstacle

    //  view
    private ExtendedFloatingActionButton game_FAB_leftArrow;
    private ExtendedFloatingActionButton game_FAB_rightArrow;
    private LinearLayout[] game_LL_obstacleCol;
    private LinearLayout game_LL_player;
    private ShapeableImageView[] game_IMG_hearts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getValuesPreviousIntent();

        findViews();
        this.gameManger = new GameManger(game_LL_obstacleCol, new Player()
                .setCurrentPos(game_LL_player.getChildCount() / 2)
                .setLife(game_IMG_hearts.length)
                .setMaxIndex(game_LL_player.getChildCount() - 1));

        refreshUI();

        game_FAB_leftArrow.setOnClickListener(view -> {
            clicked(-1);
        });
        game_FAB_rightArrow.setOnClickListener(view -> {
            clicked(1);
        });

        checkSensorMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        movementSensor.stop();
    }

    /**
     * checks if the sensor switch is on:
     * true - initiates sensors and start them, sets the arrows to invisible.
     * false - sets the arrows to visible.
     **/
    private void checkSensorMode() {
        if (isSensor) { // sensor mode
            initMovementSensor();
            game_FAB_leftArrow.setVisibility(View.INVISIBLE);
            game_FAB_rightArrow.setVisibility(View.INVISIBLE);
            movementSensor.start();
        } else {
            game_FAB_leftArrow.setVisibility(View.VISIBLE);
            game_FAB_rightArrow.setVisibility(View.VISIBLE);
        }
    }


    private void initMovementSensor() {
        movementSensor = new MovementSensor(this, new MovementCallback() {
            @Override
            public void playerMovement(int direction) {
                gameManger.movePlayer(direction);
                refreshUI();
            }
            @Override
            public void playerSpeed(int y) {
            }
        });
    }

    /**
     * getting values from the previous screen
     */
    private void getValuesPreviousIntent() {
        Intent previousIntent = getIntent();
        if (previousIntent.getBooleanExtra(KEY_SPEED, false))
            delay = 300;
        else
            delay = 600;
        isSensor = previousIntent.getBooleanExtra(KEY_SENSOR, false);
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
                findViewById(R.id.game_LL_obstacleCol3),
                findViewById(R.id.game_LL_obstacleCol4),
                findViewById(R.id.game_LL_obstacleCol5)
        };
        this.game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
    }

    private void clicked(int direction) {
        gameManger.movePlayer(direction);
        refreshUI();
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
     * moves the obstacles on the screen with random spawns.
     **/
    public void updateObstacles() {
        int numRows = game_LL_obstacleCol[0].getChildCount() - 1;
        for (int i = 0; i < game_LL_obstacleCol.length; i++) {
            if (game_LL_obstacleCol[i].getChildAt(numRows).getVisibility() == View.VISIBLE) // checks the last obstacle
                game_LL_obstacleCol[i].getChildAt(numRows).setVisibility(View.INVISIBLE);
            for (int j = numRows; j > 0; j--) {
                if (game_LL_obstacleCol[i].getChildAt(j - 1).getVisibility() == View.VISIBLE) { // Moves the obstacle one spot down
                    game_LL_obstacleCol[i].getChildAt(j - 1).setVisibility(View.INVISIBLE);
                    int tag = (int)(((ShapeableImageView) game_LL_obstacleCol[i].getChildAt(j-1))).getTag();
                    ((ShapeableImageView) game_LL_obstacleCol[i].getChildAt(j)).setImageResource(tag); // set the image of the obstacle from above
                    ((ShapeableImageView) game_LL_obstacleCol[i].getChildAt(j)).setTag(tag);
                    game_LL_obstacleCol[i].getChildAt(j).setVisibility(View.VISIBLE);
                }
            }
        }
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (gameManger.hit(v)) {
            toast();
            removeHeart();
        }
    }

    /**
     * creates a new obstacle.
     * calls sequenceCheck() to check if there is a sequence of the same column.
     * calls randomObstacleType() to pick an obstacle.
     **/
    public void newObstacle() {
        int col = 0;
        do {
            col = gameManger.randomSpawn(game_LL_obstacleCol.length);
        } while (gameManger.sequenceCheck(col));
        int currentObstacle = randomObstacleType();
        ((ShapeableImageView) game_LL_obstacleCol[col].getChildAt(0)).setImageResource(currentObstacle);
        ((ShapeableImageView) game_LL_obstacleCol[col].getChildAt(0)).setTag(currentObstacle);
        game_LL_obstacleCol[col].getChildAt(0).setVisibility(View.VISIBLE); // setting a new obstacle in the first row
    }

    /**
     * Randomly pick an obstacle type.
     *
     * @return image resource as int.
     */
    private int randomObstacleType() {
        if ((int) (Math.random() * 5) == 4)
            return R.drawable.ic_a;
        return R.drawable.ic_matrix_color;
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
        if (gameManger.getPlayer().getLife() <= 0)
            Toast
                    .makeText(this, "Game Over", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast
                    .makeText(this, "Ouch", Toast.LENGTH_SHORT)
                    .show();
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    updateObstacles();
                    if (Odometer % 2 == 0)
                        newObstacle();
                    Odometer++;
                });
            }
        }, delay, delay);
    }

    private void stopTimer() {
        timer.cancel();
    }


}