package com.example.android_hw1;

import android.view.View;
import android.widget.LinearLayout;

import com.example.android_hw1.Model.Player;
import com.example.android_hw1.Model.Record;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Arrays;

public class GameManger {

    private final String SP_RECORDS = "SP_RECORDS";

    private final int SEQUENCE_NUM = 2;

    private Player player;
    private LinearLayout[] obstacleCols;
    private int[] lastCols;
    private Record record;
    private int odometer = 0; // every even number new obstacle



    public GameManger(LinearLayout[] obstacleCols, Player player, Record record) {
        this.player = player;
        this.obstacleCols = obstacleCols;
        this.record = record;
        initLastCols();// here I store the last 2 random cols
    }

    private void initLastCols() {
        this.lastCols = new int[SEQUENCE_NUM];
        Arrays.fill(this.lastCols, -1);
    }


    /**
     * sets player new position
     **/
    public void movePlayer(int direction) {
        if (direction == -1 && player.getCurrentPos() > 0)
            player.setCurrentPos(player.getCurrentPos() - 1);
        else if (direction == 1 && player.getCurrentPos() < player.getMaxIndex())
            player.setCurrentPos((player.getCurrentPos() + 1));
    }


    /**
     * if the player collides with an obstacle lowers the player's life by 1
     *
     * @return the obstacle id
     **/
    public int hit() {
        ShapeableImageView obstacle = (ShapeableImageView) obstacleCols[player.getCurrentPos()].getChildAt((obstacleCols[player.getCurrentPos()].getChildCount() - 1));
        if (obstacle.getVisibility() == View.VISIBLE && (int) obstacle.getTag() == R.drawable.ic_matrix_color) {
            if (player.getLife() > 0)
                player.setLife(player.getLife() - 1);
            return R.drawable.ic_matrix_color;
        } else if(obstacle.getVisibility() == View.VISIBLE && (int) obstacle.getTag() == R.drawable.ic_a) {
            odometer += 10;
            return R.drawable.ic_a;
        }
        return 0;
    }

    public void updateScore(int score) {
        record.setScore(record.getScore() + score);
    }

    /**
     * Checks for sequence of 2 in the same col
     **/
    public boolean sequenceCheck(int col) {
        for (int i = 0; i < SEQUENCE_NUM; i++) {
            if (lastCols[i] != col) {
                for (int j = 0; j < SEQUENCE_NUM - 1; j++) {
                    lastCols[j + 1] = lastCols[j];
                }
                lastCols[0] = col;
                return false;
            }
        }
        return true;
    }

    public int randomNumber(int num) {
        return (int) (Math.random() * num);
    }

    public Player getPlayer() {
        return player;
    }

    public Record getRecord() {
        return record;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }
}
