package com.silverforge.controls.calculators;

import com.silverforge.controls.model.ItemCoordinate;

import lombok.Getter;

public class FiniteLoadCalculator extends LoadCalculator {

    @Getter
    private ItemCoordinate[] outerItems;

    public void initializeOuterPointPositions(float centerX, float centerY, float bigRadius, float bigPointRadius, int bigPointCount) {
        outerItems = new ItemCoordinate[bigPointCount];

        float slice = 360 / bigPointCount;
        for (int i = 0; i < bigPointCount; i++) {
            int angle = (int) (slice * i);
            ItemCoordinate itemCoordinate = getItemCoordinate(centerX, centerY, angle, bigRadius, bigPointRadius);
            outerItems[i] = itemCoordinate;
        }
    }
}
