package com.shalskar.fitnesscalculator.model;

/**
 * Created by Vincent on 28/05/2016.
 */
public class Physique {

    private float chest;
    private float shoulders;
    private float neck;
    private float waist;
    private float arm;
    private float forearm;
    private float thigh;
    private float calf;

    public Physique(float chest, float shoulders, float neck, float waist, float arm, float forearm, float thigh, float calf) {
        this.chest = chest;
        this.shoulders = shoulders;
        this.neck = neck;
        this.waist = waist;
        this.arm = arm;
        this.forearm = forearm;
        this.thigh = thigh;
        this.calf = calf;
    }

    public float getChest() {
        return chest;
    }

    public float getShoulders() {
        return shoulders;
    }

    public float getNeck() {
        return neck;
    }

    public float getWaist() {
        return waist;
    }

    public float getArm() {
        return arm;
    }

    public float getForearm() {
        return forearm;
    }

    public float getThigh() {
        return thigh;
    }

    public float getCalf() {
        return calf;
    }
}
