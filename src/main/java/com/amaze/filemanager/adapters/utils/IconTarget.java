package com.amaze.filemanager.adapters.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.widget.ImageView;

import com.amaze.filemanager.R;
import com.amaze.filemanager.utils.color.ColorPreference;
import com.amaze.filemanager.utils.color.ColorUsage;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by rpiotaix on 18/12/16.
 */

public class IconTarget extends BitmapImageViewTarget {
    private Context viewContext;
    private ColorPreference colorPreference;
    private boolean selected;

    public IconTarget(ImageView imageView, ColorPreference colorPreference) {
        super(imageView);
        this.viewContext = view.getContext();
        this.colorPreference = colorPreference;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        String s = "Loaded: " + resource.getWidth() + "x" + resource.getHeight();
        Log.i("IconTarget", s);

        super.onResourceReady(resource, glideAnimation);
    }

    @ColorInt
    public int getColor() {
        if (selected) {
            return viewContext.getResources().getColor(R.color.selection_grey);
        } else {
            return colorPreference.getColor(ColorUsage.ACCENT);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    protected boolean isSelected() {
        return selected;
    }
}
