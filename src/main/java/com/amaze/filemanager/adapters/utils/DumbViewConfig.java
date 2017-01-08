package com.amaze.filemanager.adapters.utils;

import com.amaze.filemanager.fragments.Main;

public class DumbViewConfig implements ViewConfig {
    private Main main;

    public DumbViewConfig(Main main) {
        this.main = main;
    }

    @Override
    public boolean isList() {
        return main.IS_LIST;
    }

    @Override
    public boolean showThumbs() {
        return main.SHOW_THUMBS;
    }

    @Override
    public boolean showLastModified() {
        return main.SHOW_LAST_MODIFIED;
    }

    @Override
    public boolean circularThumbnails() {
        return main.CIRCULAR_IMAGES;
    }

    @Override
    public boolean showPermissions() {
        return main.SHOW_PERMISSIONS;
    }

    @Override
    public boolean showSize() {
        return main.SHOW_SIZE;
    }
}