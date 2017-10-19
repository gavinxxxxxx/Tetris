package me.gavin.game.tetris;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import me.gavin.game.tetris.core.TetrisControl;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.core.ScoreChangeEvent;
import me.gavin.game.tetris.rocker.RockerView;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.effect.VibrateManager;
import me.gavin.game.tetris.effect.impl.SoundService;
import me.gavin.game.tetris.effect.impl.VibrateService;
import me.gavin.game.tetris.util.RxBus;

public class MainActivity extends Activity {

    private ActMainBinding mBinding;

    private SoundService mSoundService;
    private VibrateService mVibrateService;

    private TetrisControl mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        initData();
        bindView();
        mControl.gameStart();
    }

    private void initData() {
        mSoundService = new SoundManager(this);
        mVibrateService = new VibrateManager(this);
        mControl = new TetrisControl(mBinding.tetris);
        RxBus.get().toObservable(ScoreChangeEvent.class)
                .map(event -> event.score)
                .map(score -> score * 100)
                .subscribe(score -> mBinding.tvScore.setText(String.valueOf(score)));
    }

    private void bindView() {
        mBinding.btnA.setOnClickListener(v -> {
            mControl.onRotate();
            mSoundService.onRotate();
            mVibrateService.onRotate();
        });
        mBinding.btnB.setOnClickListener(v -> {
            mControl.onDrop();
            mSoundService.onDrop();
            mVibrateService.onDrop();
        });
        mBinding.rocker.setDirectionListener(event -> {
            switch (event) {
                case RockerView.EVENT_DIRECTION_LEFT:
                    mControl.onLeft();
                    mSoundService.onMove();
                    mVibrateService.onMove();
                    break;
                case RockerView.EVENT_DIRECTION_RIGHT:
                    mControl.onRight();
                    mSoundService.onMove();
                    mVibrateService.onMove();
                    break;
                case RockerView.EVENT_DIRECTION_UP:
                    mControl.onUp();
                    mSoundService.onRotate();
                    mVibrateService.onRotate();
                    break;
                case RockerView.EVENT_DIRECTION_DOWN:
                    mControl.onDown();
                    mSoundService.onMove();
                    mVibrateService.onMove();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundService.dispose();
        mVibrateService.dispose();
        mControl.dispose();
    }

}
