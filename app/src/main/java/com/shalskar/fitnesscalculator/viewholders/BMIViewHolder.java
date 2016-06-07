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

import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
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
public class BMIViewHolder extends RecyclerView.ViewHolder {

    private BodyAdapter bodyAdapter;

    private View baseView;

    @BindView(R.id.card_view)
    View cardView;

    @BindView(R.id.side_layout)
    View sideLayout;

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

    public BMIViewHolder(@NonNull BodyAdapter bodyAdapter, @NonNull View baseView) {
        super(baseView);
        this.bodyAdapter = bodyAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
    }

    public void initialiseViews() {
        titleTextView.setText(baseView.getContext().getString(R.string.body_mass_index));
        title2TextView.setText(baseView.getContext().getString(R.string.body_mass_index));
        loadImage();
        updateAll();
    }

    private void loadImage() {
        float bucketSize = baseView.getResources().getDisplayMetrics().density;
        int width = (int) (baseView.getResources().getDimension(R.dimen.basic_viewholder_width) / bucketSize);
        int height = (int) (baseView.getResources().getDimension(R.dimen.basic_viewholder_height) / bucketSize);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), R.drawable.bmi_image, width, height));
    }

    public void updateAll() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        if (height > 0 && weight > 0) {
            updateBMI();
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

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(BMI));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2(BMIClassification);
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue((int) Math.min(BMI, 40.0), context.getResources().getColor(BMIColor)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.bmi_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.975f);
        pieChartData.setSlicesSpacing(4);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void showBMIDialog() {
        bodyAdapter.showBMIDialog();
    }

    @OnClick(R.id.info_button)
    void showBMIInfoDialog() {
        bodyAdapter.showBMIInfoDialog();
    }


}
