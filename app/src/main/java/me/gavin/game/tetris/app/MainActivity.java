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
import me.gavin.game.tetris.rocker.RockerView;

public class MainActivity extends Activity implements TetrisCallback {

    private ActMainBinding mBinding;

    private boolean isRestart;
    private TetrisControl mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        init();
        gameStart();
    }

    private void init() {
        isRestart = getIntent().getBooleanExtra("isRestart", false);
        mControl = new TetrisSoleControl(mBinding.tetris, this, isRestart);

        mBinding.btnA.setOnClickListener(v -> mControl.onRotate());
        mBinding.btnB.setOnClickListener(v -> mControl.onDrop());
        mBinding.rocker.setDirectionListener(event -> {
            switch (event) {
                case RockerView.EVENT_DIRECTION_LEFT:
                    mControl.onLeft();
                    break;
                case RockerView.EVENT_DIRECTION_RIGHT:
                    mControl.onRight();
                    break;
                case RockerView.EVENT_DIRECTION_UP:
                    mControl.onUp();
                    break;
                case RockerView.EVENT_DIRECTION_DOWN:
                    mControl.onDown();
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
                mControl.onStartTimer();
            }
        });
        mBinding.ivMute.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            mControl.onMute(v.isSelected());
        });
    }

    private void gameStart() {
        mControl.ready(isRestart);
        mControl.onStartTimer();
    }

    @Override
    public void onNextShape(Shape shape, int clearCount) {
        mBinding.next.setShape(shape);
    }

    @Override
    public void onClear() {
    }

    @Override
    public void onClear(int clearCount) {
    }

    @Override
    public void onScoreChange(int lineCount, long score, int multiple) {
        mBinding.tvScore.setText(String.valueOf(score * 100));
    }

    @Override
    public void onOver() {
        // TODO: 2017/10/19 结束
        Toast.makeText(MainActivity.this, "GAME OVER", Toast.LENGTH_LONG).show();
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(arg0 -> {
                    MainActivity.this.finish();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mControl.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mControl.onPause();
        mBinding.ivPause.setSelected(true);
        mControl.onSavedInstanceState();
    }
}
