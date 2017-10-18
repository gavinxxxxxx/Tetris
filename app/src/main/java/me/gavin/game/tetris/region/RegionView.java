package me.gavin.game.tetris.region;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.gavin.game.tetris.util.DisplayUtil;

/**
 * 区域
 *
 * @author gavin.xiong 2017/10/11
 */
public class RegionView extends View {

    private RegionViewModel vm;

    private Paint mPaint, mHadPaint;

    public RegionView(Context context, @Nullable AttributeSet attrs) {
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

    public void setViewModel(RegionViewModel viewModel) {
        this.vm = viewModel;
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
        if (vm != null) {
            vm.space = h * 0.125f / vm.vCount;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || vm == null || vm.mShapes[0] == null) {
            return;
        }
        drawCell(canvas, getWidth() * 1f / vm.hCount);
        drawShape(canvas, getWidth() * 1f / vm.hCount);
    }

    public void drawCell(Canvas canvas, float cellWidth) {
        for (int i = 0; i < vm.hCount; i++) {
            for (int j = 0; j < vm.vCount; j++) {
                canvas.drawRect(
                        cellWidth * i + vm.space,
                        cellWidth * j + vm.space,
                        cellWidth * i + cellWidth - vm.space,
                        cellWidth * j + cellWidth - vm.space,
                        vm.mCells[i][j].had ? mHadPaint : mPaint);
            }
        }
    }

    public void drawShape(Canvas canvas, float cellWidth) {
        for (Point point : vm.mShapes[0].points) {
            canvas.drawRect(
                    cellWidth * point.x + vm.space,
                    cellWidth * point.y + vm.space,
                    cellWidth * point.x + cellWidth - vm.space,
                    cellWidth * point.y + cellWidth - vm.space,
                    mHadPaint);
        }
    }
}
