package me.gavin.game.tetris.service;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.service.impl.SoundService;

/**
 * 音效
 *
 * @author gavin.xiong 2017/10/18
 */
public class SoundManager implements SoundService {

    private final SoundPool mSoundPool;

    private boolean soundAble = false;

    public SoundManager(Context context) {
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build())
                .setMaxStreams(5)
                .build();
        final int[] sounds = {R.raw.effect_clear, R.raw.effect_drop, R.raw.effect_rotate, R.raw.effect_move, R.raw.start, R.raw.over};
        for (int sound : sounds) {
            mSoundPool.load(context, sound, 0);
        }
    }

    @Override
    public void onStart() {
        playSound(5);
    }

    @Override
    public void onMove() {
        playSound(4);
    }

    @Override
    public void onRotate() {
        playSound(3);
    }

    @Override
    public void onDrop() {
        playSound(2);
    }

    @Override
    public void onClear() {
        playSound(1);
    }

    @Override
    public void onOver() {
        playSound(6);
    }

    private void playSound(int index) {
        if (soundAble && mSoundPool != null) {
            mSoundPool.play(index, 1, 1, 0, 0, 1);
        }
    }

    @Override
    public void dispose() {
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    @Override
    public boolean isDisposed() {
        return mSoundPool == null;
    }
}
