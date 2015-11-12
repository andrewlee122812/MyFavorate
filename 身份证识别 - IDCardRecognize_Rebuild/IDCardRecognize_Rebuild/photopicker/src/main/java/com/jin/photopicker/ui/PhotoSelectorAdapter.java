package com.jin.photopicker.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import com.jin.photopicker.R;
import com.jin.photopicker.model.PhotoModel;

import java.util.ArrayList;

/**
 * @author Aizaz AZ
 */


public class PhotoSelectorAdapter extends MBaseAdapter<PhotoModel> {
    public interface OnItemClickedCallback {
        void onHeaderClicked();
    }

    private static final int HEADER_TYPE = 1;
    private static final int ITEM_TYPE = 2;
    private int itemWidth;
    private int horizontalNum = 3;
    private PhotoItem.OnPhotoItemCheckedListener listener;
    private LayoutParams itemLayoutParams;
    private PhotoItem.OnItemClickListener mCallback;
    private OnItemClickedCallback onItemClickedCallback;

    private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
        super(context, models);
    }

    public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models, int screenWidth, PhotoItem.OnPhotoItemCheckedListener listener, PhotoItem.OnItemClickListener mCallback,
                                OnItemClickedCallback onItemClickedCallback) {
        this(context, models);
        setItemWidth(screenWidth);
        this.listener = listener;
        this.mCallback = mCallback;
        this.onItemClickedCallback = onItemClickedCallback;
    }

    /**
     * 设置每一个Item的宽高
     */
    public void setItemWidth(int screenWidth) {
        int horizontalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
        this.itemWidth = (screenWidth - (horizontalSpace * (horizontalNum - 1))) / horizontalNum;
        this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoItem item;
        int type = getItemViewType(position);
        if (type == HEADER_TYPE) {
            View view = View.inflate(context, R.layout.list_item_camera, null);
            view.setLayoutParams(itemLayoutParams);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickedCallback != null) {
                        onItemClickedCallback.onHeaderClicked();
                    }
                }
            });
            return view;
        }
        if (convertView == null || !(convertView instanceof PhotoItem)) {
            item = new PhotoItem(context, listener);
            item.setLayoutParams(itemLayoutParams);
            convertView = item;
        } else {
            item = (PhotoItem) convertView;
        }
        item.setImageDrawable(models.get(position - 1));
        item.setSelected(models.get(position - 1).isChecked());
        item.setOnClickListener(mCallback, position - 1);
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }
}
