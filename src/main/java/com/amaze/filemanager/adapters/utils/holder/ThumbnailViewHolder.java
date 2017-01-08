package com.amaze.filemanager.adapters.utils.holder;

import android.graphics.Bitmap;
import android.view.View;

import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.ViewConfigProvider;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by rpiotaix on 18/12/16.
 */

public abstract class ThumbnailViewHolder extends ViewHolder {
    public ThumbnailViewHolder(Recycleradapter adapter,
                               View view,
                               ViewConfigProvider configProvider,
                               UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadIcon() {
        GenericRequestBuilder r = createRequest();
        List<Transformation<Bitmap>> transformationList = new ArrayList<>();

        addTransformations(transformationList);

        if (!transformationList.isEmpty()) {
            r.transform(new MultiTransformation(transformationList));
        }
        r.into(getIconTarget());
    }

    protected void addTransformations(List<Transformation<Bitmap>> transformationList) {

        if (isSelected()) {
            transformationList.add(new GrayscaleTransformation(getViewContext()));
        }
    }

    protected abstract GenericRequestBuilder createRequest();


}
