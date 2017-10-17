package me.gavin.game.tetris.test;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.TestBinding;
import me.gavin.game.tetris.util.L;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2017/10/16
 */
public class TestActivity extends Activity {

    TestBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.test);
        binding.rocker.setActionListener(event -> {
            L.e("actionEvent - " + event);
        });
        binding.rocker.setDirectionListener(event -> {
            L.e("directionEvent - " + event + " - " + Math.random());
        });
    }
}
