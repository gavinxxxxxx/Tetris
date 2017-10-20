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
import me.gavin.game.tetris.next.Utils;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.SPUtil;

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
    Shape[] mShapes;

    TetrisView mView;

    private Disposable mTimerDisposable;

    TetrisCallback mCallback;

    List<Integer> mClearLines = new ArrayList<>();

    boolean isReady;

    TetrisControl(TetrisView view, @NonNull TetrisCallback callback, boolean isRestart) {
        this.mView = view;
        this.mCallback = callback;
        view.setControl(this);
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
        if (!isReady) return;

        mShapes[0].preMove(false, 1);

        int state = 0; // 正常状态
        for (Point point : mShapes[0].prePoints) {
            if (point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                cancelInterval();
                state = 1; // 触底
                break;
            }
        }
        if (state == 0) {
            mShapes[0].move(false, 1);
            mView.postInvalidate();
        } else {
            onSole();
        }
    }

    public void onLeft() {
        if (isReady) {
            mShapes[0].preMove(true, -1);
            for (Point point : mShapes[0].prePoints) {
                if (point.x < 0 || point.y >= 0 && mCells[point.x][point.y].had) {
                    mShapes[0].preBack();
                    return;
                }
            }
            mShapes[0].move(true, -1);
            mView.postInvalidate();
        }
    }

    public void onRight() {
        if (isReady) {
            mShapes[0].preMove(true, 1);
            for (Point point : mShapes[0].prePoints) {
                if (point.x >= hCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                    mShapes[0].preBack();
                    return;
                }
            }
            mShapes[0].move(true, 1);
            mView.postInvalidate();
        }
    }

    public void onUp() {
        if (isReady) {
            onRotate();
        }
    }

    public void onDown() {
        if (isReady) {
            tryMoveDown();
        }
    }

    public void onDrop() {
        if (isReady) {
            int diff = 0;
            for (int i = 1; i <= vCount + 1; i++) { // I 旋转后可下落距离大于 vCount
                mShapes[0].preMove(false, i);
                for (Point point : mShapes[0].prePoints) {
                    if (point.y >= vCount || point.y >= 0 && mCells[point.x][point.y].had) {
                        diff = i - 1;
                        break;
                    }
                }
                if (diff > 0) {
                    cancelInterval();
                    mShapes[0].move(false, diff);
                    onSole();
                    return;
                }
            }
        }
    }

    public void onRotate() {
        if (isReady) {
            mShapes[0].preRotate();
            for (Point point : mShapes[0].prePoints) {
                if (point.x < 0 || point.x >= hCount || point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                    mShapes[0].preBack();
                    return;
                }
            }
            mShapes[0].rotate();
            mView.postInvalidate();
        }
    }

    public void onPause() {
        isReady = false;
        cancelInterval();
    }

    public void onContinue() {
        isReady = true;
        initInterval();
    }

    public void onSavedInstanceState() {
        TetrisInstanceState state = new TetrisInstanceState();
        state.setCells(mCells);
        state.setShapes(mShapes);
        SPUtil.saveString(mView.getContext(), "instanceState", JsonUtil.toJson(state));
    }

    public void onRestoreInstanceState() {
        TetrisInstanceState state = JsonUtil.toObj(SPUtil.getString(mView.getContext(), "instanceState"), TetrisInstanceState.class);
        if (state != null) {
            mCells = state.getCells();
            mShapes = state.getShapes();
            // mCallback.onNextShape(mShapes[1], 0);
        } else {
            mCells = new Cell[hCount][vCount];
            for (int i = 0; i < hCount; i++) {
                for (int j = 0; j < vCount; j++) {
                    mCells[i][j] = new Cell();
                }
            }
            mShapes = new Shape[2];
            for (int i = 0; i < mShapes.length; i++) {
                mShapes[i] = Utils.nextShape();
            }
        }
        mView.postInvalidate();
    }

    public void dispose() {
        mTimerDisposable.dispose();
        // TODO: 2017/10/18
    }

    public boolean isDisposed() {
        // TODO: 2017/10/18
        return false;
    }

    public void ready(boolean isRestart) {
        if (isRestart) {
            SPUtil.saveString(mView.getContext(), "instanceState", null);
            mCells = new Cell[hCount][vCount];
            for (int i = 0; i < hCount; i++) {
                for (int j = 0; j < vCount; j++) {
                    mCells[i][j] = new Cell();
                }
            }
            mShapes = new Shape[2];
            for (int i = 0; i < mShapes.length; i++) {
                mShapes[i] = Utils.nextShape();
            }
        } else {
            onRestoreInstanceState();
        }
        mView.postInvalidate();
    }

    public void go() {
        onContinue();
    }
}
