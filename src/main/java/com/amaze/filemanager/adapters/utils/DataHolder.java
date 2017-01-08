package com.amaze.filemanager.adapters.utils;

import android.net.Uri;

public interface DataHolder {
    Uri getUri();

    @Deprecated
    String getPermissionsAsString();

    enum Type {
        FILE,
        DIRECTORY
    }

    String getMimeType();

    Type getType();

    String getTitle();

    String getLastModified();

    String getExtension();

    @Deprecated
    String getSizeAsString();
}