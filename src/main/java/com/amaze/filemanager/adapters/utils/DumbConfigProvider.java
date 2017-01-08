package com.amaze.filemanager.adapters.utils;

import com.amaze.filemanager.fragments.Main;

public class DumbConfigProvider implements ViewConfigProvider {

    private Main main;

    public DumbConfigProvider(Main main) {
        this.main = main;
    }

    @Override
    public ViewConfig getConfig() {
        return new DumbViewConfig(main);
    }
}