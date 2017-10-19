package me.gavin.game.tetris.core;

import android.graphics.Point;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.core.shape.Shape;

/**
 * TetrisBaseControl
 *
 * @author gavin.xiong 2017/10/19
 */
public abstract class TetrisControl {

    final int hCount = Config.HORIZONTAL_COUNT;
    final int vCount = Config.VERTICAL_COUNT;
    float space = 2.5f;

    Cell[][] mCells;
    Shape mShape;

    TetrisView mView;

    private Disposable mTimerDisposable;

    TetrisCallback mCallback;

    List<Integer> mClearLines = new ArrayList<>();

    boolean isOver;

    boolean operateFlag;

    TetrisControl(TetrisView view, @NonNull TetrisCallback callback) {
        this.mView = view;
        this.mCallback = callback;
        view.setControl(this);
        mCells = new Cell[hCount][vCount];
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                mCells[i][j] = new Cell();
            }
        }
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
        initInterval();
    }

    /**
     * 触底
     */
    abstract void onSole();

    private void initInterval() {
        Observable.interval(Config.SPEED, Config.SPEED, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    cancelInterval();
                    mTimerDisposable = disposable;
                })
                .subscribe(arg0 -> tryMoveDown());
    }

    private void cancelInterval() {
        if (mTimerDisposable != null && !mTimerDisposable.isDisposed()) {
            mTimerDisposable.dispose();
        }
    }

    // TODO: 2017/10/17 忽略过滤掉密集操作
    private void tryMoveDown() {
        if (isOver) return;

        mShape.preMove(false, 1);

        int state = 0; // 正常状态
        for (Point point : mShape.prePoints) {
            if (point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                cancelInterval();
                state = 1; // 触底
                break;
            }
        }
        if (state == 0) {
            mShape.move(false, 1);
            mView.postInvalidate();
        } else {
            onSole();
        }
    }

    public void onLeft() {
        mShape.preMove(true, -1);
        for (Point point : mShape.prePoints) {
            if (point.x < 0 || point.y >= 0 && mCells[point.x][point.y].had) {
                mShape.preBack();
                return;
            }
        }
        mShape.move(true, -1);
        mView.postInvalidate();
    }

    public void onRight() {
        mShape.preMove(true, 1);
        for (Point point : mShape.prePoints) {
            if (point.x >= hCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                mShape.preBack();
                return;
            }
        }
        mShape.move(true, 1);
        mView.postInvalidate();
    }

    public void onUp() {
        onRotate();
    }

    public void onDown() {
        tryMoveDown();
    }

    public void onDrop() {
        int diff = 0;
        for (int i = 1; i <= vCount + 1; i++) { // I 旋转后可下落距离大于 vCount
            mShape.preMove(false, i);
            for (Point point : mShape.prePoints) {
                if (point.y >= vCount || point.y >= 0 && mCells[point.x][point.y].had) {
                    diff = i - 1;
                    break;
                }
            }
            if (diff > 0) {
                cancelInterval();
                mShape.move(false, diff);
                onSole();
                return;
            }
        }
    }

    public void onRotate() {
        mShape.preRotate();
        for (Point point : mShape.prePoints) {
            if (point.x < 0 || point.x >= hCount || point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                mShape.preBack();
                return;
            }
        }
        mShape.rotate();
        mView.postInvalidate();
    }

    public void onPause() {
        // TODO: 2017/10/18
    }

    public void dispose() {
        mTimerDisposable.dispose();
        // TODO: 2017/10/18
    }

    public boolean isDisposed() {
        // TODO: 2017/10/18
        return false;
    }
}
