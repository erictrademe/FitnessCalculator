package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.ItemOffsetDecoration;
import com.shalskar.fitnesscalculator.adapters.StrengthAdapter;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 7/05/2016.
 */
public class StrengthFragment extends Fragment implements StrengthAdapter.AdapterListener {

    private static final String TAG_ONE_REP_MAX_DIALOG_FRAGMENT = "fragment_1rm";
    private static final String TAG_WILKS_DIALOG_FRAGMENT = "fragment_wilks";
    private static final String TAG_STRENGTH_STANDARDS_DIALOG_FRAGMENT = "fragment_strength_standards";
    private static final String TAG_CALORIEES_BURNED_DIALOG_FRAGMENT = "fragment_calories_burned";

    @BindView(R.id.recycler_view_strength)
    RecyclerView recyclerView;

    private GridLayoutManager gridLayoutManager;
    private StrengthAdapter strengthAdapter;

    public StrengthFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_strength, container, false);
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
                return strengthAdapter.getSpanForPosition(position);
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        strengthAdapter = new StrengthAdapter(this);
        recyclerView.setAdapter(strengthAdapter);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.viewholder_padding));
    }

    public String getTitle(){
        return getString(R.string.strength);
    }

    @Override
    public void showOneRepMaxDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_ONE_REP_MAX_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        OneRepMaxDialog oneRepMaxDialog = OneRepMaxDialog.newInstance(getString(R.string.one_rep_max));
        oneRepMaxDialog.show(manager, TAG_ONE_REP_MAX_DIALOG_FRAGMENT);
    }

    @Override
    public void showOneRepMaxInfoDialog() {
        DialogUtil.showMessageDialog(getFragmentManager(),
                getActivity().getString(R.string.one_rep_max_description_title),
                getActivity().getString(R.string.one_rep_max_description),
                R.drawable.one_rep_max_image);
    }

    @Override
    public void showWilksDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_WILKS_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        WilksDialog wilksDialog = WilksDialog.newInstance(getContext().getString(R.string.wilks));
        wilksDialog.show(manager, TAG_WILKS_DIALOG_FRAGMENT);
    }

    @Override
    public void showWilksInfoDialog() {
        DialogUtil.showMessageDialog(getFragmentManager(),
                getActivity().getString(R.string.wilks_description_title),
                getActivity().getString(R.string.wilks_description),
                R.drawable.wilks_image);
    }

    @Override
    public void showStrengthStandardsDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_STRENGTH_STANDARDS_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        WilksDialog wilksDialog = WilksDialog.newInstance(getContext().getString(R.string.strength_standards), R.drawable.strength_standards_image);
        wilksDialog.show(manager, TAG_STRENGTH_STANDARDS_DIALOG_FRAGMENT);
    }

    @Override
    public void showStrengthStandardsInfoDialog() {
        DialogUtil.showMessageDialog(getFragmentManager(),
                getActivity().getString(R.string.strength_standards_description_title),
                getActivity().getString(R.string.strength_standards_description),
                R.drawable.strength_standards_image);
    }

    @Override
    public void showCaloriesBurnedDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag(TAG_CALORIEES_BURNED_DIALOG_FRAGMENT);
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        CaloriesBurnedDialog caloriesBurnedDialog = CaloriesBurnedDialog.newInstance(getContext().getString(R.string.calories_burned));
        caloriesBurnedDialog.show(manager, TAG_CALORIEES_BURNED_DIALOG_FRAGMENT);
    }

    @Override
    public void showCaloriesBurnedInfoDialog() {

    }

    /**
     * Respond to various events.
     **/

    @Subscribe
    public void onDetailsUpdatedEvent(DetailsUpdatedEvent detailsUpdatedEvent) {
        boolean updateOneRepMax = false;
        boolean updateWilks = false;
        boolean updateStrengthStandards = false;
        boolean updateCaloriesBurned = false;

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_ONE_REP_MAX)) {
            updateOneRepMax = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_EXERCISE)) {
            updateWilks = true;
            updateStrengthStandards = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_WEIGHT)) {
            updateWilks = true;
            updateStrengthStandards = true;
            updateCaloriesBurned = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_GENDER)) {
            updateWilks = true;
            updateStrengthStandards = true;
        }

        if(listContains(detailsUpdatedEvent.detailsUpdated, Constants.DETAIL_MET)) {
            updateCaloriesBurned = true;
        }

        if(updateOneRepMax) strengthAdapter.updateOneRepMax();
        if(updateWilks) strengthAdapter.updateWilks();
        if(updateStrengthStandards) strengthAdapter.updateStrengthStandards();
        if(updateCaloriesBurned) strengthAdapter.updateCaloriesBurned();

    }

    public void refresh(){
        strengthAdapter.notifyDataSetChanged();
    }

    public static boolean listContains(int[] list, int item) {
        for (int i = 0; i < list.length; i++) {
            if (list[i] == item) return true;
        }
        return false;
    }

}