package com.shalskar.fitnesscalculator.model;

/**
 * Created by Vincent on 14/06/2016.
 */
public class Breakdown {

    public static final Breakdown BREAKDOWN_GAIN_MUSCLE = new Breakdown(0.3f, 0.4f, 0.3f);
    public static final Breakdown BREAKDOWN_FAT_LOSS = new Breakdown(0.4f, 0.3f, 0.3f);
    public static final Breakdown BREAKDOWN_MAINTAIN = new Breakdown(0.3f, 0.35f, 0.35f);

    private float proteinRatio;
    private float carbohydratesRatio;
    private float fatRatio;

    public Breakdown(float proteinRatio, float carbohydratesRatio, float fatRatio) {
        this.proteinRatio = proteinRatio;
        this.carbohydratesRatio = carbohydratesRatio;
        this.fatRatio = fatRatio;
    }

    public float getProteinRatio() {
        return proteinRatio;
    }

    public void setProteinRatio(float proteinRatio) {
        this.proteinRatio = proteinRatio;
    }

    public float getCarbohydratesRatio() {
        return carbohydratesRatio;
    }

    public void setCarbohydratesRatio(float carbohydratesRatio) {
        this.carbohydratesRatio = carbohydratesRatio;
    }

    public float getFatRatio() {
        return fatRatio;
    }

    public void setFatRatio(float fatRatio) {
        this.fatRatio = fatRatio;
    }
}
