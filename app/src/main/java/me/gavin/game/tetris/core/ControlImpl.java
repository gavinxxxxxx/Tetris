package me.gavin.game.tetris.core;

/**
 * ControlImpl
 *
 * @author gavin.xiong 2017/10/21
 */
public interface ControlImpl {

    void onLeft();

    void onRight();

    void onUp();

    void onDown();

    void onDrop();

    void onRotate();

    void onPause();

    void onStart();

    void onOver();

    void onMute(boolean state);

    void onSavedInstanceState();

    void dispose();
}
