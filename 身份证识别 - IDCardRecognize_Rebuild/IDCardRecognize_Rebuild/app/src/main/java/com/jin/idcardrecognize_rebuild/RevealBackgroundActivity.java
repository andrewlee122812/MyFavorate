package com.jin.idcardrecognize_rebuild;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.jin.idcardrecognize_rebuild.widget.RevealBackgroundView;


/**
 * Created by 雅麟 on 2015/4/22.
 */
public class RevealBackgroundActivity extends ToolbarActivity implements RevealBackgroundView.OnStateChangeListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";

    RevealBackgroundView backgroundView;
    RelativeLayout rlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupBackground();
        setupToolbar(true);
        setupRevealBackground();
    }

    protected void setupBackground() {
        View view = View.inflate(this, R.layout.activity_reveal_background, null);
        backgroundView = (RevealBackgroundView) view.findViewById(R.id.vRevealBackground);
        rlContent = (RelativeLayout) view.findViewById(R.id.reveal_rl_content);
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        View contentView = rootView.getChildAt(0);
        rootView.removeView(contentView);
        rlContent.addView(contentView);
        rootView.addView(view);
        rlContent.setAlpha(0);
    }

    private void setupRevealBackground() {
        backgroundView.setOnStateChangeListener(this);
        final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
        backgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                backgroundView.startFromLocation(startingLocation);
                return true;
            }
        });
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            rlContent.setVisibility(View.VISIBLE);
            onStateFinish();
        } else {
            rlContent.setVisibility(View.INVISIBLE);
        }
    }

    protected void onStateFinish() {
        rlContent.animate()
                .alpha(1)
                .setDuration(200)
                .start();
    }
}
