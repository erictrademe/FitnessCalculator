package com.shalskar.fitnesscalculator.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.viewholders.BMIViewHolder;
import com.shalskar.fitnesscalculator.viewholders.CalorieViewHolder;
import com.shalskar.fitnesscalculator.viewholders.MacroViewHolder;
import com.shalskar.fitnesscalculator.viewholders.WaterViewHolder;

import java.util.ArrayList;

/**
 * Created by Vincent on 11/05/2016.
 */
public class FitnessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterListener adapterListener;

    private RecyclerView.ViewHolder[] viewHolders;

    private static final int VIEW_TYPE_BMI = 0;
    private static final int VIEW_TYPE_CALORIE = 1;
    private static final int VIEW_TYPE_MACRO = 2;
    private static final int VIEW_TYPE_WATER = 3;

    public FitnessAdapter(@NonNull AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
        this.viewHolders = new RecyclerView.ViewHolder[5];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view;
        switch (viewType) {
            case VIEW_TYPE_BMI:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new BMIViewHolder(this, view);
                break;
            case VIEW_TYPE_CALORIE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new CalorieViewHolder(this, view);
                break;
            case VIEW_TYPE_MACRO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new MacroViewHolder(this, view);
                break;
            case VIEW_TYPE_WATER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_small, parent, false);
                viewHolder = new WaterViewHolder(this, view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        viewHolders[position] = viewHolder;

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_BMI:
                BMIViewHolder BMIViewHolder = (BMIViewHolder) viewHolder;
                BMIViewHolder.initialiseViews();
                break;
            case VIEW_TYPE_CALORIE:
                CalorieViewHolder calorieViewHolder = (CalorieViewHolder) viewHolder;
                calorieViewHolder.initialiseViews();
                break;
            case VIEW_TYPE_MACRO:
                MacroViewHolder macroViewHolder = (MacroViewHolder) viewHolder;
                macroViewHolder.initialiseViews();
                break;
            case VIEW_TYPE_WATER:
                WaterViewHolder waterViewHolder = (WaterViewHolder) viewHolder;
                waterViewHolder.initialiseViews();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_BMI;
            case 1:
                return VIEW_TYPE_CALORIE;
            case 2:
                return VIEW_TYPE_MACRO;
            case 3:
                return VIEW_TYPE_WATER;
            case 4:
                return VIEW_TYPE_WATER;
        }
        return VIEW_TYPE_BMI;
    }

    @Override
    public int getItemCount() {
        return viewHolders.length;
    }

    /**
     * Return how many columns the viewholder should span for the position given.
     * @param position
     * @return
     */
    public int getSpanForPosition(int position){
        int viewType = getItemViewType(position);
        switch(viewType){
            case VIEW_TYPE_BMI:
                return 2;
            case VIEW_TYPE_CALORIE:
                return 2;
            case VIEW_TYPE_MACRO:
                return 2;
            case VIEW_TYPE_WATER:
                return 1;
        }
        return 0;
    }

    public void updateBMI() {
        BMIViewHolder bmiViewHolder = (BMIViewHolder) viewHolders[0];
        bmiViewHolder.updateAll();
    }

    public void updateCalorie() {
        CalorieViewHolder calorieViewHolder = (CalorieViewHolder) viewHolders[1];
        calorieViewHolder.updateAll();
    }

    public void updateMacro() {
        MacroViewHolder macroViewHolder = (MacroViewHolder) viewHolders[2];
        macroViewHolder.updateAll();
    }

    public void updateWater() {
        WaterViewHolder waterViewHolder = (WaterViewHolder) viewHolders[3];
        waterViewHolder.updateAll();
        WaterViewHolder waterViewHolder2 = (WaterViewHolder) viewHolders[4];
        waterViewHolder2.updateAll();
    }

    public void showBMIDialog() {
        adapterListener.showBMIDialog();
    }

    public void showBMIInfoDialog() {
        adapterListener.showBMIInfoDialog();
    }

    public void showCalorieDialog() {
        adapterListener.showCalorieDialog();
    }

    public void showCalorieInfoDialog() {
        adapterListener.showCalorieInfoDialog();
    }

    public void showMacroDialog() {
        adapterListener.showMacroDialog();
    }

    public void showMacroInfoDialog() {
        adapterListener.showMacroInfoDialog();
    }

    public void showWaterDialog() {
        adapterListener.showWaterDialog();
    }

    public interface AdapterListener {
        void showBMIDialog();

        void showBMIInfoDialog();

        void showCalorieDialog();

        void showCalorieInfoDialog();

        void showMacroDialog();

        void showMacroInfoDialog();

        void showWaterDialog();
    }

}
