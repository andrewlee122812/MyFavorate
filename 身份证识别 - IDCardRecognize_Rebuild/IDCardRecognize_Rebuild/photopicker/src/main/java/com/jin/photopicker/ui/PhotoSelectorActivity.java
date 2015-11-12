package com.jin.photopicker.ui;
/**
 * @author Aizaz AZ
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jin.photopicker.PhotoSelectorDomain;
import com.jin.photopicker.R;
import com.jin.photopicker.model.AlbumModel;
import com.jin.photopicker.model.PhotoModel;
import com.jin.photopicker.util.AnimationUtil;
import com.jin.photopicker.util.CommonUtils;

import java.util.ArrayList;

/**
 * @author Aizaz AZ
 */
public class PhotoSelectorActivity extends ActionBarActivity implements
        PhotoItem.OnItemClickListener, PhotoItem.OnPhotoItemCheckedListener, AdapterView.OnItemClickListener,
        OnClickListener, PhotoSelectorAdapter.OnItemClickedCallback {

    public static final String RESULT_KEY = "photos";

    public static final String KEY_MAX = "key_max";
    private int MAX_IMAGE;

    public static final int REQUEST_PHOTO = 0;
    private static final int REQUEST_CAMERA = 1;

    public static String RECENT_PHOTO = null;


    private GridView gvPhotos;
    private ListView lvAblum;
    private TextView tvAlbum, tvPreview;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoSelectorAdapter photoAdapter;
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutAlbum;
    private ArrayList<PhotoModel> selected;

    private Button btnComplete;

    public static void arrayPick(Activity activity, int count, int requestCode) {
        Intent intent = new Intent(activity, PhotoSelectorActivity.class);
        intent.putExtra(PhotoSelectorActivity.KEY_MAX, count);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void pick(Fragment fragment, int count, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), PhotoSelectorActivity.class);
        intent.putExtra(PhotoSelectorActivity.KEY_MAX, count);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RECENT_PHOTO = getResources().getString(R.string.all_photos);
        setContentView(R.layout.activity_photoselector);

        btnComplete = (Button) findViewById(R.id.toolbar_btn_complete);
        btnComplete.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (getIntent().getExtras() != null) {
            MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 10);
        }

        photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());

        selected = new ArrayList<>();

        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);

        tvAlbum.setOnClickListener(this);
        tvPreview.setOnClickListener(this);

        photoAdapter = new PhotoSelectorAdapter(getApplicationContext(),
                new ArrayList<PhotoModel>(), CommonUtils.getWidthPixels(this),
                this, this, this);
        gvPhotos.setAdapter(photoAdapter);

        albumAdapter = new AlbumAdapter(getApplicationContext(),
                new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);

        photoSelectorDomain.getRecent(recentListener);
        photoSelectorDomain.updateAlbum(albumListener);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_album_ar) {
            album();
        } else if (id == R.id.tv_preview_ar) {
            preview();
        } else if (id == R.id.toolbar_btn_complete) {
            ok();
        }
    }

    private void catchPicture() {
        CommonUtils.launchActivityForResult(this, new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            PhotoModel photoModel = new PhotoModel(CommonUtils.query(
                    getApplicationContext(), data.getData()));
            if (selected.size() >= MAX_IMAGE) {
                Toast.makeText(
                        this,
                        String.format(
                                getString(R.string.max_img_limit_reached),
                                MAX_IMAGE), Toast.LENGTH_SHORT).show();
                photoModel.setChecked(false);
                photoAdapter.notifyDataSetChanged();
            } else {
                if (!selected.contains(photoModel)) {
                    selected.add(photoModel);
                }
            }
            ok();
        } else if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            int status = extras.getInt("status");
            selected = (ArrayList<PhotoModel>) extras.getSerializable("checked");
            if (status == PhotoPreviewActivity.BACK_STATUS) {
                ArrayList<PhotoModel> photos = photoAdapter.getItems();
                for (PhotoModel model : photos) {
                    if (selected.contains(model)) {
                        model.setChecked(true);
                    }
                }
                photoAdapter.notifyDataSetChanged();
                initView();
            } else {
                ok();
            }
        }
    }

    private void ok() {
        if (selected.isEmpty()) {
            setResult(RESULT_CANCELED);
        } else {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(RESULT_KEY, selected);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    private void preview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", selected);
        bundle.putInt("maxCount", MAX_IMAGE);
        CommonUtils.launchActivityForResult(this, PhotoPreviewActivity.class, bundle, REQUEST_PHOTO);
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

    private void reset() {
        selected.clear();
        tvPreview.setEnabled(false);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("checked", selected);
        bundle.putString("album", tvAlbum.getText().toString());
        bundle.putInt("maxCount", MAX_IMAGE);
        CommonUtils.launchActivityForResult(this, PhotoPreviewActivity.class, bundle, REQUEST_PHOTO);
    }

    @Override
    public void onCheckedChanged(PhotoModel photoModel,
                                 CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!selected.contains(photoModel))
                selected.add(photoModel);
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
        }
        if (selected.size() > MAX_IMAGE) {
            photoModel.setChecked(false);
            selected.remove(photoModel);
        }
        initView();
    }

    private void initView() {
        btnComplete.setText("完成(" + selected.size() + "/" + MAX_IMAGE + ")");
        btnComplete.setEnabled(true);

        if (selected.isEmpty()) {
            btnComplete.setEnabled(false);
            tvPreview.setEnabled(false);
            btnComplete.setText("完成");
            tvPreview.setText(getString(R.string.preview));
        } else {
            tvPreview.setEnabled(true);
        }
    }

    @Override
    public boolean canChecked() {
        return selected.size() < MAX_IMAGE;
    }

    @Override
    public void onCheckedMax() {
        Toast.makeText(this, "您最多只能选择" + MAX_IMAGE + "张照片", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (layoutAlbum.getVisibility() == View.VISIBLE) {
            hideAlbum();
        } else
            super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
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
        // tvTitle.setText(current.getName());

        if (position == 0)
            photoSelectorDomain.getRecent(recentListener);
        else
            photoSelectorDomain.getAlbum(current.getName(), recentListener);
    }

    @Override
    public void onHeaderClicked() {
        catchPicture();
    }

    public interface OnLocalRecentListener {
        public void onPhotoLoaded(ArrayList<PhotoModel> photos);
    }

    public interface OnLocalAlbumListener {
        public void onAlbumLoaded(ArrayList<AlbumModel> albums);
    }

    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(ArrayList<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };

    private OnLocalRecentListener recentListener = new OnLocalRecentListener() {
        @Override
        public void onPhotoLoaded(ArrayList<PhotoModel> photos) {
            for (PhotoModel model : photos) {
                if (selected.contains(model)) {
                    model.setChecked(true);
                }
            }
            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0);
        }
    };
}
