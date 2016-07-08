package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.AnimationUtil;

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
public class BodyfatViewHolder extends BaseViewHolder {

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

    public BodyfatViewHolder(@NonNull BodyAdapter bodyAdapter, @NonNull View baseView) {
        super(baseView);
        this.bodyAdapter = bodyAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
        titleTextView.setText(baseView.getContext().getString(R.string.bodyfat));
        title2TextView.setText(baseView.getContext().getString(R.string.bodyfat));

        loadImage(R.dimen.basic_viewholder_width, R.dimen.basic_viewholder_height, R.drawable.bodyfat_image);
        initialiseViews();
    }

    public void initialiseViews() {
        updateAll();
    }

    @Override
    public void updateAll() {
        float bodyfat = FitnessCalculator.calculateBodyFat();
        if (bodyfat != -1) {
            updateBodyfat(bodyfat);
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

    private void updateBodyfat(float bodyfat) {
        int gender = SharedPreferencesManager.getGender();


        updateChart(gender, bodyfat);
    }

    private void updateChart(int gender, float bodyfat) {
        Context context = baseView.getContext();

        int bodyfatCategory = FitnessCalculator.getBodyfatCategory(gender, bodyfat);
        String bodyfatString = "";
        switch (bodyfatCategory) {
            case Constants.BODYFAT_CATEGORY_ESSENTIAL:
                bodyfatString = "Dangerously lean";
                break;
            case Constants.BODYFAT_CATEGORY_ATHLETES:
                bodyfatString = "Athlete";
                break;
            case Constants.BODYFAT_CATEGORY_FITNESS:
                bodyfatString = "Fit";
                break;
            case Constants.BODYFAT_CATEGORY_ACCEPTABLE:
                bodyfatString = "Average";
                break;
            case Constants.BODYFAT_CATEGORY_OBESE:
                bodyfatString = "Obese";
                break;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(bodyfat));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2(bodyfatString);
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue((int) Math.min(bodyfat, 100), context.getResources().getColor(R.color.white)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize(getUnconvertedDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize(getUnconvertedDimension(R.dimen.bmi_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.975f);
        pieChartData.setSlicesSpacing(4);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showBodyfatDialog() {
        bodyAdapter.showBodyfatDialog();
    }

    @OnClick(R.id.info_button)
    void showBodyfatInfoDialog() {
        bodyAdapter.showBodyfatInfoDialog();
    }


}
