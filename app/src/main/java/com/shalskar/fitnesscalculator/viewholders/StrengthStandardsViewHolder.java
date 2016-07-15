package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.StrengthView;
import com.shalskar.fitnesscalculator.adapters.StrengthAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.AnimationUtil;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Vincent on 11/05/2016.
 */
public class StrengthStandardsViewHolder extends BaseViewHolder {

    private StrengthAdapter strengthAdapter;

    @BindView(R.id.squatStrengthView)
    StrengthView squatStrengthView;

    @BindView(R.id.benchStrengthView)
    StrengthView benchStrengthView;

    @BindView(R.id.deadliftStrengthView)
    StrengthView deadliftStrengthView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    public StrengthStandardsViewHolder(@NonNull StrengthAdapter strengthAdapter, @NonNull View baseView) {
        super(baseView);
        this.strengthAdapter = strengthAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
        titleTextView.setText(baseView.getContext().getString(R.string.strength_standards));
        title2TextView.setText(baseView.getContext().getString(R.string.strength_standards));
        loadImage(R.dimen.basic_viewholder_column_width, R.dimen.basic_viewholder_column_height, R.drawable.strength_standards_image);
        initialiseViews();
    }

    public void initialiseViews(){
        updateAll();
    }

    @Override
    public void updateAll() {
        float squatWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_SQUAT);
        float benchWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_BENCH_PRESS);
        float deadliftWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_DEADLIFT);
        double weight = SharedPreferencesManager.getWeight();

        int gender = SharedPreferencesManager.getGender();
        if (weight > 0 && gender != -1 && squatWeightLifted > 0 && benchWeightLifted > 0 && deadliftWeightLifted > 0) {
            updateWilks(weight, gender, squatWeightLifted, benchWeightLifted, deadliftWeightLifted);
            if (titleTextView.getVisibility() == View.VISIBLE) {
                animateSideLayout();
                animateTitle();
            } else {
                //AnimationUtil.refreshView(squatStrengthView);
            }
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            title2TextView.setVisibility(View.GONE);
            sideLayout.setVisibility(View.GONE);
        }
    }

    private void animateTitle() {
        titleTextView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                titleTextView.setVisibility(View.GONE);
                titleTextView.setAlpha(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        title2TextView.setAlpha(0);
        title2TextView.setVisibility(View.VISIBLE);
        title2TextView.animate().alpha(1).start();
    }

    private void updateWilks(double weight, int gender, float squatWeightLifted, float benchWeightLifted, float deadliftWeightLifted) {
        int squatStrengthStandard = FitnessCalculator.calculateStrengthStandard(Constants.EXERCISE_SQUAT, gender, (float) weight, squatWeightLifted);
        int benchStrengthStandard = FitnessCalculator.calculateStrengthStandard(Constants.EXERCISE_BENCH_PRESS, gender, (float) weight, benchWeightLifted);
        int deadliftStrengthStandard = FitnessCalculator.calculateStrengthStandard(Constants.EXERCISE_DEADLIFT, gender, (float) weight, deadliftWeightLifted);

        squatStrengthView.setLevel(squatStrengthStandard);
        benchStrengthView.setLevel(benchStrengthStandard);
        deadliftStrengthView.setLevel(deadliftStrengthStandard);
    }

    @OnClick(R.id.card_view)
    void showCalorieDialog() {
        strengthAdapter.showStrengthStandardsDialog();
    }

    @OnClick(R.id.info_button)
    void showInfoDialog() {
        strengthAdapter.showStrengthStandardsInfoDialog();
    }


}
