package me.gavin.game.tetris.app;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.TestBinding;
import me.gavin.game.tetris.util.L;
import me.gavin.widget.color.picker.ColorPickerDialogBuilder;

public class TestActivity extends Activity {

    TestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.test);

        ColorPickerDialogBuilder.with(this)
                .setTitle("选择颜色")
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setPositiveButton("确定", (dialog, color) -> {
                    L.e(color);
                })
                .setNegativeButton("取消", null)
                .setInputButton("输入", (dialog, color) -> {
                    L.e(color);
                })
                .show();
    }
}
