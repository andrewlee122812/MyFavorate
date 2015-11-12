package com.jin.idcardrecognize_rebuild;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.jin.idcardrecognize_rebuild.domain.RetData;
import com.jin.idcardrecognize_rebuild.net.IDsManagerPostRequest;
import com.jin.idcardrecognize_rebuild.net.NetService;
import com.jin.idcardrecognize_rebuild.net.RequestQueueHelper;
import com.jin.idcardrecognize_rebuild.net.response.RecResultResponse;
import com.jin.idcardrecognize_rebuild.utils.IdCardUtil;
import com.jin.photopicker.util.ImageUtil;
import com.jin.photopicker.model.PhotoModel;
import com.jin.photopicker.ui.SinglePhotoSelectorActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends BaseLoadNoRevealActivity implements View.OnClickListener {

    public static final String API_KEY = "0af566cb11cac9ee6f6f41e44a5bb25d";
    public static final int REQUEST_FRONT = 1;
    public static final int REQUEST_BACK = 2;

    ImageView ivFront;
    ImageView ivBack;
    Button btnSelectFront;
    Button btnSelectBack;
    Button btnNext;

    String frontPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        ivFront = (ImageView) findViewById(R.id.iv_front);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnSelectFront = (Button) findViewById(R.id.btn_select_front);
        btnSelectBack = (Button) findViewById(R.id.btn_select_back);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnSelectFront.setOnClickListener(this);
        btnSelectBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_select_front:
                showSelection(REQUEST_FRONT, "front");
                break;
            case R.id.btn_select_back:
                showSelection(REQUEST_BACK, "back");
                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(frontPath)) {
                    Toast.makeText(this, getString(R.string.please_select_front_image), Toast.LENGTH_SHORT).show();
                } else {
                    doDetect(frontPath);
                }
                break;
        }
    }

    void showSelection(final int requestCode, final String name) {
        new AlertDialog.Builder(this)
                .setItems(getResources().getStringArray(R.array.photo_from),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                switch (which) {
                                    case 0:
                                        IDCardCameraActivity.pick(MainActivity.this, requestCode, name);
                                        break;
                                    case 1:
                                        SinglePhotoSelectorActivity.pick(MainActivity.this, true, false, requestCode, name);
                                        break;
                                }
                            }
                        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PhotoModel photo = (PhotoModel) data.getSerializableExtra(SinglePhotoSelectorActivity.RESULT_KEY);
            File f;
            long time;
            switch (requestCode) {
                case REQUEST_FRONT:
                    frontPath = photo.getOriginalPath();
                    f = new File(frontPath);
                    time = f.lastModified();
                    Glide.with(this)
                            .load("file://" + frontPath)
                            .signature(new StringSignature(time + ""))
                            .into(ivFront);
                    break;
                case REQUEST_BACK:
                    f = new File(photo.getOriginalPath());
                    time = f.lastModified();
                    Glide.with(this)
                            .load("file://" + photo.getOriginalPath())
                            .signature(new StringSignature(time + ""))
                            .into(ivBack);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doDetect(String path) {
        showLoading();
        String base64 = null;
        try {
            base64 = ImageUtil.createImgToBase64(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(base64)) {
            return;
        }

        RequestQueue requestQueue = RequestQueueHelper.getInstance(getApplicationContext());
        final IDsManagerPostRequest<RecResultResponse> request = new IDsManagerPostRequest<>(NetService.DETECT_URL, RecResultResponse.class, NetService.getDetectHeader(API_KEY), NetService.detect(base64.replaceAll("\\s+", "")),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        authSuccess((RecResultResponse) response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        authFailed(error);
                    }
                }
        );
        requestQueue.add(request);
    }

    private void authSuccess(RecResultResponse response) {
        dismissLoading();
        dealRecData(response.retData);
    }

    private void authFailed(VolleyError error) {
        dismissLoading();
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void dealRecData(ArrayList<RetData> datas) {
        if (datas == null || datas.size() == 0) {
            Toast.makeText(this, getString(R.string.select_more_clear_image), Toast.LENGTH_SHORT).show();
            return;
        }
        if (IdCardUtil.containIdCardField(datas)) {
            FormActivity.open(this, datas);
        } else {
            Toast.makeText(this, getString(R.string.select_more_clear_image), Toast.LENGTH_SHORT).show();
        }
    }
}
