package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.model.Breakdown;
import com.shalskar.fitnesscalculator.utils.AnimationUtil;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

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
public class MacroViewHolder extends BaseViewHolder {

    private BodyAdapter bodyAdapter;

    @BindView(R.id.chart)
    PieChartView chartView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    public MacroViewHolder(@NonNull BodyAdapter bodyAdapter, @NonNull View baseView) {
        super(baseView);
        this.bodyAdapter = bodyAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);

        titleTextView.setText(baseView.getContext().getString(R.string.macro));
        title2TextView.setText(baseView.getContext().getString(R.string.macro));
        loadImage(R.dimen.basic_viewholder_width, R.dimen.basic_viewholder_height, R.drawable.macro_image);
        initialiseViews();
    }

    public void initialiseViews() {
        updateAll();
    }

    @Override
    public void updateAll() {
        float calorieIntake = SharedPreferencesManager.getCalorieIntake();
        int goal = SharedPreferencesManager.getGoal();

        if (canUpdate(goal, calorieIntake)) {
            updateMacros(goal, calorieIntake);
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

    private boolean canUpdate(int goal, float calorieIntake) {
        if (calorieIntake > 0 && goal != -1) {
            if (goal == Constants.GOAL_CUSTOM) {
                float protein = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_PROTEIN);
                float carbohydrates = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES);
                float fat = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_FAT);
                return protein >= 0 && carbohydrates >= 0 && fat >= 0;
            } else
                return true;
        }
        return false;
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

    private void updateMacros(int goal, float calorieIntake) {
        double[] macros = FitnessCalculator.calculateMacros(getBreakdown(goal), calorieIntake);
        updateChart(macros);
    }

    private Breakdown getBreakdown(int goal) {
        if (goal == Constants.GOAL_GAIN_MUSCLE) return Breakdown.BREAKDOWN_GAIN_MUSCLE;
        else if (goal == Constants.GOAL_FAT_LOSS) return Breakdown.BREAKDOWN_FAT_LOSS;
        else if (goal == Constants.GOAL_MAINTAIN) return Breakdown.BREAKDOWN_MAINTAIN;
        else if (goal == Constants.GOAL_CUSTOM) {
            float protein = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_PROTEIN);
            float carbohydrates = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES);
            float fat = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_FAT);
            return new Breakdown(protein / 100, carbohydrates / 100, fat / 100);
        }
        return null;
    }

    private void updateChart(double[] macros) {
        Context context = baseView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("#");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1("Breakdown");
        pieChartData.setCenterText2("P: " + decimalFormat.format(macros[0]) + " | C: " + decimalFormat.format(macros[1]) + " | F: " + decimalFormat.format(macros[2]));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setValueLabelTypeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue((float) macros[0], context.getResources().getColor(R.color.paleGreen)));
        sliceValues.add(new SliceValue((float) macros[1], context.getResources().getColor(R.color.deepRed)));
        sliceValues.add(new SliceValue((float) macros[2], context.getResources().getColor(R.color.mustardOrange)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize(getUnconvertedDimension(R.dimen.macro_pie_chart_text_size));
        pieChartData.setCenterText2FontSize(getUnconvertedDimension(R.dimen.macro_pie_chart_text_size_small));
        pieChartData.setHasLabels(true);
        pieChartData.setCenterCircleScale(0.975f);
        pieChartData.setSlicesSpacing(4);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showMacroDialog() {
        bodyAdapter.showMacroDialog();
    }

    @OnClick(R.id.info_button)
    void showInfoDialog() {
        bodyAdapter.showMacroInfoDialog();
    }


}
