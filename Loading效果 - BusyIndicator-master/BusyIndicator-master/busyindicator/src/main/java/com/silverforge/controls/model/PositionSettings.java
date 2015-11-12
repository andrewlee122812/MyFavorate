package com.silverforge.controls.model;

import lombok.Data;

@Data
public class PositionSettings {
    private int width;
    private int height;
    private int paddingLeft;
    private int paddingTop;
    private float layOutCenterX;
    private float layOutCenterY;

    private float bigRadius;
    private float bigPointRadius;
    private float singleRadius;
    private float singlePointRadius;
}
