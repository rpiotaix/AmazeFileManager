package com.amaze.filemanager.adapters.utils.config;

import android.content.SharedPreferences;

import com.amaze.filemanager.fragments.Main;

/**
 * Created by rpiotaix on 24/12/16.
 */

public class SharedPreferencesConfig implements ViewConfig {
    @Deprecated
    private Main main;
    private SharedPreferences preferences;

    public SharedPreferencesConfig(SharedPreferences preferences, Main main) {
        this.preferences = preferences;
        this.main = main;
    }

    @Override
    public boolean isList() {
        return main.IS_LIST;
    }

    @Override
    public boolean showThumbs() {
        return preferences.getBoolean("showThumbs", true);
    }

    @Override
    public boolean showLastModified() {
        return preferences.getBoolean("showLastModified", true);
    }

    @Override
    public boolean circularThumbnails() {
        return preferences.getBoolean("circularimages", true);
    }

    @Override
    public boolean showPermissions() {
        return preferences.getBoolean("showPermissions", false);
    }

    @Override
    public boolean showSize() {
        return preferences.getBoolean("showFileSize", false);
    }
}
