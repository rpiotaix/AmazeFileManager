package com.amaze.filemanager.adapters.utils.config;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.amaze.filemanager.fragments.Main;

/**
 * Created by rpiotaix on 24/12/16.
 */

public class SharedPreferencesConfigProvider implements ViewConfigProvider {

    private SharedPreferencesConfig config;

    public SharedPreferencesConfigProvider(SharedPreferences preferences, Main main) {
        config = new SharedPreferencesConfig(preferences, main);
    }

    @NonNull
    @Override
    public ViewConfig getConfig() {
        return config;
    }
}
