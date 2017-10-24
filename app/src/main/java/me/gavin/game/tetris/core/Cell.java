package me.gavin.game.tetris.core;

/**
 * 方格最小单位
 *
 * @author gavin.xiong 2017/10/12
 */
class Cell {

    boolean had;
    public Integer color;

    public boolean getHad() {
        return this.had;
    }

    public void setHad(boolean had) {
        this.had = had;
    }

    public Integer getColor() {
        return this.color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

}
