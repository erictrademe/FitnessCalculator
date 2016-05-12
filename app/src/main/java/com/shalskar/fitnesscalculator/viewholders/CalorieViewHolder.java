package com.shalskar.fitnesscalculator.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
            updateCalorie();
        } else {
            calorieTitleTextView.setVisibility(View.VISIBLE);
            calorieTitle2TextView.setVisibility(View.GONE);
            sideLayout.setVisibility(View.GONE);
        }
    }

    private void updateCalorie() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        int age = SharedPreferencesManager.getAge();
        int gender = SharedPreferencesManager.getGender();
        double activityLevel = SharedPreferencesManager.getActivityLevel();

        int dailyCalorieIntake = FitnessCalculator.calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);
        calorieTitleTextView.setVisibility(View.GONE);
        calorieTitle2TextView.setVisibility(View.VISIBLE);
        sideLayout.setVisibility(View.VISIBLE);

        updateChart(0, dailyCalorieIntake);
    }

    private void updateChart(int basalMetabolicRate, int dailyIntake) {
        Context context = baseView.getContext();

//        String BMIClassification = "";
//        int BMIColor = R.color.paleGreen;
//        if (BMI < 18.5) {
//            BMIClassification = context.getString(R.string.bmi_underweight);
//            BMIColor = R.color.yellowGreen;
//        } else if (BMI < 25) {
//            BMIClassification = context.getString(R.string.bmi_normal);
//            BMIColor = R.color.paleGreen;
//        } else if (BMI < 30) {
//            BMIClassification = context.getString(R.string.bmi_overweight);
//            BMIColor = R.color.mustardOrange;
//        } else if (BMI < 40) {
//            BMIClassification = context.getString(R.string.bmi_obese);
//            BMIColor = R.color.lightRed;
//        } else if (BMI >= 40) {
//            BMIClassification = context.getString(R.string.bmi_morbidly_obese);
//            BMIColor = R.color.deepRed;
//        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(dailyIntake));
        //pieChartData.setCenterText2(BMIClassification);
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.paleGreen)));
//        if (BMI < 40) {
//            sliceValues.add(new SliceValue((int)(40 - BMI), context.getResources().getColor(R.color.defaultGrey)));
//        }

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size_small));

        chartView.setPieChartData(pieChartData);
    }

    @OnClick({R.id.calorie_card_view, R.id.edit_button})
    void showCalorieDialog() {
        //fitnessAdapter.showBMIDialog();
    }


}
