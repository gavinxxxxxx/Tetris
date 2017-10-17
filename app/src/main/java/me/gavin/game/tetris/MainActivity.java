package me.gavin.game.tetris;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.rocker.RockerView;

public class MainActivity extends Activity {

    private Vibrator vibrator;
    private final long[] pattern = {5, 5, 5, 5};

    private SoundPool soundPool;
    private final int[] sounds = {R.raw.music};

    ActMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_main);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build())
                .setMaxStreams(1)
                .build();
        for (int sound : sounds) {
            soundPool.load(this, sound, 0);
        }

        binding.btnB.setOnClickListener(v -> {
            binding.region.onRotate();
            vibrate();
        });

        binding.rocker.setDirectionListener(event -> {
            switch (event) {
                case RockerView.EVENT_DIRECTION_LEFT:
                    binding.region.onLeft();
                    vibrate();
                    break;
                case RockerView.EVENT_DIRECTION_RIGHT:
                    binding.region.onRight();
                    vibrate();
                    break;
                case RockerView.EVENT_DIRECTION_UP:
                    binding.region.onUp();
                    vibrate();
                    playSound(1);
                    break;
                case RockerView.EVENT_DIRECTION_DOWN:
                    binding.region.onDown();
                    vibrate();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
        soundPool.release();
    }

    private void vibrate() {
        Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe(arg0 -> {
                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
                    } else {
                        vibrator.vibrate(pattern, -1);
                    }
                });
    }

    private void playSound(int index) {
        soundPool.play(index, 1, 1, 0, 0, 1);
    }
}
