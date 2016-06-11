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
public class OneRepMaxViewHolder extends BaseViewHolder {

    private StrengthAdapter strengthAdapter;

    @BindView(R.id.card_view)
    View cardView;

    @BindView(R.id.chart)
    PieChartView chartView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    public OneRepMaxViewHolder(@NonNull StrengthAdapter strengthAdapter, @NonNull View baseView) {
        super(baseView);
        this.strengthAdapter = strengthAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
    }

    public void initialiseViews() {
        initialiseTitle();
        loadImage(R.dimen.thin_viewholder_width, R.dimen.thin_viewholder_height, R.drawable.one_rep_max_image);
        updateAll();
    }

    private void initialiseTitle() {
        titleTextView.setText(baseView.getContext().getString(R.string.one_rep_max));
        title2TextView.setText(baseView.getContext().getString(R.string.one_rep_max));
    }

    public void updateAll() {
        double weightLifted = SharedPreferencesManager.getWeightLifted();
        int repsLifted = SharedPreferencesManager.getRepsLifted();
        if (weightLifted > 0 && repsLifted > 0) {
            updateOneRepMax();
            if (titleTextView.getVisibility() == View.VISIBLE) {animateSideLayout();
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

    private void animateSideLayout() {
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

    private void updateOneRepMax() {
        float weightLifted = SharedPreferencesManager.getWeightLifted();
        int repsLifted = SharedPreferencesManager.getRepsLifted();

        double oneRepMax = FitnessCalculator.calculateOneRepMax(repsLifted, weightLifted);

        updateChart(oneRepMax);
    }

    private void updateChart(double oneRepMax) {
        Context context = baseView.getContext();
        int unit = SharedPreferencesManager.getUnit();

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        PieChartData pieChartData = new PieChartData();
        if (unit == Constants.UNIT_IMPERIAL) {
            oneRepMax = ConverterUtil.kgsToPounds(oneRepMax);
            pieChartData.setCenterText2(baseView.getContext().getString(R.string.pounds));
        } else if (unit == Constants.UNIT_METRIC) {
            pieChartData.setCenterText2(baseView.getContext().getString(R.string.kilograms));
        }
        pieChartData.setCenterText1(decimalFormat.format(oneRepMax));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText2Typeface(Typeface.createFromAsset(baseView.getContext().getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText1Color(context.getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(context.getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(100, context.getResources().getColor(R.color.white)));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) context.getResources().getDimension(R.dimen.one_rep_max_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) context.getResources().getDimension(R.dimen.one_rep_max_pie_chart_text_size_small));
        pieChartData.setCenterCircleScale(0.975f);
        pieChartData.setSlicesSpacing(4);

        chartView.setInteractive(false);
        chartView.setPieChartData(pieChartData);
    }

    @OnClick(R.id.card_view)
    void onCardViewClicked() {
        strengthAdapter.showOneRepMaxDialog();
    }


}
