package com.example.network.environment;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.network.R;

public class EnvironmentActivity extends AppCompatActivity {
    public static final String NETWORK_ENVIRONMENT_PREF_KEY = "network_environment_type";
    private static String sCurrentNetworkEnvironment = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new EnvironmentFragment(sCurrentNetworkEnvironment)).commit();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SharedPreferences defaultSharedPreferencesName = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            sCurrentNetworkEnvironment = defaultSharedPreferencesName.getString(NETWORK_ENVIRONMENT_PREF_KEY, "1");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String value = preferences.getString(NETWORK_ENVIRONMENT_PREF_KEY, "1");
        if (!sCurrentNetworkEnvironment.equalsIgnoreCase(value)) {
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            finish();
        }
    }

    public static boolean isOfficialEnvironment(Application application) {
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(application);
        String environment = prefs.getString(EnvironmentActivity.NETWORK_ENVIRONMENT_PREF_KEY, "1");
        return "1".equalsIgnoreCase(environment);
    }
}
