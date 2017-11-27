package me.gavin.widget.color.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * HuePicker
 *
 * @author gavin.xiong 2017/11/27
 */
public class HuePicker extends View {

    private final int[] COLORS = new int[]{
            0xFFFF0000,
            0xFFFF00FF,
            0xFF0000FF,
            0xFF00FFFF,
            0xFF00FF00,
            0xFFFFFF00,
            0xFFFF0000,
    };

    private final Paint mHuePaint, mPaint;

    private float cx, cy, or, ir;

    private final float[] mHSL = {0f, 1f, 0.5f};

    private float mRadians = 0f;

    public HuePicker(Context context) {
        this(context, null);
    }

    public HuePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHuePaint.setStyle(Paint.Style.STROKE);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ColorUtils.HSLToColor(mHSL));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cx = w / 2f;
        cy = h / 2f;
        or = Math.min(cx, cy) - 128f;
        ir = or - 24f;
        mHuePaint.setStrokeWidth(or - ir);
        mHuePaint.setShader(new SweepGradient(cx, cy, COLORS, null));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, or / 2f + ir / 2f, mHuePaint);

        mPaint.setColor(ColorUtils.HSLToColor(mHSL));
        canvas.drawCircle(cx, cy, ir, mPaint);

        double radian = Math.toRadians(mHSL[0]);
        float x = (float) (cx + (or + ir) / 2 * Math.cos(radian));
        float y = (float) (cy - (or + ir) / 2 * Math.sin(radian));
        canvas.drawCircle(x, y, 48f, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                double distance = Math.sqrt(Math.pow(cx - event.getX(), 2) + Math.pow(cy - event.getY(), 2));
                if (ir - 20 < distance && distance < or + 20) {
                    mHSL[0] = getAngle(event.getX() - cx, event.getY() - cy);
                    invalidate();
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                mHSL[0] = getAngle(event.getX() - cx, event.getY() - cy);
                invalidate();
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取偏移角度
     *
     * @param dx end.x - start.x
     * @param dy end.y - start.y
     * @return [0, 360)
     */
    private int getAngle(float dx, float dy) {
        double radian = Math.atan2(dy, dx);
        double degrees = Math.toDegrees(radian);
        int angle = (int) Math.round(degrees);
        return angle > 0 ? 360 - angle : -angle;
    }
}
