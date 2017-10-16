package me.gavin.game.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 区域
 *
 * @author gavin.xiong 2017/10/11
 */
public class RegionView extends View {

    public ViewModel vm;

    public RegionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setKeepScreenOn(true);
    }

    public void setViewModel(ViewModel mViewModel) {
        this.vm = mViewModel;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : 400,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : 800);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode() || vm == null) {
            return;
        }
        vm.drawCell(canvas, getWidth() * 1f / vm.hCount);
        vm.drawShape(canvas, getWidth() * 1f / vm.hCount);
    }
}
