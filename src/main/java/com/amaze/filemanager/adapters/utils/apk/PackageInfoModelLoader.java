package com.amaze.filemanager.adapters.utils.apk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;

public class PackageInfoModelLoader implements ModelLoader<String, PackageInfo> {
    private Context context;

    public PackageInfoModelLoader(Context context) {
        this.context = context;
    }

    @Override
    public DataFetcher<PackageInfo> getResourceFetcher(final String model, int width, int height) {
        return new DataFetcher<PackageInfo>() {
            @Override
            public PackageInfo loadData(Priority priority) throws Exception {
                PackageManager pm = context.getPackageManager();
                android.content.pm.PackageInfo packageInfo = pm.getPackageArchiveInfo(model, 0);
                // The secret are these two lines....
                packageInfo.applicationInfo.sourceDir = model;
                packageInfo.applicationInfo.publicSourceDir = model;

                return packageInfo;
            }

            @Override
            public void cleanup() {

            }

            @Override
            public String getId() {
                return "PackageInfoModelLoader";
            }

            @Override
            public void cancel() {

            }
        };
    }
}