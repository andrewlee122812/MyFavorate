package com.jin.photopicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jin.photopicker.Constants;
import com.jin.photopicker.PhotoSelectorDomain;
import com.jin.photopicker.R;
import com.jin.photopicker.crop.Crop;
import com.jin.photopicker.model.AlbumModel;
import com.jin.photopicker.model.PhotoModel;
import com.jin.photopicker.util.AnimationUtil;
import com.jin.photopicker.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by 雅麟 on 2015/4/23.
 */
public class SinglePhotoSelectorActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SinglePickPhotoAdapter.OnItemClickedCallback {
    private static final int REQUEST_CAMERA = 1;
    public static final String CROP_KEY = "crop";
    public static final String RESULT_KEY = "photo";
    public static final String CAMERA_KEY = "camera";
    public static final String NAME_KEY = "name";

    Uri imageUri;

    private GridView gvPhotos;
    private ListView lvAblum;
    private TextView tvAlbum;
    private RelativeLayout layoutAlbum;

    private AlbumAdapter albumAdapter;
    private PhotoSelectorDomain photoSelectorDomain;

    private SinglePickPhotoAdapter photoAdapter;

    boolean camera = true;
    boolean crop = true;
    String name;

    public static String RECENT_PHOTO = null;

    public static void pick(Activity activity, boolean crop, int requestCode) {
        Intent intent = new Intent(activity, SinglePhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(CROP_KEY, crop);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void pick(Fragment fragment, boolean crop, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), SinglePhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(CROP_KEY, crop);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static void pick(Activity activity, boolean crop, boolean camera, int requestCode, String name) {
        Intent intent = new Intent(activity, SinglePhotoSelectorActivity.class);
        intent.putExtra(CROP_KEY, crop);
        intent.putExtra(CAMERA_KEY, camera);
        intent.putExtra(NAME_KEY, name);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_pick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        crop = getIntent().getBooleanExtra(CROP_KEY, true);
        camera = getIntent().getBooleanExtra(CAMERA_KEY, true);
        name = getIntent().getStringExtra(NAME_KEY);

        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
        tvAlbum.setOnClickListener(this);

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        photoAdapter = new SinglePickPhotoAdapter(getApplicationContext(),
                new ArrayList<PhotoModel>(), camera, CommonUtils.getWidthPixels(this),
                this);
        gvPhotos.setAdapter(photoAdapter);
        gvPhotos.setOnItemClickListener(this);

        albumAdapter = new AlbumAdapter(getApplicationContext(),
                new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);

        photoSelectorDomain.getRecent(recentListener);
        photoSelectorDomain.updateAlbum(albumListener);

        crop = getIntent().getBooleanExtra(CROP_KEY, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
            if (i == position)
                album.setCheck(true);
            else
                album.setCheck(false);
        }
        albumAdapter.notifyDataSetChanged();
        hideAlbum();
        tvAlbum.setText(current.getName());

        if (position == 0)
            photoSelectorDomain.getRecent(recentListener);
        else
            photoSelectorDomain.getAlbum(current.getName(), recentListener);

    }

    private void album() {
        if (layoutAlbum.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    private void popAlbum() {
        layoutAlbum.setVisibility(View.VISIBLE);
        new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
                .setLinearInterpolator().startAnimation(layoutAlbum);
    }

    private void hideAlbum() {
        new AnimationUtil(getApplicationContext(), R.anim.translate_down)
                .setLinearInterpolator().startAnimation(layoutAlbum);
        layoutAlbum.setVisibility(View.GONE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    private PhotoSelectorActivity.OnLocalAlbumListener albumListener = new PhotoSelectorActivity.OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(ArrayList<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };

    private PhotoSelectorActivity.OnLocalRecentListener recentListener = new PhotoSelectorActivity.OnLocalRecentListener() {
        @Override
        public void onPhotoLoaded(ArrayList<PhotoModel> photos) {
            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0);
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_album_ar) {
            album();
        } else if (id == R.id.tv_camera_vc) {
            catchPicture();
        }
    }

    private void catchPicture() {
        File file = new File(Environment.getExternalStorageDirectory(), Constants.TAKE_PHOTO_STORE_NAME);
        file.getParentFile().mkdirs();
        imageUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        CommonUtils.launchActivityForResult(this, intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            PhotoModel photoModel = new PhotoModel(imageUri.getPath());
            if (crop) {
                startCrop(photoModel);
                return;
            }

            returnPhoto(photoModel);
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
    }

    @Override
    public void onHeaderClicked() {
        catchPicture();
    }

    @Override
    public void onItemClicked(PhotoModel photo) {
        if (crop) {
            startCrop(photo);
            return;
        }
        returnPhoto(photo);

    }

    private void startCrop(PhotoModel photoModel) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(Uri.parse("file://" + photoModel.getOriginalPath()), destination).asSquare().start(this);
        return;
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            returnPhoto(new PhotoModel(Crop.getOutput(result).getPath()));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void returnPhoto(PhotoModel photo) {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESULT_KEY, photo);
        data.putExtras(bundle);
        setResult(RESULT_OK, data);
        finish();
    }
}
