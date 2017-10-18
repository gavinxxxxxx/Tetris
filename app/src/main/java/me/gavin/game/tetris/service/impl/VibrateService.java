package me.gavin.game.tetris.service.impl;

import io.reactivex.disposables.Disposable;

/**
 * 震动
 *
 * @author gavin.xiong 2017/10/18
 */
public interface VibrateService extends Disposable {

    void onStart();

    void onMove();

    void onRotate();

    void onDrop();

    void onClear();

    void onOver();

}
