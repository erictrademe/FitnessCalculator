package com.shalskar.fitnesscalculator;

import java.text.DecimalFormat;

/**
 * Created by Vincent on 7/05/2016.
 */
public class FitnessCalculator {

    public static double calculateBMI(double weight, double height) {
        height = height / 100;
        return weight / (height * height);
    }

    public static int calculateDailyCalorieIntake(double weight, double height, int gender, int age, double activityLevel){
        double BMR = 0;
        if(gender == Constants.GENDER_FEMALE){
            BMR = 665.09 + (9.56 * weight) + (1.84 * height) - (4.67 * age);
        } else if (gender == Constants.GENDER_MALE){
            BMR = 66.47+ (13.75 * weight) + (5.0 * height) - (6.75 * age);
        }
        return (int)(BMR * activityLevel);
    }

}
