package com.example.android_hw1;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;

import com.example.android_hw1.Model.Player;
import com.example.android_hw1.Model.Record;
import com.google.android.material.imageview.ShapeableImageView;

public class GameManger {

    private final int SEQUENCE_NUM = 1;

    private Player player;
    private LinearLayout[] obstacleCols;
    private int[] lastCols;
    private Record record;

    public GameManger(LinearLayout[] obstacleCols, Player player) {
        this.player = player;
        this.obstacleCols = obstacleCols;
        initLastCols(); // here I store the last 2 random cols
    }

    private void initLastCols() {
        this.lastCols = new int[SEQUENCE_NUM];
        for (int i = 0; i < this.lastCols.length; i++) {
            this.lastCols[i] = -1;
        }
    }

    public Boolean isEnded() {
        return player.getLife() > 0;
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
     **/
    public boolean hit(Vibrator v) {
        ShapeableImageView obstacle = (ShapeableImageView) obstacleCols[player.getCurrentPos()].getChildAt((obstacleCols[player.getCurrentPos()].getChildCount() - 1));
        if (obstacle.getVisibility() == View.VISIBLE && (int)obstacle.getTag() == R.drawable.ic_matrix_color) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            if (player.getLife() > 0)
                player.setLife(player.getLife() - 1);
            return true;
        }
        else{
//            record.setDistance(record.getDistance()+10);
            return false;
        }
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

    public int randomSpawn(int num) {
        return (int) (Math.random() * num);
    }

    public Player getPlayer() {
        return player;
    }
}
