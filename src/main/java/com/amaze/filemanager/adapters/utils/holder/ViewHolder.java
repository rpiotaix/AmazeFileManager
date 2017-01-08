package com.amaze.filemanager.adapters.utils.holder;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amaze.filemanager.R;
import com.amaze.filemanager.adapters.Recycleradapter;
import com.amaze.filemanager.adapters.utils.DataHolder;
import com.amaze.filemanager.adapters.utils.IconTarget;
import com.amaze.filemanager.adapters.utils.ViewConfig;
import com.amaze.filemanager.adapters.utils.ViewConfigProvider;
import com.amaze.filemanager.ui.views.CircleGradientDrawable;
import com.amaze.filemanager.ui.views.RoundedImageView;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.amaze.filemanager.utils.color.ColorPreference;
import com.amaze.filemanager.utils.color.ColorUsage;
import com.amaze.filemanager.utils.theme.AppTheme;

public abstract class ViewHolder extends RecyclerView.ViewHolder implements ViewConfigProvider {
    private Context viewContext;
    private UtilitiesProviderInterface utilsProvider;
    public TextView date;
    public View rl;
    public ImageButton about;
    private IconTarget iconTarget;
    // each data item is just a string in this case
    private Recycleradapter adapter;
    private ViewConfigProvider configProvider;
    private DataHolder data;
    @Deprecated
    private RoundedImageView pictureIcon;
    protected ImageView icon;
    @Deprecated
    private ImageView apkIcon;
    @Deprecated
    private ImageView imageView1;
    private TextView txtTitle;
    private TextView txtDesc;
    private TextView perm;
    private TextView genericText;
    private ImageView checkImageView;
    private ImageView checkImageViewGrid;

    public ViewHolder(Recycleradapter adapter,
                      View view,
                      ViewConfigProvider configProvider,
                      UtilitiesProviderInterface utilsProvider) {
        super(view);
        this.viewContext = view.getContext();
        this.utilsProvider = utilsProvider;

        this.adapter = adapter;
        this.configProvider = configProvider;

        takeRefs(view);

        this.iconTarget = createIconTarget(icon);

        configureEventHandlers();

        setCheckIcons();

        applySelectionStyle();

        if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT))
            // TODO change color here and put correct one (from recycleradapter)
            about.setColorFilter(getViewContext().getResources().getColor(R.color.selection_grey));
        getAdapter().showPopup(about, this);
    }

    private void setCheckIcons() {
        int accentColor = getUtilsProvider().getColorPreference()
                                            .getColor(ColorUsage.ACCENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getCheckView().setBackground(
                    new CircleGradientDrawable(
                            accentColor,
                            utilsProvider.getAppTheme(),
                            getViewContext().getResources().getDisplayMetrics()
                    )
            );
        } else
            getCheckView().setBackgroundDrawable(
                    new CircleGradientDrawable(
                            accentColor,
                            getUtilsProvider().getAppTheme(),
                            getViewContext().getResources().getDisplayMetrics()
                    )
            );
    }

    public UtilitiesProviderInterface getUtilsProvider() {
        return utilsProvider;
    }

    protected Context getViewContext() {
        return viewContext;
    }

    @Override
    public ViewConfig getConfig() {
        return configProvider.getConfig();
    }

    public DataHolder getData() {
        return data;
    }

    protected IconTarget createIconTarget(ImageView view) {
        return new IconTarget(view, getUtilsProvider().getColorPreference());
    }

    @Deprecated
    public ColorPreference getColorPreference() {
        return getUtilsProvider().getColorPreference();
    }

    public IconTarget getIconTarget() {
        return iconTarget;
    }

    public Recycleradapter getAdapter() {
        return adapter;
    }

    public ImageView getCheckView() {
        return configProvider.getConfig().isList() ? checkImageView : checkImageViewGrid;
    }

    private void configureEventHandlers() {
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getListener().onListItemClicked(ViewHolder.this, data);
            }
        });
        rl.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View p1) {
                return adapter.getListener().onLongClick(ViewHolder.this);
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getListener().onIconClickListener(ViewHolder.this);
            }
        });
    }

    private void takeRefs(@NonNull View view) {
        txtTitle = (TextView) view.findViewById(R.id.firstline);
        pictureIcon = (RoundedImageView) view.findViewById(R.id.picture_icon);
        rl = view.findViewById(R.id.second);
        perm = (TextView) view.findViewById(R.id.permis);
        date = (TextView) view.findViewById(R.id.date);
        txtDesc = (TextView) view.findViewById(R.id.secondLine);
        apkIcon = (ImageView) view.findViewById(R.id.apk_icon);
        genericText = (TextView) view.findViewById(R.id.generictext);
        about = (ImageButton) view.findViewById(R.id.properties);
        checkImageView = (ImageView) view.findViewById(R.id.check_icon);
        icon = (ImageView) view.findViewById(R.id.generic_icon);

        //GRID ONLY
        checkImageViewGrid = (ImageView) view.findViewById(R.id.check_icon_grid);
        imageView1 = (ImageView) view.findViewById(R.id.icon_thumb);
    }

    public void render(@Nullable DataHolder data) {
        this.data = data;
        //TODO remove?
        if (data == null) {
            return;
        }
        // HACKS
        hacks();

        txtTitle.setText(data.getTitle());
        updateDate();

        handleSelection();

        loadIcon();

        if (getConfig().showPermissions()) {
            perm.setText(data.getPermissionsAsString());
        }

        if (getConfig().showSize()) {
            txtDesc.setText(data.getSizeAsString());
        }

    }

    protected void handleSelection() {
        boolean selected = isSelected();
        iconTarget.setSelected(selected);

        rl.setSelected(selected);

        getCheckView().setVisibility(selected ? View.VISIBLE : View.GONE);

    }

    private void applySelectionStyle() {
        int backgroundResource = R.drawable.safr_ripple_white;

        if (utilsProvider.getAppTheme().equals(AppTheme.DARK)) {
            backgroundResource = R.drawable.safr_ripple_black;
        }

        rl.setBackgroundResource(backgroundResource);
    }

    /**
     * TODO clean this
     */
    @Deprecated
    private void hacks() {
        if (apkIcon != null)
            apkIcon.setVisibility(View.GONE);
        if (pictureIcon != null)
            pictureIcon.setVisibility(View.GONE);
        if (icon != null)
            icon.setVisibility(View.VISIBLE);
        if (imageView1 != null)
            imageView1.setVisibility(View.GONE);
    }

    private int dpToPx(int dp) {
        return Math.round(adapter.getContext().getResources().getDisplayMetrics().density * dp);
    }

    protected abstract void loadIcon();

    private void updateDate() {
        if (configProvider.getConfig().showLastModified()) {
            date.setText(data.getLastModified());
        } else {
            date.setText("");
        }
    }

    public boolean isSelected() {
        return getAdapter().isChecked(getAdapterPosition());
    }
}