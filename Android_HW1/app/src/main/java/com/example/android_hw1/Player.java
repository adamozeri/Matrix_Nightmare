package com.example.android_hw1;

public class Player {

    private int life;
    private int index;
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

    public int getIndex() {
        return index;
    }

    public Player setIndex(int index) {
        this.index = index;
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
