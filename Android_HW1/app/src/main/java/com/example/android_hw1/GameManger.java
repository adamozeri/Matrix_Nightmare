package com.example.android_hw1;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;

public class GameManger {

    private final int SEQUENCE_NUM = 2;

    private Player player;
    private LinearLayout[] obstacleCols;
    private int[] lastCols;

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
    public void movePlayer(String direction) {
        if (direction == "left" && player.getCurrentPos() > 0)
            player.setCurrentPos(player.getCurrentPos() - 1);
        else if (direction == "right" && player.getCurrentPos() < player.getMaxIndex())
            player.setCurrentPos((player.getCurrentPos() + 1));
    }


    /**
     * if the player collides with an obstacle lowers the player's life by 1
     **/
    public boolean hit(Vibrator v) {
        if (obstacleCols[player.getCurrentPos()].getChildAt((obstacleCols[player.getCurrentPos()].getChildCount() - 1)).getVisibility() == View.VISIBLE) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            if (player.getLife() > 0)
                player.setLife(player.getLife() - 1);
            return true;
        }
        return false;
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
                return true;
            }
        }
        return false;
    }

    public int randomSpawn(int num) {
        return (int) (Math.random() * num);
    }

    public Player getPlayer() {
        return player;
    }
}
