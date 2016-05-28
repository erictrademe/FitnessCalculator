package com.shalskar.fitnesscalculator.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.viewholders.BMIViewHolder;
import com.shalskar.fitnesscalculator.viewholders.CalorieViewHolder;
import com.shalskar.fitnesscalculator.viewholders.IdealWeightViewHolder;
import com.shalskar.fitnesscalculator.viewholders.MacroViewHolder;
import com.shalskar.fitnesscalculator.viewholders.OneRepMaxViewHolder;
import com.shalskar.fitnesscalculator.viewholders.WaterViewHolder;
import com.shalskar.fitnesscalculator.viewholders.WilksViewHolder;

/**
 * Created by Vincent on 11/05/2016.
 */
public class StrengthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private AdapterListener adapterListener;

    private RecyclerView.ViewHolder[] viewHolders;

    private static final int VIEW_TYPE_ONE_REP_MAX = 0;
    private static final int VIEW_TYPE_WILKS = 1;

    public StrengthAdapter(@NonNull AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
        this.viewHolders = new RecyclerView.ViewHolder[2];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view;
        switch (viewType) {
            case VIEW_TYPE_ONE_REP_MAX:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_thin, parent, false);
                viewHolder = new OneRepMaxViewHolder(this, view);
                break;
            case VIEW_TYPE_WILKS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_basic, parent, false);
                viewHolder = new WilksViewHolder(this, view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        viewHolders[position] = viewHolder;

        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_ONE_REP_MAX:
                ((OneRepMaxViewHolder) viewHolder).initialiseViews();
                break;
            case VIEW_TYPE_WILKS:
                ((WilksViewHolder) viewHolder).initialiseViews();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_WILKS;
            case 1:
                return VIEW_TYPE_ONE_REP_MAX;
        }
        return VIEW_TYPE_ONE_REP_MAX;
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
            case VIEW_TYPE_ONE_REP_MAX:
                return 2;
            case VIEW_TYPE_WILKS:
                return 2;
        }
        return 2;
    }

    public void updateOneRepMax() {
        ((OneRepMaxViewHolder) viewHolders[1]).updateAll();
    }

    public void updateWilks() {
        ((WilksViewHolder) viewHolders[0]).updateAll();
    }

    public void showOneRepMaxDialog() {
        adapterListener.showOneRepMaxDialog();
    }

    public void showWilksDialog() {
        adapterListener.showWilksDialog();
    }

    public void showWilksInfoDialog() {
        adapterListener.showWilksInfoDialog();
    }

    public interface AdapterListener {
        void showOneRepMaxDialog();

        void showWilksDialog();

        void showWilksInfoDialog();
    }

}