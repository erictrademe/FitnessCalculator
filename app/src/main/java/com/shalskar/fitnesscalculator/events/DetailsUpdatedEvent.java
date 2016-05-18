package com.shalskar.fitnesscalculator.events;

/**
 * Created by RachelTeTau on 17/05/16.
 */
public class DetailsUpdatedEvent {

    public int[] detailsUpdated;

    public DetailsUpdatedEvent(int... detailUpdated){
        detailsUpdated = detailUpdated;
    }

}
