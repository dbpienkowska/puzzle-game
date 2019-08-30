package com.pram.puzzlegame.Utility;

import android.content.Context;
import android.widget.RelativeLayout;

public class LayoutWrapper {
    public final RelativeLayout view;
    public final Context context;

    public int width;
    public int height;
    public int margin;

    public LayoutWrapper(RelativeLayout layout, Context context) {
        this.view = layout;
        this.context = context;
    }

    public boolean isInLayoutBounds(Position position, int size) {
        return position.x >= 0 && position.x + size <= width
                && position.y >= 0 && position.y + size <= height;
    }

    public void setLayoutDimensions(int marginDp) {
        DisplayConvert convert = new DisplayConvert(context);
        int statusBarHeight = convert.dpToPx(Dimensions.STATUS_BAR_SIZE);
        margin = convert.dpToPx(marginDp);

        width = convert.metrics.widthPixels - 2 * margin;
        height = convert.metrics.heightPixels - 2 * margin - statusBarHeight;
    }
}
