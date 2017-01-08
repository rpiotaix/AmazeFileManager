package com.amaze.filemanager.adapters.utils.holder;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.config.ViewConfigProvider;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by rpiotaix on 18/12/16.
 */

public abstract class ThumbnailViewHolder extends ViewHolder {
    ThumbnailViewHolder(Recycleradapter adapter,
                        View view,
                        ViewConfigProvider configProvider,
                        UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadIcon() {
        GenericRequestBuilder r = createRequest();
        r.placeholder(getPlaceholder());

        List<Transformation<Bitmap>> transformationList = new ArrayList<>();

        addTransformations(transformationList);

        if (!transformationList.isEmpty()) {
            r.transform(new MultiTransformation(transformationList));
        }
        r.into(getIconTarget());
    }

    protected void addTransformations(List<Transformation<Bitmap>> transformationList) {
        transformationList.add(new CenterCrop(getViewContext()));

        if (isSelected()) {
            transformationList.add(new GrayscaleTransformation(getViewContext()));
        }
    }

    @DrawableRes
    public abstract int getPlaceholder();

    protected abstract GenericRequestBuilder createRequest();


}
