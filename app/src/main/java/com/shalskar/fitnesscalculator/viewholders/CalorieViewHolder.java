package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.FitnessAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

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
public class CalorieViewHolder extends RecyclerView.ViewHolder {

    private FitnessAdapter fitnessAdapter;

    private View baseView;

    @BindView(R.id.calorie_card_view)
    View calorieCardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    @BindView(R.id.chart)
    PieChartView chartView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.calorie_title2_textview)
    TextView calorieTitle2TextView;

    @BindView(R.id.calorie_title_textview)
    TextView calorieTitleTextView;

    public CalorieViewHolder(@NonNull FitnessAdapter fitnessAdapter, @NonNull View baseView) {
        super(baseView);
        this.fitnessAdapter = fitnessAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);

        updateAll();
    }

    public void updateAll() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        int age = SharedPreferencesManager.getAge();
        int gender = SharedPreferencesManager.getGender();
        double activityLevel = SharedPreferencesManager.getActivityLevel();
        if (height > 0 && weight > 0 && age > 0 && gender != -1 && activityLevel != -1) {
            if(calorieTitleTextView.getVisibility() == View.VISIBLE){
                animateSideLayout();
                animateTitle();
            }
            updateCalorie();
        } else {
            calorieTitleTextView.setVisibility(View.VISIBLE);
            calorieTitle2TextView.setVisibility(View.GONE);
            sideLayout.setVisibility(View.GONE);
        }
    }

    private void animateSideLayout(){
        sideLayout.setTranslationX(sideLayout.getWidth());
        sideLayout.setAlpha(0);
        sideLayout.setVisibility(View.VISIBLE);
        sideLayout.animate().alpha(1).translationX(0).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void animateTitle(){
        calorieTitleTextView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                calorieTitleTextView.setVisibility(View.GONE);
                calorieTitleTextView.setAlpha(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        calorieTitle2TextView.setAlpha(0);
        calorieTitle2TextView.setVisibility(View.VISIBLE);
        calorieTitle2TextView.animate().alpha(1).start();
    }

    private void updateCalorie() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        int age = SharedPreferencesManager.getAge();
        int gender = SharedPreferencesManager.getGender();
        double activityLevel = SharedPreferencesManager.getActivityLevel();

        int dailyCalorieIntake = FitnessCalculator.calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);

        updateChart(0, dailyCalorieIntake);
    }

    private void updateChart(int basalMetabolicRate, int dailyIntake) {
        Context context = baseView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(dailyIntake));
        pieChartData.setCenterText2(context.getString(R.string.calories_daily));
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.paleGreen)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size_small));

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick({R.id.calorie_card_view, R.id.edit_button})
    void showCalorieDialog() {
        fitnessAdapter.showCalorieDialog();
    }


}
