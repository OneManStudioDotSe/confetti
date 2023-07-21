package com.github.jinatonic.confetti.confetto;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * A lightly more optimal way to draw a circle shape that doesn't require the use of a bitmap.
 */
public class CircleConfetto extends Confetto {
    private final int color;
    private final float radius;
    private final int diamater;

    public CircleConfetto(int color, float radius) {
        this.color = color;
        this.radius = radius;
        this.diamater = (int) (this.radius * 2);
    }

    @Override
    public int getWidth() {
        return diamater;
    }

    @Override
    public int getHeight() {
        return diamater;
    }

    @Override
    protected void configurePaint(Paint paint) {
        super.configurePaint(paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    @Override
    protected void drawInternal(Canvas canvas, Matrix matrix, Paint paint, float x, float y,
                                float rotation, float percentageAnimated) {
        canvas.drawCircle(x + radius, y + radius, radius, paint);
    }
}
