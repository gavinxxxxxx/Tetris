package me.gavin.game.tetris.core;

/**
 * TetrisCallback
 *
 * @author gavin.xiong 2017/10/19
 */
public interface TetrisCallback {

    void onNextShape(int clearCount);

    void onClear();

    void onClear(int clearCount);

    void onOver();

}
