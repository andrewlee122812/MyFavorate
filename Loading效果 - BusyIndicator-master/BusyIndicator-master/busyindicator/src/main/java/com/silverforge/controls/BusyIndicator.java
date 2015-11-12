package com.silverforge.controls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.silverforge.controls.calculators.CoordinateCalculator;
import com.silverforge.controls.calculators.FiniteLoadCalculator;
import com.silverforge.controls.calculators.InfiniteLoadCalculator;
import com.silverforge.controls.model.ItemCoordinate;
import com.silverforge.controls.model.PositionSettings;
import com.silverforge.controls.painters.CanvasPainter;

public final class BusyIndicator extends Indicator {

    private boolean firstLoad = true;

    private Bitmap canvasBackground;
    private RectF rect = new RectF();

    private float maxValue;
    private float currentValue;

    private byte angleModifier = 1;
    private float arcAngle;
    private int textAlpha;

    private float layOutCenterX;
    private float layOutCenterY;
    private float bigRadius;
    private float singleRadius;
    private float singlePointRadius;
    private float textPosX;
    private float textPosY;

    private CanvasPainter canvasPainter;
    private InfiniteLoadCalculator infiniteLoadCalculator;
    private FiniteLoadCalculator finiteLoadCalculator;


    public BusyIndicator(Context context) {
        this(context, null);
    }

    public BusyIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        canvasPainter = new CanvasPainter();
        infiniteLoadCalculator = new InfiniteLoadCalculator();
        finiteLoadCalculator = new FiniteLoadCalculator();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (firstLoad) {
            initialize();
            firstLoad = false;
        } else {
            if (configSettings.isInfinite())
                calculateInfiniteMoves();
        }

        if (configSettings.isBackgroundVisible())
            canvas.drawBitmap(canvasBackground, 0, 0, null);

