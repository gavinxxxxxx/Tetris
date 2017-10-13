package me.gavin.game.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.model.Cell;
import me.gavin.game.tetris.model.shape.Shape;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/10/12
 */
public class ViewModel implements OnKeyPassListener {

    public final int hCount = Config.HORIZONTAL_COUNT;
    public final int vCount = Config.VERTICAL_COUNT;
    public final float space = 2.5f;

    public View regionView;

    public final Cell[][] cells;
    public Shape shape;

    private Paint mPaint, mHadPaint;

    private Disposable timerDisposable;

    public ViewModel(View regionView) {
        this.regionView = regionView;
        cells = new Cell[hCount][vCount];
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                cells[i][j] = new Cell();
            }
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0x40999999);
        mHadPaint = new Paint();
        mPaint.setAntiAlias(true);
        mHadPaint.setColor(0xFF222222);

        shape = Utils.nextShape();

        Observable.interval(Config.SPEED, Config.SPEED, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> timerDisposable = disposable)
                .subscribe(arg0 -> downTimeOut());
    }

    public void drawCell(Canvas canvas, float cellWidth) {
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < vCount; j++) {
                canvas.drawRect(
                        cellWidth * i + space,
                        cellWidth * j + space,
                        cellWidth * i + cellWidth - space,
                        cellWidth * j + cellWidth - space,
                        cells[i][j].had ? mHadPaint : mPaint);
            }
        }
    }

    public void drawShape(Canvas canvas, float cellWidth) {
        for (Point point : shape.points) {
            canvas.drawRect(
                    cellWidth * point.x + space,
                    cellWidth * point.y + space,
                    cellWidth * point.x + cellWidth - space,
                    cellWidth * point.y + cellWidth - space,
                    mHadPaint);
        }
    }

    public void downTimeOut() {
        if (shape == null) return;

        shape.preMove(false, 1);

        int state = 0; // 正常状态
        for (Point point : shape.prePoints) {
            if (point.y >= vCount || point.x >= 0 && point.y >= 0 && cells[point.x][point.y].had) {
                state = 1; // 触底
                break;
            }
        }
        if (state != 0) {
            for (Point point : shape.prePoints) {
                if (point.y <= 0) {
                    timerDisposable.dispose();
                    Toast.makeText(regionView.getContext(), "GAME OVER", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
        if (state == 1) {
            for (Point point : shape.points) {
                cells[point.x][point.y].had = true;
            }

            boolean clearFlag = false;
            Arrays.sort(shape.points, ((o1, o2) -> o1.y > o2.y ? -1 : 1));
            for (Point point : shape.points) {
                boolean hadFlag = true;
                for (int i = 0; i < hCount; i++) {
                    if (!cells[i][point.y].had) {
                        hadFlag = false;
                        break;
                    }
                }
                if (hadFlag) {
                    clearFlag = true;
                    for (int j = point.y; j > 0; j--) {
                        for (int i = 0; i < hCount; i++) {
                            cells[i][j] = cells[i][j - 1];
                        }
                    }
                    for (int i = 0; i < hCount; i++) {
                        cells[i][0] = new Cell();
                    }
                    for (Point p : shape.points) {
                        p.y++;
                    }
                }
            }

            shape = Utils.nextShape();

            if (clearFlag) {
                regionView.postInvalidate();
            }

            return;
        }

        shape.move(false, 1);
        regionView.postInvalidate();
    }

    @Override
    public void onLeft() {
        shape.preMove(true, -1);
        for (Point point : shape.prePoints) {
            if (point.x < 0 || point.y >= 0 && cells[point.x][point.y].had) {
                shape.preBack();
                return;
            }
        }
        shape.move(true, -1);
        regionView.postInvalidate();
    }

    @Override
    public void onRight() {
        shape.preMove(true, 1);
        for (Point point : shape.prePoints) {
            if (point.x >= hCount || point.x >= 0 && point.y >= 0 && cells[point.x][point.y].had) {
                shape.preBack();
                return;
            }
        }
        shape.move(true, 1);
        regionView.postInvalidate();
    }

    @Override
    public void onUp() {
        // do nothing
    }

    @Override
    public void onDown() {
        downTimeOut();
    }

    @Override
    public void onRotate() {
        shape.preRotate();
        for (Point point : shape.prePoints) {
            if (point.x < 0 || point.x >= hCount || point.y >= vCount || point.x >= 0 && point.y >= 0 && cells[point.x][point.y].had) {
                shape.preBack();
                return;
            }
        }
        shape.rotate();
        regionView.postInvalidate();
    }

    @Override
    public void onPause() {

    }

}
