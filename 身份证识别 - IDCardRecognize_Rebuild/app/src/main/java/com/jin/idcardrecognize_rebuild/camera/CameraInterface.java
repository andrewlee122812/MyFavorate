package com.jin.idcardrecognize_rebuild.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;


import com.jin.idcardrecognize_rebuild.utils.CamParaUtil;
import com.jin.idcardrecognize_rebuild.utils.FileUtil;
import com.jin.photopicker.util.ImageUtil;

import java.io.IOException;
import java.util.List;

public class CameraInterface {
    private static final String TAG = "CameraInterface";
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static CameraInterface mCameraInterface;

    private String name;
    private TakeSuccessCallback takeSuccessCallback;

    public interface CamOpenOverCallback {
        void cameraHasOpened();
    }

    public interface TakeSuccessCallback {
        void onTakeSuccess(String path);
    }

    private CameraInterface() {

    }

    public static synchronized CameraInterface getInstance() {
        if (mCameraInterface == null) {
            mCameraInterface = new CameraInterface();
        }
        return mCameraInterface;
    }

    public void doOpenCamera(CamOpenOverCallback callback) {
        mCamera = Camera.open();
        callback.cameraHasOpened();
    }

    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }
    }

    public void doStartPreview(SurfaceTexture surface, float previewRate) {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            initCamera(previewRate);
        }

    }

    public void doStopCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }

    int DST_RECT_WIDTH, DST_RECT_HEIGHT;

    public void doTakePicture(int w, int h) {
        if (isPreviewing && (mCamera != null)) {
            DST_RECT_WIDTH = w;
            DST_RECT_HEIGHT = h;
            mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
        }
    }

    public Point doGetPrictureSize() {
        Size s = mCamera.getParameters().getPictureSize();
        return new Point(s.width, s.height);
    }


    private void initCamera(float previewRate) {
        if (mCamera != null) {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);
            Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
                    mParams.getSupportedPictureSizes(), previewRate, 800);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
                    mParams.getSupportedPreviewSizes(), previewRate, 800);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setDisplayOrientation(90);

            List<String> focusModes = mParams.getSupportedFocusModes();
            if(focusModes.contains("continuous-video")){
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);
            mCamera.startPreview();


            isPreviewing = true;
            mPreviwRate = previewRate;

            mParams = mCamera.getParameters();
        }
    }


    ShutterCallback mShutterCallback = new ShutterCallback() {
        public void onShutter() {
        }
    };
    PictureCallback mRawCallback = new PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    PictureCallback mRectJpegPictureCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);
                mCamera.stopPreview();
                isPreviewing = false;
            }
            if (null != b) {
                Bitmap rotaBitmap = ImageUtil.getRotateBitmap(b, 90.0f);
                int x = rotaBitmap.getWidth() / 2 - DST_RECT_WIDTH / 2;
                int y = rotaBitmap.getHeight() / 2 - DST_RECT_HEIGHT / 2;
                Log.i(TAG, "rotaBitmap.getWidth() = " + rotaBitmap.getWidth()
                        + " rotaBitmap.getHeight() = " + rotaBitmap.getHeight());
                Bitmap rectBitmap = Bitmap.createBitmap(rotaBitmap, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);
                String str = FileUtil.saveBitmap(rectBitmap, CameraInterface.this.name);
                if (!rotaBitmap.isRecycled()) {
                    rotaBitmap.recycle();
                }
                if (!rectBitmap.isRecycled()) {
                    rectBitmap.recycle();
                }
                doStopCamera();
                if (takeSuccessCallback != null) {
                    takeSuccessCallback.onTakeSuccess(str);
                }
            }
            if (!b.isRecycled()) {
                b.recycle();
            }

        }
    };

    public CameraInterface setName(String paramString) {
        this.name = paramString;
        return this;
    }

    public CameraInterface setTakeSuccessCallback(TakeSuccessCallback takeSuccessCallback) {
        this.takeSuccessCallback = takeSuccessCallback;
        return this;
    }

}
