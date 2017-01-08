package com.amaze.filemanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amaze.filemanager.R;
import com.amaze.filemanager.activities.BaseActivity;
import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.adapters.utils.ActionListener;
import com.amaze.filemanager.adapters.utils.DataHolder;
import com.amaze.filemanager.adapters.utils.DumbActionListener;
import com.amaze.filemanager.adapters.utils.DumbDataHolder;
import com.amaze.filemanager.adapters.utils.config.SharedPreferencesConfigProvider;
import com.amaze.filemanager.adapters.utils.config.ViewConfigProvider;
import com.amaze.filemanager.adapters.utils.holder.ApkViewHolder;
import com.amaze.filemanager.adapters.utils.holder.MediaViewHolder;
import com.amaze.filemanager.adapters.utils.holder.ResourceViewHolder;
import com.amaze.filemanager.adapters.utils.holder.ViewHolder;
import com.amaze.filemanager.filesystem.BaseFile;
import com.amaze.filemanager.fragments.Main;
import com.amaze.filemanager.fragments.PropertiesSheet;
import com.amaze.filemanager.ui.Layoutelements;
import com.amaze.filemanager.ui.icons.Icons;
import com.amaze.filemanager.ui.views.CircleGradientDrawable;
import com.amaze.filemanager.utils.DataUtils;
import com.amaze.filemanager.utils.provider.UtilitiesProviderInterface;
import com.amaze.filemanager.utils.theme.AppTheme;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Arpit on 11-04-2015.
 */
public class Recycleradapter extends RecyclerArrayAdapter<String, RecyclerView.ViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    private UtilitiesProviderInterface utilsProvider;
    private ViewConfigProvider viewConfigProvider;
    private ActionListener<DataHolder> listener;

    private Main main;
    private ArrayList<Layoutelements> items;
    private Context context;
    private SparseBooleanArray myChecked = new SparseBooleanArray();
    private SparseBooleanArray myanim = new SparseBooleanArray();
    private LayoutInflater mInflater;
