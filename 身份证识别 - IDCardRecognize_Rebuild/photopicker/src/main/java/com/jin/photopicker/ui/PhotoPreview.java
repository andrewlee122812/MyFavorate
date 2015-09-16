package com.jin.photopicker.ui;
/**
 * @author Aizaz AZ
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jin.photopicker.R;
import com.jin.photopicker.model.PhotoModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoPreview extends LinearLayout {

    private ProgressBar pbLoading;
    private ImageView ivContent;

    public PhotoPreview(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_photopreview, this, true);

        pbLoading = (ProgressBar) findViewById(R.id.pb_loading_vpp);
        ivContent = (ImageView) findViewById(R.id.iv_content_vpp);
    }

    public PhotoPreview(Context context, AttributeSet attrs, int defStyle) {
        this(context);
    }

    public PhotoPreview(Context context, AttributeSet attrs) {
        this(context);
    }

    public void loadImage(PhotoModel photoModel) {
        loadImage("file://" + photoModel.getOriginalPath());
    }

    private void loadImage(String path) {
        Picasso.with(getContext())
                .load(path)
                .into(ivContent, new Callback() {
                    @Override
                    public void onSuccess() {
                        pbLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        pbLoading.setVisibility(View.GONE);
                    }
                });
    }
}
