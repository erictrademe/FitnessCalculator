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
public class StrengthStandardsViewHolder extends RecyclerView.ViewHolder {

    private StrengthAdapter strengthAdapter;

    private View baseView;

    @BindView(R.id.card_view)
    View cardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    @BindView(R.id.chart)
    ColumnChartView chartView;

    @BindView(R.id.image)
    ImageView imageView;

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
    }

    public void initialiseViews(){
        titleTextView.setText(baseView.getContext().getString(R.string.strength_standards));
        title2TextView.setText(baseView.getContext().getString(R.string.strength_standards));
        loadImage();
        updateAll();
    }

    private void loadImage() {
        float bucketSize = baseView.getResources().getDisplayMetrics().density;
        int width = (int) (baseView.getResources().getDimension(R.dimen.basic_viewholder_width) / bucketSize);
        int height = (int) (baseView.getResources().getDimension(R.dimen.basic_viewholder_height) / bucketSize);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), R.drawable.strength_standards_image, width, height));
    }

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
                AnimationUtil.refreshView(chartView);
            }
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            title2TextView.setVisibility(View.GONE);
            sideLayout.setVisibility(View.GONE);
        }
    }

    private void animateSideLayout() {
        sideLayout.setTranslationX(sideLayout.getWidth());
        sideLayout.setAlpha(0);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.animate().alpha(1).translationX(0).setInterpolator(new DecelerateInterpolator()).start();
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
        int unit = SharedPreferencesManager.getUnit();

        int squatStrengthStandard = Constants.STRENGTH_STANDARD_ADVANCED;//FitnessCalculator.calculateBenchStrengthStandard(gender, (float) weight, benchWeightLifted);
        int benchStrengthStandard = FitnessCalculator.calculateBenchStrengthStandard(gender, (float) weight, benchWeightLifted);
        int deadliftStrengthStandard = Constants.STRENGTH_STANDARD_INTERMEDIATE;//FitnessCalculator.calculateBenchStrengthStandard(gender, (float) weight, benchWeightLifted);

        updateChart(squatWeightLifted,squatStrengthStandard, benchWeightLifted, benchStrengthStandard, deadliftWeightLifted, deadliftStrengthStandard);
    }

    private void updateChart(float squatWeightLifted, int squatStrengthStandard, float benchWeightLifted, int benchStrengthStandard,
                             float deadliftWeightLifted, int deadliftStrengthStandard) {

        ColumnChartData columnChartData = new ColumnChartData();

        List<Column> columns = new ArrayList<>();
        columns.add(makeColumn(squatStrengthStandard, squatStrengthStandard));
        columns.add(makeColumn(benchStrengthStandard, benchStrengthStandard));
        columns.add(makeColumn(deadliftStrengthStandard, deadliftStrengthStandard));

        Axis axisX = new Axis();
        List<AxisValue> axisValues = new ArrayList<>();
        axisValues.add(new AxisValue(0).setLabel("Squat"));
        axisValues.add(new AxisValue(1).setLabel("Bench"));
        axisValues.add(new AxisValue(2).setLabel("Dead"));
        axisX.setValues(axisValues);

        columnChartData.setAxisXBottom(axisX);
        columnChartData.setColumns(columns);

        chartView.setInteractive(false);
        chartView.setColumnChartData(columnChartData);
    }

    private Column makeColumn(float weightLifted, int standard){
        int color = R.color.paleGreen;
        switch (standard){
            case Constants.STRENGTH_STANDARD_UNTRAINED:
                color = R.color.paleGreen;
                break;
            case Constants.STRENGTH_STANDARD_NOVICE:
                color = R.color.yellowGreen;
                break;
            case Constants.STRENGTH_STANDARD_INTERMEDIATE:
                color = R.color.mustardOrange;
                break;
            case Constants.STRENGTH_STANDARD_ADVANCED:
                color = R.color.lightRed;
                break;
            case Constants.STRENGTH_STANDARD_ELITE:
                color = R.color.deepRed;
                break;
        }
        List<SubcolumnValue> subcolumnValues = new ArrayList<>();
        subcolumnValues.add(new SubcolumnValue(weightLifted, baseView.getContext().getResources().getColor(color)));
        return new Column(subcolumnValues);
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
