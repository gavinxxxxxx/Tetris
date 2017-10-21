package me.gavin.game.tetris.core;

import me.gavin.game.tetris.core.shape.Shape;

/**
 * TetrisCallback
 *
 * @author gavin.xiong 2017/10/19
 */
public interface TetrisCallback {

    void onNextShape(Shape shape, int clearCount);

    void onClear();

    void onClear(int clearCount);

    void onScoreChange(int lineCount, long score, int multiple);

    void onOver();

}
