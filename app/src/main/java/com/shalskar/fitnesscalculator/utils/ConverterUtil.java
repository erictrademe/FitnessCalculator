package com.shalskar.fitnesscalculator.utils;

/**
 * Created by Vincent on 7/05/2016.
 */
public class ConverterUtil {

    public static double poundsToKgs(double pounds) {
        return pounds / 2.205;
    }

    public static float poundsToKgs(float pounds) {
        return pounds / 2.205f;
    }

    public static double kgsToPounds(double kgs) {
        return kgs * 2.205;
    }

    public static float feetAndInchesToCm(float feet, float inches){
        return ((feet * 12) + inches) * 2.54f;
    }

    public static float[] cmToFeetAndInches(float cm){
        float inches = cm / 2.54f;
        float feet = (float) Math.floor(inches / 12);
        return new float[] {feet, inches % 12};
    }

    public static double cmToInches(double cm){
        return cm / 2.54;
    }

    public static float cmToInches(float cm){
        return cm / 2.54f;
    }

    public static float mmToInches(float mm){
        return (mm /10) / 2.54f;
    }

    public static double inchesToCm(double inches){
        return inches * 2.54;
    }

    public static float inchesToCm(float inches){
        return inches * 2.54f;
    }
}
