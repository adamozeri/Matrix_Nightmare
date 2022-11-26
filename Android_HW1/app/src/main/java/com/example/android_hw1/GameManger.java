package com.example.android_hw1;

import android.widget.LinearLayout;

public class GameManger {


    private Player player;
    private LinearLayout[] obstacleCols;
    private int numOfCols;

    public GameManger(LinearLayout[] obstacleCols, Player player) {
        this.numOfCols = 3;
        this.player = player;
        this.obstacleCols = obstacleCols;
    }

    public Boolean isEnded() {
        return player.getLife() > 0;
    }


    public void movePlayer(String direction) {
        if (direction == "left" && player.getIndex() > 0)
            player.setIndex(player.getIndex() - 1);
        else if (direction == "right" && player.getIndex() < player.getMaxIndex())
            player.setIndex((player.getIndex() + 1));
    }

    public void updateObstacles() {
        for (int i = 0; i < obstacleCols.length; i++) {

        }
    }

    private int randomSpawn() {
        return (int) (Math.random() * numOfCols + 1);
    }
}
