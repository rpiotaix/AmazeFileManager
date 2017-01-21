package com.amaze.filemanager.adapters.utils.holder;

import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.amaze.filemanager.R;
import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.config.ViewConfigProvider;
import com.amaze.filemanager.adapters.utils.apk.PackageInfoIconGetter;
import com.amaze.filemanager.adapters.utils.apk.PackageInfoModelLoader;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by rpiotaix on 21/12/16.
 */

public class ApkViewHolder extends ThumbnailViewHolder {
    @DrawableRes
    private static final int PLACEHOLDER = R.drawable.ic_doc_apk;

    public ApkViewHolder(Recycleradapter adapter, View view, ViewConfigProvider configProvider, UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    public int getPlaceholder() {
        return PLACEHOLDER;
    }

    @Override
    protected GenericRequestBuilder createRequest() {
        String path = getData().getUri().getPath();

        return Glide.with(getViewContext())
                    .using(new PackageInfoModelLoader(getViewContext()), PackageInfo.class)
                    .from(String.class)
                    .as(Bitmap.class)
                    .decoder(new PackageInfoIconGetter(getViewContext()))
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // cannot disk cache ApplicationInfo, nor Drawables
                    .load(path);
    }


}
