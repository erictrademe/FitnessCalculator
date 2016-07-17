package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Slider;
import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.StrengthAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.model.MetabolicEquivalent;

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
public class CaloriesBurnedViewHolder extends BaseViewHolder {

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

    PieChartData pieChartData;

    public CaloriesBurnedViewHolder(@NonNull StrengthAdapter strengthAdapter, @NonNull View baseView) {
        super(baseView);
        this.strengthAdapter = strengthAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
        titleTextView.setText(baseView.getContext().getString(R.string.calories_burned));
        title2TextView.setText(baseView.getContext().getString(R.string.calories_burned));
        loadImage(R.dimen.basic_viewholder_width, R.dimen.basic_viewholder_height, R.drawable.calories_burned_image);
        initialiseViews();
    }

    public void initialiseViews(){
        initialiseChart();
        updateAll();
    }

    private void initialiseChart(){
        Context context = baseView.getContext();

        pieChartData = new PieChartData();
        Typeface typeface = Typeface.createFromAsset(baseView.getContext().getAssets(), Constants.DEFAULT_FONT_PATH);
        pieChartData.setCenterText2Typeface(typeface);
        pieChartData.setCenterText2(baseView.getContext().getString(R.string.calories_burned));
        int white = ContextCompat.getColor(context, R.color.white);
        pieChartData.setCenterText1Color(white);
        pieChartData.setCenterText2Color(white);
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.white)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setCenterText1FontSize(getUnconvertedDimension(R.dimen.calories_burned_pie_chart_text_size));
        pieChartData.setCenterText2FontSize(getUnconvertedDimension(R.dimen.calories_burned_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.975f);

        chartView.setPieChartData(pieChartData);
        chartView.setInteractive(false);
        chartView.setChartRotationEnabled(false);
    }

    @Override
    public void updateAll() {
        float weight = SharedPreferencesManager.getWeight();
        MetabolicEquivalent MET = SharedPreferencesManager.getMET();
        int activityDuration = SharedPreferencesManager.getActivityDuration();

        if (weight > 0 && activityDuration > 0 && MET != null) {
            updateCaloriesBurned();
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

    private void updateCaloriesBurned() {
        float weight = SharedPreferencesManager.getWeight();
        MetabolicEquivalent MET = SharedPreferencesManager.getMET();
        int activityDuration = SharedPreferencesManager.getActivityDuration();

        float caloriesBurned = FitnessCalculator.calculateCaloriesBurned(MET, weight, activityDuration);
        DecimalFormat decimalFormat = new DecimalFormat("#");

        pieChartData.setCenterText1(String.format("%s%s", decimalFormat.format(caloriesBurned), baseView.getContext().getString(R.string.kcal)));

        String minuteString = activityDuration == 1 ? baseView.getContext().getString(R.string.min) : baseView.getContext().getString(R.string.mins);
        String descriptionString = MET.description.length() > 12 ? MET.description.substring(0, 12).trim() + ".." : MET.description;
        pieChartData.setCenterText2(String.format("%d %s %s %s", activityDuration, minuteString, "-", descriptionString));
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showCalorieDialog() {
        strengthAdapter.showCaloriesBurnedDialog();
    }

    @OnClick(R.id.info_button)
    void showInfoDialog() {
        strengthAdapter.showCaloriesBurnedInfoDialog();
    }


}
