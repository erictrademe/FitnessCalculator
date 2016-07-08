package com.shalskar.fitnesscalculator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.viewholders.BMIViewHolder;
import com.shalskar.fitnesscalculator.viewholders.BaseViewHolder;
import com.shalskar.fitnesscalculator.viewholders.BodyfatViewHolder;
import com.shalskar.fitnesscalculator.viewholders.CalorieViewHolder;
import com.shalskar.fitnesscalculator.viewholders.IdealPhysiqueViewHolder;
import com.shalskar.fitnesscalculator.viewholders.IdealWeightViewHolder;
import com.shalskar.fitnesscalculator.viewholders.MacroViewHolder;
import com.shalskar.fitnesscalculator.viewholders.PlaceholderViewHolder;
import com.shalskar.fitnesscalculator.viewholders.WaterViewHolder;

/**
 * Created by Vincent on 11/05/2016.
 */
public class BodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterListener adapterListener;

    private RecyclerView.ViewHolder[] viewHolders;

    private static final int VIEW_TYPE_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_CALORIE = 1;
    private static final int VIEW_TYPE_MACRO = 2;
    private static final int VIEW_TYPE_WATER = 3;
    private static final int VIEW_TYPE_IDEAL_WEIGHT = 4;
    private static final int VIEW_TYPE_BMI = 5;
    private static final int VIEW_TYPE_BODYFAT = 6;
    private static final int VIEW_TYPE_IDEAL_PHYSIQUE = 7;

    public BodyAdapter(@NonNull AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
        this.viewHolders = new RecyclerView.ViewHolder[8];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view;
        switch (viewType) {
            case VIEW_TYPE_PLACEHOLDER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_placeholder, parent, false);
                viewHolder = new PlaceholderViewHolder(view);
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
            case VIEW_TYPE_IDEAL_WEIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_small, parent, false);
                viewHolder = new IdealWeightViewHolder(this, view);
                break;
            case VIEW_TYPE_BMI:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new BMIViewHolder(this, view);
                break;
            case VIEW_TYPE_BODYFAT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new BodyfatViewHolder(this, view);
                break;
            case VIEW_TYPE_IDEAL_PHYSIQUE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_ideal_physique, parent, false);
                viewHolder = new IdealPhysiqueViewHolder(this, view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        viewHolders[position] = viewHolder;
        int viewType = getItemViewType(position);
        if (viewType != VIEW_TYPE_PLACEHOLDER)
            ((BaseViewHolder) viewHolder).updateAll();
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_PLACEHOLDER;
            case 1:
                return VIEW_TYPE_CALORIE;
            case 2:
                return VIEW_TYPE_MACRO;
            case 3:
                return VIEW_TYPE_WATER;
            case 4:
                return VIEW_TYPE_IDEAL_WEIGHT;
            case 5:
                return VIEW_TYPE_BMI;
            case 6:
                return VIEW_TYPE_BODYFAT;
            case 7:
                return VIEW_TYPE_IDEAL_PHYSIQUE;
        }
        return VIEW_TYPE_BMI;
    }

    @Override
    public int getItemCount() {
        return viewHolders.length;
    }

    /**
     * Return how many columns the viewholder should span for the position given.
     *
     * @param position
     * @return
     */
    public int getSpanForPosition(int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_PLACEHOLDER:
                return 2;
            case VIEW_TYPE_CALORIE:
                return 2;
            case VIEW_TYPE_MACRO:
                return 2;
            case VIEW_TYPE_WATER:
                return 1;
            case VIEW_TYPE_IDEAL_WEIGHT:
                return 1;
            case VIEW_TYPE_BMI:
                return 2;
            case VIEW_TYPE_BODYFAT:
                return 2;
            case VIEW_TYPE_IDEAL_PHYSIQUE:
                return 2;
        }
        return 0;
    }

    public void updateCalorie() {
        if (viewHolders[1] != null)
            ((CalorieViewHolder) viewHolders[1]).updateAll();
    }

    public void updateMacro() {
        if (viewHolders[2] != null)
            ((MacroViewHolder) viewHolders[2]).updateAll();
    }

    public void updateWater() {
        if (viewHolders[3] != null)
            ((WaterViewHolder) viewHolders[3]).updateAll();
    }

    public void updateIdealWeight() {
        if (viewHolders[4] != null)
            ((IdealWeightViewHolder) viewHolders[4]).updateAll();
    }

    public void updateBMI() {
        if (viewHolders[5] != null)
            ((BMIViewHolder) viewHolders[5]).updateAll();
    }

    public void updateBodyfat() {
        if (viewHolders[6] != null)
            ((BodyfatViewHolder) viewHolders[6]).updateAll();
    }

    public void updateIdealPhysique() {
        if (viewHolders[7] != null)
            ((IdealPhysiqueViewHolder) viewHolders[7]).updateAll();
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

    public void showIdealWeightDialog() {
        adapterListener.showIdealWeightDialog();
    }

    public void showBodyfatDialog() {
        adapterListener.showBodyfatDialog();
    }

    public void showBodyfatInfoDialog() {
        adapterListener.showBodyfatInfoDialog();
    }

    public void showIdealPhysiqueDialog() {
        adapterListener.showIdealPhysiqueDialog();
    }

    public void showIdealPhysiqueInfoDialog() {
        adapterListener.showIdealPhysiqueInfoDialog();
    }

    public interface AdapterListener {
        void showBMIDialog();

        void showBMIInfoDialog();

        void showCalorieDialog();

        void showCalorieInfoDialog();

        void showMacroDialog();

        void showMacroInfoDialog();

        void showWaterDialog();

        void showIdealWeightDialog();

        void showIdealPhysiqueDialog();

        void showIdealPhysiqueInfoDialog();

        void showBodyfatDialog();

        void showBodyfatInfoDialog();
    }

}
