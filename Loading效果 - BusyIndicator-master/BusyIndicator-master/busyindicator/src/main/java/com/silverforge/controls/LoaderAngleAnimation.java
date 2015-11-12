package com.silverforge.controls;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class LoaderAngleAnimation extends Animation {
    private BusyIndicator busyIndicator;
    private float newAngle;
    private float oldAngle;

    public LoaderAngleAnimation(BusyIndicator busyIndicator, float newAngle) {
        this.busyIndicator = busyIndicator;
        this.newAngle = newAngle;
        oldAngle = busyIndicator.getArcAngle();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float angle = oldAngle + ((newAngle - oldAngle) * interpolatedTime);
        busyIndicator.setArcAngle(angle);

        busyIndicator.requestLayout();
    }
}
