package me.gavin.game.tetris.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.core.ControlImpl;
import me.gavin.game.tetris.core.LandControl;
import me.gavin.game.tetris.core.TetrisCallback;
import me.gavin.game.tetris.core.shape.Shape;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.databinding.DialogRankBinding;
import me.gavin.game.tetris.effect.Rank;
import me.gavin.game.tetris.effect.impl.ScoreService;
import me.gavin.game.tetris.rocker.RockerView;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.L;
import me.gavin.game.tetris.util.SPUtil;

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

    private List<Rank> rankList;

    @Override
    public void onOver(int clearedLineCount, long score) {
        Toast.makeText(MainActivity.this, "GAME OVER", Toast.LENGTH_LONG).show();
        Observable.just(BundleKey.RANK)
                .map(s -> SPUtil.getString(s, s))
                .map(json -> {
                    rankList = JsonUtil.toList(json, new TypeToken<ArrayList<Rank>>() {
                    });
                    if (rankList == null) {
                        rankList = new ArrayList<>();
                    }
                    return rankList;
                })
                .flatMap(Observable::fromIterable)
                .filter(rank -> rank.getScore() >= score)
                .toList()
                .map(List::size)
                .subscribe(integer -> {
                    boolean enter = integer < Config.RANK_COUNT && clearedLineCount > 0;
                    DialogRankBinding rankBinding = DialogRankBinding.inflate(getLayoutInflater());
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(enter ? String.format("TOP %s", integer + 1) : "GAME OVER");
                    if (enter) {
                        builder.setView(rankBinding.getRoot());
                    } else {
                        builder.setMessage("很遗憾，暂未入榜");
                    }
                    builder.setCancelable(false);
                    builder.setPositiveButton("确定", (dialog, which) ->
                            refreshRank(enter, clearedLineCount, score, rankBinding.editText.getText().toString()));
                    builder.setNegativeButton("取消", (dialog, which) ->
                            refreshRank(enter, clearedLineCount, score, null));
                    builder.show();
                }, Throwable::printStackTrace);
    }

    private void refreshRank(boolean enter, int clearedLineCount, long score, String name) {
        Observable.just(enter)
                .filter(Boolean::booleanValue)
                .map(arg0 -> {
                    Rank rank = new Rank();
                    rank.setLineCount(clearedLineCount);
                    rank.setScore(score);
                    rank.setTime(System.currentTimeMillis());
                    rank.setTitle(name);
                    rankList.add(rank);
                    return rankList;
                })
                .map(list -> {
                    Collections.sort(list, (o1, o2) -> o1.getScore() > o2.getScore() ? -1 : 1);
                    return list;
                })
                .map(list -> list.size() > Config.RANK_COUNT ? list.subList(0, Config.RANK_COUNT) : list)
                .map(JsonUtil::toJson)
                .doOnComplete(this::finish)
                .subscribe(json -> SPUtil.saveString(BundleKey.RANK, BundleKey.RANK, json), L::e);
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
