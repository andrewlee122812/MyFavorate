package com.silverforge.controls.model;

public enum ClipShape {
    ROUNDED_RECTANGLE(0),
    CIRCLE(1);

    private int index;

    ClipShape(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
