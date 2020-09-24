package com.example.network.environment;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.network.R;

import static com.example.network.environment.EnvironmentActivity.NETWORK_ENVIRONMENT_PREF_KEY;

public class EnvironmentFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private String mCurrentNetworkEnvironment = "";

    public EnvironmentFragment(String networkState) {
        mCurrentNetworkEnvironment = networkState;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.environment_preference);
        findPreference(NETWORK_ENVIRONMENT_PREF_KEY).setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.e("tag", "onPreferenceChange: ----newValue=" + newValue);
        if (!mCurrentNetworkEnvironment.equalsIgnoreCase(String.valueOf(newValue))) {
            Toast.makeText(getContext(), "您已经更改了网络环境，再您退出当前页面的时候APP将会重启切换环境！", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
