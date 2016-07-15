package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.StrengthAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Vincent on 11/05/2016.
 */
public class OneRepMaxViewHolder extends BaseViewHolder {

    // todo copy over improvements in this class to other ViewHolder classes

    private static final int REPS_MAX_VALUE_UPPER_LIMIT = 12;
    private static final int REPS_MAX_VALUE_LOWER_LIMIT = 1;

    private StrengthAdapter strengthAdapter;

    @BindView(R.id.chart)
    PieChartView chartView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    @BindView(R.id.increase_button)
    ImageButton increaseButton;

    @BindView(R.id.decrease_button)
    ImageButton decreaseButton;

    private PieChartData pieChartData;

    private int repsMaxValue;

    public OneRepMaxViewHolder(@NonNull StrengthAdapter strengthAdapter, @NonNull View baseView) {
        super(baseView);
        this.strengthAdapter = strengthAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
        repsMaxValue = SharedPreferencesManager.getRepsMaxValue();
        initialiseTitle();
        loadImage(R.dimen.thin_viewholder_width, R.dimen.thin_viewholder_height, R.drawable.one_rep_max_image);
        initialiseViews();
    }

    public void initialiseViews() {
        initialiseChart();
        updateAll();
    }

    private void initialiseTitle() {
        titleTextView.setText(baseView.getContext().getString(R.string.one_rep_max));
        title2TextView.setText(baseView.getContext().getString(R.string.one_rep_max));
    }

    private void initialiseChart() {
        Context context = baseView.getContext();
        pieChartData = new PieChartData();

        Typeface typeface = Typeface.createFromAsset(baseView.getContext().getAssets(), Constants.DEFAULT_FONT_PATH);
        pieChartData.setCenterText1Typeface(typeface);
        pieChartData.setCenterText2Typeface(typeface);

        int white = ContextCompat.getColor(context, R.color.white);
        pieChartData.setCenterText1Color(white);
        pieChartData.setCenterText2Color(white);
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, white));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize(getUnconvertedDimension(R.dimen.one_rep_max_pie_chart_text_size));
        pieChartData.setCenterText2FontSize(getUnconvertedDimension(R.dimen.one_rep_max_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.975f);
        pieChartData.setSlicesSpacing(4);
        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @Override
    public void updateAll() {
        double weightLifted = SharedPreferencesManager.getWeightLifted();
        int repsLifted = SharedPreferencesManager.getRepsLifted();
        if (weightLifted > 0 && repsLifted > 0) {
            updateRepMax();
            if (titleTextView.getVisibility() == View.VISIBLE) {
                animateSideLayout();
                animateTitle();
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

    private void updateRepMax() {
        float weightLifted = SharedPreferencesManager.getWeightLifted();
        int repsLifted = SharedPreferencesManager.getRepsLifted();
        int oneRepMaxFormula = SharedPreferencesManager.getOneRepMaxFormula();

        float repMax = FitnessCalculator.calculateRepMax(repsMaxValue, oneRepMaxFormula, repsLifted, weightLifted);

        updateChart(repMax);
    }

    private void updateChart(float oneRepMax) {
        int unit = SharedPreferencesManager.getUnit();
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        updateChartPrimaryText(decimalFormat, oneRepMax, unit);
        updateChartSecondaryText(decimalFormat);
        updateRepButtons();
        chartView.setPieChartData(pieChartData);
    }

    private void updateChartPrimaryText(@NonNull DecimalFormat decimalFormat, float oneRepMax, int unit){
        if (unit == Constants.UNIT_IMPERIAL) {
            pieChartData.setCenterText1(decimalFormat.format(ConverterUtil.kgsToPounds(oneRepMax)) + baseView.getContext().getString(R.string.pounds_short));
        } else if (unit == Constants.UNIT_METRIC) {
            pieChartData.setCenterText1(decimalFormat.format(oneRepMax) + baseView.getContext().getString(R.string.kilograms_short));
        }
    }

    private void updateChartSecondaryText(@NonNull DecimalFormat decimalFormat){
        if (repsMaxValue > 1)
            pieChartData.setCenterText2(decimalFormat.format(repsMaxValue) + " " + baseView.getContext().getString(R.string.reps).toLowerCase());
        else
            pieChartData.setCenterText2(decimalFormat.format(repsMaxValue) + " " + baseView.getContext().getString(R.string.rep).toLowerCase());
    }

    private void updateRepButtons() {
        if (repsMaxValue == REPS_MAX_VALUE_UPPER_LIMIT)
            increaseButton.animate().alpha(0.5f).start();
        else increaseButton.animate().alpha(1f).start();
        if (repsMaxValue == REPS_MAX_VALUE_LOWER_LIMIT)
            decreaseButton.animate().alpha(0.5f).start();
        else decreaseButton.animate().alpha(1f).start();
    }

    @OnClick(R.id.card_view)
    void onCardViewClicked() {
        strengthAdapter.showOneRepMaxDialog();
    }

    @OnClick(R.id.info_button)
    void onInfoButtonClicked() {
        strengthAdapter.showOneRepMaxInfoDialog();
    }

    @OnClick(R.id.increase_button)
    void onIncreaseButtonClicked() {
        if (repsMaxValue < REPS_MAX_VALUE_UPPER_LIMIT) {
            repsMaxValue++;
            updateRepMax();
            SharedPreferencesManager.saveRepsMaxValue(repsMaxValue);
        }
    }

    @OnClick(R.id.decrease_button)
    void onDecreaseButtonClicked() {
        if (repsMaxValue > REPS_MAX_VALUE_LOWER_LIMIT) {
            repsMaxValue--;
            updateRepMax();
            SharedPreferencesManager.saveRepsMaxValue(repsMaxValue);
        }
    }


}
