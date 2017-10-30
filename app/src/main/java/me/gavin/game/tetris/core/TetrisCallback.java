package me.gavin.game.tetris.core;

import me.gavin.game.tetris.shape.Shape;

/**
 * TetrisCallback
 *
 * @author gavin.xiong 2017/10/19
 */
public interface TetrisCallback {

    void onNextShape(Shape shape);

    void onClear();

    void onClear(int clearCount);

    void onScoreChange();

    void onOver();

}
