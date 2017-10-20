package me.gavin.game.tetris.app;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.ActStartBinding;
import me.gavin.game.tetris.util.DisplayUtil;

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

        mBinding.btnContinue.setOnClickListener(v -> {
            v.setOnClickListener(null);
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra("isRestart", false));
            finish();
        });

        mBinding.btnStart.setOnClickListener(v -> {
            v.setOnClickListener(null);
            startActivity(new Intent(this, MainActivity.class)
                    .putExtra("isRestart", true));
            finish();
        });
    }
}
