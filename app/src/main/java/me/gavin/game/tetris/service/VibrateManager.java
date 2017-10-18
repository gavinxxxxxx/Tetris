package me.gavin.game.tetris.service;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.service.impl.VibrateService;

/**
 * 震动
 *
 * @author gavin.xiong 2017/10/18
 */
public class VibrateManager implements VibrateService {

    private Vibrator mVibrator;
    private final long[] pattern = {5, 5, 5, 5};

    private CompositeDisposable mCompositeDisposable;

    private boolean vibrateAble = false;

    public VibrateManager(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStart() {
        // do nothing
    }

    @Override
    public void onMove() {
        vibrate();
    }

    @Override
    public void onRotate() {
        vibrate();
    }

    @Override
    public void onDrop() {
        vibrate();
    }

    @Override
    public void onClear() {
        // do nothing
    }

    @Override
    public void onOver() {
        // do nothing
    }

    private void vibrate() {
        Observable.just(vibrateAble)
                .filter(Boolean::booleanValue)
                .observeOn(Schedulers.io())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(arg0 -> {
                    if (Build.VERSION.SDK_INT >= 26) {
                        mVibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    } else {
                        mVibrator.vibrate(pattern, -1);
                    }
                });
    }

    @Override
    public void dispose() {
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        mCompositeDisposable.dispose();
    }

    @Override
    public boolean isDisposed() {
        return mVibrator == null;
    }
}
