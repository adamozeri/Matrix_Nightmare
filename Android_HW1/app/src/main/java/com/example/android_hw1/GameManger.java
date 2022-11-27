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

//    public void initGameUI() {
//        initObstacles();
//        initPlayer();
//    }
//
//    /**
//     * Sets all obstacles to invisible (we use it at the beginning of the game)
//     **/
//    public void initObstacles() {
//        for (int i = 0; i < obstacleCols.length; i++) {
//            for (int j = 0; j < obstacleCols[i].getChildCount(); j++) {
//                obstacleCols[i].getChildAt(j).setVisibility(View.INVISIBLE);
//            }
//        }
//    }
//
//    /**
//     * Sets all player's image to invisible but the center one
//     **/
//    public void initPlayer() {
//        for (int i = 0; i < playerLL.getChildCount(); i++) {
//            if (i != player.getCurrentPos())
//                playerLL.getChildAt(i).setVisibility(View.INVISIBLE);
//            else
//                playerLL.getChildAt(i).setVisibility(View.VISIBLE);
//        }
//    }

    /**
     * if the player collides with an obstacle lowers the player's life by 1
     **/
    public void hit() {
        if (obstacleCols[player.getCurrentPos()].getChildAt((obstacleCols[player.getCurrentPos()].getChildCount() - 1)).getVisibility() == View.VISIBLE)
            player.setLife(player.getLife() - 1);
    }

//    public void updateObstacles() {
//        int numRows = obstacleCols[0].getChildCount() - 1;
//        for (int i = 0; i < obstacleCols.length; i++) {
//            if (obstacleCols[i].getChildAt(numRows).getVisibility() == View.VISIBLE) // checks the last obstacle
//                obstacleCols[i].getChildAt(numRows).setVisibility(View.INVISIBLE);
//            for (int j = numRows; j > 0; j++) {
//                if (obstacleCols[i].getChildAt(j - 1).getVisibility() == View.VISIBLE) { // Moves the obstacle one spot down
//                    obstacleCols[i].getChildAt(j - 1).setVisibility(View.INVISIBLE);
//                    obstacleCols[i].getChildAt(j).setVisibility(View.VISIBLE);
//                }
//            }
//        }
//        obstacleCols[randomSpawn()].getChildAt(0).setVisibility(View.VISIBLE); // setting a new obstacle in the first row
//        hit();
//    }

    public int randomSpawn() {
        return (int) (Math.random() * obstacleCols.length);
    }

    public Player getPlayer() {
        return player;
    }
}
