package me.gavin.game.tetris;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import me.gavin.game.tetris.databinding.ActMainBinding;

public class MainActivity extends Activity {

    ActMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.act_main);

        binding.region.setViewModel(new ViewModel(binding.region));
        binding.region.setOnClickListener(v -> {
            binding.region.vm.shape.rotate();
            binding.region.postInvalidate();
        });

        binding.btnLeft.setOnClickListener(v -> binding.region.vm.onLeft());
        binding.btnRight.setOnClickListener(v -> binding.region.vm.onRight());
        binding.btnDown.setOnClickListener(v -> binding.region.vm.onDown());
        binding.btnRotate.setOnClickListener(v -> binding.region.vm.onRotate());
    }
}
