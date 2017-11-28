package me.gavin.game.tetris.app;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.TestBinding;
import me.gavin.widget.color.picker.ColorPickerDialog;

public class TestActivity extends Activity {

    TestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.test);

        new ColorPickerDialog.Builder(this)
                .setTitle("选择颜色")
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)

                .show();
    }
}
