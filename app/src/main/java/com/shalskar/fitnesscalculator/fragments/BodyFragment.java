package com.shalskar.fitnesscalculator.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
import com.shalskar.fitnesscalculator.adapters.ItemOffsetDecoration;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 7/05/2016.
 */
public class BodyFragment extends Fragment implements BodyAdapter.AdapterListener {

    private static final String TAG_BMI_DIALOG_FRAGMENT = "fragment_bmi";
    private static final String TAG_CALORIE_DIALOG_FRAGMENT = "fragment_calorie";
    private static final String TAG_MACRO_DIALOG_FRAGMENT = "fragment_macro";
    private static final String TAG_IDEAL_WEIGHT_DIALOG_FRAGMENT = "fragment_ideal_weight";
    private static final String TAG_IDEAL_PHYSIQUE_DIALOG_FRAGMENT = "fragment_ideal_physique";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private BodyAdapter bodyAdapter;

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

    private void initialiseRecyclerView() {
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return bodyAdapter.getSpanForPosition(position);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        bodyAdapter = new BodyAdapter(this);
        recyclerView.setAdapter(bodyAdapter);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.viewholder_padding));
    }

    @Override
    public void showBMIDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_BMI_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        BMIDialog BMIDialog = com.shalskar.fitnesscalculator.fragments.BMIDialog.newInstance(getString(R.string.body_mass_index));
        BMIDialog.show(manager, TAG_BMI_DIALOG_FRAGMENT);
    }

    @Override
    public void showCalorieDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_CALORIE_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        CalorieDialog calorieDialog = new CalorieDialog();
        calorieDialog.show(manager, TAG_CALORIE_DIALOG_FRAGMENT);
    }

    @Override
    public void showCalorieInfoDialog() {
        DialogUtil.showMessageDialog(getActivity(),
                getActivity().getString(R.string.daily_calorie_intake_description_title),
                getActivity().getString(R.string.daily_calorie_intake_description));
    }

    @Override
    public void showMacroDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_MACRO_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        MacroDialog macroDialog = new MacroDialog();
        macroDialog.show(manager, TAG_MACRO_DIALOG_FRAGMENT);
    }

    @Override
    public void showMacroInfoDialog() {
        DialogUtil.showMessageDialog(getActivity(),
                getActivity().getString(R.string.macro_description_title),
                getActivity().getString(R.string.macro_description));
    }

    @Override
    public void showWaterDialog() {
        // Same as calorie dialog
        showCalorieDialog();
    }

    @Override
    public void showIdealWeightDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_IDEAL_WEIGHT_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        IdealWeightDialog idealWeightDialog = new IdealWeightDialog();
        idealWeightDialog.show(manager, TAG_IDEAL_WEIGHT_DIALOG_FRAGMENT);
    }

    @Override
    public void showIdealPhysiqueDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_IDEAL_PHYSIQUE_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        IdealPhysiqueDialog idealPhysiqueDialog = new IdealPhysiqueDialog();
        idealPhysiqueDialog.show(manager, TAG_IDEAL_PHYSIQUE_DIALOG_FRAGMENT);
    }

    @Override
    public void showIdealPhysiqueInfoDialog() {

    }

    @Override
    public void showBMIInfoDialog() {
        DialogUtil.showMessageDialog(getActivity(),
                getActivity().getString(R.string.bmi_description_title),
                getActivity().getString(R.string.bmi_description));
    }

    /**
     * Respond to various events.
     **/

    @Subscribe
    public void onDetailsUpdatedEvent(DetailsUpdatedEvent detailsUpdatedEvent) {
        boolean updateBMI = false;
        boolean updateCalories = false;
        boolean updateMacro = false;
        boolean updateWater = false;
        boolean updateIdealWeight = false;
        boolean updateIdealPhysique = false;

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_AGE)) {
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_GENDER)) {
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_WEIGHT)) {
            updateBMI = true;
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
            updateIdealWeight = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_HEIGHT)) {
            updateBMI = true;
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
            updateIdealWeight = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_ACTIVITY_LEVEL)) {
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_GOAL)) {
            updateMacro = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_MEASUREMENT)) {
            updateIdealPhysique = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_UNIT)) {
            updateBMI = true;
            updateCalories = true;
            updateMacro = true;
            updateWater = true;
            updateIdealWeight = true;
            updateIdealPhysique = true;
        }

        if(updateBMI) bodyAdapter.updateBMI();
        if(updateCalories) bodyAdapter.updateCalorie();
        if(updateMacro) bodyAdapter.updateMacro();
        if(updateWater) bodyAdapter.updateWater();
        if(updateIdealWeight) bodyAdapter.updateIdealWeight();
        if(updateIdealPhysique) bodyAdapter.updateIdealPhysique();

    }

    public void refresh(){
        bodyAdapter.notifyDataSetChanged();
    }

    public static boolean listContains(int[] list, int item) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == item) return true;
        }
        return false;
    }

}