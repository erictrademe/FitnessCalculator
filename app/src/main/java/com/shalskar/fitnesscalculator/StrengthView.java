package com.shalskar.fitnesscalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Vincent on 3/06/2016.
 */
public class StrengthView extends View {

    private Paint defaultPaint;
    private Paint greenPaint;
    private Paint yellowPaint;
    private Paint orangePaint;
    private Paint redPaint;
    private Paint darkRedPaint;

    private RectF currentRect;
    private Paint currentPaint;
    private float circleRadius;
    private float buffer;

    private int level = 0;

    public StrengthView(Context context) {
        this(context, null, 0);
    }

    public StrengthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrengthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        defaultPaint = new Paint();
        defaultPaint.setColor(context.getResources().getColor(R.color.defaultGrey));
        greenPaint = new Paint();
        greenPaint.setColor(context.getResources().getColor(R.color.paleGreen));
        yellowPaint = new Paint();
        yellowPaint.setColor(context.getResources().getColor(R.color.yellowGreen));
        orangePaint = new Paint();
        orangePaint.setColor(context.getResources().getColor(R.color.mustardOrange));
        redPaint = new Paint();
        redPaint.setColor(context.getResources().getColor(R.color.lightRed));
        darkRedPaint = new Paint();
        darkRedPaint.setColor(context.getResources().getColor(R.color.deepRed));
        currentRect = new RectF(0, 0, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 5; i++)
            drawLevel(canvas, i);
    }

    private void drawLevel(Canvas canvas, int position) {
        if (position < level) {
            currentPaint = getPaint(level);
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeWidth(position * 1.5f);
        } else {
            currentPaint = getPaint(0);
            currentPaint.setStyle(Paint.Style.STROKE);
            currentPaint.setStrokeWidth(1.5f);
        }
        currentPaint.setAntiAlias(true);
        currentRect.set((position * circleRadius + buffer) - buffer / 2, buffer / 2, ((position + 1) * circleRadius) - buffer / 2, getHeight() - buffer / 2);
        canvas.drawOval(currentRect, currentPaint);
    }

    private Paint getPaint(int level) {
        switch (level) {
            case 0:
                return defaultPaint;
            case 1:
                return greenPaint;
            case 2:
                return yellowPaint;
            case 3:
                return orangePaint;
            case 4:
                return redPaint;
            case 5:
                return darkRedPaint;
        }
        return greenPaint;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        circleRadius = Math.min(width, height);
        buffer = circleRadius / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        invalidate();
    }
}
