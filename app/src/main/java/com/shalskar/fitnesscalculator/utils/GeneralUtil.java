package com.shalskar.fitnesscalculator.utils;

import java.util.List;

/**
 * Created by Vincent on 10/06/2016.
 */
public class GeneralUtil {
    public static int[] convertToPrimitive(List<Integer> integerList) {
        int[] output = new int[integerList.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = integerList.get(i).intValue();
        }
        return output;
    }
}
