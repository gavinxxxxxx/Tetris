package me.gavin.widget.color.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
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

    private final Paint mHuePaint, mSaturationPaint, mLightnessPaint, mAlphaPaint, mPaint;

    private float cx, cy, or, ir;

    private final float[] mHSL = {0f, 1f, 0.5f}, mHSLCopyS = new float[3];

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

        mSaturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSaturationPaint.setStyle(Paint.Style.STROKE);

        mLightnessPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLightnessPaint.setStyle(Paint.Style.STROKE);

        mAlphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAlphaPaint.setStyle(Paint.Style.STROKE);

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

        System.arraycopy(mHSL, 0, mHSLCopyS, 0, 3);
        mHSLCopyS[1] = 0f;
        int startColor = ColorUtils.HSLToColor(mHSLCopyS);
        mHSLCopyS[1] = .5f;
        int cColor = ColorUtils.HSLToColor(mHSLCopyS);
        mHSLCopyS[1] = 1f;
        int endColor = ColorUtils.HSLToColor(mHSLCopyS);
        mSaturationPaint.setStrokeWidth(24);
        mSaturationPaint.setShader(new LinearGradient(38, 0, w - 38, 0,
                new int[]{startColor, cColor, endColor}, null, Shader.TileMode.REPEAT));

        System.arraycopy(mHSL, 0, mHSLCopyS, 0, 3);
        mHSLCopyS[2] = 0f;
        startColor = ColorUtils.HSLToColor(mHSLCopyS);
        mHSLCopyS[2] = .5f;
        cColor = ColorUtils.HSLToColor(mHSLCopyS);
        mHSLCopyS[2] = 1f;
        endColor = ColorUtils.HSLToColor(mHSLCopyS);
        mLightnessPaint.setStrokeWidth(24);
        mLightnessPaint.setShader(new LinearGradient(38, 0, w - 38, 0,
                new int[]{startColor, cColor, endColor}, null, Shader.TileMode.REPEAT));

        System.arraycopy(mHSL, 0, mHSLCopyS, 0, 3);
        int color = ColorUtils.HSLToColor(mHSLCopyS);
        mAlphaPaint.setStrokeWidth(24);
        mAlphaPaint.setShader(new LinearGradient(38, 0, w - 38, 0,
                color & 0x00FFFFFF, color | 0xFF000000, Shader.TileMode.REPEAT));

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

        canvas.drawLine(32, 1800, getWidth() - 32, 1800, mSaturationPaint);
        canvas.drawLine(32, 1900, getWidth() - 32, 1900, mLightnessPaint);
        canvas.drawLine(32, 2000, getWidth() - 32, 2000, mAlphaPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                double distance = Math.sqrt(Math.pow(cx - event.getX(), 2) + Math.pow(cy - event.getY(), 2));
                if (ir - 32 < distance && distance < or + 32) {
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
