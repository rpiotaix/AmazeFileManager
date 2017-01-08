package com.amaze.filemanager.adapters.utils.holder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.ViewConfigProvider;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by rpiotaix on 18/12/16.
 */

public class ThumbnailViewHolder extends ViewHolder {
    public ThumbnailViewHolder(Recycleradapter adapter,
                               View view,
                               ViewConfigProvider configProvider,
                               UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadIcon() {
        BitmapRequestBuilder<Uri, Bitmap> r = Glide.with(getViewContext())
                                                   .load(getData().getUri())
                                                   .asBitmap()
                                                   .centerCrop();

        List<Transformation<Bitmap>> transformationList = new ArrayList<>();

        if (getConfig().circularThumbnails()) {
            transformationList.add(new CropCircleTransformation(getAdapter().getContext()));
        }

        if (isSelected()) {
            transformationList.add(new GrayscaleTransformation(getViewContext()));
        }

        if (!transformationList.isEmpty()) {
            r.transform(new MultiTransformation<>(transformationList));
        }

        r.into(getIconTarget());
    }
}
