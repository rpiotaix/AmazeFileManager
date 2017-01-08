package com.amaze.filemanager.adapters.utils;

import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.holder.ViewHolder;
import com.amaze.filemanager.fragments.Main;

/**
 * Created by rpiotaix on 11/12/16.
 */

public class DumbActionListener implements ActionListener<DataHolder> {
    private final Recycleradapter adapter;
    private Main main;

    public DumbActionListener(Main main, Recycleradapter adapter) {
        this.main = main;
        this.adapter = adapter;
    }

    @Override
    public void onListItemClicked(ViewHolder holder, DataHolder data) {
        main.onListItemClicked(holder.getAdapterPosition(), holder.getCheckView());
    }

    @Override
    public boolean onLongClick(ViewHolder holder) {
        adapter.toggleChecked(holder.getAdapterPosition(), holder.getCheckView());
        return true;
    }

    @Override
    public void onIconClickListener(ViewHolder holder) {
        adapter.toggleChecked(holder.getAdapterPosition(), holder.getCheckView());
    }
}
