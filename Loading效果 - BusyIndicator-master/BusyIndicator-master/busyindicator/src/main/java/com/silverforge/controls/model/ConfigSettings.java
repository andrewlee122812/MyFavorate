package com.silverforge.controls.model;

import lombok.Data;

@Data
public class ConfigSettings {
    private boolean isBackgroundVisible;
    private ClipShape backgroundShape;
    private int backgroundColor;
    private int bigPointColor;
    private int smallPointColor;
    private int bigPointCount;
    private float maxValue;
    private boolean isPercentageVisible;
    private int percentageDecimalPlaces;
    private boolean isInfinite;
}
