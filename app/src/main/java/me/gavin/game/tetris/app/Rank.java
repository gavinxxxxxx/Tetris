package me.gavin.game.tetris.app;

/**
 * 纪录
 *
 * @author gavin.xiong 2017/10/24
 */
public class Rank {

    private String title;
    private long score;
    private int count;
    private long time;

    String getTitle() {
        return this.title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    long getScore() {
        return this.score;
    }

    void setScore(long score) {
        this.score = score;
    }

    int getCount() {
        return this.count;
    }

    void setCount(int count) {
        this.count = count;
    }

    long getTime() {
        return this.time;
    }

    void setTime(long time) {
        this.time = time;
    }
}
