package com.example.android_hw1.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.android_hw1.DataManager;
import com.example.android_hw1.GameManger;
import com.example.android_hw1.Utils.BackgroundSound;
import com.example.android_hw1.Model.Record;
import com.example.android_hw1.Interfaces.MovementCallback;
import com.example.android_hw1.Sensor.MovementSensor;
import com.example.android_hw1.Model.Player;
import com.example.android_hw1.R;
import com.example.android_hw1.Utils.SignalGenerator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    // Intent values
    public static final String KEY_SPEED = "KEY_SPEED";
    public static final String KEY_SENSOR = "KEY_SENSOR";
    public static final String KEY_LONGITUDE = "KEY_LONGITUDE";
    public static final String KEY_LATITUDE = "KEY_LATITUDE";


    private Timer timer;
    private GameManger gameManger;
    private MovementSensor movementSensor;

    // game settings
    private int delay;
    private boolean isSensor;

    //  view
    private ExtendedFloatingActionButton game_FAB_leftArrow;
    private ExtendedFloatingActionButton game_FAB_rightArrow;
    private LinearLayout[] game_LL_obstacleCol;
    private LinearLayout game_LL_player;
    private ShapeableImageView[] game_IMG_hearts;
    private MaterialTextView game_LBL_score;

    // sound
    private BackgroundSound matrixSound;
    private int previousHit = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        findViews();
        this.gameManger = new GameManger(game_LL_obstacleCol, new Player()
                .setCurrentPos(game_LL_player.getChildCount() / 2)
                .setLife(game_IMG_hearts.length)
                .setMaxIndex(game_LL_player.getChildCount() - 1), new Record().setScore(0));
        getValuesPreviousIntent();
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
        if(gameManger.getPlayer().getLife() > 0)
            startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
        if (isSensor)
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
        gameManger.getRecord().setLatitude(previousIntent.getDoubleExtra(KEY_LATITUDE,31.0));
        gameManger.getRecord().setLongitude(previousIntent.getDoubleExtra(KEY_LONGITUDE,31.0));
    }

    /**
     * sets all attributes to the correct view id
     **/
    private void findViews() {
        this.game_FAB_leftArrow = findViewById(R.id.game_FAB_leftArrow);
        this.game_FAB_rightArrow = findViewById(R.id.game_FAB_rightArrow);
        this.game_LL_player = findViewById(R.id.game_LL_player);
        this.game_LBL_score = findViewById(R.id.game_LBL_score);
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
                    int tag = (int) game_LL_obstacleCol[i].getChildAt(j - 1).getTag();
                    ((ShapeableImageView) game_LL_obstacleCol[i].getChildAt(j)).setImageResource(tag); // set the image of the obstacle from above
                    game_LL_obstacleCol[i].getChildAt(j).setTag(tag);
                    game_LL_obstacleCol[i].getChildAt(j).setVisibility(View.VISIBLE);
                }
            }
        }
        hitCheck();
    }

    /**
     * checks if the "bad" obstacle hits the player in order to remove hearts in view
     */
    private void hitCheck() {
        if (gameManger.hit() == R.drawable.ic_matrix_color) {
            removeHeart();
            this.matrixSound = new BackgroundSound(this, R.raw.wrong_answer_new);
            matrixSound.execute();
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
            col = gameManger.randomNumber(game_LL_obstacleCol.length);
        } while (gameManger.sequenceCheck(col));
        int currentObstacle = randomObstacleType();
        ((ShapeableImageView) game_LL_obstacleCol[col].getChildAt(0)).setImageResource(currentObstacle);
        game_LL_obstacleCol[col].getChildAt(0).setTag(currentObstacle);
        game_LL_obstacleCol[col].getChildAt(0).setVisibility(View.VISIBLE); // setting a new obstacle in the first row
    }

    /**
     * Randomly pick an obstacle type.
     *
     * @return image resource as int.
     */
    private int randomObstacleType() {
        if (gameManger.randomNumber(5) == 4)
            return R.drawable.ic_a;
        return R.drawable.ic_matrix_color;
    }

    /**
     * sets hearts invisible
     **/
    private void removeHeart() {
        game_IMG_hearts[gameManger.getPlayer().getLife()].setVisibility(View.INVISIBLE);
        if (gameManger.getPlayer().getLife() == 0)
            gameOver();
    }

    /**
     * updates the score for the new record and changes to the top grades activity.
     */
    private void gameOver() {
        stopTimer();
        if (isSensor)
            movementSensor.stop();
        gameManger.updateScore(gameManger.getOdometer());
        initNamePopup();
    }

    private void initNamePopup() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_name, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        popupWindow.showAtLocation(findViewById(R.id.activity_game), Gravity.CENTER, 0, 0);

        MaterialButton popup_BTN_submit = popupView.findViewById(R.id.popup_BTN_submit);
        MaterialTextView popup_LBL_score = popupView.findViewById(R.id.popup_LBL_score);
        TextInputEditText popup_ET_name = popupView.findViewById(R.id.popup_ET_name);

        popup_LBL_score.setText("Score "+gameManger.getOdometer());

        popup_BTN_submit.setOnClickListener(view -> popupClicked(popup_ET_name,popupWindow));

    }

    private void popupClicked(TextInputEditText popup_ET_name, PopupWindow popupWindow){
        if(popup_ET_name.length() == 0){
            SignalGenerator.getInstance().toast("Please enter a name");
        }
        else{
            gameManger.getRecord().setName(popup_ET_name.getText().toString());
            popupWindow.dismiss();
            openGradesScreen();
        }
    }

    private void openGradesScreen() {
        DataManager.getInstance().addRecord(gameManger.getRecord());
        Intent scoreIntent = new Intent(this, ScoreActivity.class);
        startActivity(scoreIntent);
        finish();
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
                    if (gameManger.getOdometer() % 2 == 0)
                        newObstacle();
                    gameManger.setOdometer(gameManger.getOdometer() + 1);
                    if(gameManger.getPlayer().getLife() > 0)
                        game_LBL_score.setText("" + gameManger.getOdometer());
                });
            }
        }, delay, delay);
    }

    private void stopTimer() {
        timer.cancel();
    }


}