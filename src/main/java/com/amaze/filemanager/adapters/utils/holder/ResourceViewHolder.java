package com.amaze.filemanager.adapters.utils.holder;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amaze.filemanager.R;
import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.DataHolder;
import com.amaze.filemanager.adapters.utils.IconTarget;
import com.amaze.filemanager.adapters.utils.config.ViewConfigProvider;
import com.amaze.filemanager.ui.icons.Icons;
import com.amaze.filemanager.ui.views.CircleGradientDrawable;
import com.amaze.filemanager.utils.color.ColorUsage;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * Created by rpiotaix on 18/12/16.
 */

public class ResourceViewHolder extends ViewHolder {
    private static final int DP_PADDING = 8;

    public ResourceViewHolder(Recycleradapter adapter,
                              View view,
                              ViewConfigProvider configProvider,
                              UtilitiesProviderInterface utilsProvider) {
        super(adapter, view, configProvider, utilsProvider);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadIcon() {
        BitmapRequestBuilder<Integer, Bitmap> r = Glide.with(getAdapter().getContext())
                                                       .load(getIconRes(getData()))
                                                       .asBitmap()
                                                       .fitCenter()
                                                       .atMost();

        if (!getConfig().isList()) {
            List<Transformation<Bitmap>> transformations = new ArrayList<>();
            transformations.add(new ColorFilterTransformation(getViewContext(), getUtilsProvider().getColorPreference()
                                                                                                  .getColor(ColorUsage.ICON_SKIN)));

            if (isSelected()) {
                transformations.add(new GrayscaleTransformation(getViewContext()));
            }

            r.transform(new MultiTransformation<>(transformations));
        }
        r.into(getIconTarget());
    }

    @DrawableRes
    private int getIconRes(DataHolder data) {
        int res = R.drawable.ic_grid_folder_new;

        if (data.getType() == DataHolder.Type.FILE) {
            String mimeType = data.getMimeType();
            Log.i("RecyclerAdapter", "Mimetype = " + mimeType);
            res = Icons.iconFromMimeType(mimeType);
        }

        return res;
    }

    private int dpToPx(int dp) {
        return Math.round(getAdapter().getContext().getResources().getDisplayMetrics().density * dp * 2);
    }

    @Override
    protected IconTarget createIconTarget(ImageView view) {
        return new IconTarget(view, getUtilsProvider().getColorPreference()) {

            @Override
            public void getSize(final SizeReadyCallback cb) {
                super.getSize(new SizeReadyCallback() {
                    @Override
                    public void onSizeReady(int width, int height) {
                        int pxPadding = dpToPx(DP_PADDING);
                        cb.onSizeReady(width - pxPadding, height - pxPadding);
                    }
                });
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                ImageView view = getView();

                if (getConfig().isList()) {
                    view.setBackgroundDrawable(
                            new CircleGradientDrawable(
                                    getColor(),
                                    getUtilsProvider().getAppTheme(),
                                    view.getResources().getDisplayMetrics())
                    );
                    view.setScaleType(ImageView.ScaleType.CENTER);
                }

                super.onResourceReady(resource, glideAnimation);
            }
        };
    }

}