        if (configSettings.isInfinite())
            drawInfiniteIndicator(canvas);
        else
            drawLoadingIndicator(canvas);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                angleModifier = 1;
                break;
            case MotionEvent.ACTION_DOWN:
                angleModifier = 3;
                break;
        }

        return true;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = (float)maxValue;
    }

    public void setValue(float value) {
        calculateProgress(value);
    }

    public void setValue(int value) {
        calculateProgress((float)value);
    }


    float getArcAngle() {
        return arcAngle;
    }

    void setArcAngle(float aa) {
        arcAngle = aa;
    }

    int getTextAlpha() {
        return textAlpha;
    }

    void setTextAlpha(int ta) {
        textAlpha = ta;
    }


    private void initialize() {
        int height = getHeight();
        int width = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        CoordinateCalculator coordinateCalculator = new CoordinateCalculator();
        PositionSettings positionSettings = coordinateCalculator.calculateBasePositions(width, height, paddingLeft, paddingTop);
        initializeCalculators(positionSettings);
        initializePaints(positionSettings);
        initializeTextPositions(positionSettings);
        initializeCanvas(positionSettings);

        layOutCenterX = positionSettings.getLayOutCenterX();
        layOutCenterY = positionSettings.getLayOutCenterY();
        bigRadius = positionSettings.getBigRadius();
        singleRadius = positionSettings.getSingleRadius();
        singlePointRadius = positionSettings.getSinglePointRadius();
    }

    private void initializeCalculators(PositionSettings posSettings) {
        float layOutCenterX = posSettings.getLayOutCenterX();
        float layOutCenterY = posSettings.getLayOutCenterY();
        float bigRadius = posSettings.getBigRadius();
        float bigPointRadius = posSettings.getBigPointRadius();
        int bigPointCount = configSettings.getBigPointCount();
        float singleRadius = posSettings.getSingleRadius();
        float singlePointRadius = posSettings.getSinglePointRadius();

        if (configSettings.isInfinite()) {
            infiniteLoadCalculator
                    .initializeOuterPointPositions(layOutCenterX,
                            layOutCenterY,
                            bigRadius,
                            bigPointRadius,
                            bigPointCount);

            infiniteLoadCalculator
                    .initializePointPosition(layOutCenterX,
                            layOutCenterY,
                            singleRadius,
                            singlePointRadius);
        } else {
            finiteLoadCalculator
                    .initializeOuterPointPositions(layOutCenterX,
                            layOutCenterY,
                            bigRadius,
                            singlePointRadius,
                            bigPointCount);
        }
    }

    private void initializePaints(PositionSettings posSettings) {
        float bigRadius = posSettings.getBigRadius();
        float singlePointRadius = posSettings.getSinglePointRadius();
        canvasPainter.initializePaints(configSettings.getBigPointColor(), configSettings.getSmallPointColor(), bigRadius, singlePointRadius);
    }

    private void initializeTextPositions(PositionSettings posSettings) {
        float layOutCenterX = posSettings.getLayOutCenterX();
        float layOutCenterY = posSettings.getLayOutCenterY();
        float descent = canvasPainter.getTextPaint().descent();
        float ascent = canvasPainter.getTextPaint().ascent();
        textPosX = layOutCenterX;
        textPosY = layOutCenterY - ((descent + ascent) / 2);
    }

    private void initializeCanvas(PositionSettings posSettings) {
        Bitmap cb = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(cb);
        cv.drawColor(configSettings.getBackgroundColor());
        canvasBackground = canvasPainter.getRoundedBitmap(cb, configSettings.getBackgroundShape());

        float layOutCenterX = posSettings.getLayOutCenterX();
        float layOutCenterY = posSettings.getLayOutCenterY();
        float bigRadius = posSettings.getBigRadius();

        rect.set(layOutCenterX - bigRadius, layOutCenterY - bigRadius, layOutCenterX + bigRadius, layOutCenterY + bigRadius);
    }

    private void calculateProgress(float value) {
        if (value > 0){
            if (value >= maxValue)
                value = maxValue;

            currentValue = value;

            float currentAngle = (360 / maxValue) * value;
            currentAngle += 270;

            float aa = (currentAngle - 270);
            LoaderAngleAnimation angleAnimation = new LoaderAngleAnimation(this, aa);
            angleAnimation.setDuration(300);

            LoaderTextAnimation textAnimation = new LoaderTextAnimation(this);
            textAnimation.setDuration(300);

            AnimationSet animationSet = new AnimationSet(false);
            animationSet.addAnimation(angleAnimation);
            animationSet.addAnimation(textAnimation);
            this.startAnimation(animationSet);
        }
    }

    private void drawInfiniteIndicator(Canvas canvas) {
        // draw outer points
        Paint bigPaint = canvasPainter.getBigPaint();
        for (ItemCoordinate item : infiniteLoadCalculator.getOuterItems()) {
            canvas.drawCircle(item.getX(), item.getY(), item.getRadius(), bigPaint);
        }

        // draw inner point
        ItemCoordinate innerItem = infiniteLoadCalculator.getInnerItem();
        Paint singlePaint = canvasPainter.getSinglePaint();

        canvas.drawCircle(innerItem.getX(), innerItem.getY(), singlePointRadius, singlePaint);

        invalidate();
    }

    private void drawLoadingIndicator(Canvas canvas) {
        canvas.drawArc(rect, 630, arcAngle, false, canvasPainter.getSinglePaintTransparent());

        for (ItemCoordinate item : finiteLoadCalculator.getOuterItems()) {

            float itemAngle = item.getAngle();
            if (itemAngle >= 270)
                itemAngle -= 270;
            else
                itemAngle += 90;

            if (itemAngle > arcAngle) {
                canvasPainter.getBigPaint().setAlpha(100);
                canvas.drawCircle(item.getX(), item.getY(), singlePointRadius, canvasPainter.getBigPaint());
            } else {
                canvas.drawCircle(item.getX(), item.getY(), singlePointRadius, canvasPainter.getSinglePaint());
            }
        }

        if (configSettings.isPercentageVisible()) {
            float v = (100 / maxValue) * currentValue;

            String formatString = decimalPlacesMap.get(configSettings.getPercentageDecimalPlaces());
            String text = String.format(formatString, v);

            float descent = canvasPainter.getTextPaint().descent();
            float ascent = canvasPainter.getTextPaint().ascent();

            textPosY = layOutCenterY - ((descent + ascent) / 2);

            canvasPainter.getTextPaint().setAlpha(textAlpha);
            canvas.drawText(text, textPosX, textPosY, canvasPainter.getTextPaint());
        }
    }

    private void calculateInfiniteMoves() {
        infiniteLoadCalculator.recalculateOuterPointPositions(layOutCenterX, layOutCenterY, angleModifier, bigRadius);
        infiniteLoadCalculator.recalculatePointPosition(layOutCenterX, layOutCenterY, angleModifier, singleRadius);
    }
}
