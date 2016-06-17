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
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Vincent on 11/05/2016.
 */
public class WilksViewHolder extends BaseViewHolder {

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

    public WilksViewHolder(@NonNull StrengthAdapter strengthAdapter, @NonNull View baseView) {
        super(baseView);
        this.strengthAdapter = strengthAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
        titleTextView.setText(baseView.getContext().getString(R.string.wilks));
        title2TextView.setText(baseView.getContext().getString(R.string.wilks));
        loadImage(R.dimen.basic_viewholder_width, R.dimen.basic_viewholder_height, R.drawable.wilks_image);
        initialiseViews();
    }

    public void initialiseViews(){
        updateAll();
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
        float total = squatWeightLifted + benchWeightLifted +deadliftWeightLifted;
        float wilks = FitnessCalculator.calculateWilks(unit, gender, (float) weight, total);

        updateChart(wilks);
    }

    private void updateChart(float wilks) {
        Context context = baseView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(wilks));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.white)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);

        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize(getUnconvertedDimension(R.dimen.wilks_pie_chart_text_size));
        pieChartData.setCenterCircleScale(0.975f);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showCalorieDialog() {
        strengthAdapter.showWilksDialog();
    }

    @OnClick(R.id.info_button)
    void showInfoDialog() {
        strengthAdapter.showWilksInfoDialog();
    }


}
