package me.gavin.game.tetris.rocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.R;

/**
 * 虚拟摇杆
 *
 * @author gavin.xiong 2017/10/16
 */
public class RockerView extends View {

    private static int DEFAULT_AREA_RADIUS = 68;
    private static int DEFAULT_ROCKER_RADIUS = 24;

    private static int DEFAULT_AREA_COLOR = 0x11111111;
    private static int DEFAULT_ROCKER_COLOR = 0x22FFFFFF;

    private static int DEFAULT_REFRESH_DELAY = 250;
    private static int DEFAULT_REFRESH_CYCLE = 50;

    private Paint mPaint;

    private Point mAreaPosition;
    private Point mRockerPosition;

    private int mAreaRadius;
    private int mRockerRadius;

    private int mAreaColor;
    private int mRockerColor;

    private OnRockerDirectionListener mDirectionListener;
    public static final int EVENT_DIRECTION_CENTER = 0;
    public static final int EVENT_DIRECTION_LEFT = 1;
    public static final int EVENT_DIRECTION_RIGHT = 2;
    public static final int EVENT_DIRECTION_UP = 3;
    public static final int EVENT_DIRECTION_DOWN = 4;

    private int mLastDirectionEvent;

    private OnRockerActionListener mActionListener;
    public static final int EVENT_ACTION_DOWN = 0;
    public static final int EVENT_ACTION_UP = 1;

    private int mRefreshDelay = DEFAULT_REFRESH_DELAY;
    private int mRefreshCycle = DEFAULT_REFRESH_CYCLE;

    private Disposable mTimerDisposable;

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DEFAULT_AREA_RADIUS = dip2px(context, 75);
        DEFAULT_ROCKER_RADIUS = dip2px(context, 25);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RockerView);
        mAreaRadius = ta.getDimensionPixelOffset(R.styleable.RockerView_rocker_areaRadius, DEFAULT_AREA_RADIUS);
        mRockerRadius = ta.getDimensionPixelOffset(R.styleable.RockerView_rocker_rockerRadius, DEFAULT_ROCKER_RADIUS);
        mRefreshDelay = ta.getInteger(R.styleable.RockerView_rocker_refreshDelay, DEFAULT_REFRESH_DELAY);
        mRefreshCycle = ta.getInteger(R.styleable.RockerView_rocker_refreshCycle, DEFAULT_REFRESH_CYCLE);
        mAreaColor = ta.getColor(R.styleable.RockerView_rocker_areaBackground, DEFAULT_AREA_COLOR);
        mRockerColor = ta.getColor(R.styleable.RockerView_rocker_rockerBackground, DEFAULT_ROCKER_COLOR);
        ta.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setKeepScreenOn(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int defaultSize = (mAreaRadius + mRockerRadius) * 2;
        setMeasuredDimension(
                MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(widthMeasureSpec)
                        : defaultSize,
                MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
                        ? MeasureSpec.getSize(heightMeasureSpec)
                        : defaultSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mAreaPosition = new Point(w / 2, h / 2);
        mRockerPosition = new Point(mAreaPosition);
        int tempRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
        if (mAreaRadius == -1)
            mAreaRadius = (int) (tempRadius * 0.75);
        if (mRockerRadius == -1)
            mRockerRadius = (int) (tempRadius * 0.25);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawArea(canvas);
        drawRocker(canvas);
    }

    private void drawArea(Canvas canvas) {
        mPaint.setColor(mAreaColor);
        canvas.drawCircle(mAreaPosition.x, mAreaPosition.y, mAreaRadius, mPaint);
    }

    private void drawRocker(Canvas canvas) {
        mPaint.setColor(mRockerColor);
        canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRockerRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int distance = MathUtils.getDistance(mAreaPosition.x, mAreaPosition.y, event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (distance > mAreaRadius) {
                    return false;
                } else {
                    if (mActionListener != null) {
                        mActionListener.accept(EVENT_ACTION_DOWN);
                    }
                    mRockerPosition.set((int) event.getX(), (int) event.getY());
                    invalidate();
                    if (mDirectionListener != null) {
                        responseDirectionEvent();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (distance <= mAreaRadius) {
                    mRockerPosition.set((int) event.getX(), (int) event.getY());
                } else {
                    mRockerPosition = MathUtils.getPointByCutLength(mAreaPosition, new Point((int) event.getX(), (int) event.getY()), mAreaRadius);
                }
                invalidate();
                if (mDirectionListener != null) {
                    responseDirectionEvent();
                }
                break;
            case MotionEvent.ACTION_UP:
                cancelInterval();
                mRockerPosition.set(mAreaPosition.x, mAreaPosition.y);
                invalidate();
                mLastDirectionEvent = EVENT_DIRECTION_CENTER;
                if (mActionListener != null) {
                    mActionListener.accept(EVENT_ACTION_UP);
                }
                break;
        }
        return true;
    }

    private void responseDirectionEvent() {
        float distance = MathUtils.getDistance(mAreaPosition.x, mAreaPosition.y, mRockerPosition.x, mRockerPosition.y);
        int directionEvent = EVENT_DIRECTION_CENTER;
        if (distance > mRockerRadius / 3 * 2) {
            float radian = MathUtils.getRadian(mAreaPosition, new Point(mRockerPosition.x, mRockerPosition.y));
            int angle = getAngleConvert(radian);
            if (angle >= 315) {
                directionEvent = EVENT_DIRECTION_RIGHT;
            } else if (angle > 225) {
                directionEvent = EVENT_DIRECTION_DOWN;
            } else if (angle >= 135) {
                directionEvent = EVENT_DIRECTION_LEFT;
            } else if (angle > 45) {
                directionEvent = EVENT_DIRECTION_UP;
            } else if (angle >= 0) {
                directionEvent = EVENT_DIRECTION_RIGHT;
            }
        }
        if (directionEvent != mLastDirectionEvent) {
            if (directionEvent == EVENT_DIRECTION_CENTER) {
                cancelInterval();
                mDirectionListener.accept(EVENT_DIRECTION_CENTER);
            } else {
                mDirectionListener.accept(directionEvent);
                initInterval(directionEvent);
                mLastDirectionEvent = directionEvent;
            }
        }
    }

    private void initInterval(int directionEvent) {
        Observable.interval(mRefreshDelay, mRefreshCycle, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    cancelInterval();
                    mTimerDisposable = disposable;
                })
                .map(arg0 -> directionEvent)
                .subscribe(mDirectionListener::accept);
    }

    private void cancelInterval() {
        if (mTimerDisposable != null && !mTimerDisposable.isDisposed()) {
            mTimerDisposable.dispose();
        }
    }

    /**
     * 获取偏移角度 0-360°
     */
    private int getAngleConvert(float radian) {
        int tmp = (int) Math.round(radian / Math.PI * 180);
        if (tmp < 0) {
            return -tmp;
        } else {
            return 180 + (180 - tmp);
        }
    }

    public void setActionListener(OnRockerActionListener listener) {
        this.mActionListener = listener;
    }

    public void setDirectionListener(OnRockerDirectionListener listener) {
        this.mDirectionListener = listener;
    }
}
