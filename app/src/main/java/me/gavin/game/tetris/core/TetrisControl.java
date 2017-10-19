package me.gavin.game.tetris.core;

import android.graphics.Point;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.next.NextShapeEvent;
import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.util.RxBus;
import me.gavin.game.tetris.next.Utils;

/**
 * Tetris ViewModel
 *
 * @author gavin.xiong 2017/10/19
 */
public class TetrisControl implements OnKeyPassListener, Disposable {

    final int hCount = Config.HORIZONTAL_COUNT;
    final int vCount = Config.VERTICAL_COUNT;
    float space = 2.5f;

    Cell[][] mCells;
    final Shape[] mShapes = new Shape[2];

    private TetrisView mView;

    private int multiple = 1;
    private long score = 0;

    private Disposable mTimerDisposable;

    private boolean isOver;

    public TetrisControl(TetrisView view) {
        this.mView = view;
        view.setControl(this);
        mCells = new Cell[hCount][vCount];
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                mCells[i][j] = new Cell();
            }
        }
    }

    @Override
    public void onLeft() {
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

    @Override
    public void onRight() {
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

    @Override
    public void onUp() {
        // do nothing
    }

    @Override
    public void onDown() {
        run();
    }

    @Override
    public void onDrop() {
        int diff = 0;
        for (int i = 1; i < vCount; i++) {
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

    @Override
    public void onRotate() {
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

    @Override
    public void onPause() {
        // TODO: 2017/10/18
    }

    @Override
    public void dispose() {
        mTimerDisposable.dispose();
        // TODO: 2017/10/18
    }

    @Override
    public boolean isDisposed() {
        // TODO: 2017/10/18
        return false;
    }


    public void gameStart() {
        for (int i = 0; i < mShapes.length; i++) {
            mShapes[i] = Utils.nextShape();
        }
        RxBus.get().post(new NextShapeEvent(mShapes[1]));
        initInterval();
    }

    private void initInterval() {
        Observable.interval(Config.SPEED, Config.SPEED, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    cancelInterval();
                    mTimerDisposable = disposable;
                })
                .subscribe(arg0 -> run());
    }

    private void cancelInterval() {
        if (mTimerDisposable != null && !mTimerDisposable.isDisposed()) {
            mTimerDisposable.dispose();
        }
    }

    // TODO: 2017/10/17 忽略过滤掉密集操作
    private void run() {
        if (isOver) return;

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

    /**
     * 触底
     */
    private void onSole() {
        for (Point point : mShapes[0].prePoints) {
            if (point.y <= 0) { // game over
                isOver = true;
                Toast.makeText(mView.getContext(), "GAME OVER", Toast.LENGTH_LONG).show();
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

        int clearFlag = 0;
        for (Point point : temp) {
            boolean hadFlag = true;
            for (int i = 0; i < hCount; i++) {
                if (!mCells[i][point.y].had) {
                    hadFlag = false;
                    break;
                }
            }
            if (hadFlag) {
                clearFlag++;
                multiple *= clearFlag;
                score += multiple;
                RxBus.get().post(new ScoreChangeEvent(score));
                multiple *= 2;
                // TODO: 2017/10/17 arrayCopy
                for (int j = point.y; j > 0; j--) {
                    for (int i = 0; i < hCount; i++) {
                        mCells[i][j] = mCells[i][j - 1];
                    }
                }
                for (int i = 0; i < hCount; i++) {
                    mCells[i][0] = new Cell();
                }
                for (Point p : temp) {
                    if (p.y <= point.y) {
                        p.y++;
                    }
                }
            }
        }

        System.arraycopy(mShapes, 1, mShapes, 0, mShapes.length - 1);
        mShapes[mShapes.length - 1] = Utils.nextShape();
        RxBus.get().post(new NextShapeEvent(mShapes[1]));
        if (clearFlag > 0) {
            mView.postInvalidate();
        } else {
            multiple = 1; // 触底未消除，清空倍数
            mView.postInvalidate();
        }
        initInterval();
    }
}
