package com.pram.puzzlegame.Model;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.pram.puzzlegame.Utility.DisplayConvert;
import com.pram.puzzlegame.Utility.LayoutWrapper;
import com.pram.puzzlegame.Utility.Position;

public class Puzzle {

    public static final int DEFAULT_ELEVATION_DP = 2;
    public static final int LIFT_ELEVATION_DP = 8;

    public final int index;
    public ImageView view;
    public int rotationLevel;

    private RelativeLayout.LayoutParams layoutParams;

    private static int defaultElevation = 0;
    private static int liftElevation = 0;

    public Puzzle(int index, Bitmap image, int size, Context context) {
        this.index = index;

        view = new ImageView(context);
        view.setAdjustViewBounds(true);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        view.setElevation(defaultElevation);
        Glide.with(context).load(image).into(view);

        layoutParams = new RelativeLayout.LayoutParams(size, size);
        layoutParams.setMargins(0,0,0,0);

        rotationLevel = 0;
    }

    public static void setElevationForDisplay(DisplayConvert convert) {
        defaultElevation = convert.dpToPx(DEFAULT_ELEVATION_DP);
        liftElevation = convert.dpToPx(LIFT_ELEVATION_DP);
    }

    public void attachToLayout(LayoutWrapper layout) {
        layout.view.addView(view, layoutParams);
    }

    public void setX(int x) {
        layoutParams.leftMargin = x;
        view.requestLayout();
    }

    public void setY(int y) {
        layoutParams.topMargin = y;
        view.requestLayout();
    }

    public int getX() {
        return layoutParams.leftMargin;
    }

    public int getY() {
        return layoutParams.topMargin;
    }

    public void move(Position position) {
        layoutParams.leftMargin = position.x;
        layoutParams.topMargin = position.y;
    }

    public void animateMove(Position position) {
        ObjectAnimator xAnimator = ObjectAnimator.ofInt(this, "x", this.getX(), position.x);
        ObjectAnimator yAnimator = ObjectAnimator.ofInt(this, "y", this.getY(), position.y);

        AnimatorSet moveAnimator = new AnimatorSet();
        moveAnimator.play(xAnimator).with(yAnimator);
        moveAnimator.setDuration(200);
        moveAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        moveAnimator.start();
    }

    public void rotate(int times) {
        int level = (rotationLevel + times) % 4;
        int degrees = (rotationLevel + times) * 90;

        view.setRotation(rotationLevel * 90);
        view.animate().rotation(degrees).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(100);

        rotationLevel = level;
    }

    public void lift() {
        animateElevationChange(defaultElevation, liftElevation);
    }

    public void drop() {
        animateElevationChange(liftElevation, defaultElevation);
    }

    private void animateElevationChange(float from, float to) {
        ValueAnimator elevationAnimator = ValueAnimator.ofFloat(from, to);
        elevationAnimator.setDuration(200);
        elevationAnimator.start();

        elevationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setElevation((float) animation.getAnimatedValue());
            }
        });
    }
}
