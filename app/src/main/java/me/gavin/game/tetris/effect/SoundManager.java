package me.gavin.game.tetris.effect;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.app.App;

/**
 * 音效
 *
 * @author gavin.xiong 2017/10/18
 */
public class SoundManager {

    private SoundPool mSoundPool;
    private boolean enable = true;

    private final int[] sounds = {
            R.raw.effect_clear,
            R.raw.effect_drop,
            R.raw.effect_rotate,
            R.raw.effect_move,
            R.raw.start,
            R.raw.over
    };

    private SoundManager(Context context) {
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build())
                .setMaxStreams(1)
                .build();
        for (int sound : sounds) {
            mSoundPool.load(context, sound, 0);
        }
    }

    public static SoundManager get() {
        return Holder.INSTANCE;
    }

    public void setEnable(boolean state) {
        enable = state;
        if (!enable && mSoundPool != null) {
            for (int i = 0; i < sounds.length; i++) {
                mSoundPool.stop(i);
            }
        }
    }

    public void onStart() {
        playSound(5, 8);
    }

    public void onMove() {
        playSound(4, 0);
    }

    public void onRotate() {
        playSound(3, 1);
    }

    public void onDrop() {
        playSound(2, 2);
    }

    public void onClear() {
        playSound(1, 5);
    }

    public void onOver() {
        playSound(6, 10);
    }

    private void playSound(int index, int priority) {
        if (enable && mSoundPool != null) {
            mSoundPool.play(index, 1, 1, priority, 0, 1);
        }
    }

    public void dispose() {
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    private static class Holder {
        private static final SoundManager INSTANCE = new SoundManager(App.get());
    }
}
