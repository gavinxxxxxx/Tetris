package me.gavin.game.tetris.core;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.game.tetris.next.Utils;

/**
 * Tetris ViewModel
 *
 * @author gavin.xiong 2017/10/19
 */
public class TetrisSoleControl extends TetrisControl {

    public TetrisSoleControl(TetrisView view, @NonNull TetrisCallback callback, boolean isRestart) {
        super(view, callback, isRestart);
    }

    @Override
    void onSole() {
        isReady = false;
        for (Point point : mShapes[0].prePoints) {
            if (point.y <= 0) { // game over
                isReady = false;
                mSoundService.onOver();
                mVibrateService.onOver();
                mCallback.onOver();
                return;
            }
        }

        // 触底未结束
        for (Point point : mShapes[0].points) {
            mCells[point.x][point.y].had = true;
        }

        SparseArray<Point> sparseArray = new SparseArray<>();
        for (Point point : mShapes[0].points) {
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
            doClear();
            mScoreService.onClear(mClearLines.size());
            mCallback.onClear(mClearLines.size());
        } else {
            toNext();
        }
    }

    private void toNext() {
        mView.postInvalidate();
        System.arraycopy(mShapes, 1, mShapes, 0, mShapes.length - 1);
        mShapes[mShapes.length - 1] = Utils.nextShape();
        mScoreService.onNextShape(mClearLines.size());
        mCallback.onNextShape(mShapes[1], mClearLines.size());
        onStartTimer();
    }

    private void doClear() {
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
                .map(arg0 -> mClearLines)
                .flatMapObservable(Observable::fromIterable)
                .map(y -> {
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
                    mSoundService.onClear();
                    mVibrateService.onClear();
                    mCallback.onClear();
                    return 0;
                })
                .toList()
                .subscribe(arg0 -> toNext());
    }
}
