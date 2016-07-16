package com.shalskar.fitnesscalculator.model;

import android.support.annotation.NonNull;

/**
 * Created by Vincent on 15/07/2016.
 */
public class MetabolicEquivalent {

    /**
     * MET values
     */
    public static final MetabolicEquivalent MET_BICYCLING = new MetabolicEquivalent(1015, 7.5f, "Bicycling");
    public static final MetabolicEquivalent MET_ROPE_SKIPPING = new MetabolicEquivalent(2068, 11, "Rope skipping");
    public static final MetabolicEquivalent MET_ELLIPTICAL_TRAINER = new MetabolicEquivalent(2048, 5, "Elliptical Trainer");
    public static final MetabolicEquivalent MET_YOGA = new MetabolicEquivalent(2150, 3.3f, "Yoga");
    public static final MetabolicEquivalent MET_ROWING = new MetabolicEquivalent(2072, 7, "Rowing");
    public static final MetabolicEquivalent MET_DANCING = new MetabolicEquivalent(3015, 7.3f, "Dancing");
    public static final MetabolicEquivalent MET_FISHING = new MetabolicEquivalent(4001, 3.5f, "Fishing");
    public static final MetabolicEquivalent MET_HUNTING = new MetabolicEquivalent(4001, 5, "Hunting");
    public static final MetabolicEquivalent MET_CLEANING = new MetabolicEquivalent(5030, 3.3f, "Cleaning");
    public static final MetabolicEquivalent MET_INACTIVE = new MetabolicEquivalent(7010, 1, "Inactive");
    public static final MetabolicEquivalent MET_YARD_WORK = new MetabolicEquivalent(8261, 4, "Yard work");
    public static final MetabolicEquivalent MET_PLAYING_MUSIC = new MetabolicEquivalent(10074, 2, "Playing music");
    public static final MetabolicEquivalent MET_LIGHT_LABOUR = new MetabolicEquivalent(11475, 2.8f, "Light labour");
    public static final MetabolicEquivalent MET_MEDIUM_LABOUR = new MetabolicEquivalent(11476, 4.5f, "Medium labour");
    public static final MetabolicEquivalent MET_HEAVY_LABOUR = new MetabolicEquivalent(11477, 6.5f, "Heavy labour");
    public static final MetabolicEquivalent MET_JOGGING = new MetabolicEquivalent(12020, 7, "Jogging");
    public static final MetabolicEquivalent MET_RUNNING_SLOWLY = new MetabolicEquivalent(12029, 9, "Running slowly");
    public static final MetabolicEquivalent MET_RUNNING_NORMAL = new MetabolicEquivalent(12070, 11, "Running normal");
    public static final MetabolicEquivalent MET_RUNNING_FAST = new MetabolicEquivalent(12132, 17, "Running fast");
    public static final MetabolicEquivalent MET_SEX = new MetabolicEquivalent(14020, 1.8f, "Sex");
    public static final MetabolicEquivalent MET_BASKETBALL = new MetabolicEquivalent(15055, 6.5f, "Basketball");
    public static final MetabolicEquivalent MET_BOXING = new MetabolicEquivalent(15100, 12.8f, "Boxing");
    public static final MetabolicEquivalent MET_AMERICAN_FOOTBALL = new MetabolicEquivalent(15230, 8, "American football");
    public static final MetabolicEquivalent MET_FOOTBALL = new MetabolicEquivalent(15605, 10, "Soccer");
    public static final MetabolicEquivalent MET_HANDBALL = new MetabolicEquivalent(15320, 12, "Handball");
    public static final MetabolicEquivalent MET_HOCKEY = new MetabolicEquivalent(15350, 8, "Hockey");
    public static final MetabolicEquivalent HORSEBACK_RIDING = new MetabolicEquivalent(15370, 5.5f, "Horeseback riding");
    public static final MetabolicEquivalent MET_MARTIAL_ARTS_SLOW = new MetabolicEquivalent(15425, 5.3f, "Martial arts slow pace");
    public static final MetabolicEquivalent MET_MARTIAL_ARTS_MODERATE = new MetabolicEquivalent(15430, 10.3f, "Martial arts moderate pace");
    public static final MetabolicEquivalent MET_MOTORCROSS = new MetabolicEquivalent(15470, 4, "Motocross");
    public static final MetabolicEquivalent MET_ROCK_CLIMBING = new MetabolicEquivalent(15537, 5.8f, "Rock climbing");
    public static final MetabolicEquivalent MET_RUGBY = new MetabolicEquivalent(15562, 8.3f, "Rugby");
    public static final MetabolicEquivalent MET_SKATEBOARDING = new MetabolicEquivalent(15580, 5, "Skateboarding");
    public static final MetabolicEquivalent MET_ROLLERBLADING = new MetabolicEquivalent(15592, 9.8f, "Rollerblading");
    public static final MetabolicEquivalent MET_SQUASH = new MetabolicEquivalent(15652, 7.3f, "Squash");
    public static final MetabolicEquivalent MET_TABLE_TENNIS = new MetabolicEquivalent(15660, 4, "Table tennis");
    public static final MetabolicEquivalent MET_TENNIS = new MetabolicEquivalent(15675, 7.3f, "Tennis");
    public static final MetabolicEquivalent MET_VOLLEYBALL = new MetabolicEquivalent(15711, 6, "Volleyball");
    public static final MetabolicEquivalent MET_CLIMBING_HILLS = new MetabolicEquivalent(17033, 6.3f, "Climbing hills");
    public static final MetabolicEquivalent MET_HIKING = new MetabolicEquivalent(17082, 5.3f, "Hiking");
    public static final MetabolicEquivalent MET_WALKING = new MetabolicEquivalent(17160, 3.5f, "Walking");
    public static final MetabolicEquivalent MET_CANOEING = new MetabolicEquivalent(18070, 3.5f, "Canoeing");
    public static final MetabolicEquivalent MET_KAYAKING = new MetabolicEquivalent(18060, 12.5f, "Kayaking");
    public static final MetabolicEquivalent MET_SWIMMING = new MetabolicEquivalent(18310, 6, "Swimming");
    public static final MetabolicEquivalent MET_WATER_POLO = new MetabolicEquivalent(18360, 10, "Water polo");
    public static final MetabolicEquivalent MET_ICE_SKATING = new MetabolicEquivalent(19030, 7, "Ice skating");
    public static final MetabolicEquivalent MET_SKIING = new MetabolicEquivalent(19075, 7, "Skiing");

