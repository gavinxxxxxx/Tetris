package me.gavin.game.tetris.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.View;

import me.gavin.game.tetris.R;
import me.gavin.game.tetris.util.L;

/**
 * SettingActivity
 *
 * @author gavin.xiong 2017/10/26
 */
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

    public static class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceManager().setSharedPreferencesName("SETTINGS");
            addPreferencesFromResource(R.xml.pref_settings);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
            afterCreate();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        private void afterCreate() {
            findPreference("other").setOnPreferenceChangeListener((preference, newValue) -> {
                L.e("onPreferenceChange - " + newValue);
                return true;
            });
            findPreference("shape_type").setOnPreferenceChangeListener(((preference, newValue) -> {
                L.e("onPreferenceChange - " + newValue + " - " + newValue.getClass());
                return true;
            }));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            L.e("onSharedPreferenceChanged - " + key);
        }
    }
}
