package me.gavin.game.tetris.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.game.tetris.util.DisplayUtil;

/**
 * TetrisView
 *
 * @author gavin.xiong 2017/10/19
 */
public class TetrisView extends View {

    private Control mControl;

    private Paint mPaint, mHadPaint;

    public TetrisView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        setKeepScreenOn(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0x20444444);
        mHadPaint = new Paint();
        mPaint.setAntiAlias(true);
        mHadPaint.setColor(0xFF222222);
    }

    public void setControl(Control control) {
        this.mControl = control;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = Math.min(DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight() / 2);
        setMeasuredDimension(defaultSize / 2, defaultSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mControl != null) {
            mControl.space = h * 0.08f / mControl.vCount;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInEditMode() && mControl != null) {
            drawCell(canvas, getWidth() * 1f / mControl.hCount);
            if (mControl.mShapes != null && mControl.mShapes[0] != null) {
                drawShape(canvas, getWidth() * 1f / mControl.hCount);
            }
        }
    }

    public void drawCell(Canvas canvas, float cellWidth) {
        for (int i = 0; i < mControl.vCount; i++) {
            for (int j = 0; j < mControl.hCount; j++) {
                canvas.drawRect(
                        cellWidth * j + mControl.space,
                        cellWidth * i + mControl.space,
                        cellWidth * j + cellWidth - mControl.space,
                        cellWidth * i + cellWidth - mControl.space,
                        mControl.mCells[i][j].had ? mHadPaint : mPaint);
            }
        }
    }

    public void drawShape(Canvas canvas, float cellWidth) {
        for (Point point : mControl.mShapes[0].points) {
            canvas.drawRect(
                    cellWidth * point.x + mControl.space,
                    cellWidth * point.y + mControl.space,
                    cellWidth * point.x + cellWidth - mControl.space,
                    cellWidth * point.y + cellWidth - mControl.space,
                    mHadPaint);
        }
    }
}
