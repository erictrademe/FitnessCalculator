package com.shalskar.fitnesscalculator.events;

import com.shalskar.fitnesscalculator.utils.GeneralUtil;

import java.util.List;

/**
 * Created by RachelTeTau on 17/05/16.
 */
public class DetailsUpdatedEvent {

    public int[] detailsUpdated;

    public DetailsUpdatedEvent(int... detailUpdated){
        detailsUpdated = detailUpdated;
    }

    public DetailsUpdatedEvent(List<Integer> detailsUpdated){
        this.detailsUpdated = GeneralUtil.convertToPrimitive(detailsUpdated);
    }

}
