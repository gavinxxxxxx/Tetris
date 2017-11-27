package me.gavin.game.tetris.app;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import java.util.Set;

import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.next.Utils;
import me.gavin.game.tetris.util.SPUtil;

public class MainActivity extends Activity {

    private ActMainBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_main);

        mBinding.btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class)
                    .putExtra(BundleKey.CONTINUE, true));
            finish();
        });
        mBinding.btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class)
                    .putExtra(BundleKey.CONTINUE, false));
            finish();
        });
        mBinding.btnRank.setOnClickListener(v ->
                startActivity(new Intent(this, RankActivity.class)));
        mBinding.ivSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        SoundManager.get().setEnable(PreferenceManager
                .getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.effect_sound), true));

        Set<String> set = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getStringSet(getString(R.string.mode_shape_type), null);
        Utils.resetLimit(set);

        mBinding.ivTest.setOnClickListener(v -> startActivity(new Intent(this, TestActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String save = SPUtil.getString(BundleKey.SAVE, BundleKey.SAVE);
        mBinding.btnContinue.setVisibility(TextUtils.isEmpty(save) ? View.GONE : View.VISIBLE);
        mBinding.btnStart.setText(TextUtils.isEmpty(save) ? "开始" : "重新开始");
    }
}
