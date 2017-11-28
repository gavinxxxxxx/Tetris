package me.gavin.widget.color.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Locale;

/**
 * HSLColorPicker
 *
 * @author gavin.xiong 2017/11/27
 */
public class HSLColorPicker extends View {

    private final int[] COLORS = new int[]{
            0xFFFF0000,
            0xFFFF00FF,
            0xFF0000FF,
            0xFF00FFFF,
            0xFF00FF00,
            0xFFFFFF00,
            0xFFFF0000,
    };

    private final static int TOUCH_FLAG_H = 1;
    private final static int TOUCH_FLAG_S = 2;
    private final static int TOUCH_FLAG_L = 3;
    private final static int TOUCH_FLAG_A = 4;

    private final float[] mHSL = {0f, 1f, 0.5f, 1f}, mHSL2 = {0f, 1f, 0.5f, 1f}, mHSLTemp = new float[mHSL.length];

    private float mHStoreWidth = DisplayUtil.dp2px(8), mLineWidth = DisplayUtil.dp2px(3);
    private float mCx, mCy, mOr, mIr;
    private int mLineHMargin, mLineVMargin = DisplayUtil.dp2px(32);

    private final Paint mHPaint, mSPaint, mLPaint, mAPaint, mCoverPaint, mPaint, mTextPaint;

    private boolean mWithAlpha = true;

    private int mTouchFlag;

    public HSLColorPicker(Context context) {
        this(context, null);
    }

