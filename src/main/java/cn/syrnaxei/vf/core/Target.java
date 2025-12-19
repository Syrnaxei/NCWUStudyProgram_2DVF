package cn.syrnaxei.vf.core;

public class Target {
    private final int x;
    private final int y;
    private final int size;
    private boolean isAlive;

    public Target(int x,int y,int size){
        this.x = x;
        this.y = y;
        this.size = size;
        this.isAlive = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isHit(int mouseX, int mouseY , int targetSize) {
        return isAlive && mouseX >= x && mouseX <= x + targetSize && mouseY >= y && mouseY <= y + targetSize;
    }
}
