package com.example.android_hw1;

import android.view.View;
import android.widget.LinearLayout;

public class GameManger {


    private Player player;
    private LinearLayout[] obstacleCols;

    public GameManger(LinearLayout[] obstacleCols, Player player) {
        this.player = player;
        this.obstacleCols = obstacleCols;
    }

    public Boolean isEnded() {
        return player.getLife() > 0;
    }


    public void movePlayer(String direction) {
        if (direction == "left" && player.getCurrentPos() > 0)
            player.setCurrentPos(player.getCurrentPos() - 1);
        else if (direction == "right" && player.getCurrentPos() < player.getMaxIndex())
            player.setCurrentPos((player.getCurrentPos() + 1));
    }

    /**
     * Sets all obstacles to invisible (we use it at the beginning of the game)
     **/
    public void initObstacles() {
        for (int i = 0; i < obstacleCols.length; i++) {
            for (int j = 0; j < obstacleCols[i].getChildCount(); j++) {
                obstacleCols[i].getChildAt(j).setVisibility(View.INVISIBLE);
            }
        }
    }

    public boolean hit() {
        return obstacleCols[player.getCurrentPos()].getChildAt((obstacleCols[player.getCurrentPos()].getChildCount() - 1)).getVisibility() == View.VISIBLE;
    }

    public void updateObstacles() {
        int numRows = obstacleCols[0].getChildCount() - 1;
        for (int i = 0; i < obstacleCols.length; i++) {
            if (obstacleCols[i].getChildAt(numRows).getVisibility() == View.VISIBLE) // checks the last obstacle
                obstacleCols[i].getChildAt(numRows).setVisibility(View.INVISIBLE);
            for (int j = numRows; j > 0; j++) {
                if (obstacleCols[i].getChildAt(j - 1).getVisibility() == View.VISIBLE) { // Moves the obstacle one spot down
                    obstacleCols[i].getChildAt(j - 1).setVisibility(View.INVISIBLE);
                    obstacleCols[i].getChildAt(j).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private int randomSpawn() {
        return (int) (Math.random() * obstacleCols.length + 1);
    }
}
