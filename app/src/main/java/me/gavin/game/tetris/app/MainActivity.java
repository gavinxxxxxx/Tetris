package me.gavin.game.tetris.app;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.core.ControlImpl;
import me.gavin.game.tetris.core.LandControl;
import me.gavin.game.tetris.core.TetrisCallback;
import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.effect.impl.ScoreService;
import me.gavin.game.tetris.rocker.RockerView;

public class MainActivity extends Activity implements TetrisCallback {

    private ActMainBinding mBinding;
    private ControlImpl mControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        init();
        gameStart();
    }

    private void init() {
        boolean isContinue = getIntent().getBooleanExtra(BundleKey.CONTINUE, false);
        mControl = new LandControl(mBinding.tetris, this, isContinue);

        mBinding.ivRotate.setOnClickListener(v -> mControl.onRotate());
        mBinding.ivDrop.setOnClickListener(v -> mControl.onDrop());
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
                mControl.onStart();
            }
        });
        mBinding.ivMute.setOnClickListener(v -> {
            v.setSelected(!v.isSelected());
            mControl.onMute(v.isSelected());
        });
    }

    private void gameStart() {
        mControl.onStart();
    }

    @Override
    public void onNextShape(Shape shape) {
        mBinding.next.setShape(shape);
    }

    @Override
    public void onClear() {
    }

    @Override
    public void onClear(int clearCount) {
    }

    @Override
    public void onScoreChange(ScoreService service) {
        mBinding.tvScore.setText(String.valueOf(service.getScore() * 100));
    }

    @Override
    public void onOver() {
        Toast.makeText(MainActivity.this, "GAME OVER", Toast.LENGTH_LONG).show();
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(arg0 -> {
                    // TODO: 2017/10/19 结束
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
