package me.gavin.game.tetris.core;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Tetris ViewModel
 *
 * @author gavin.xiong 2017/10/19
 */
public class TetrisSoleControl extends TetrisControl {

    public TetrisSoleControl(TetrisView view, @NonNull TetrisCallback callback) {
        super(view, callback);
    }

    @Override
    void onSole() {
        for (Point point : mShape.prePoints) {
            if (point.y <= 0) { // game over
                isOver = true;
                mCallback.onOver();
                return;
            }
        }

        // 触底未结束
        for (Point point : mShape.points) {
            mCells[point.x][point.y].had = true;
        }

        SparseArray<Point> sparseArray = new SparseArray<>();
        for (Point point : mShape.points) {
            sparseArray.append(point.y, point);
        }
        Point[] temp = new Point[sparseArray.size()];
        for (int i = 0; i < sparseArray.size(); i++) {
            temp[i] = sparseArray.valueAt(i);
        }
        Arrays.sort(temp, ((o1, o2) -> o1.y > o2.y ? -1 : 1));

        mClearLines.clear();
        for (Point point : temp) {
            boolean hadFlag = true;
            for (int i = 0; i < hCount; i++) {
                if (!mCells[i][point.y].had) {
                    hadFlag = false;
                    break;
                }
            }
            if (hadFlag) {
                mClearLines.add(point.y);
            }
        }

        if (mClearLines.size() > 0) {
//            doClear1();
            doClear2();
            mCallback.onClear(mClearLines.size());
        } else {
            mView.postInvalidate();
        }

        mCallback.onNextShape(mClearLines.size());
    }

    private void doClear1() {
        for (Integer y : mClearLines) {
            // TODO: 2017/10/17 arrayCopy
            for (int j = y; j > 0; j--) {
                for (int i = 0; i < hCount; i++) {
                    mCells[i][j] = mCells[i][j - 1];
                }
            }
            for (int i = 0; i < hCount; i++) {
                mCells[i][0] = new Cell();
            }
            for (int i = 0; i < mClearLines.size(); i++) {
                if (mClearLines.get(i) <= y) {
                    mClearLines.set(i, mClearLines.get(i) + 1);
                }
            }
            mCallback.onClear();
        }
        mView.postInvalidate();
    }

    private void doClear2() {
        Observable.just(mClearLines)
                .flatMap(Observable::fromIterable)
                .delay(100, TimeUnit.MILLISECONDS)
                .map(integer -> {
                    for (int i = 0; i < hCount; i++) {
                        mCells[i][integer].had = false;
                    }
                    mView.postInvalidate();
                    return integer;
                })
                .delay(100, TimeUnit.MILLISECONDS)
                .map(integer -> {
                    for (int i = 0; i < hCount; i++) {
                        mCells[i][integer].had = true;
                    }
                    mView.postInvalidate();
                    return integer;
                })
                .delay(100, TimeUnit.MILLISECONDS)
                .map(integer -> {
                    for (int i = 0; i < hCount; i++) {
                        mCells[i][integer].had = false;
                    }
                    mView.postInvalidate();
                    return integer;
                })
                .delay(100, TimeUnit.MILLISECONDS)
                .map(integer -> {
                    for (int i = 0; i < hCount; i++) {
                        mCells[i][integer].had = true;
                    }
                    mView.postInvalidate();
                    return integer;
                })
                .toList()
                .map(arg0 -> {
                    doClear1();
                    return arg0;
                })
                .subscribe();
    }
}
