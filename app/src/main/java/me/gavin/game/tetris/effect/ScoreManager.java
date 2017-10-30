package me.gavin.game.tetris.effect;

/**
 * ScoreManager
 *
 * @author gavin.xiong 2017/10/19
 */
public class ScoreManager {

    private int count = 0;
    private long score = 0;
    private int multiple = 1;

    private ScoreManager() {
    }

    private static class Holder {
        private static final ScoreManager INSTANCE = new ScoreManager();
    }

    public static ScoreManager get() {
        return Holder.INSTANCE;
    }

    public void onDrop() {
        // score += 1;
    }

    public void onEliminate(int eliminatedCount) {
        count += eliminatedCount;
        for (int i = 1; i <= eliminatedCount; i++) {
            multiple *= i;
            score += multiple;
        }
    }

    public void onNextShape(int eliminatedCount) {
        if (eliminatedCount == 0) {
            multiple = 1; // 触底未消除，重置倍数
        } else {
            multiple *= 2;
        }
    }

    public int getCount() {
        return count;
    }

    public long getScore() {
        return score;
    }

    public int getMultiple() {
        return multiple;
    }

    public void reset(int count, long score, int multiple) {
        this.count = count;
        this.score = score;
        this.multiple = multiple;
    }
}
