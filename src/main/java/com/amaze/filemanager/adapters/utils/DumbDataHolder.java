package com.amaze.filemanager.adapters.utils;

import android.net.Uri;

import com.amaze.filemanager.ui.Layoutelements;
import com.amaze.filemanager.ui.icons.MimeTypes;

public class DumbDataHolder implements DataHolder {
    private final Layoutelements e;

    public DumbDataHolder(Layoutelements e) {
        this.e = e;
    }

    @Override
    public Uri getUri() {
        return Uri.parse("file://" + e.getDesc());
    }

    @Override
    public String getPermissionsAsString() {
        return e.getPermissions();
    }

    @Override
    public String getMimeType() {
        return MimeTypes.fromExtension(getExtension());
    }

    @Override
    public Type getType() {
        return e.isDirectory() ? Type.DIRECTORY : Type.FILE;
    }

    @Override
    public String getTitle() {
        return e.getTitle();
    }

    @Override
    public String getLastModified() {
        return e.getDate();
    }

    public String getExtension() {
        String name = e.getDesc();
        int lastDotPos = name.lastIndexOf('.');

        return lastDotPos == -1 ? "" : name.substring(lastDotPos + 1);
    }

    @Override
    public String getSizeAsString() {
        return e.getSize();
    }
}