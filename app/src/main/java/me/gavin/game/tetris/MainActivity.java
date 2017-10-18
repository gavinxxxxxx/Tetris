package me.gavin.game.tetris;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.region.RegionViewModel;
import me.gavin.game.tetris.region.ScoreChangeEvent;
import me.gavin.game.tetris.rocker.RockerView;
import me.gavin.game.tetris.service.SoundManager;
import me.gavin.game.tetris.service.VibrateManager;
import me.gavin.game.tetris.service.impl.SoundService;
import me.gavin.game.tetris.service.impl.VibrateService;
import me.gavin.game.tetris.util.RxBus;

public class MainActivity extends Activity {

    private ActMainBinding mBinding;

    private SoundService mSoundService;
    private VibrateService mVibrateService;

    private RegionViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        initData();
        bindView();
        mViewModel.gameStart();
    }

    private void initData() {
        mSoundService = new SoundManager(this);
        mVibrateService = new VibrateManager(this);
        mViewModel = new RegionViewModel(mBinding.region);
        RxBus.get().toObservable(ScoreChangeEvent.class)
                .map(event -> event.score)
                .map(score -> score * 10)
                .subscribe(score -> mBinding.tvScore.setText(String.valueOf(score)));
    }

    private void bindView() {
        mBinding.btnA.setOnClickListener(v -> {
            mViewModel.onRotate();
            mSoundService.onRotate();
            mVibrateService.onRotate();
        });
        mBinding.btnB.setOnClickListener(v -> {
            mViewModel.onDrop();
            mSoundService.onDrop();
            mVibrateService.onDrop();
        });
        mBinding.rocker.setDirectionListener(event -> {
            switch (event) {
                case RockerView.EVENT_DIRECTION_LEFT:
                    mViewModel.onLeft();
                    mSoundService.onMove();
                    mVibrateService.onMove();
                    break;
                case RockerView.EVENT_DIRECTION_RIGHT:
                    mViewModel.onRight();
                    mSoundService.onMove();
                    mVibrateService.onMove();
                    break;
                case RockerView.EVENT_DIRECTION_UP:
                    mViewModel.onUp();
                    mSoundService.onRotate();
                    mVibrateService.onRotate();
                    break;
                case RockerView.EVENT_DIRECTION_DOWN:
                    mViewModel.onDown();
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
        mViewModel.dispose();
    }

}
