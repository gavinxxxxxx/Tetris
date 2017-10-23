package me.gavin.game.tetris.effect;

import android.content.Context;

import me.gavin.game.tetris.effect.impl.ScoreService;
import me.gavin.game.tetris.util.SPUtil;

/**
 * ScoreManager
 *
 * @author gavin.xiong 2017/10/19
 */
public class ScoreManager implements ScoreService {

    private Context mContext;

    private int lineCount = 0;
    private long score = 0;
    private int multiple = 1;

    public ScoreManager(Context context) {
        this(context, 0, 0, 1);
    }

    public ScoreManager(Context context, int lineCount, long score, int multiple) {
        this.mContext = context;
        this.lineCount = lineCount;
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
    public void onSavedInstanceState() {
        SPUtil.saveInt(mContext, "lineCount", lineCount);
        SPUtil.saveLong(mContext, "score", score);
        SPUtil.saveInt(mContext, "multiple", multiple);
    }

    @Override
    public void onRestoreInstanceState() {
        lineCount = SPUtil.getInt(mContext, "lineCount");
        score = SPUtil.getLong(mContext, "score");
        int m = SPUtil.getInt(mContext, "multiple");
        multiple = m > 0 ? m : 1;
    }
}
