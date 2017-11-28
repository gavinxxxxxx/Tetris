package me.gavin.game.tetris.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.TestBinding;

public class TestActivity extends Activity {

    TestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.test);

        new AlertDialog.Builder(this)
                .setTitle("选择颜色")
                .setView(TestBinding.inflate(getLayoutInflater()).getRoot())
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .show();
    }
}
