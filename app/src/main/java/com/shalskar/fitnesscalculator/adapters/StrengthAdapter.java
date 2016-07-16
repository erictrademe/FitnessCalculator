package com.shalskar.fitnesscalculator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.fragments.CaloriesBurnedDialog;
import com.shalskar.fitnesscalculator.viewholders.BaseViewHolder;
import com.shalskar.fitnesscalculator.viewholders.CaloriesBurnedViewHolder;
import com.shalskar.fitnesscalculator.viewholders.OneRepMaxViewHolder;
import com.shalskar.fitnesscalculator.viewholders.PlaceholderViewHolder;
import com.shalskar.fitnesscalculator.viewholders.StrengthStandardsViewHolder;
import com.shalskar.fitnesscalculator.viewholders.WilksViewHolder;

/**
 * Created by Vincent on 11/05/2016.
 */
public class StrengthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterListener adapterListener;

    private RecyclerView.ViewHolder[] viewHolders;

    private static final int VIEW_TYPE_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_ONE_REP_MAX = 1;
    private static final int VIEW_TYPE_WILKS = 2;
    private static final int VIEW_TYPE_STRENGTH_STANDARDS = 3;
    private static final int VIEW_TYPE_CALORIES_BURNED = 4;

    public StrengthAdapter(@NonNull AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
        this.viewHolders = new RecyclerView.ViewHolder[5];
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
            case VIEW_TYPE_ONE_REP_MAX:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_1rm, parent, false);
                viewHolder = new OneRepMaxViewHolder(this, view);
                break;
            case VIEW_TYPE_WILKS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new WilksViewHolder(this, view);
                break;
            case VIEW_TYPE_STRENGTH_STANDARDS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic_column, parent, false);
                viewHolder = new StrengthStandardsViewHolder(this, view);
                break;
            case VIEW_TYPE_CALORIES_BURNED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new CaloriesBurnedViewHolder(this, view);
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
                return VIEW_TYPE_WILKS;
            case 2:
                return VIEW_TYPE_ONE_REP_MAX;
            case 3:
                return VIEW_TYPE_STRENGTH_STANDARDS;
            case 4:
                return VIEW_TYPE_CALORIES_BURNED;
        }
        return VIEW_TYPE_ONE_REP_MAX;
    }

    @Override
    public int getItemCount() {
        return viewHolders.length;
    }

    /**
     * Return how many columns the viewholder should span for the position given.
     * This may be changed to return 1 for smaller viewholders in the future.
     */
    public int getSpanForPosition(int position) {
        int viewType = getItemViewType(position);
//        switch (viewType) {
//            case VIEW_TYPE_PLACEHOLDER:
//                return 2;
//            case VIEW_TYPE_ONE_REP_MAX:
//                return 2;
//            case VIEW_TYPE_WILKS:
//                return 2;
//            case VIEW_TYPE_STRENGTH_STANDARDS:
//                return 2;
//            case VIEW_TYPE_CALORIES_BURNED:
//                return 2;
//        }
        return 2;
    }

    public void updateWilks() {
        if (viewHolders[1] != null)
            ((WilksViewHolder) viewHolders[1]).updateAll();
    }

    public void updateOneRepMax() {
        if (viewHolders[2] != null)
            ((OneRepMaxViewHolder) viewHolders[2]).updateAll();
    }

    public void updateStrengthStandards() {
        if (viewHolders[3] != null)
            ((StrengthStandardsViewHolder) viewHolders[3]).updateAll();
    }

    public void updateCaloriesBurned() {
        if (viewHolders[4] != null)
            ((CaloriesBurnedViewHolder) viewHolders[4]).updateAll();
    }

    public void showOneRepMaxDialog() {
        adapterListener.showOneRepMaxDialog();
    }

    public void showOneRepMaxInfoDialog() {
        adapterListener.showOneRepMaxInfoDialog();
    }

    public void showWilksDialog() {
        adapterListener.showWilksDialog();
    }

    public void showWilksInfoDialog() {
        adapterListener.showWilksInfoDialog();
    }

    public void showStrengthStandardsDialog() {
        adapterListener.showStrengthStandardsDialog();
    }

    public void showStrengthStandardsInfoDialog() {
        adapterListener.showStrengthStandardsInfoDialog();
    }

    public void showCaloriesBurnedDialog() {
        adapterListener.showCaloriesBurnedDialog();
    }

    public void showCaloriesBurnedInfoDialog() {
        adapterListener.showCaloriesBurnedInfoDialog();
    }

    public interface AdapterListener {
        void showOneRepMaxDialog();

        void showOneRepMaxInfoDialog();

        void showWilksDialog();

        void showWilksInfoDialog();

        void showStrengthStandardsDialog();

        void showStrengthStandardsInfoDialog();

        void showCaloriesBurnedDialog();

        void showCaloriesBurnedInfoDialog();
    }

}
