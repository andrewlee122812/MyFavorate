package com.silverforge.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.silverforge.controls.model.ClipShape;
import com.silverforge.controls.model.ConfigSettings;
import com.silverforge.library.R;

import java.util.HashMap;
import java.util.Map;

class Indicator extends View {

    protected final Map<Integer, String> decimalPlacesMap = new HashMap<>();
    protected ConfigSettings configSettings;

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        configSettings = readAttributes(context, attrs, defStyleAttr);
        initializeMembers(configSettings);
    }

    private void initializeMembers(ConfigSettings configSettings) {
        decimalPlacesMap.put(0, "%.0f%%");
        decimalPlacesMap.put(1, "%.1f%%");
        decimalPlacesMap.put(2, "%.2f%%");
    }

    private ConfigSettings readAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes
            = context
                .getTheme()
                .obtainStyledAttributes(attrs,
                                        R.styleable.BusyIndicator,
                                        defStyleAttr,
                                        0);

        ConfigSettings configSettings = readAttributeValues(attributes);
        attributes.recycle();

        return configSettings;
    }

    private ConfigSettings readAttributeValues(TypedArray attributes) {
        ConfigSettings configSettings = new ConfigSettings();

        boolean isBackgroundVisible = attributes.getBoolean(R.styleable.BusyIndicator_background_is_visible, false);
        int backgroundColor = attributes.getColor(R.styleable.BusyIndicator_background_color, Color.argb(100, 200, 200, 200));

        boolean isPercentageVisible = attributes.getBoolean(R.styleable.BusyIndicator_percentage_is_visible, true);

        int clipShapeIndex = attributes.getInteger(R.styleable.BusyIndicator_background_shape, ClipShape.ROUNDED_RECTANGLE.getIndex());
        ClipShape backgroundShape = ClipShape.values()[clipShapeIndex];

        int bigPointCount = attributes.getInteger(R.styleable.BusyIndicator_bigpoint_count, 12);
        if (bigPointCount < 4)
            bigPointCount = 4;
        if (bigPointCount > 20)
            bigPointCount = 20;

        int smallPointColor = attributes.getColor(R.styleable.BusyIndicator_smallpoint_color, Color.BLACK);
        int bigPointColor = attributes.getColor(R.styleable.BusyIndicator_bigpoint_color, Color.DKGRAY);

        boolean infinite = attributes.getBoolean(R.styleable.BusyIndicator_infinite, true);
        float maxValue = attributes.getFloat(R.styleable.BusyIndicator_max_value, 100F);

        int decimalPlaces = attributes.getInt(R.styleable.BusyIndicator_percentage_decimal_places, 0);
        if (decimalPlaces > 2)
            decimalPlaces = 2;
        if (decimalPlaces < 0)
            decimalPlaces = 0;

        configSettings.setBackgroundColor(backgroundColor);
        configSettings.setBackgroundShape(backgroundShape);
        configSettings.setBackgroundVisible(isBackgroundVisible);
        configSettings.setBigPointColor(bigPointColor);
        configSettings.setBigPointCount(bigPointCount);
        configSettings.setSmallPointColor(smallPointColor);
        configSettings.setPercentageVisible(isPercentageVisible);
        configSettings.setPercentageDecimalPlaces(decimalPlaces);
        configSettings.setMaxValue(maxValue);
        configSettings.setInfinite(infinite);

        return configSettings;
    }
}
