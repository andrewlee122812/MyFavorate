package com.jin.idcardrecognize_rebuild;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

/**
 * Created by YaLin on 2015/9/15.
 */
public class BaseLoadNoRevealActivity extends ToolbarActivity{
    ViewStub vsLoading;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base_loading);
        vsLoading = (ViewStub) findViewById(R.id.vs_loading);
        View view = View.inflate(this, layoutResID, null);
        FrameLayout content = (FrameLayout) findViewById(R.id.base_loading_fl_content);
        content.addView(view);
        setupToolbar(false);
    }

    protected void showLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.VISIBLE);
        }
    }

    protected void dismissLoading() {
        if (vsLoading != null) {
            vsLoading.setVisibility(View.GONE);
        }
    }
}
