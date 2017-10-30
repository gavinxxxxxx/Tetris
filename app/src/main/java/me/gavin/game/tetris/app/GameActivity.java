package me.gavin.game.tetris.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.core.Control;
import me.gavin.game.tetris.core.LandControl;
import me.gavin.game.tetris.core.TetrisCallback;
import me.gavin.game.tetris.databinding.ActGameBinding;
import me.gavin.game.tetris.databinding.DialogRankBinding;
import me.gavin.game.tetris.effect.ScoreManager;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.rocker.RockerView;
import me.gavin.game.tetris.shape.Shape;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.SPUtil;

public class GameActivity extends Activity implements TetrisCallback {

    private ActGameBinding mBinding;
    private Control mControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_game);
        init();
        gameStart();
    }

    private void init() {
        boolean isContinue = getIntent().getBooleanExtra(BundleKey.CONTINUE, false);
        mControl = new LandControl(mBinding.tetris, this, isContinue);

        mBinding.ivRotate.setOnClickListener(v -> {
            mControl.onRotate();
        });
        mBinding.ivDrop.setOnClickListener(v -> {
            mControl.onDrop();
        });
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
        boolean soundEnable = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.effect_sound), true);
        SoundManager.get().setEnable(soundEnable);
        mBinding.ivMute.setSelected(!soundEnable);
        mBinding.ivMute.setOnClickListener(v -> {
            SoundManager.get().setEnable(v.isSelected());
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putBoolean(getString(R.string.effect_sound), v.isSelected())
                    .apply();
            v.setSelected(!v.isSelected());
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
    public void onScoreChange() {
        mBinding.tvScore.setText(String.valueOf(ScoreManager.get().getScore() * 100));
    }

    private List<Rank> rankList;

    @Override
    public void onOver() {
        Toast.makeText(this, "GAME OVER", Toast.LENGTH_LONG).show();
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
                .filter(rank -> rank.getScore() >= ScoreManager.get().getScore())
                .toList()
                .map(List::size)
                .subscribe(integer -> {
                    boolean enter = integer < Config.RANK_COUNT && ScoreManager.get().getLineCount() > 0;
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
                            refreshRank(enter, rankBinding.editText.getText().toString()));
                    builder.setNegativeButton("取消", (dialog, which) ->
                            refreshRank(enter, null));
                    builder.show();
                }, Throwable::printStackTrace);
    }

    private void refreshRank(boolean enter, String name) {
        Observable.just(enter)
                .filter(Boolean::booleanValue)
                .map(arg0 -> {
                    Rank rank = new Rank();
                    rank.setLineCount(ScoreManager.get().getLineCount());
                    rank.setScore(ScoreManager.get().getScore());
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
                .subscribe(json -> SPUtil.saveString(BundleKey.RANK, BundleKey.RANK, json), Throwable::printStackTrace);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mControl.onDispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mControl.onPause();
        mBinding.ivPause.setSelected(true);
        mControl.onSave();
    }
}
