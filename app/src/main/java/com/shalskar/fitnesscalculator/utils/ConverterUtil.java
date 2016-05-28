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

    public static double feetAndInchesToCm(double feet, double inches){
        return ((feet * 12) + inches) * 2.54;
    }

    public static double[] cmToFeetAndInches(double cm){
        double inches = cm / 2.54;
        double feet = Math.floor(inches / 12);
        return new double[] {feet, inches % 12};
    }
}
