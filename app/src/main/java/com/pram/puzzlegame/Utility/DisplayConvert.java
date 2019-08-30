package com.pram.puzzlegame.Utility;

import android.content.Context;
import android.util.DisplayMetrics;

public class DisplayConvert {

    public DisplayMetrics metrics;

    public DisplayConvert(Context context) {
        metrics = context.getResources().getDisplayMetrics();
    }

    public int dpToPx(int dp) {
        return (int) (dp * metrics.density);
    }

    public int pxToDp(int px) {
        return (int) (px / metrics.density);
    }
}