    public static final MetabolicEquivalent[] MET_ACTIVITIES = {MET_AMERICAN_FOOTBALL, MET_BASKETBALL, MET_BICYCLING, MET_BOXING, MET_CANOEING,
            MET_CLEANING, MET_CLIMBING_HILLS, MET_DANCING, MET_ELLIPTICAL_TRAINER, MET_FISHING, MET_FOOTBALL, MET_HANDBALL, MET_HEAVY_LABOUR,
            MET_HIKING, MET_HOCKEY, HORSEBACK_RIDING, MET_HUNTING, MET_ICE_SKATING, MET_JOGGING, MET_INACTIVE, MET_KAYAKING, MET_LIGHT_LABOUR,
            MET_MARTIAL_ARTS_MODERATE, MET_MARTIAL_ARTS_SLOW, MET_MEDIUM_LABOUR, MET_MOTORCROSS, MET_PLAYING_MUSIC, MET_ROCK_CLIMBING, MET_ROLLERBLADING,
            MET_ROPE_SKIPPING, MET_ROWING, MET_RUGBY, MET_RUNNING_FAST, MET_RUNNING_NORMAL, MET_RUNNING_SLOWLY, MET_SEX, MET_SKATEBOARDING, MET_SKIING,
            MET_SQUASH, MET_SWIMMING, MET_TABLE_TENNIS, MET_TENNIS, MET_VOLLEYBALL, MET_WALKING, MET_WATER_POLO, MET_YARD_WORK, MET_YOGA};

    private static String[] MET_STRINGS;

    /**
     * Immutable class for MET values of various activities.
     */

    public final int code;
    public final float value;
    public final String description;

    private MetabolicEquivalent(int code, float value, String description) {
        this.code = code;
        this.value = value;
        this.description = description;
    }

    // todo implement

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static MetabolicEquivalent getMET(int code) {
        for (int i = 0; i < MET_ACTIVITIES.length; i++)
            if (MET_ACTIVITIES[i].code == code) return MET_ACTIVITIES[i];
        return null;
    }

    // todo use equals method instead
    public static int getPositionForMET(@NonNull MetabolicEquivalent MET){
        for (int i = 0; i < MET_ACTIVITIES.length; i++)
            if (MET_ACTIVITIES[i].code == MET.code) return i;
        return 0;
    }

    /**
     * Array of the descriptions of the METs, this uses lazy loading.
     */
    public static String[] getMETSasStringArray(){
        if (MET_STRINGS == null) {
            MET_STRINGS = new String[MET_ACTIVITIES.length];
            for (int i = 0; i < MET_ACTIVITIES.length; i++) {
                MET_STRINGS[i] = MET_ACTIVITIES[i].description;
            }
        }
        return MET_STRINGS;
    }
}
