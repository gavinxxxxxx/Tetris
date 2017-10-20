package me.gavin.game.tetris.app;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.core.TetrisCallback;
import me.gavin.game.tetris.core.TetrisControl;
import me.gavin.game.tetris.core.TetrisSoleControl;
import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.effect.ScoreManager;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.effect.VibrateManager;
import me.gavin.game.tetris.effect.impl.ScoreService;
import me.gavin.game.tetris.effect.impl.SoundService;
import me.gavin.game.tetris.effect.impl.VibrateService;
import me.gavin.game.tetris.rocker.RockerView;

public class MainActivity extends Activity {

    private ActMainBinding mBinding;

    private SoundService mSoundService;
    private VibrateService mVibrateService;
    private ScoreService mScoreService;

    private TetrisControl mControl;

    boolean isRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        bindView();
        initData();
        gameStart();
    }

    private void initData() {
        mSoundService = new SoundManager(this);
        mVibrateService = new VibrateManager(this);
        mScoreService = new ScoreManager();

        isRestart = getIntent().getBooleanExtra("isRestart", false);
        mControl = new TetrisSoleControl(mBinding.tetris, new TetrisCallback() {
            @Override
            public void onNextShape(Shape shape, int clearCount) {
                mBinding.next.setShape(shape);
                mScoreService.onNextShape(clearCount);
            }

            @Override
            public void onClear() {
                mSoundService.onClear();
                mVibrateService.onClear();
            }

            @Override
            public void onClear(int clearCount) {
                mScoreService.onClear(clearCount);
                mBinding.tvScore.setText(String.valueOf(mScoreService.getScore() * 100));
            }

            @Override
            public void onOver() {
                // TODO: 2017/10/19 结束
                mSoundService.onOver();
                mVibrateService.onOver();
                Toast.makeText(MainActivity.this, "GAME OVER", Toast.LENGTH_LONG).show();
                Observable.timer(3000, TimeUnit.MILLISECONDS)
                        .subscribe(arg0 -> {
                            MainActivity.this.finish();
                        });
            }
        }, isRestart);
    }

    private void gameStart() {
        mControl.ready(isRestart);
        mControl.go();
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
        mBinding.ivPause.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            if (v.isSelected()) {
                mControl.onPause();
            } else {
                mControl.onContinue();
            }
        });
        mBinding.ivMute.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            mSoundService.setEnable(!v.isSelected());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundService.dispose();
        mVibrateService.dispose();
        mControl.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mControl.onPause();
        mBinding.ivPause.setSelected(true);
        mControl.onSavedInstanceState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mControl.onRestoreInstanceState();
    }
}
