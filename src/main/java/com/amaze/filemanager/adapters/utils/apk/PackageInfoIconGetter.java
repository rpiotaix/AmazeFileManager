package com.amaze.filemanager.adapters.utils.apk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Util;

import java.io.IOException;

public class PackageInfoIconGetter implements ResourceDecoder<PackageInfo, Bitmap> {
    private final Context context;

    public PackageInfoIconGetter(Context context) {
        this.context = context;
    }

    @Override
    public Resource<Bitmap> decode(PackageInfo source, int width, int height) throws IOException {
        Drawable icon = source.applicationInfo.loadIcon(context.getPackageManager());

        if (icon instanceof BitmapDrawable) {
            final Bitmap bm = ((BitmapDrawable) icon).getBitmap();
            return new BitmapResource(bm, Glide.get(context).getBitmapPool()) {
                @Override
                public int getSize() { // best effort
                    return Util.getBitmapByteSize(bm);
                }

                @Override
                public void recycle() { /* not from our pool */ }
            };
        } else {
            return null;
        }
    }

    @Override
    public String getId() {
        return "PackageInfoIconGetter";
    }
}