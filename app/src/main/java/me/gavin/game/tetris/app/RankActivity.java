package me.gavin.game.tetris.app;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.R;
import me.gavin.game.tetris.databinding.ItemRankBinding;
import me.gavin.game.tetris.effect.Rank;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.SaveHelper;

/**
 * 排行榜
 *
 * @author gavin.xiong 2017/10/24
 */
public class RankActivity extends ListActivity {

    private List<Rank> rankList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getListView().setBackgroundColor(getResources().getColor(R.color.colorBackground));

        rankList = JsonUtil.toList(SaveHelper.read(BundleKey.RANK), new TypeToken<ArrayList<Rank>>() {
        });
        setListAdapter(new RankAdapter());
    }

    private class RankAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rankList == null ? 0 : rankList.size();
        }

        @Override
        public Object getItem(int position) {
            return rankList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                ItemRankBinding binding = ItemRankBinding.inflate(getLayoutInflater());
                convertView = binding.getRoot();
                holder = new ViewHolder(binding);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            onBind(holder.binding, rankList.get(position), position);
            return convertView;
        }

        private void onBind(ItemRankBinding binding, Rank t, int position) {
            binding.tvNum.setText(String.valueOf(position));
            binding.tvTitle.setText(TextUtils.isEmpty(t.getTitle()) ? "Tetris" : t.getTitle());
            binding.tvScore.setText(String.valueOf(t.getScore()));
        }
    }

    private class ViewHolder {
        ItemRankBinding binding;

        ViewHolder(ItemRankBinding binding) {
            this.binding = binding;
        }
    }
}
