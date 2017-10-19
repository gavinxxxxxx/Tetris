package me.gavin.game.tetris.effect;

import me.gavin.game.tetris.effect.impl.ScoreService;

/**
 * ScoreManager
 *
 * @author gavin.xiong 2017/10/19
 */
public class ScoreManager implements ScoreService {

    private long score = 0;
    private int multiple = 1;

    public ScoreManager() {
        this(0, 1);
    }

    public ScoreManager(long score, int multiple) {
        this.score = score;
        this.multiple = multiple;
    }

    @Override
    public void onDrop() {
        // do nothing
    }

    @Override
    public void onClear(int clearCount) {
        for (int i = 1; i <= clearCount; i++) {
            multiple *= i;
            score += multiple;
        }
    }

    @Override
    public void onNextShape(int clearCount) {
        if (clearCount == 0) {
            multiple = 1; // 触底未消除，重置倍数
        } else {
            multiple *= 2;
        }
    }

    @Override
    public long getScore() {
        return score;
    }
}
