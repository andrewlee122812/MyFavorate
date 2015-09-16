package com.jin.photopicker.ui;
/**
 *
 * @author Aizaz AZ
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jin.photopicker.R;
import com.jin.photopicker.model.PhotoModel;
import com.jin.photopicker.util.AnimationUtil;

import java.util.ArrayList;


public class BasePhotoPreviewActivity extends ActionBarActivity implements OnPageChangeListener, OnClickListener {
    public static final int BACK_STATUS = 1;
    public static final int FINISH_STATUS = 2;

    private ViewPager mViewPager;
    protected ArrayList<PhotoModel> photos;
    protected ArrayList<PhotoModel> checkedPhotos;
    protected int current;
    protected int maxCount;

    protected Toolbar toolbar;
    protected Button btnComplete;
    protected RelativeLayout footer;
    protected LinearLayout cbWrapper;
    protected CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopreview);
        btnComplete = (Button) findViewById(R.id.toolbar_btn_complete);
        btnComplete.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("图片预览");

        mViewPager = (ViewPager) findViewById(R.id.vp_base_app);
        mViewPager.setOnPageChangeListener(this);

        footer = (RelativeLayout) findViewById(R.id.layout_toolbar_ar);
        checkBox = (CheckBox) findViewById(R.id.cb_photo_lpsi);
        cbWrapper = (LinearLayout) findViewById(R.id.cb_wrapper);
        cbWrapper.setOnClickListener(this);

        overridePendingTransition(R.anim.activity_alpha_action_in, 0);
    }

    protected void bindData() {
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(current);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Bundle bundle = new Bundle();
                bundle.putInt("status", BACK_STATUS);
                bundle.putSerializable("checked", checkedPhotos);
                setResult(RESULT_OK, new Intent().putExtras(bundle));
                finish();
                break;
        }
        return false;
    }

    private PagerAdapter mPagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            if (photos == null) {
                return 0;
            } else {
                return photos.size();
            }
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            PhotoPreview photoPreview = new PhotoPreview(getApplicationContext());
            container.addView(photoPreview);
            photoPreview.loadImage(photos.get(position));
            photoPreview.setOnClickListener(photoItemClickListener);
            return photoPreview;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    };
    protected boolean isUp;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cb_wrapper) {
            PhotoModel obj = photos.get(current);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                obj.setChecked(false);
                checkedPhotos.remove(obj);
                updateSeclectCount();
            } else {
                if (checkedPhotos.size() >= maxCount) {
                    Toast.makeText(this, "您最多只能选择" + maxCount + "张照片", Toast.LENGTH_SHORT).show();
                    return;
                }
                checkBox.setChecked(true);
                obj.setChecked(true);
                checkedPhotos.add(obj);
                updateSeclectCount();
            }
        } else if (v.getId() == R.id.toolbar_btn_complete) {
            Bundle bundle = new Bundle();
            bundle.putInt("status", FINISH_STATUS);
            if (checkedPhotos.size() == 0) {
                PhotoModel obj = photos.get(current);
                checkedPhotos.add(obj);
            }
            bundle.putSerializable("checked", checkedPhotos);
            setResult(RESULT_OK, new Intent().putExtras(bundle));
            finish();
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        current = arg0;
        updatePercent();
    }

    protected void updatePercent() {
        toolbar.setTitle((current + 1) + "/" + photos.size());
        checkBox.setChecked(checkedPhotos.contains(photos.get(current)));
    }

    protected void updateSeclectCount() {
        int size = checkedPhotos == null ? 0 : checkedPhotos.size();
        btnComplete.setText("完成(" + size + "/" + maxCount + ")");
    }

    private OnClickListener photoItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isUp) {
                new AnimationUtil(getApplicationContext(), R.anim.translate_up)
                        .setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(toolbar);
                footer.animate()
                        .setDuration(150)
                        .translationY(footer.getHeight())
                        .setInterpolator(new LinearInterpolator())
                        .start();
                isUp = true;
            } else {
                new AnimationUtil(getApplicationContext(), R.anim.translate_down_current)
                        .setInterpolator(new LinearInterpolator()).setFillAfter(true).startAnimation(toolbar);
                footer.animate()
                        .setDuration(150)
                        .translationY(0)
                        .setInterpolator(new LinearInterpolator())
                        .start();
                isUp = false;
            }
        }
    };
}
