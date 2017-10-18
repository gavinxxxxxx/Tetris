package me.gavin.game.tetris.region;

/**
 * 按键监听
 *
 * @author gavin.xiong 2017/10/12
 */
public interface OnKeyPassListener {

    void onLeft();

    void onRight();

    void onUp();

    void onDown();

    void onDrop();

    void onRotate();

    void onPause();

}
