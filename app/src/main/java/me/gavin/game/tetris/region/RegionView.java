package me.gavin.game.tetris.region;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.shape.Shape;
import me.gavin.game.tetris.util.DisplayUtil;
import me.gavin.game.tetris.util.RxBus;
import me.gavin.game.tetris.util.Utils;

/**
 * 区域
 *
 * @author gavin.xiong 2017/10/11
 */
public class RegionView extends View implements Runnable, OnKeyPassListener {

    public final int hCount = Config.HORIZONTAL_COUNT;
    public final int vCount = Config.VERTICAL_COUNT;
    public float space = 2.5f;

    public Cell[][] mCells;
    public final Shape[] mShapes = new Shape[2];

    private Paint mPaint, mHadPaint;

    private boolean isOver;

    private Disposable mTimerDisposable;

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        setKeepScreenOn(true);

        mCells = new Cell[hCount][vCount];
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                mCells[i][j] = new Cell();
            }
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0x20444444);
        mHadPaint = new Paint();
        mPaint.setAntiAlias(true);
        mHadPaint.setColor(0xFF222222);

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
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = Math.min(DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight() / 2);
        space = defaultSize * 0.125f / vCount;
        setMeasuredDimension(defaultSize / 2, defaultSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || mShapes[0] == null) {
            return;
        }
        drawCell(canvas, getWidth() * 1f / hCount);
        drawShape(canvas, getWidth() * 1f / hCount);
    }

    public void drawCell(Canvas canvas, float cellWidth) {
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                canvas.drawRect(
                        cellWidth * i + space,
                        cellWidth * j + space,
                        cellWidth * i + cellWidth - space,
                        cellWidth * j + cellWidth - space,
                        mCells[i][j].had ? mHadPaint : mPaint);
            }
        }
    }

    public void drawShape(Canvas canvas, float cellWidth) {
        for (Point point : mShapes[0].points) {
            canvas.drawRect(
                    cellWidth * point.x + space,
                    cellWidth * point.y + space,
                    cellWidth * point.x + cellWidth - space,
                    cellWidth * point.y + cellWidth - space,
                    mHadPaint);
        }
    }

    // TODO: 2017/10/17 忽略过滤掉密集操作
    @Override
    public void run() {
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
        if (state == 1) {
            for (Point point : mShapes[0].prePoints) {
                if (point.y <= 0) { // game over
                    isOver = true;
                    Toast.makeText(getContext(), "GAME OVER", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        if (state == 1) { // 触底未结束
            for (Point point : mShapes[0].points) {
                mCells[point.x][point.y].had = true;
            }

            boolean clearFlag = false;

            SparseArray<Point> sparseArray = new SparseArray<>();
            for (Point point : mShapes[0].points) {
                sparseArray.append(point.y, point);
            }
            Point[] temp = new Point[sparseArray.size()];
            for (int i = 0; i < sparseArray.size(); i++) {
                temp[i] = sparseArray.valueAt(i);
            }
            Arrays.sort(temp, ((o1, o2) -> o1.y > o2.y ? -1 : 1));

            for (Point point : temp) {
                boolean hadFlag = true;
                for (int i = 0; i < hCount; i++) {
                    if (!mCells[i][point.y].had) {
                        hadFlag = false;
                        break;
                    }
                }
                if (hadFlag) {
                    clearFlag = true;
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
            initInterval();

            if (clearFlag) {
                postInvalidate();
            }

            return;
        }

        mShapes[0].move(false, 1);
        postInvalidate();
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
        postInvalidate();
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
        postInvalidate();
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
    public void onRotate() {
        mShapes[0].preRotate();
        for (Point point : mShapes[0].prePoints) {
            if (point.x < 0 || point.x >= hCount || point.y >= vCount || point.x >= 0 && point.y >= 0 && mCells[point.x][point.y].had) {
                mShapes[0].preBack();
                return;
            }
        }
        mShapes[0].rotate();
        postInvalidate();
    }

    @Override
    public void onPause() {

    }
}
