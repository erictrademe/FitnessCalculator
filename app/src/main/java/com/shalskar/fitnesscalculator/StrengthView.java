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

    private Paint greenPaint;
    private Paint yellowPaint;
    private Paint orangePaint;
    private Paint redPaint;
    private Paint darkRedPaint;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < level; i++) {
            drawLevel(canvas, i);
        }
    }

    private void drawLevel(Canvas canvas, int position) {
        canvas.drawOval(new RectF((position * circleRadius + buffer) - buffer / 2, buffer / 2, ((position + 1) * circleRadius) - buffer / 2, getHeight() - buffer / 2), getPaint(position));
    }

    private Paint getPaint(int position){
        switch(position){
            case 0: return greenPaint;
            case 1: return yellowPaint;
            case 2: return orangePaint;
            case 3: return redPaint;
            case 4: return darkRedPaint;
        }
        return greenPaint;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        circleRadius = Math.min(width, height);
        buffer = circleRadius / 5;
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
