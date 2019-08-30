package com.pram.puzzlegame.Model;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pram.puzzlegame.R;
import com.pram.puzzlegame.Utility.Position;

public class Slot {
    public static int DEFAULT_DRAWABLE = R.drawable.slot;

    public final int index;

    public ImageView view;
    public int size;

    public RelativeLayout.LayoutParams layoutParams;

    public Slot(int index, int size, Context context) {
        this.index = index;
        this.size = size;
        view = new ImageView(context);
        view.setAdjustViewBounds(true);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);

        layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.setMargins(0, 0, 0, 0);
    }

    public void setPosition(Position position) {
        layoutParams.leftMargin = position.x;
        layoutParams.topMargin = position.y;
    }

    public void focus() {
        view.setAlpha(0.5f);
    }

    public void unFocus() {
        view.setAlpha(1f);
    }
}
