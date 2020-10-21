package com.yiwang.safekeyboard;

public class ViewPoint {
    private int cooX;
    private int cooY;
    private long cooTime;

    public ViewPoint() {
        this.cooX = 0;
        this.cooY = 0;
        this.cooTime = 0;
    }

    public ViewPoint(int cooX, int cooY, long cooTime) {
        this.cooX = cooX;
        this.cooY = cooY;
        this.cooTime = cooTime;
    }

    public int getCooX() {
        return cooX;
    }

    public void setCooX(int cooX) {
        this.cooX = cooX;
    }

    public int getCooY() {
        return cooY;
    }

    public void setCooY(int cooY) {
        this.cooY = cooY;
    }

    public long getCooTime() {
        return cooTime;
    }

    public void setCooTime(long cooTime) {
        this.cooTime = cooTime;
    }

    public void clearPoint() {
        this.cooX = 0;
        this.cooY = 0;
        this.cooTime = 0;
    }
}
