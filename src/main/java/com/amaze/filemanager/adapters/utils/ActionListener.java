package com.amaze.filemanager.adapters.utils;

import com.amaze.filemanager.adapters.utils.holder.ViewHolder;

/**
 * Created by rpiotaix on 11/12/16.
 */

public interface ActionListener<DataClass> {
    void onListItemClicked(ViewHolder holder, DataClass data);

    boolean onLongClick(ViewHolder holder);

    void onIconClickListener(ViewHolder viewHolder);
}
