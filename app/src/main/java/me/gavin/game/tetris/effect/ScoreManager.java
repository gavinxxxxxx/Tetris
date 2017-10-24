package me.gavin.game.tetris.effect;

import me.gavin.game.tetris.effect.impl.ScoreService;

/**
 * ScoreManager
 *
 * @author gavin.xiong 2017/10/19
 */
public class ScoreManager implements ScoreService {

    private int lineCount = 0;
    private long score = 0;
    private int multiple = 1;

    public ScoreManager() {
    }

    @Override
    public void onDrop() {
        // score += 1;
    }

    @Override
    public void onClear(int clearCount) {
        lineCount += clearCount;
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
    public int getLineCount() {
        return lineCount;
    }

    @Override
    public long getScore() {
        return score;
    }

    @Override
    public int getMultiple() {
        return multiple;
    }

    @Override
    public void onRestore(int count, long score, int multiple) {
        this.lineCount = count;
        this.score = score;
        this.multiple = multiple;
    }
}
