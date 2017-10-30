package me.gavin.game.tetris.core;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.game.tetris.effect.ScoreManager;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.next.Utils;

public class LandControl extends Control {

    private List<Point> shapePoint = new ArrayList<>();

    public LandControl(TetrisView view, TetrisCallback callback, boolean isContinue) {
        super(view, callback, isContinue);
    }

    @Override
    void onLand() {
        isRunning = true;

        shapePoint.clear();
        shapePoint.addAll(Arrays.asList(mShapes[0].points));
        mShapes[0] = null;

        mClearLines.clear();
        Set<Integer> set = new HashSet<>();

        for (Point point : shapePoint) {
            if (point.y >= 0) {
                mCells[point.y][point.x].had = true;
                set.add(point.y);
            }
        }
        for (Integer y : set) {
            boolean eliminable = true;
            for (int x = 0; x < hCount; x++) {
                if (!mCells[y][x].had) {
                    eliminable = false;
                    break;
                }
            }
            if (eliminable) {
                mClearLines.add(y);
            }
        }

        if (mClearLines.size() > 0) {
            Collections.sort(mClearLines);
            for (Integer y : mClearLines) {
                for (int i = 0, count = shapePoint.size(); i < count; i++) {
                    if (y == shapePoint.get(i).y) {
                        shapePoint.remove(i);
                        count--;
                        i--;
                    } else if (y > shapePoint.get(i).y) {
                        shapePoint.get(i).offset(0, 1);
                    }
                }
            }
            doClear();
            ScoreManager.get().onClear(mClearLines.size());
            mCallback.onClear(mClearLines.size());
            mCallback.onScoreChange();
        } else {
            tryNext();
        }
    }

    private void doClear() {
        Observable.interval(100, 100, TimeUnit.MILLISECONDS)
                .map(aLong -> { // 消除前闪烁
                    for (Integer y : mClearLines) {
                        for (int x = 0; x < hCount; x++) {
                            mCells[y][x].had = !mCells[y][x].had;
                        }
                    }
                    mView.postInvalidate();
                    return aLong;
                })
                .take(6)
                .toList()
                .map(arg0 -> mClearLines)
                .flatMapObservable(Observable::fromIterable)
                .doOnComplete(this::tryNext)
                .subscribe(y -> {
                    for (int i = y; i > 0; i--) {
                        System.arraycopy(mCells[i - 1], 0, mCells[i], 0, hCount);
                    }
                    for (int x = 0; x < hCount; x++) {
                        mCells[0][x] = new Cell();
                    }
                    for (int i = 0; i < mClearLines.size(); i++) {
                        if (mClearLines.get(i) <= y) {
                            mClearLines.set(i, mClearLines.get(i) + 1);
                        }
                    }
                    SoundManager.get().onClear();
                    mCallback.onClear();
                });
    }

    private void tryNext() {
        mView.postInvalidate();
        for (Point point : shapePoint) {
            if (point.y < 0) {
                isOver = true;
                onOver();
                return;
            } else {
                mCells[point.y][point.x].had = true;
            }
        }
        toNext();
    }

    private void toNext() {
        System.arraycopy(mShapes, 1, mShapes, 0, mShapes.length - 1);
        mShapes[mShapes.length - 1] = Utils.nextShape();
        ScoreManager.get().onNextShape(mClearLines.size());
        mCallback.onNextShape(mShapes[1]);
        isRunning = false;
        onStart();
    }
}
