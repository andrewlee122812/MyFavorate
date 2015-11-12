package com.silverforge.controls.calculators;

import com.silverforge.controls.model.PositionSettings;

public class CoordinateCalculator {

    public PositionSettings calculateBasePositions(int width, int height, int paddingLeft, int paddingTop) {
        PositionSettings positionSettings = new PositionSettings();

        float bigRadius = height > width ? width /2 : height / 2;
        bigRadius = (float)(bigRadius * 0.8);
        float bigPointRadius = (float) (bigRadius * 0.15);

        float layoutCenterX = paddingLeft + width / 2;
        float layoutCenterY = paddingTop + height / 2;

        float singleRadius = (float) (bigRadius * 0.75);
        float singlePointRadius = (float) (singleRadius * 0.05);

        positionSettings.setWidth(width);
        positionSettings.setHeight(height);
        positionSettings.setPaddingLeft(paddingLeft);
        positionSettings.setPaddingTop(paddingTop);
        positionSettings.setLayOutCenterX(layoutCenterX);
        positionSettings.setLayOutCenterY(layoutCenterY);
        positionSettings.setBigRadius(bigRadius);
        positionSettings.setBigPointRadius(bigPointRadius);
        positionSettings.setSingleRadius(singleRadius);
        positionSettings.setSinglePointRadius(singlePointRadius);
        return positionSettings;
    }

    public static float getYCoordinate(float angleInDegrees, float layoutCenterY, float radius) {
        return (float) ((radius * Math.sin(angleInDegrees * Math.PI / 180F)) + layoutCenterY);
    }

    public static float getXCoordinate(float angleInDegrees, float layoutCenterX, float radius) {
        return (float) ((radius * Math.cos(angleInDegrees * Math.PI / 180F)) + layoutCenterX);
    }
}
