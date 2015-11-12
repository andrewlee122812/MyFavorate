package com.silverforge.controls.calculators;

import com.silverforge.controls.model.ItemCoordinate;

abstract class LoadCalculator {
    protected ItemCoordinate getItemCoordinate(float centerX, float centerY, float angleInDegrees, float radius, float pointRadius) {
        float x = CoordinateCalculator.getXCoordinate(angleInDegrees, centerX, radius);
        float y = CoordinateCalculator.getYCoordinate(angleInDegrees, centerY, radius);

        ItemCoordinate retValue = new ItemCoordinate();
        retValue.setX(x);
        retValue.setY(y);
        retValue.setAngle(angleInDegrees);
        retValue.setRadius(pointRadius);

        return retValue;
    }

    protected void recalculateItemCoordinate(float centerX, float centerY, float angleInDegrees, float radius, ItemCoordinate itemCoordinate) {
        float x = CoordinateCalculator.getXCoordinate(angleInDegrees, centerX, radius);
        float y = CoordinateCalculator.getYCoordinate(angleInDegrees, centerY, radius);

        itemCoordinate.setX(x);
        itemCoordinate.setY(y);
        itemCoordinate.setAngle(angleInDegrees);
    }
}