    public HSLColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSLColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHPaint.setStyle(Paint.Style.STROKE);
        mHPaint.setStrokeCap(Paint.Cap.ROUND);
        mSPaint = new Paint(mHPaint);
        mLPaint = new Paint(mSPaint);
        mAPaint = new Paint(mSPaint);

        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint.setColor(0x40000000);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(ColorUtils.HSLToColor(mHSL));

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xFFFFFFFF);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = Math.min(width + mLineVMargin * (mWithAlpha ? 3 : 2), MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCx = w / 2f;
        mCy = (h - mLineVMargin * (mWithAlpha ? 3 : 2)) / 2f;
        mOr = Math.min(mCx, mCy) - mHStoreWidth * 2f + mHStoreWidth / 2f;
        mIr = mOr - mHStoreWidth;
        mLineHMargin = (int) (w / 2f - mOr + mLineWidth / 2f);
        mHPaint.setStrokeWidth(mHStoreWidth);
        mHPaint.setShader(new SweepGradient(mCx, mCy, COLORS, null));

        mSPaint.setStrokeWidth(mLineWidth);
        mLPaint.setStrokeWidth(mLineWidth);
        mAPaint.setStrokeWidth(mLineWidth);

        resetLinePaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // H 渐变色圆环
        canvas.drawCircle(mCx, mCy, mOr / 2f + mIr / 2f, mHPaint);
        //  圆形填充
        mPaint.setColor(ColorUtils.HSLToColor(mHSL) & ((Math.round(0xFF * mHSL[3]) << 24) + 0xFFFFFF));
        canvas.drawCircle(mCx, mCy, mIr + 0.5f, mPaint);
        // 文字居中
        mTextPaint.setTextSize(70f);
        int baseY = (int) (mCy - mTextPaint.descent() / 2 - mTextPaint.ascent() / 2);
        canvas.drawText(String.format("#%08X", mPaint.getColor()), mCx, baseY, mTextPaint);
        mTextPaint.setTextSize(30f);
        canvas.drawText(String.format(Locale.getDefault(), "%3d° %3d%% %3d%% %3d%%",
                Math.round(mHSL[0]), Math.round(mHSL[1] * 100),
                Math.round(mHSL[2] * 100), Math.round(mHSL[3] * 100)
        ), mCx, baseY + 60, mTextPaint);

        // 圆环选择状态
        mHSL2[0] = mHSL[0];
        mPaint.setColor(ColorUtils.HSLToColor(mHSL2));
        double radian = Math.toRadians(mHSL[0]);
        float x = (float) (mCx + (mOr + mIr) / 2 * Math.cos(radian));
        float y = (float) (mCy - (mOr + mIr) / 2 * Math.sin(radian));
        canvas.drawCircle(x, y, mHStoreWidth * 2f, mCoverPaint);
        canvas.drawCircle(x, y, mHStoreWidth, mPaint);

        // 线性调节器
        float a = mCy + mOr;
        canvas.drawLine(mLineHMargin, a + mLineVMargin, getMeasuredWidth() - mLineHMargin, a + mLineVMargin, mSPaint);
        canvas.drawCircle(mLineHMargin + mHSL[1] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin, mLineWidth * 2.5f, mCoverPaint);
        canvas.drawCircle(mLineHMargin + mHSL[1] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin, mLineWidth, mPaint);
        canvas.drawLine(mLineHMargin, a + mLineVMargin * 2, getMeasuredWidth() - mLineHMargin, a + mLineVMargin * 2, mLPaint);
        canvas.drawCircle(mLineHMargin + mHSL[2] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin * 2, mLineWidth * 2.5f, mCoverPaint);
        canvas.drawCircle(mLineHMargin + mHSL[2] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin * 2, mLineWidth, mPaint);
        if (mWithAlpha) {
            canvas.drawLine(mLineHMargin, a + mLineVMargin * 3, getMeasuredWidth() - mLineHMargin, a + mLineVMargin * 3, mAPaint);
            canvas.drawCircle(mLineHMargin + mHSL[3] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin * 3, mLineWidth * 2.5f, mCoverPaint);
            canvas.drawCircle(mLineHMargin + mHSL[3] * (getMeasuredWidth() - mLineHMargin * 2), a + mLineVMargin * 3, mLineWidth, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                double distance = Math.sqrt(Math.pow(mCx - event.getX(), 2) + Math.pow(mCy - event.getY(), 2));
                if (Math.abs(distance - (mOr + mIr) / 2f) < mHStoreWidth * 3) {
                    mTouchFlag = TOUCH_FLAG_H;
                    mHSL[0] = getAngle(event.getX() - mCx, event.getY() - mCy);
                    resetLinePaint();
                    invalidate();
                    return true;
                } else if (Math.abs(mCy + mOr + mLineVMargin - event.getY()) < mLineWidth * 3) {
                    mTouchFlag = TOUCH_FLAG_S;
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[1] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                    return true;
                } else if (Math.abs(mCy + mOr + mLineVMargin * 2 - event.getY()) < mLineWidth * 3) {
                    mTouchFlag = TOUCH_FLAG_L;
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[2] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                    return true;
                } else if (mWithAlpha && Math.abs(mCy + mOr + mLineVMargin * 3 - event.getY()) < mLineWidth * 3) {
                    mTouchFlag = TOUCH_FLAG_A;
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[3] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                    return true;
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (mTouchFlag == TOUCH_FLAG_H) {
                    mHSL[0] = getAngle(event.getX() - mCx, event.getY() - mCy);
                    resetLinePaint();
                    invalidate();
                } else if (mTouchFlag == TOUCH_FLAG_S) {
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[1] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                } else if (mTouchFlag == TOUCH_FLAG_L) {
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[2] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                } else if (mWithAlpha && mTouchFlag == TOUCH_FLAG_A) {
                    float progress = (event.getX() - mLineHMargin) / (getMeasuredWidth() - mLineHMargin * 2);
                    mHSL[3] = progress < 0 ? 0 : progress > 1 ? 1 : progress;
                    resetLinePaint();
                    invalidate();
                }
                return true;
            default:
                mTouchFlag = 0;
                return false;
        }
    }

    private void resetLinePaint() {
        System.arraycopy(mHSL, 0, mHSLTemp, 0, mHSL.length);
        mHSLTemp[1] = 0f;
        int startColor = ColorUtils.HSLToColor(mHSLTemp);
        mHSLTemp[1] = .5f;
        int cColor = ColorUtils.HSLToColor(mHSLTemp);
        mHSLTemp[1] = 1f;
        int endColor = ColorUtils.HSLToColor(mHSLTemp);
        mSPaint.setShader(new LinearGradient(mLineHMargin, 0, getMeasuredWidth() - mLineHMargin, 0,
                new int[]{startColor, cColor, endColor}, null, Shader.TileMode.CLAMP));

        System.arraycopy(mHSL, 0, mHSLTemp, 0, mHSL.length);
        mHSLTemp[2] = 0f;
        startColor = ColorUtils.HSLToColor(mHSLTemp);
        mHSLTemp[2] = .5f;
        cColor = ColorUtils.HSLToColor(mHSLTemp);
        mHSLTemp[2] = 1f;
        endColor = ColorUtils.HSLToColor(mHSLTemp);
        mLPaint.setShader(new LinearGradient(mLineHMargin, 0, getMeasuredWidth() - mLineHMargin, 0,
                new int[]{startColor, cColor, endColor}, null, Shader.TileMode.CLAMP));

        if (mWithAlpha) {
            System.arraycopy(mHSL, 0, mHSLTemp, 0, mHSL.length);
            int color = ColorUtils.HSLToColor(mHSLTemp);
            mAPaint.setShader(new LinearGradient(mLineHMargin, 0, getMeasuredWidth() - mLineHMargin, 0,
                    color & 0x00FFFFFF, color | 0xFF000000, Shader.TileMode.CLAMP));
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
