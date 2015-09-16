package com.jin.photopicker.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.jin.photopicker.R;
import com.jin.photopicker.model.PhotoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by 雅麟 on 2015/4/23.
 */
public class SinglePickPhotoAdapter extends MBaseAdapter<PhotoModel> {
    public interface OnItemClickedCallback {
        void onHeaderClicked();

        void onItemClicked(PhotoModel photo);
    }

    private static final int HEADER_TYPE = 1;
    private static final int ITEM_TYPE = 2;

    private int screenWidth;
    private int itemWidth;
    private int horizontalNum = 3;
    private boolean camera = true;
    private AbsListView.LayoutParams itemLayoutParams;

    private OnItemClickedCallback onItemClickedCallback;

    public SinglePickPhotoAdapter(Context context, ArrayList<PhotoModel> models) {
        super(context, models);
    }

    public SinglePickPhotoAdapter(Context context, ArrayList<PhotoModel> models, boolean camera, int screenWidth, OnItemClickedCallback onItemClickedCallback) {
        this(context, models);
        this.screenWidth = screenWidth;
        setItemWidth(screenWidth);
        this.camera = camera;
        this.onItemClickedCallback = onItemClickedCallback;
    }

    public void setItemWidth(int screenWidth) {
        int horizontalSpace = context.getResources().getDimensionPixelSize(R.dimen.sticky_item_horizontalSpacing);
        this.itemWidth = (screenWidth - (horizontalSpace * (horizontalNum - 1))) / horizontalNum;
        this.itemLayoutParams = new AbsListView.LayoutParams(itemWidth, itemWidth);
    }

    @Override
    public int getItemViewType(int position) {
        if (!camera) {
            return ITEM_TYPE;
        }
        if (position == 0) {
            return HEADER_TYPE;
        }
        return ITEM_TYPE;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView item;
        int type = getItemViewType(position);
        if (type == HEADER_TYPE) {
            View view = View.inflate(context, R.layout.list_item_camera, null);
            view.setLayoutParams(itemLayoutParams);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickedCallback != null) {
                        onItemClickedCallback.onHeaderClicked();
                    }
                }
            });
            return view;
        }
        if (convertView == null || !(convertView instanceof ImageView)) {
            item = new ImageView(context);
            item.setLayoutParams(itemLayoutParams);
            convertView = item;
        } else {
            item = (ImageView) convertView;
        }
        final PhotoModel photo = camera ? models.get(position - 1) : models.get(position);
        Picasso.with(context)
                .load("file://" + photo.getOriginalPath())
                .resize(screenWidth / 4, screenWidth / 4)
                .centerCrop()
                .error(context.getResources().getDrawable(R.drawable.ic_picture_loadfailed))
                .into(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickedCallback != null) {
                    onItemClickedCallback.onItemClicked(photo);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        if (!camera) {
            return super.getCount();
        }
        return super.getCount() + 1;
    }
}
