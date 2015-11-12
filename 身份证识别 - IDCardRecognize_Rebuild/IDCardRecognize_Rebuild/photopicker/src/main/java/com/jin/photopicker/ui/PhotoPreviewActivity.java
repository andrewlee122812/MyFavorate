package com.jin.photopicker.ui;
/**
 *
 * @author Aizaz AZ
 *
 */

import android.os.Bundle;

import com.jin.photopicker.PhotoSelectorDomain;
import com.jin.photopicker.model.PhotoModel;
import com.jin.photopicker.util.CommonUtils;

import java.util.ArrayList;

public class PhotoPreviewActivity extends BasePhotoPreviewActivity implements PhotoSelectorActivity.OnLocalRecentListener {

    private PhotoSelectorDomain photoSelectorDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        init(getIntent().getExtras());
        btnComplete.setEnabled(true);
        int selectCount = checkedPhotos == null ? photos == null ? 0 : photos.size() : checkedPhotos.size();
        btnComplete.setText("完成(" + selectCount + "/" + maxCount + ")");
    }

    @SuppressWarnings("unchecked")
    protected void init(Bundle extras) {
        if (extras == null)
            return;
        maxCount = extras.getInt("maxCount", 0);
        if (extras.containsKey("photos")) {
            photos = (ArrayList<PhotoModel>) extras.getSerializable("photos");
            current = extras.getInt("position", 0);
            checkedPhotos = new ArrayList<>(photos);
            updatePercent();
            bindData();
        } else if (extras.containsKey("album")) {
            String albumName = extras.getString("album");
            current = extras.getInt("position");
            checkedPhotos = (ArrayList<PhotoModel>) extras.getSerializable("checked");
            if (!CommonUtils.isNull(albumName) && albumName.equals(PhotoSelectorActivity.RECENT_PHOTO)) {
                photoSelectorDomain.getRecent(this);
            } else {
                photoSelectorDomain.getAlbum(albumName, this);
            }
        }
        if (checkedPhotos == null) {
            checkedPhotos = new ArrayList<>();
        }
    }

    @Override
    public void onPhotoLoaded(ArrayList<PhotoModel> photos) {
        this.photos = photos;
        updatePercent();
        bindData();
    }

}
