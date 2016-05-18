package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.FitnessAdapter;
import com.shalskar.fitnesscalculator.fragments.BMIDialog;
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
public class BMIViewHolder extends RecyclerView.ViewHolder {

    private FitnessAdapter fitnessAdapter;

    private View baseView;

    @BindView(R.id.bmi_card_view)
    View BMICardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    @BindView(R.id.chart)
    PieChartView chartView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.bmi_title2_textview)
    TextView BMITitle2TextView;

    @BindView(R.id.bmi_title_textview)
    TextView BMITitleTextView;

    public BMIViewHolder(@NonNull FitnessAdapter fitnessAdapter, @NonNull View baseView) {
        super(baseView);
        this.fitnessAdapter = fitnessAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);

        updateAll();
    }

    public void updateAll() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        if (height > 0 && weight > 0) {
            if(BMITitleTextView.getVisibility() == View.VISIBLE) {
                animateSideLayout();
                animateTitle();
            }
            updateBMI();
        } else {
            BMITitleTextView.setVisibility(View.VISIBLE);
            BMITitle2TextView.setVisibility(View.GONE);
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
        BMITitleTextView.animate().alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                BMITitleTextView.setVisibility(View.GONE);
                BMITitleTextView.setAlpha(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

        BMITitle2TextView.setAlpha(0);
        BMITitle2TextView.setVisibility(View.VISIBLE);
        BMITitle2TextView.animate().alpha(1).start();
    }

    private void updateBMI() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();

        double BMI = FitnessCalculator.calculateBMI(weight, height);

        updateChart(BMI);
    }

    private void updateChart(double BMI) {
        Context context = baseView.getContext();

        String BMIClassification = "";
        int BMIColor = R.color.paleGreen;
        if (BMI < 18.5) {
            BMIClassification = context.getString(R.string.bmi_underweight);
            BMIColor = R.color.yellowGreen;
        } else if (BMI < 25) {
            BMIClassification = context.getString(R.string.bmi_normal);
            BMIColor = R.color.paleGreen;
        } else if (BMI < 30) {
            BMIClassification = context.getString(R.string.bmi_overweight);
            BMIColor = R.color.mustardOrange;
        } else if (BMI < 40) {
            BMIClassification = context.getString(R.string.bmi_obese);
            BMIColor = R.color.lightRed;
        } else if (BMI >= 40) {
            BMIClassification = context.getString(R.string.bmi_morbidly_obese);
            BMIColor = R.color.deepRed;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(BMI));
        pieChartData.setCenterText2(BMIClassification);
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue((int) Math.min(BMI, 40.0), context.getResources().getColor(BMIColor)));
        if (BMI < 40) {
            sliceValues.add(new SliceValue((int)(40 - BMI), context.getResources().getColor(R.color.defaultGrey)));
        }

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size_small));

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick({R.id.bmi_card_view, R.id.edit_button})
    void showBMIDialog() {
        fitnessAdapter.showBMIDialog();
    }


}
