package com.example.android_hw1.Model;

public class Player {

    private int life;
    private int currentPos;
    private int maxIndex;

    public Player() {
    }

    public int getLife() {
        return life;
    }

    public Player setLife(int life) {
        this.life = life;
        return this;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public Player setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        return this;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public Player setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
        return this;
    }
}
