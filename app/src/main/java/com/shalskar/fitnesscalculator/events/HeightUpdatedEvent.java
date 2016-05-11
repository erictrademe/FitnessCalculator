package com.shalskar.fitnesscalculator.events;

/**
 * Created by Vincent on 7/05/2016.
 */
public class HeightUpdatedEvent {
    public final double height;

    public HeightUpdatedEvent(double height) {
        this.height = height;
    }
}
