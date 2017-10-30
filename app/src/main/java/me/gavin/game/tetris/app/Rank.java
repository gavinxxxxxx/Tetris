package me.gavin.game.tetris.app;

/**
 * 纪录
 *
 * @author gavin.xiong 2017/10/24
 */
public class Rank {

    private String title;
    private long score;
    private int lineCount;
    private long time;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getScore() {
        return this.score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getLineCount() {
        return this.lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