//    private int rowHeight;
//    private int grey_color;
//    private int c1;
//    private int c2;
//    private int c3;
//    private int c4;
//    private int c5;
//    private int c6;
//    private int c7;
//    private int c8;
//    private int c9;

    private int offset = 0;
    public boolean stoppedAnimation = false;

    public Context getContext() {
        return context;
    }

    public ActionListener<DataHolder> getListener() {
        return listener;
    }

    public Recycleradapter(Main main, UtilitiesProviderInterface utilsProvider, ArrayList<Layoutelements> items, Context context) {
        this.viewConfigProvider = new SharedPreferencesConfigProvider(PreferenceManager.getDefaultSharedPreferences(main.getContext()), main);
        this.listener = new DumbActionListener(main, this, viewConfigProvider);

        this.main = main;
        this.utilsProvider = utilsProvider;
        this.items = items;
        this.context = context;
        for (int i = 0; i < items.size(); i++) {
            myChecked.put(i, false);
            myanim.put(i, false);
        }
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        c1 = Color.parseColor("#757575"); // grey_600
//        c2 = Color.parseColor("#f06292"); // pink_300
//        c3 = Color.parseColor("#9575cd"); // deep_purple_300
//        c4 = Color.parseColor("#da4336"); // ?? red?
//        c5 = Color.parseColor("#00bfa5"); // teal_a_700
//        c6 = Color.parseColor("#e06055"); // ?? red?
//        c7 = Color.parseColor("#f9a825"); // ?? orange?
//        c8 = Color.parseColor("#a4c439"); // ?? green?
//        c9 = Color.parseColor("#9e9e9e"); // grey_500
//        rowHeight = main.dpToPx(100);
//        grey_color = Color.parseColor("#ff666666");
    }

    public void addItem() {
        //notifyDataSetChanged();
        notifyItemInserted(getItemCount());
    }

    /**
     * called as to toggle selection of any item in adapter
     *
     * @param position  the position of the item
     * @param imageView the check {@link CircleGradientDrawable} that is to be animated
     */
    public void toggleChecked(int position, ImageView imageView) {
        if (!stoppedAnimation) main.stopAnimation();
        if (myChecked.get(position)) {
            // if the view at position is checked, un-check it
            myChecked.put(position, false);

            Animation iconAnimation = AnimationUtils.loadAnimation(context, R.anim.check_out);
            if (imageView != null) {

                imageView.setAnimation(iconAnimation);
            } else {

                // TODO: we don't have the check icon object probably because of config change
            }
        } else {
            // if view is un-checked, check it
            myChecked.put(position, true);

            Animation iconAnimation = AnimationUtils.loadAnimation(context, R.anim.check_in);
            if (imageView != null) {

                imageView.setAnimation(iconAnimation);
            } else {

                // TODO: we don't have the check icon object probably because of config change
            }
            if (main.mActionMode == null || !main.selection) {
                // start actionmode if not already started
                // null condition if there is config change
                main.selection = true;
                main.mActionMode = main.MAIN_ACTIVITY.startSupportActionMode(main.mActionModeCallback);
            }
        }

        notifyDataSetChanged();
        //notifyItemChanged(position);
        if (main.mActionMode != null && main.selection) {
            // we have the actionmode visible, invalidate it's views
            main.mActionMode.invalidate();
        }
        if (getCheckedItemPositions().size() == 0) {
            main.selection = false;
            main.mActionMode.finish();
            main.mActionMode = null;
        }
    }

    public void toggleChecked(boolean b, String path) {
        int a;
        if (path.equals("/") || !main.GO_BACK_ITEM) {
            a = 0;
        } else {
            a = 1;
        }
        for (int i = a; i < items.size(); i++) {
            myChecked.put(i, b);
            notifyItemChanged(i);
        }
        if (main.mActionMode != null)
            main.mActionMode.invalidate();
        if (getCheckedItemPositions().size() == 0) {
            main.selection = false;
            if (main.mActionMode != null)
                main.mActionMode.finish();
            main.mActionMode = null;
        }
    }

    /**
     * called when we would want to toggle check for all items in the adapter
     *
     * @param b if to toggle true or false
     */
    public void toggleChecked(boolean b) {
        for (int i = 0; i < items.size(); i++) {
            myChecked.put(i, b);
            notifyItemChanged(i);
        }

        if (main.mActionMode != null) main.mActionMode.invalidate();
        if (getCheckedItemPositions().size() == 0) {
            main.selection = false;
            if (main.mActionMode != null)
                main.mActionMode.finish();
            main.mActionMode = null;
        }
    }

    public ArrayList<Integer> getCheckedItemPositions() {
        ArrayList<Integer> checkedItemPositions = new ArrayList<>();

        for (int i = 0; i < myChecked.size(); i++) {
            if (myChecked.get(i)) {
                (checkedItemPositions).add(i);
            }
        }

        return checkedItemPositions;
    }

    public boolean areAllChecked(String path) {
        boolean b = true;
        int a;
        if (path.equals("/") || !main.GO_BACK_ITEM) a = 0;
        else a = 1;
        for (int i = a; i < myChecked.size(); i++) {
            if (!myChecked.get(i)) {
                b = false;
            }
        }
        return b;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewConfigProvider.getConfig().isList() ? R.layout.rowlayout : R.layout.griditem;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        switch (viewType) {
            case TYPE_THUMBNAIL_MEDIA_VIEW:
                return new MediaViewHolder(this, view, viewConfigProvider, utilsProvider);

            case TYPE_THUMBNAIL_APK_VIEW:
                return new ApkViewHolder(this, view, viewConfigProvider, utilsProvider);

            default:
            case TYPE_RESOURCE_VIEW:
                return new ResourceViewHolder(this, view, viewConfigProvider, utilsProvider);
        }

    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ((ViewHolder) holder).rl.clearAnimation();
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        ((ViewHolder) holder).rl.clearAnimation();
        return super.onFailedToRecycleView(holder);
    }

    private void animate(ViewHolder holder) {
        holder.rl.clearAnimation();
        Animation localAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in_top);
        localAnimation.setStartOffset(this.offset);
        holder.rl.startAnimation(localAnimation);
        this.offset += 30;
    }

    public void generate(ArrayList<Layoutelements> arrayList) {
        offset = 0;
        stoppedAnimation = false;
        notifyDataSetChanged();
        items = arrayList;
        for (int i = 0; i < items.size(); i++) {
            myChecked.put(i, false);
            myanim.put(i, false);
        }
    }

    protected DataHolder dataHolderAtPos(int position) {
        return new DumbDataHolder(items.get(position));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, final int p) {
        final ViewHolder holder = ((ViewHolder) vholder);

        final DataHolder dataHolder = dataHolderAtPos(p);
        holder.render(dataHolder);


// TODO comment: additional space to pass over the fab icon
//if (main.IS_LIST) {
//    if (p == getItemCount() - 1) {
//        holder.rl.setMinimumHeight(rowHeight);
//        if (items.size() == (main.GO_BACK_ITEM ? 1 : 0))
//            holder.txtTitle.setText(R.string.nofiles);
//        else holder.txtTitle.setText("");
//        return;
//    }
//}
        if (!this.stoppedAnimation && !myanim.get(p)) {
            animate(holder);
            myanim.put(p, true);
        }
        final Layoutelements rowItem = items.get(p);
//        if (main.IS_LIST) {
//TODO comment: draw selection icon based on the app theme
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//    holder.checkImageView.setBackground(new CircleGradientDrawable(main.fabSkin,
//            utilsProvider.getAppTheme(), main.getResources().getDisplayMetrics()));
//} else
//    holder.checkImageView.setBackgroundDrawable(new CircleGradientDrawable(main.fabSkin,
//            utilsProvider.getAppTheme(), main.getResources().getDisplayMetrics()));
// TODO comment: setting the file type on the current element
//            int filetype = -1;
//            if (Icons.isPicture((rowItem.getDesc().toLowerCase()))) filetype = 0;
//            else if (Icons.isApk((rowItem.getDesc()))) filetype = 1;
//            else if (Icons.isVideo(rowItem.getDesc())) filetype = 2;
//            else if (Icons.isgeneric(rowItem.getDesc())) filetype = 3;
// TODO comment: setting the generic icon with the drawable given by the mime type of the current file
////            holder.genericIcon.setImageDrawable(rowItem.getImageId());
// TODO comment: emptying special test activated when unknown file is encountered (behavior is to display file ext in icon)
//            holder.genericText.setText("");
//
// TODO comment: contextual menu
//if (holder.about != null) {
//    if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT))
//        holder.about.setColorFilter(grey_color);
//    showPopup(holder.about, rowItem, p);
//}
// TODO comment: Event handler for click on the icon. ACTION=select item
//holder.genericIcon.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//        int id = v.getId();
//        if (id == R.id.generic_icon || id == R.id.picture_icon
//                || id == R.id.apk_icon) {
//
//            // TODO: transform icon on press to the properties dialog with animation
//            if (!rowItem.getSize().equals(main.goback)) {
//
//                toggleChecked(p, holder.checkImageView);
//            } else main.goBack();
//        }
//
//    }
//});
// TODO comment: same event as previously
//holder.pictureIcon.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        if (!rowItem.getSize().equals(main.goback)) {
//
//            toggleChecked(p, holder.checkImageView);
//        } else main.goBack();
//    }
//});
// TODO comment: same event as previously
//holder.apkIcon.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        if (!rowItem.getSize().equals(main.goback)) {
//
//            toggleChecked(p, holder.checkImageView);
//        } else main.goBack();
//    }
//});
//
//// TODO comment: resetting icons visibility
//holder.genericIcon.setVisibility(View.VISIBLE);
//holder.pictureIcon.setVisibility(View.INVISIBLE);
//holder.apkIcon.setVisibility(View.INVISIBLE);
//holder.checkImageView.setVisibility(View.INVISIBLE);
//
// TODO comment: setting icons for various cases
//            // apkIcon holder refers to square/non-circular drawable
//            // pictureIcon is circular drawable
//if (filetype == 0) { // TODO type 0=picture
//    if (main.SHOW_THUMBS) {
//        holder.genericIcon.setVisibility(View.GONE);
//
//        if (main.CIRCULAR_IMAGES) {
//            holder.apkIcon.setVisibility(View.GONE);
//            holder.pictureIcon.setVisibility(View.VISIBLE);
//            holder.pictureIcon.setImageDrawable(main.DARK_IMAGE);
//            main.ic.cancelLoad(holder.pictureIcon);
//            main.ic.loadDrawable(holder.pictureIcon, (rowItem.getDesc()), null);
//        } else {
//            holder.apkIcon.setVisibility(View.VISIBLE);
//            holder.apkIcon.setImageDrawable(main.DARK_IMAGE);
//            main.ic.cancelLoad(holder.apkIcon);
//            main.ic.loadDrawable(holder.apkIcon, (rowItem.getDesc()), null);
//        }
//    }
//            } else if (filetype == 1) { // TODO type 1=apk
//                if (main.SHOW_THUMBS) {
//                    holder.genericIcon.setVisibility(View.GONE);
//                    holder.pictureIcon.setVisibility(View.GONE);
//                    holder.apkIcon.setVisibility(View.VISIBLE);
//                    holder.apkIcon.setImageDrawable(main.apk);
//                    main.ic.cancelLoad(holder.apkIcon);
//                    main.ic.loadDrawable(holder.apkIcon, (rowItem.getDesc()), null);
//                }
//
//            } else if (filetype == 2) { // TODO type 2=video
//                if (main.SHOW_THUMBS) {
//                    holder.genericIcon.setVisibility(View.GONE);
//                    if (main.CIRCULAR_IMAGES) {
//                        holder.pictureIcon.setVisibility(View.VISIBLE);
//                        holder.pictureIcon.setImageDrawable(main.DARK_VIDEO);
//                        main.ic.cancelLoad(holder.pictureIcon);
//                        main.ic.loadDrawable(holder.pictureIcon, (rowItem.getDesc()), null);
//                    } else {
//                        holder.apkIcon.setVisibility(View.VISIBLE);
//                        holder.apkIcon.setImageDrawable(main.DARK_VIDEO);
//                        main.ic.cancelLoad(holder.apkIcon);
//                        main.ic.loadDrawable(holder.apkIcon, (rowItem.getDesc()), null);
//                    }
//                }
//            } else if (filetype == 3) { // TODO type 3=generic (all other cases)
// TODO comment: file ext found but unknown: put the ext as icon
//                // if the file type is any unknown variable
//                String ext = !new File(rowItem.getDesc()).isDirectory()
//                        ? MimeTypes.getExtension(rowItem.getTitle()) : null;
//                if (ext != null && ext.trim().length() != 0) {
//                    holder.genericText.setText(ext);
//                    holder.genericIcon.setImageDrawable(null);
//                    //holder.genericIcon.setVisibility(View.INVISIBLE);
//                } else {
// TODO comment: no ext = default icon
//                    // we could not find the extension, set a generic file type icon
//                    // probably a directory
//                    holder.genericIcon.setVisibility(View.VISIBLE);
//                }
//                holder.pictureIcon.setVisibility(View.GONE);
//                holder.apkIcon.setVisibility(View.GONE);
//
//            } else {
// TODO comment: safe case => display default icon
//                holder.pictureIcon.setVisibility(View.GONE);
//                holder.apkIcon.setVisibility(View.GONE);
//                holder.genericIcon.setVisibility(View.VISIBLE);
//            }
//
// TODO comment: handle selection style
//            Boolean checked = myChecked.get(p);
//if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT)) {
//
//    holder.rl.setBackgroundResource(R.drawable.safr_ripple_white);
//} else {
//
//    holder.rl.setBackgroundResource(R.drawable.safr_ripple_black);
//}
// TODO comment: selection: reset selection on the current item to default
//holder.rl.setSelected(false);
// TODO comment: selection: handle
//if (checked) {
//    holder.checkImageView.setVisibility(View.VISIBLE);
//                // making sure the generic icon background color filter doesn't get changed
//                // to grey on picture/video/apk/generic text icons when checked
//                // so that user can still look at the thumbs even after selection
//                if ((filetype != 0 && filetype != 1 && filetype != 2)) {
//                    holder.apkIcon.setVisibility(View.GONE);
//                    holder.pictureIcon.setVisibility(View.GONE);
//                    holder.genericIcon.setVisibility(View.VISIBLE);
//                    GradientDrawable gradientDrawable = (GradientDrawable) holder.genericIcon.getBackground();
//                    gradientDrawable.setColor(c1);
//                }
//holder.rl.setSelected(true);
//            } else { // TODO comment: selection: not checked
//                holder.checkImageView.setVisibility(View.INVISIBLE);
//                GradientDrawable gradientDrawable = (GradientDrawable) holder.genericIcon.getBackground();
// TODO comment: background color based on data type
//                if (main.COLORISE_ICONS) {
//                    if (rowItem.isDirectory())
//                        gradientDrawable.setColor(main.icon_skin_color);
//                    else if (Icons.isVideo(rowItem.getDesc()) || Icons.isPicture(rowItem
//                            .getDesc()))
//                        gradientDrawable.setColor(c2);
//                    else if (Icons.isAudio(rowItem.getDesc()))
//                        gradientDrawable.setColor(c3);
//                    else if (Icons.isPdf(rowItem.getDesc()))
//                        gradientDrawable.setColor(c4);
//                    else if (Icons.isCode(rowItem.getDesc()))
//                        gradientDrawable.setColor(c5);
//                    else if (Icons.isText(rowItem.getDesc()))
//                        gradientDrawable.setColor(c6);
//                    else if (Icons.isArchive(rowItem.getDesc()))
//                        gradientDrawable.setColor(c7);
//                    else if (Icons.isApk(rowItem.getDesc()))
//                        gradientDrawable.setColor(c8);
//                    else if (Icons.isgeneric(rowItem.getDesc())) {
//                        gradientDrawable.setColor(c9);
//                    } else {
//                        gradientDrawable.setColor(main.icon_skin_color);
//                    }
//                } else gradientDrawable.setColor((main.icon_skin_color));
//                if (rowItem.getSize().equals(main.goback))
//                    gradientDrawable.setColor(c1);
//
//
//            }
//if (main.SHOW_PERMISSIONS)
//    holder.perm.setText(rowItem.getPermissions());
//if (main.SHOW_LAST_MODIFIED)
//    holder.date.setText(rowItem.getDate());

//String size = rowItem.getSize();
//
//if (size.equals(main.goback)) {
//
//    holder.date.setText(size);
//
//    holder.txtDesc.setText("");
//} else if (main.SHOW_SIZE)
//
//    holder.txtDesc.setText(rowItem.getSize());
//} else {


// TODO comment: GRID VIEW
//            // view is a grid view
//            Boolean checked = myChecked.get(p);
//
//            holder.checkImageViewGrid.setColorFilter(Color.parseColor(main.fabSkin));
////            holder.rl.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    main.onListItemClicked(p, holder.checkImageViewGrid);
////                }
////            });
//
//            holder.imageView1.setVisibility(View.INVISIBLE);
//            holder.genericIcon.setVisibility(View.VISIBLE);
//            holder.checkImageViewGrid.setVisibility(View.INVISIBLE);
//            holder.genericIcon.setImageDrawable(rowItem.getImageId());
//
//            if (Icons.isPicture((rowItem.getDesc().toLowerCase())) || Icons.isVideo(rowItem.getDesc().toLowerCase())) {
//                holder.genericIcon.setColorFilter(null);
//                holder.imageView1.setVisibility(View.VISIBLE);
//                holder.imageView1.setImageDrawable(null);
//                if (utilsProvider.getAppTheme().equals(AppTheme.DARK))
//                    holder.imageView1.setBackgroundColor(Color.BLACK);
//                main.ic.cancelLoad(holder.imageView1);
//                main.ic.loadDrawable(holder.imageView1, (rowItem.getDesc()), null);
//            } else if (Icons.isApk((rowItem.getDesc()))) {
//                holder.genericIcon.setColorFilter(null);
//                main.ic.cancelLoad(holder.genericIcon);
//                main.ic.loadDrawable(holder.genericIcon, (rowItem.getDesc()), null);
//            }
//            if (rowItem.isDirectory())
//                holder.genericIcon.setColorFilter(main.icon_skin_color);
//            else if (Icons.isVideo(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c2);
//            else if (Icons.isAudio(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c3);
//            else if (Icons.isPdf(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c4);
//            else if (Icons.isCode(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c5);
//            else if (Icons.isText(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c6);
//            else if (Icons.isArchive(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c7);
//            else if (Icons.isgeneric(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(c9);
//            else if (Icons.isApk(rowItem.getDesc()) || Icons.isPicture(rowItem.getDesc()))
//                holder.genericIcon.setColorFilter(null);
//            else holder.genericIcon.setColorFilter(main.icon_skin_color);
//            if (rowItem.getSize().equals(main.goback))
//                holder.genericIcon.setColorFilter(c1);
//
//            if (checked) {
//                holder.genericIcon.setColorFilter(main.icon_skin_color);
//                //holder.genericIcon.setImageDrawable(main.getResources().getDrawable(R.drawable.abc_ic_cab_done_holo_dark));
//
//                holder.checkImageViewGrid.setVisibility(View.VISIBLE);
//                holder.rl.setBackgroundColor(Color.parseColor("#9f757575"));
//            } else {
//                holder.checkImageViewGrid.setVisibility(View.INVISIBLE);
//                if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT))
//                    holder.rl.setBackgroundResource(R.drawable.item_doc_grid);
//                else {
//                    holder.rl.setBackgroundResource(R.drawable.ic_grid_card_background_dark);
//                    holder.rl.findViewById(R.id.icon_frame).setBackgroundColor(Color.parseColor("#303030"));
//                }
//            }
//
//            if (holder.about != null) {
//                if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT))
//                    holder.about.setColorFilter(grey_color);
//                showPopup(holder.about, rowItem, p);
//            }
    }

    @Override
    public long getHeaderId(int i) {
        if (items.size() == 0) return -1;
        if (i >= 0 && i < items.size())
            if (main.IS_LIST) {
                if (i != items.size()) {
                    if (items.get(i).getSize().equals(main.goback)) return -1;
                    if (items.get(i).isDirectory()) return 'D';
                    else return 'F';
                }
            }
        return -1;
    }

    public boolean isChecked(int position) {
        return myChecked.get(position, false);
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView ext;

        public HeaderViewHolder(View view) {
            super(view);

            ext = (TextView) view.findViewById(R.id.headertext);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.listheader, viewGroup, false);
        /*if(utilsProvider.getAppTheme().equals(AppTheme.DARK))
            view.setBackgroundResource(R.color.holo_dark_background);*/
        HeaderViewHolder holder = new HeaderViewHolder(view);
        if (utilsProvider.getAppTheme().equals(AppTheme.LIGHT))
            holder.ext.setTextColor(Color.parseColor("#8A000000"));
        else holder.ext.setTextColor(Color.parseColor("#B3ffffff"));
        return holder;
    }

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_RESOURCE_VIEW = 2;
    private static final int TYPE_THUMBNAIL_MEDIA_VIEW = 3;
    private static final int TYPE_THUMBNAIL_APK_VIEW = 4;


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return getTypeForItem(dataHolderAtPos(position));
    }

    private int getTypeForItem(DataHolder dataHolder) {
        if (dataHolder.getType() == DataHolder.Type.FILE
                && viewConfigProvider.getConfig().showThumbs()
                && hasThumb(dataHolder)) {
            if (isApk(dataHolder.getMimeType())) {
                return TYPE_THUMBNAIL_APK_VIEW;
            } else {
                return TYPE_THUMBNAIL_MEDIA_VIEW;
            }
        } else {
            return TYPE_RESOURCE_VIEW;
        }
    }

    private boolean isApk(String mimeType) {
        return mimeType.equals("application/vnd.android.package-archive");
    }

    /**
     * TODO: 18/12/16 rewrite this
     */
    private boolean hasThumb(DataHolder data) {
        int iconRes = Icons.iconFromMimeType(data.getMimeType());

        return iconRes == R.drawable.ic_doc_image
                || iconRes == R.drawable.ic_doc_video_am
                || iconRes == R.drawable.ic_doc_apk_white;
    }

    public void showPopup(View v, final ViewHolder holder) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = holder.getAdapterPosition();
                final Layoutelements rowItem = items.get(position);
                PopupMenu popupMenu = new PopupMenu(main.getActivity(), view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.about:
                                utilsProvider.getFutils().showProps((rowItem).generateBaseFile(),
                                        rowItem.getPermissions(), main,
                                        BaseActivity.rootMode, utilsProvider.getAppTheme());
                                /*PropertiesSheet propertiesSheet = new PropertiesSheet();
                                Bundle arguments = new Bundle();
                                arguments.putParcelable(PropertiesSheet.KEY_FILE, rowItem.generateBaseFile());
                                arguments.putString(PropertiesSheet.KEY_PERMISSION, rowItem.getPermissions());
                                arguments.putBoolean(PropertiesSheet.KEY_ROOT, BaseActivity.rootMode);
                                propertiesSheet.setArguments(arguments);
                                propertiesSheet.show(main.getFragmentManager(), PropertiesSheet.TAG_FRAGMENT);*/
                                return true;
                            case R.id.share:
                                ArrayList<File> arrayList = new ArrayList<>();
                                arrayList.add(new File(rowItem.getDesc()));
                                utilsProvider.getFutils().shareFiles(arrayList, main.MAIN_ACTIVITY, utilsProvider.getAppTheme(), Color.parseColor(main.fabSkin));
                                return true;
                            case R.id.rename:
                                main.rename(rowItem.generateBaseFile());
                                return true;
                            case R.id.cpy:
                                MainActivity MAIN_ACTIVITY = main.MAIN_ACTIVITY;
                                main.MAIN_ACTIVITY.MOVE_PATH = null;
                                ArrayList<BaseFile> copies = new ArrayList<>();
                                copies.add(rowItem.generateBaseFile());
                                MAIN_ACTIVITY.COPY_PATH = copies;
                                MAIN_ACTIVITY.supportInvalidateOptionsMenu();
                                return true;
                            case R.id.cut:
                                MainActivity MAIN_ACTIVITY1 = main.MAIN_ACTIVITY;
                                MAIN_ACTIVITY1.COPY_PATH = null;
                                ArrayList<BaseFile> copie = new ArrayList<>();
                                copie.add(rowItem.generateBaseFile());
                                MAIN_ACTIVITY1.MOVE_PATH = copie;
                                MAIN_ACTIVITY1.supportInvalidateOptionsMenu();
                                return true;
                            case R.id.ex:
                                main.MAIN_ACTIVITY.mainActivityHelper.extractFile(new File(rowItem.getDesc()));
                                return true;
                            case R.id.book:
                                DataUtils.addBook(new String[]{rowItem.getTitle(), rowItem.getDesc()}, true);
                                main.MAIN_ACTIVITY.updateDrawer();
                                Toast.makeText(main.getActivity(), main.getResources().getString(R.string.bookmarksadded), Toast.LENGTH_LONG).show();
                                return true;
                            case R.id.delete:
                                ArrayList<Integer> positions = new ArrayList<>();
                                positions.add(position);
                                utilsProvider.getFutils().deleteFiles(main.LIST_ELEMENTS, main, positions, utilsProvider.getAppTheme());
                                return true;
                            case R.id.open_with:
                                utilsProvider.getFutils().openWith(new File(rowItem.getDesc()), main.getActivity());
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.item_extras);
                String x = rowItem.getDesc().toLowerCase();
                if (rowItem.isDirectory()) {
                    popupMenu.getMenu().findItem(R.id.open_with).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.share).setVisible(false);
                } else {
                    popupMenu.getMenu().findItem(R.id.book).setVisible(false);
                }
                if (x.endsWith(".zip") || x.endsWith(".jar") || x.endsWith(".apk") || x.endsWith(".rar") || x.endsWith(".tar") || x.endsWith(".tar.gz"))
                    popupMenu.getMenu().findItem(R.id.ex).setVisible(true);
                popupMenu.show();
            }
        });

    }

    private boolean isPositionHeader(int position) {
        if (main.IS_LIST)
            return (position == items.size());
        return false;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (i != getItemCount() - 1) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            if (items.get(i).isDirectory()) holder.ext.setText(R.string.directories);
            else holder.ext.setText(R.string.files);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
