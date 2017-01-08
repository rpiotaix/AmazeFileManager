package com.amaze.filemanager.adapters.utils.holder;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.amaze.filemanager.R;
import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.ViewConfigProvider;
import com.amaze.filemanager.utils.UtilitiesProviderInterface;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by rpiotaix on 21/12/16.
 */

public class MediaViewHolder extends ThumbnailViewHolder {
    @DrawableRes
    private static final int PLACEHOLDER = R.drawable.ic_doc_image;

    public MediaViewHolder(Recycleradapter adapter, View view, ViewConfigProvider configProvider, UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    protected GenericRequestBuilder createRequest() {
        return Glide.with(getViewContext())
                    .load(getData().getUri())
                    .asBitmap()
                    .placeholder(PLACEHOLDER);
    }

    @Override
    protected void addTransformations(List<Transformation<Bitmap>> transformationList) {
        super.addTransformations(transformationList);

        if (getConfig().isList() && getConfig().circularThumbnails()) {
            transformationList.add(new CropCircleTransformation(getAdapter().getContext()));
        }
    }

    @Override
    public int getPlaceholder() {
        return PLACEHOLDER;
    }
}
