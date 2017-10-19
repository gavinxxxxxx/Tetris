package me.gavin.game.tetris.next;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.util.DisplayUtil;

/**
 * 等候区域
 *
 * @author gavin.xiong 2017/10/11
 */
public class NextView extends View {

    private Shape mShape;
    public float space;

    private Paint mPaint, mHadPaint;

    public NextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0x20444444);
        mHadPaint = new Paint();
        mPaint.setAntiAlias(true);
        mHadPaint.setColor(0xFF222222);
    }

    public void setShape(Shape shape) {
        this.mShape = shape;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = Math.min(DisplayUtil.getScreenWidth() / 4, DisplayUtil.dp2px(48));
        space = defaultSize * 0.08f / 4;
        setMeasuredDimension(defaultSize, defaultSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || mShape == null) {
            return;
        }
        drawCell(canvas, getWidth() / 4);
        drawShape(canvas, getWidth() / 4);
    }

    public void drawCell(Canvas canvas, float cellWidth) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                canvas.drawRect(
                        cellWidth * i + space,
                        cellWidth * j + space,
                        cellWidth * i + cellWidth - space,
                        cellWidth * j + cellWidth - space,
                        mPaint);
            }
        }
    }

    public void drawShape(Canvas canvas, float cellWidth) {
        for (Point point : mShape.points) {
            canvas.drawRect(
                    cellWidth * (point.x - 3) + space,
                    cellWidth * (point.y + 3) + space,
                    cellWidth * (point.x - 3) + cellWidth - space,
                    cellWidth * (point.y + 3) + cellWidth - space,
                    mHadPaint);
        }
    }
}
