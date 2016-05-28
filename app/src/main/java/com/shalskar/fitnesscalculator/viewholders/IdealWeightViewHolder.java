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
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.AnimationUtil;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
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
public class IdealWeightViewHolder extends RecyclerView.ViewHolder {

    private BodyAdapter bodyAdapter;

    private View baseView;

    @BindView(R.id.card_view)
    View cardView;

    @BindView(R.id.chart)
    PieChartView chartView;

    @BindView(R.id.image)
    ImageView imageView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    public IdealWeightViewHolder(@NonNull BodyAdapter bodyAdapter, @NonNull View baseView) {
        super(baseView);
        this.bodyAdapter = bodyAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
    }

    public void initialiseViews(){
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), R.drawable.bmi_image, 180, 180));
        titleTextView.setText(baseView.getContext().getString(R.string.ideal_weight));
        title2TextView.setText(baseView.getContext().getString(R.string.ideal_weight));
        loadImage();
        updateAll();
    }

    private void loadImage() {
        float bucketSize = baseView.getResources().getDisplayMetrics().density;
        int width = (int) (baseView.getResources().getDimension(R.dimen.small_viewholder_width) / bucketSize);
        int height = (int) (baseView.getResources().getDimension(R.dimen.small_viewholder_height) / bucketSize);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), R.drawable.ideal_weight_image, width, height));
    }

    public void updateAll() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        int age = SharedPreferencesManager.getAge();
        int gender = SharedPreferencesManager.getGender();
        double activityLevel = SharedPreferencesManager.getActivityLevel();
        if (height > 0 && weight > 0 && age > 0 && gender != -1 && activityLevel != -1) {
            updateWater();
            if (titleTextView.getVisibility() == View.VISIBLE) {
                animateChartView();
                animateTitle();
            } else {
                AnimationUtil.refreshView(chartView);
            }
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            title2TextView.setVisibility(View.GONE);
            chartView.setVisibility(View.GONE);
        }
    }

    private void animateChartView() {
        chartView.setTranslationX(chartView.getWidth());
        chartView.setAlpha(0);
        chartView.setVisibility(View.VISIBLE);
        chartView.animate().alpha(1).translationX(0).setInterpolator(new DecelerateInterpolator()).start();
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

    private void updateWater() {
        int unit = SharedPreferencesManager.getUnit();
        double height = SharedPreferencesManager.getHeight();

        double[] idealBodyWeight = FitnessCalculator.calculateIdealBodyWeight(height);
        if (unit == Constants.UNIT_IMPERIAL) {
            idealBodyWeight[0] = ConverterUtil.kgsToPounds(idealBodyWeight[0]);
            idealBodyWeight[1] = ConverterUtil.kgsToPounds(idealBodyWeight[1]);
        }

        updateChart(unit, idealBodyWeight);
    }

    private void updateChart(int unit, double[] idealBodyWeight) {
        Context context = baseView.getContext();

        DecimalFormat decimalFormat = new DecimalFormat("#");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(idealBodyWeight[0]) + " - " + decimalFormat.format(idealBodyWeight[1]));
        if (unit == Constants.UNIT_IMPERIAL)
            pieChartData.setCenterText2(context.getString(R.string.pounds));
        else
            pieChartData.setCenterText2(context.getString(R.string.kilograms));

        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.mustardOrange)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.water_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.water_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.95f);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showIdealWeightDialog() {
        bodyAdapter.showIdealWeightDialog();
    }

}
