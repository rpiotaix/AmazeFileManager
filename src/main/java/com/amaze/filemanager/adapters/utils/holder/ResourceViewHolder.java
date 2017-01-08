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
import com.amaze.filemanager.adapters.utils.ViewConfigProvider;
import com.amaze.filemanager.ui.icons.Icons;
import com.amaze.filemanager.ui.views.CircleGradientDrawable;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.amaze.filemanager.utils.theme.AppTheme;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;

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
    protected void loadIcon() {
        Glide.with(getAdapter().getContext())
             .load(getIconRes(getData()))
             .asBitmap()
             .into(getIconTarget());
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

                view.setBackgroundDrawable(
                        new CircleGradientDrawable(
                                getColor(),
                                getUtilsProvider().getAppTheme(),
                                view.getResources().getDisplayMetrics())
                );
                view.setScaleType(ImageView.ScaleType.CENTER);

                super.onResourceReady(resource, glideAnimation);
            }
        };
    }

}
