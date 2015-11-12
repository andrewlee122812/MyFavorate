package com.jin.idcardrecognize_rebuild.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Created by YaLin on 2015/9/15.
 */
public class PixUtil {
    private static final String TAG = "PixUtil";

    public static int dip2px(Context paramContext, float dp) {
        return (int) (dp * paramContext.getResources().getDisplayMetrics().density + 0.5F);
    }

    public static Point getScreenMetrics(Context paramContext) {
        DisplayMetrics displayMetrics = paramContext.getResources().getDisplayMetrics();
        int i = displayMetrics.widthPixels;
        int j = displayMetrics.heightPixels;
        return new Point(i, j);
    }

    public static float getScreenRate(Context paramContext) {
        Point point = getScreenMetrics(paramContext);
        return point.y / point.x;
    }

    public static int px2dp(Context paramContext, float px) {
        return (int) (px / paramContext.getResources().getDisplayMetrics().density + 0.5F);
    }
}
