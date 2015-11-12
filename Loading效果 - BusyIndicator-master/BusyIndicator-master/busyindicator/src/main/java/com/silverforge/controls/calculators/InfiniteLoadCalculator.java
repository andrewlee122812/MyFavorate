package com.silverforge.controls.calculators;

import com.silverforge.controls.model.ItemCoordinate;

import lombok.Getter;

public class InfiniteLoadCalculator extends LoadCalculator {

    private final static int INITIAL_INNER_POINT_ANGLE = 270;

    @Getter
    private ItemCoordinate[] outerItems;

    @Getter
    private ItemCoordinate innerItem;

    public void initializePointPosition(float centerX, float centerY, float smallRadius, float smallPointRadius) {
        innerItem = getItemCoordinate(centerX, centerY, INITIAL_INNER_POINT_ANGLE, smallRadius, smallPointRadius);
    }

    public void initializeOuterPointPositions(float centerX, float centerY, float bigRadius, float bigPointRadius, int bigPointCount) {
        outerItems = new ItemCoordinate[bigPointCount];

        float slice = 360 / bigPointCount;
        for (int i = 0; i < bigPointCount; i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(centerX, centerY, angle, bigRadius, bigPointRadius);
            outerItems[i] = itemCoordinate;
        }
    }

    public void recalculatePointPosition(float centerX, float centerY, float deltaAngleInDegrees, float radius) {
        float innerItemAngle = innerItem.getAngle();
        innerItemAngle -= deltaAngleInDegrees;
        if (innerItemAngle < 0)
            innerItemAngle = innerItemAngle + 360;

        recalculateItemCoordinate(centerX, centerY, innerItemAngle, radius, innerItem);
    }

    public void recalculateOuterPointPositions(float centerX, float centerY, float deltaAngleInDegrees, float radius) {
        for (ItemCoordinate item : outerItems) {
            float itemAngle = item.getAngle();
            itemAngle += deltaAngleInDegrees;
            if (itemAngle > 360)
                itemAngle = itemAngle - 360;

            recalculateItemCoordinate(centerX, centerY, itemAngle, radius, item);
        }
    }
}
