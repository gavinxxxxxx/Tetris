package me.gavin.game.tetris.core;

import android.content.Context;
import android.graphics.Point;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.effect.ScoreManager;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.next.Utils;
import me.gavin.game.tetris.shape.Shape;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.SPUtil;

public abstract class Control {

    int hCount;
    int vCount;
    float space;

    Cell[][] mCells;
    Shape[] mShapes;

    private Context mContext;

    TetrisView mView;

    private Disposable mTimerDisposable;

    TetrisCallback mCallback;

    List<Integer> mEliminableLines = new ArrayList<>();

    private boolean isReady;
    boolean isRunning;
    boolean isOver;

    Control(TetrisView view, TetrisCallback callback, boolean isContinue) {
        this.mView = view;
        this.mContext = view.getContext();
        this.mCallback = callback;
        init(isContinue);
        view.setControl(this);
    }

    private void init(boolean isContinue) {
        Save save = JsonUtil.toObj(SPUtil.getString(BundleKey.SAVE, BundleKey.SAVE), Save.class);
        if (isContinue && save != null) {
            hCount = save.gethCount();
            vCount = save.getvCount();
            mCallback.onScoreChange();
            mCells = save.getCells();
            mShapes = new Shape[save.getShapes().length];
            for (int i = 0; i < mShapes.length; i++) {
                mShapes[i] = Utils.fromShape(save.getShapes()[i]);
            }
            ScoreManager.get().reset(save.getcCount(), save.getScore(), save.getMultiple());
        } else {
            hCount = Integer.parseInt(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(mContext.getString(R.string.ground_horizontal_count), "10"));
            vCount = Integer.parseInt(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(mContext.getString(R.string.ground_vertical_count), "20"));
            mCells = new Cell[vCount][hCount];
            for (int y = 0; y < vCount; y++) {
                for (int x = 0; x < hCount; x++) {
                    mCells[y][x] = new Cell();
                }
            }
            int nCount = Integer.parseInt(PreferenceManager
                    .getDefaultSharedPreferences(mContext)
                    .getString(mContext.getString(R.string.next_shape_count), "2"));
            mShapes = new Shape[nCount];
            for (int i = 0; i < mShapes.length; i++) {
                mShapes[i] = Utils.nextShape();
            }
            ScoreManager.get().reset(0, 0, 1);
        }
        space = PreferenceManager
                .getDefaultSharedPreferences(mContext)
                .getFloat(mContext.getString(R.string.ground_shape_space), Config.DEFAULT_GROUND_SHAPE_SPACE);
        mCallback.onNextShape(mShapes[1]);
    }

    public void onLeft() {
        if (isReady && !isRunning) {
            isRunning = true;
            SoundManager.get().onMove();
            mShapes[0].preMove(-1, 0);
            for (Point point : mShapes[0].prePoints) {
                if (point.x < 0 || point.y >= 0 && mCells[point.y][point.x].had) {
                    mShapes[0].preBack();
                    isRunning = false;
                    return;
                }
            }
            mShapes[0].move(-1, 0);
            mView.postInvalidate();
            isRunning = false;
        }
    }

    public void onRight() {
        if (isReady && !isRunning) {
            isRunning = true;
            SoundManager.get().onMove();
            mShapes[0].preMove(1, 0);
            for (Point point : mShapes[0].prePoints) {
                if (point.x >= hCount || point.y >= 0 && mCells[point.y][point.x].had) {
                    mShapes[0].preBack();
                    isRunning = false;
                    return;
                }
            }
            mShapes[0].move(1, 0);
            mView.postInvalidate();
            isRunning = false;
        }
    }

    public void onUp() {
        // onRotate();
    }

    public void onDown() {
        // TODO: 2017/10/28
        if (isReady && !isRunning) {
            // isRunning = true;
            SoundManager.get().onMove();
            tryMoveDown();
            // isRunning = false;
        }
    }

    public void onDrop() {
        if (isReady && !isRunning) {
            isRunning = true;
            SoundManager.get().onDrop();
            boolean flag = false;
            int dy = 0;
            for (int y = 1; y <= vCount + 1; y++) { // I 旋转后可下落距离大于 vCount
                mShapes[0].preMove(0, y);
                for (Point point : mShapes[0].prePoints) {
                    if (point.y >= vCount || point.y >= 0 && mCells[point.y][point.x].had) {
                        flag = true;
                        dy = y - 1;
                        break;
                    }
                }
                if (flag) {
                    cancelInterval();
                    ScoreManager.get().onDrop();
                    mCallback.onScoreChange();
                    mShapes[0].move(0, dy);
                    onLand();
                    return;
                }
            }
        }
    }

    public void onRotate() {
        if (isReady && !isRunning) {
            isRunning = true;
            SoundManager.get().onRotate();
            mShapes[0].preRotate();
            for (Point point : mShapes[0].prePoints) {
                if (point.x < 0 || point.x >= hCount || point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.y][point.x].had) {
                    mShapes[0].preBack();
                    isRunning = false;
                    return;
                }
            }
            mShapes[0].rotate();
            mView.postInvalidate();
            isRunning = false;
        }
    }

    abstract void onLand();

    void onOver() {
        isReady = false;
        isRunning = false;
        SoundManager.get().onOver();
        mCallback.onOver();
    }

    public void onSave() {
        if (isOver) {
            SPUtil.saveString(BundleKey.SAVE, BundleKey.SAVE, "");
        } else {
            Save save = new Save();
            save.sethCount(hCount);
            save.setvCount(vCount);
            save.setcCount(ScoreManager.get().getLineCount());
            save.setScore(ScoreManager.get().getScore());
            save.setMultiple(ScoreManager.get().getMultiple());
            save.setCells(mCells);
            save.setShapes(mShapes);
            SPUtil.saveString(BundleKey.SAVE, BundleKey.SAVE, JsonUtil.toJson(save));
        }
    }

    public void onPause() {
        isReady = false;
        cancelInterval();
    }

    public void onStart() {
        isReady = true;
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

    public void onDispose() {
        // TODO: 2017/10/28
    }

    private void tryMoveDown() {
        if (isReady && !isRunning) {
            isRunning = true;
            mShapes[0].preMove(0, 1);
            int state = 0; // 正常状态
            for (Point point : mShapes[0].prePoints) {
                if (point.y >= vCount || point.y >= 0 && mCells[point.y][point.x].had) {
                    cancelInterval();
                    state = 1; // 触底
                    break;
                }
            }
            if (state == 0) {
                mShapes[0].move(0, 1);
                mView.postInvalidate();
                isRunning = false;
            } else {
                onLand();
            }
        }
    }
}
