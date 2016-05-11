package com.shalskar.fitnesscalculator.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.FitnessAdapter;
import com.shalskar.fitnesscalculator.events.HeightUpdatedEvent;
import com.shalskar.fitnesscalculator.events.WeightUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Vincent on 7/05/2016.
 */
public class BodyFragment extends Fragment implements FitnessAdapter.AdapterListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private FitnessAdapter fitnessAdapter;

    public BodyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        ButterKnife.bind(this, view);
        initialiseRecyclerView();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initialiseRecyclerView(){
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        fitnessAdapter = new FitnessAdapter(this);
        recyclerView.setAdapter(fitnessAdapter);
    }

    public void showBMIDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_edit_name");
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        BMIDialog editNameDialog = new BMIDialog();
        editNameDialog.show(manager, "fragment_edit_name");
    }

    /**
     * Respond to various events.
     **/

    @Subscribe
    public void onWeightUpdatedEvent(WeightUpdatedEvent weightUpdatedEvent) {
        fitnessAdapter.updateBMI();
    }

    @Subscribe
    public void onHeightUpdatedEvent(HeightUpdatedEvent heightUpdatedEvent) {
        fitnessAdapter.updateBMI();
    }

}