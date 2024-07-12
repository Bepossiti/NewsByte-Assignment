package com.restaurant.foodrecipes.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.restaurant.foodrecipes.R;

public class ColorCircleView extends View {
    private int circleColor;
    private int borderColor;
    private float borderWidth;
    private Paint paint;

    public ColorCircleView(Context context) {
        super(context);
        init(null);
    }

    public ColorCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ColorCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        // Initialize paint for drawing the circle
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Retrieve custom attributes from XML
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorCircleView);
            circleColor = a.getColor(R.styleable.ColorCircleView_circleColor, Color.GRAY); // Default to gray
            borderColor = a.getColor(R.styleable.ColorCircleView_circleborderColor, Color.BLACK); // Default to black
            borderWidth = a.getDimension(R.styleable.ColorCircleView_circleborderWidth, 0); // Default to no border
            a.recycle();
        } else {
            circleColor = Color.BLUE; // Default to blue if attrs are null
            borderColor = Color.BLACK; // Default to black if attrs are null
            borderWidth = 0; // Default to no border if attrs are null
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;

        // Draw the border circle
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2f, height / 2f, radius - borderWidth / 2, paint);

        // Draw the filled circle
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(circleColor);
        canvas.drawCircle(width / 2f, height / 2f, radius - borderWidth, paint);
    }

    public void setCircleColor(int color) {
        circleColor = color;
        invalidate(); // Redraw the view
    }

    public void setBorderColor(int color) {
        borderColor = color;
        invalidate(); // Redraw the view
    }

    public void setBorderWidth(float width) {
        borderWidth = width;
        invalidate(); // Redraw the view
    }
}
