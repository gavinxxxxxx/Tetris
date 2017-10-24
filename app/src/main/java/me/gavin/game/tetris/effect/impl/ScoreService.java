package me.gavin.game.tetris.effect.impl;

/**
 * ScoreService
 *
 * @author gavin.xiong 2017/10/19
 */
public interface ScoreService {

    void onDrop();

    void onClear(int clearCount);

    void onNextShape(int clearCount);

    int getLineCount();

    long getScore();

    int getMultiple();

    void onRestore(int count, long score, int multiple);

}
