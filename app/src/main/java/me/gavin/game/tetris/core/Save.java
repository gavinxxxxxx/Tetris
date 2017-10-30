package me.gavin.game.tetris.core;

import me.gavin.game.tetris.shape.Shape;

/**
 * 存档数据
 *
 * @author gavin.xiong 2017/10/24
 */
public class Save {

    private int hCount;
    private int vCount;
    private int cCount;
    private long score;
    private int multiple;

    private Cell[][] cells;
    private Shape[] shapes;

    public int gethCount() {
        return hCount;
    }

    public void sethCount(int hCount) {
        this.hCount = hCount;
    }

    public int getvCount() {
        return vCount;
    }

    public void setvCount(int vCount) {
        this.vCount = vCount;
    }

    public int getcCount() {
        return cCount;
    }

    public void setcCount(int cCount) {
        this.cCount = cCount;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public Shape[] getShapes() {
        return shapes;
    }

    public void setShapes(Shape[] shapes) {
        this.shapes = shapes;
    }
}
