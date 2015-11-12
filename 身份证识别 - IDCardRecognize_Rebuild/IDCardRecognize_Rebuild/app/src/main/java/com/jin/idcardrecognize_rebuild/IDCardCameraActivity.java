package com.jin.idcardrecognize_rebuild;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jin.idcardrecognize_rebuild.camera.CameraInterface;
import com.jin.idcardrecognize_rebuild.camera.preview.CameraSurfaceView;
import com.jin.idcardrecognize_rebuild.utils.DisplayUtil;
import com.jin.idcardrecognize_rebuild.utils.PixUtil;
import com.jin.idcardrecognize_rebuild.widget.MaskView;
import com.jin.photopicker.model.PhotoModel;

/**
 * Created by YaLin on 2015/9/15.
 */
public class IDCardCameraActivity extends Activity implements CameraInterface.CamOpenOverCallback, CameraInterface.TakeSuccessCallback, View.OnClickListener {
    public static final String NAME_KEY = "name";
    public static final String RESULT_KEY = "photo";
    int DST_CENTER_RECT_HEIGHT = 200;
    int DST_CENTER_RECT_WIDTH = 320;
    MaskView maskView = null;
    String name;
    float previewRate = -1.0F;
    Point rectPictureSize = null;
    ImageButton shutterBtn;
    CameraSurfaceView surfaceView = null;

    public static void pick(Activity activity, int requestCode, String name) {
        Intent intent = new Intent(activity, IDCardCameraActivity.class);
        intent.putExtra(NAME_KEY, name);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_camera);
        name = getIntent().getStringExtra(NAME_KEY);
        new Thread() {
            @Override
            public void run() {
                CameraInterface.getInstance().setName(name).setTakeSuccessCallback(IDCardCameraActivity.this).doOpenCamera(IDCardCameraActivity.this);
            }
        }.start();
        initUI();
        initViewParams();
        shutterBtn.setOnClickListener(this);
    }

    private Point createCenterPictureRect(int w, int h) {
        int wScreen = DisplayUtil.getScreenMetrics(this).x;
        int hScreen = DisplayUtil.getScreenMetrics(this).y;
        int wSavePicture = CameraInterface.getInstance().doGetPrictureSize().y;
        int hSavePicture = CameraInterface.getInstance().doGetPrictureSize().x;
        float wRate = (float) (wSavePicture) / (float) (wScreen);
        float hRate = (float) (hSavePicture) / (float) (hScreen);
        float rate = (wRate <= hRate) ? wRate : hRate;

        int wRectPicture = (int) (w * wRate);
        int hRectPicture = (int) (h * hRate);
        return new Point(wRectPicture, hRectPicture);
    }

    private Rect createCenterScreenRect(int w, int h) {
        int x1 = DisplayUtil.getScreenMetrics(this).x / 2 - w / 2;
        int y1 = DisplayUtil.getScreenMetrics(this).y / 2 - h / 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        return new Rect(x1, y1, x2, y2);
    }

    private void initUI() {
        surfaceView = ((CameraSurfaceView) findViewById(R.id.camera_surfaceview));
        shutterBtn = ((ImageButton) findViewById(R.id.btn_shutter));
        maskView = ((MaskView) findViewById(R.id.view_mask));
    }

    private void initViewParams() {
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        Point p = DisplayUtil.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y;
        previewRate = DisplayUtil.getScreenRate(this);
        surfaceView.setLayoutParams(params);
    }

    private void returnPhoto(PhotoModel paramPhotoModel) {
        Intent intent = new Intent();
        Bundle localBundle = new Bundle();
        localBundle.putSerializable(RESULT_KEY, paramPhotoModel);
        intent.putExtras(localBundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void cameraHasOpened() {
        SurfaceHolder holder = this.surfaceView.getSurfaceHolder();
        CameraInterface.getInstance().doStartPreview(holder, previewRate);
        if (this.maskView != null) {
            Rect rect = createCenterScreenRect(PixUtil.dip2px(this, DST_CENTER_RECT_WIDTH), PixUtil.dip2px(this, DST_CENTER_RECT_HEIGHT));
            maskView.setCenterRect(rect);
        }
    }

    @Override
    public void onTakeSuccess(String path) {
        returnPhoto(new PhotoModel(path));
    }

    @Override
    public void onClick(View v) {
        if (rectPictureSize == null) {
            rectPictureSize = createCenterPictureRect(PixUtil.dip2px(IDCardCameraActivity.this, DST_CENTER_RECT_WIDTH), PixUtil.dip2px(IDCardCameraActivity.this, DST_CENTER_RECT_HEIGHT));
        }
        CameraInterface.getInstance().doTakePicture(rectPictureSize.x, rectPictureSize.y);
    }
}
