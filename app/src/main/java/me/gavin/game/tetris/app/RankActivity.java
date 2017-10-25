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
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import me.gavin.game.tetris.BundleKey;
import me.gavin.game.tetris.Config;
import me.gavin.game.tetris.databinding.ItemRankBinding;
import me.gavin.game.tetris.effect.Rank;
import me.gavin.game.tetris.util.JsonUtil;
import me.gavin.game.tetris.util.SPUtil;

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
        Observable.just(BundleKey.RANK)
                .map(s -> SPUtil.getString(s, s))
                .map(json -> {
                    List<Rank> list = JsonUtil.toList(json, new TypeToken<ArrayList<Rank>>() {
                    });
                    return list == null ? new ArrayList<Rank>() : list;
                })
                .map(list -> {
                    Collections.sort(list, (o1, o2) -> o1.getScore() > o2.getScore() ? -1 : 1);
                    return list;
                })
                .map(list -> list.size() > Config.RANK_COUNT ? list.subList(0, Config.RANK_COUNT) : list)
                .subscribe(list -> {
                    rankList = list;
                    setListAdapter(new RankAdapter());
                }, Throwable::printStackTrace);
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
            binding.tvNum.setText(String.format("NO.%s", position + 1));
            binding.tvTitle.setText(TextUtils.isEmpty(t.getTitle()) ? "未名" : t.getTitle());
            binding.tvScore.setText(String.valueOf(t.getScore() * 100));
        }
    }

    private class ViewHolder {
        ItemRankBinding binding;

        ViewHolder(ItemRankBinding binding) {
            this.binding = binding;
        }
    }
}
