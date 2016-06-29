package com.shalskar.fitnesscalculator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by Vincent on 3/06/2016.
 */
public class MeasurementView extends View {

    private final static int LINE_SCALE_RATIO = 200;
    private final static DecimalFormat decimalFormat = new DecimalFormat(Constants.FORMAT_NUMBER);

    private float fontSize;
    private float strokeSize;
    private Paint currentPaint;
    private float lineLength;
    private float buffer;

    private String text;
    private String unitText;
    private float value;
    private float lineScale;

    public MeasurementView(Context context) {
        this(context, null, 0);
    }

    public MeasurementView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasurementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull Context context) {
        lineScale = 1;
        value = 0;
        text = "";
        unitText = "cm";

        fontSize = context.getResources().getDimension(R.dimen.measurement_view_text_size);
        strokeSize = context.getResources().getDimension(R.dimen.measurement_view_line_size);
        initPaint(context);
    }

    private void initPaint(@NonNull Context context) {
        currentPaint = new Paint();
        currentPaint.setColor(ContextCompat.getColor(context, R.color.white));
        currentPaint.setTextSize(fontSize);
        currentPaint.setTextAlign(Paint.Align.CENTER);
        currentPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        currentPaint.setStrokeWidth(strokeSize);
        currentPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getHeight() / 2, lineLength, getHeight() / 2, currentPaint);
        canvas.drawText(String.format("%s %s", decimalFormat.format(value), unitText), getWidth() / 2, getHeight() / 2 + (buffer * 3), currentPaint);
        canvas.drawText(text, getWidth() / 2, getHeight() / 2 - (buffer / 2), currentPaint);
        canvas.drawLine(getWidth() - lineLength, getHeight() / 2, getWidth(), getHeight() / 2, currentPaint);
    }

    private void updateLineWidth(int width) {
        lineLength = (width / 4) * lineScale;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        updateLineWidth(width);
        buffer = height / 8;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setText(@NonNull String text) {
        this.text = text;
        invalidate();
    }

    public void setValue(float value) {
        this.value = value;
        lineScale = 1 - (value / LINE_SCALE_RATIO);
        updateLineWidth(getWidth());
        invalidate();
    }

    public void setValueAndUnit(float value, int unit) {
        setValue(value);
        setUnit(unit);
    }

    public void setUnit(int unit) {
        if (unit == Constants.UNIT_IMPERIAL)
            unitText = "in";
        else if (unit == Constants.UNIT_METRIC)
            unitText = "cm";
    }
}
