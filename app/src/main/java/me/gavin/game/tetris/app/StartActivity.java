package me.gavin.game.tetris.app;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.ActStartBinding;
import me.gavin.game.tetris.util.DisplayUtil;
import me.gavin.game.tetris.util.SPUtil;

public class StartActivity extends Activity {

    private ActStartBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_start);
        init();
    }

    private void init() {
        int size = DisplayUtil.getScreenWidth() / 5 * 3;
        mBinding.ivLogo.getLayoutParams().width = size;
        mBinding.ivLogo.getLayoutParams().height = size;
        ((LinearLayout.LayoutParams) mBinding.ivLogo.getLayoutParams()).topMargin = size / 3;
        mBinding.btnContinue.getLayoutParams().width = size;
        mBinding.btnStart.getLayoutParams().width = size;
        mBinding.btnRank.getLayoutParams().width = size;

        mBinding.btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra(BundleKey.CONTINUE, true));
            finish();
        });
        mBinding.btnStart.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra(BundleKey.CONTINUE, false));
            finish();
        });
        mBinding.btnRank.setOnClickListener(v ->
                startActivity(new Intent(this, RankActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String save = SPUtil.getString(BundleKey.SAVE, BundleKey.SAVE);
        mBinding.btnContinue.setVisibility(TextUtils.isEmpty(save) ? View.GONE : View.VISIBLE);
        mBinding.btnStart.setText(TextUtils.isEmpty(save) ? "开始" : "重新开始");
    }
}
