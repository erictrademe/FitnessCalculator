package com.shalskar.fitnesscalculator.events;

import android.support.annotation.NonNull;

/**
 * Created by Vincent on 7/05/2016.
 */
public class WeightUpdatedEvent {
    public final double weight;

    public WeightUpdatedEvent(double weight) {
        this.weight = weight;
    }
}
