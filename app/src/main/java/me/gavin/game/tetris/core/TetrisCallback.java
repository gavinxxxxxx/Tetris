package me.gavin.game.tetris.core;

import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.effect.impl.ScoreService;

/**
 * TetrisCallback
 *
 * @author gavin.xiong 2017/10/19
 */
public interface TetrisCallback {

    void onNextShape(Shape shape);

    void onClear();

    void onClear(int clearCount);

    void onScoreChange(ScoreService service);

    void onOver(int clearedLineCount, long score);

}
