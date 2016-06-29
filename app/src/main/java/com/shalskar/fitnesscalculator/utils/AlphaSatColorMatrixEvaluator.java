package com.shalskar.fitnesscalculator.utils;

import android.animation.TypeEvaluator;
import android.graphics.ColorMatrix;

public class AlphaSatColorMatrixEvaluator implements TypeEvaluator {
    private ColorMatrix colorMatrix;
    float[] elements = new float[20];

    public AlphaSatColorMatrixEvaluator() {
        colorMatrix = new ColorMatrix();
    }

    public ColorMatrix getColorMatrix() {
        return colorMatrix;
    }

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        float phase = fraction * 3;

        float alpha = Math.min(phase, 2f) / 2f;
        elements[18] = alpha;

        final int maxBlacker = 100;
        float blackening = (float) Math.round((1 - Math.min(phase, 2.5f) / 2.5f) * maxBlacker);
        elements[4] = elements[9] = elements[14] = -blackening;

        float invSat = 1 - Math.max(0.2f, fraction);
        float r = 0.213f * invSat;
        float g = 0.715f * invSat;
        float b = 0.072f * invSat;

        elements[0] = r + fraction;
        elements[1] = g;
        elements[2] = b;
        elements[5] = r;
        elements[6] = g + fraction;
        elements[7] = b;
        elements[10] = r;
        elements[11] = g;
        elements[12] = b + fraction;

        colorMatrix.set(elements);
        return colorMatrix;
    }
}