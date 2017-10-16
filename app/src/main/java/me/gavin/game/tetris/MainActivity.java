package me.gavin.game.tetris;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import me.gavin.game.tetris.databinding.ActMainBinding;
import me.gavin.game.tetris.test.RockerView;

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

        binding.btnRotate.setOnClickListener(v -> binding.region.vm.onRotate());

        binding.rocker.setDirectionListener(event -> {
            switch (event) {
                case RockerView.EVENT_DIRECTION_LEFT:
                    binding.region.vm.onLeft();
                    break;
                case RockerView.EVENT_DIRECTION_RIGHT:
                    binding.region.vm.onRight();
                    break;
                case RockerView.EVENT_DIRECTION_UP:
                    binding.region.vm.onUp();
                    break;
                case RockerView.EVENT_DIRECTION_DOWN:
                    binding.region.vm.onDown();
                    break;
                default:
                    break;
            }
        });

    }
}
