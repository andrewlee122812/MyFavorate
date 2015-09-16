package com.jin.photopicker.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jin.photopicker.R;
import com.jin.photopicker.model.PhotoModel;
import com.squareup.picasso.Picasso;

/**
 * @author Aizaz AZ
 */

public class PhotoItem extends LinearLayout implements View.OnClickListener {

    private ImageView ivPhoto;
    private CheckBox cbPhoto;
    private LinearLayout llPhoto;
    private OnPhotoItemCheckedListener listener;
    private PhotoModel photo;
    private boolean isCheckAll;
    private OnItemClickListener l;
    private int position;

    private PhotoItem(Context context) {
        super(context);
    }

    public PhotoItem(Context context, final OnPhotoItemCheckedListener listener) {
        this(context);
        LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this,
                true);
        this.listener = listener;

        setOnClickListener(this);

        ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
        cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);
        llPhoto = (LinearLayout) findViewById(R.id.ll_photo_lpsi);
        llPhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = cbPhoto.isChecked();
                if (!isChecked) {
                    if (!listener.canChecked()) {
                        listener.onCheckedMax();
                        return;
                    }
                }
                cbPhoto.setChecked(!isChecked);
                if (!isCheckAll) {
                    listener.onCheckedChanged(photo, cbPhoto, !isChecked); // 调用主界面回调函数
                }
                // 让图片变暗或者变亮
                if (!isChecked) {
                    setDrawingable();
                    ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                } else {
                    ivPhoto.clearColorFilter();
                }
                photo.setChecked(!isChecked);
            }
        });
    }

    /**
     * 设置路径下的图片对应的缩略图
     */
    public void setImageDrawable(final PhotoModel photo) {
        this.photo = photo;
        // You may need this setting form some custom ROM(s)
        /*
         * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { ImageLoader.getInstance().displayImage(
		 * "file://" + photo.getOriginalPath(), ivPhoto); } }, new
		 * Random().nextInt(10));
		 */
//        ImageLoader.getInstance().displayImage(
//                "file://" + photo.getOriginalPath(), ivPhoto);
        Picasso.with(getContext())
                .load("file://" + photo.getOriginalPath())
                .resize(200, 200)
                .centerCrop()
                .error(getResources().getDrawable(R.drawable.ic_picture_loadfailed))
                .into(ivPhoto);
    }

    public void setImageDrawable(final int res) {
        ivPhoto.setImageResource(res);
    }

    private void setDrawingable() {
        ivPhoto.setDrawingCacheEnabled(true);
        ivPhoto.buildDrawingCache();
    }

    @Override
    public void setSelected(boolean selected) {
        if (photo == null) {
            return;
        }
        isCheckAll = true;
        cbPhoto.setChecked(selected);
        isCheckAll = false;
    }

    public void setOnClickListener(OnItemClickListener l, int position) {
        this.l = l;
        this.position = position;
    }

    // @Override
    // public void
    // onClick(View v) {
    // if (l != null)
    // l.onItemClick(position);
    // }

    /**
     * 图片Item选中事件监听器
     */
    public static interface OnPhotoItemCheckedListener {
        public void onCheckedChanged(PhotoModel photoModel,
                                     CompoundButton buttonView, boolean isChecked);

        public boolean canChecked();

        public void onCheckedMax();
    }

    /**
     * 图片点击事件
     */
    public interface OnItemClickListener {
        public void onItemClick(int position);
    }

    @Override
    public void onClick(View v) {
        if (l != null)
            l.onItemClick(position);
    }
}
