package com.pram.puzzlegame.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

    public static Bitmap getBitmapFromDrawable(int drawable, Context context) {
        return BitmapFactory.decodeResource(context.getResources(), drawable);
    }

    public static double euclideanDistance(Position from, Position to) {
        int deltaX = to.x - from.x;
        int deltaY = to.y - from.y;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
