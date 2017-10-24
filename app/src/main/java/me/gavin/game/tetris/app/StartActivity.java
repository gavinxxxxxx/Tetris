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
import me.gavin.game.tetris.util.SaveHelper;

public class StartActivity extends Activity {

    ActStartBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_start);

        int size = DisplayUtil.getScreenWidth() / 5 * 3;
        mBinding.ivLogo.getLayoutParams().width = size;
        mBinding.ivLogo.getLayoutParams().height = size;
        ((LinearLayout.LayoutParams) mBinding.ivLogo.getLayoutParams()).topMargin = size / 3;
        mBinding.btnContinue.getLayoutParams().width = size;
        mBinding.btnStart.getLayoutParams().width = size;

        String save = SaveHelper.read(BundleKey.SAVE);
        mBinding.btnContinue.setVisibility(TextUtils.isEmpty(save) ? View.GONE : View.VISIBLE);
        mBinding.btnContinue.setOnClickListener(v -> {
            v.setOnClickListener(null);
            startActivity(new Intent(this, MainActivity.class).putExtra(BundleKey.CONTINUE, true));
            finish();
        });

        mBinding.btnStart.setText(TextUtils.isEmpty(save) ? "开始" : "重新开始");
        mBinding.btnStart.setOnClickListener(v -> {
            v.setOnClickListener(null);
            startActivity(new Intent(this, MainActivity.class).putExtra(BundleKey.CONTINUE, false));
            finish();
        });
    }
}
