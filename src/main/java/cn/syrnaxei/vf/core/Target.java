package cn.syrnaxei.vf.core;

public class Target {
    private int x;
    private int y;
    private boolean isAlive;
    private boolean isMoving;

    public Target(int x,int y,boolean isMoving){
        this.x = x;
        this.y = y;
        this.isAlive = true;
        this.isMoving = isMoving;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
