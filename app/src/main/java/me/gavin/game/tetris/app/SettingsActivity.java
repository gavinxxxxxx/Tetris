package me.gavin.game.tetris.app;

import android.app.Activity;
import android.os.Bundle;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.effect.SoundManager;
import me.gavin.game.tetris.next.Utils;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            findPreference(getString(R.string.effect_sound)).setOnPreferenceChangeListener((preference, newValue) -> {
                SoundManager.get().setEnable(Boolean.valueOf(newValue.toString()));
                return true;
            });
//            findPreference(getString(R.string.ground_horizontal_count)).setOnPreferenceChangeListener((preference, newValue) -> {
//                preference.setSummary(String.valueOf(newValue));
//                return true;
//            });
//            findPreference(getString(R.string.ground_vertical_count)).setOnPreferenceChangeListener((preference, newValue) -> {
//                preference.setSummary(String.valueOf(newValue));
//                return true;
//            });
            MultiSelectListPreference shapeMode = (MultiSelectListPreference) findPreference(getString(R.string.mode_shape_type));
            shapeMode.setOnPreferenceChangeListener((preference, newValue) -> {
                @SuppressWarnings("unchecked")
                Set<String> set = (Set<String>) newValue;
                if (set == null || set.isEmpty()) {
                    return false;
                }
                setShapeModeSummary(preference, set);
                Utils.resetLimit(set);
                return true;
            });
            if (shapeMode.getValues() == null || shapeMode.getValues().isEmpty()) {
                String[] types = getResources().getStringArray(R.array.shape_type_value);
                HashSet<String> set = new HashSet<>();
                set.add(types[0]);
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putStringSet(getString(R.string.mode_shape_type), set)
                        .apply();
            } else {
                Set<String> set = shapeMode.getValues();
                setShapeModeSummary(shapeMode, set);
            }
        }

        private void setShapeModeSummary(Preference preference, Set<String> set) {
            StringBuilder sb = new StringBuilder();
            if (set.contains("4.0")) {
                sb.append("四格骨牌、");
            }
            if (set.contains("5.0")) {
                sb.append("五格骨牌、");
            }
            preference.setSummary(sb.toString().substring(0, sb.length() - 1));
        }

    }
}
