package com.shalskar.fitnesscalculator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.viewholders.BMIViewHolder;
import com.shalskar.fitnesscalculator.viewholders.CalorieViewHolder;

/**
 * Created by Vincent on 11/05/2016.
 */
public class FitnessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterListener adapterListener;

    private static final int VIEW_TYPE_BMI = 0;
    private static final int VIEW_TYPE_CALORIE = 1;

    public FitnessAdapter(@NonNull AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view;
        switch(viewType){
            case VIEW_TYPE_BMI:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_bmi, parent, false);
                viewHolder = new BMIViewHolder(this, view);
                break;
            case VIEW_TYPE_CALORIE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_calorie, parent, false);
                viewHolder = new CalorieViewHolder(this, view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch(viewType){
            case VIEW_TYPE_BMI:
                BMIViewHolder BMIViewHolder = (BMIViewHolder) viewHolder;
                BMIViewHolder.updateAll();
                break;
            case VIEW_TYPE_CALORIE:
                CalorieViewHolder calorieViewHolder = (CalorieViewHolder) viewHolder;
                calorieViewHolder.updateAll();
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
        }
        return VIEW_TYPE_BMI;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public void updateBMI(){
        notifyItemChanged(0);
    }

    public void updateCalorie(){
        notifyItemChanged(1);
    }

    public void showBMIDialog(){
        adapterListener.showBMIDialog();
    }

    public interface AdapterListener{
        public void showBMIDialog();
    }

}
