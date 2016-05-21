package com.shalskar.fitnesscalculator;

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

    /**
     * Macros are returned in carbohydrate/fat/protein format
     */
    public static double[] calculateMacros(double weight, double height, int gender, int age, double activityLevel, int goal){
        double[] macros = new double[3];
        int dailyCalorieIntake = calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);

        // todo switch from dummy values
        switch(goal){
            case Constants.GOAL_GAIN_MUSCLE:
                macros[0] = 0.4 * dailyCalorieIntake / 4;
                macros[1] = 0.2 * dailyCalorieIntake / 9;
                macros[2] = 0.4 * dailyCalorieIntake / 4;
                break;
            case Constants.GOAL_FAT_LOSS:
                macros[0] = 0.4 * dailyCalorieIntake / 4;
                macros[1] = 0.3 * dailyCalorieIntake / 9;
                macros[2] = 0.3 * dailyCalorieIntake / 4;
                break;
            case Constants.GOAL_MAINTAIN:
                macros[0] = 0.3 * dailyCalorieIntake / 4;
                macros[1] = 0.3 * dailyCalorieIntake / 9;
                macros[2] = 0.4 * dailyCalorieIntake / 4;
                break;

        }
        return macros;
    }

}
