package com.shalskar.fitnesscalculator.fragments;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.HeightUpdatedEvent;
import com.shalskar.fitnesscalculator.events.WeightUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Vincent on 7/05/2016.
 */
public class BodyFragment extends Fragment {

    @BindView(R.id.bmi_card_view)
    View BMICardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    /**
     * We have 2 title text views in 2 different positions
     **/
    @BindView(R.id.bmi_title2_textview)
    TextView BMITitle2TextView;

    @BindView(R.id.bmi_title_textview)
    TextView BMITitleTextView;

    @BindView(R.id.chart)
    PieChartView chartView;

//    @BindView(R.id.bmi_number_textview)
//    TextView BMINumberTextView;
//
//    @BindView(R.id.bmi_description_textview)
//    TextView BMIDescriptionTextView;

    public BodyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body, container, false);
        ButterKnife.bind(this, view);
        updateAll();
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.bmi_card_view, R.id.edit_button})
    void showBMIDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("fragment_edit_name");
        if (frag != null)
            manager.beginTransaction().remove(frag).commit();

        BMIDialog editNameDialog = new BMIDialog();
        editNameDialog.show(manager, "fragment_edit_name");
    }

    /**
     * Respond to various events.
     **/

    @Subscribe
    public void onWeightUpdatedEvent(WeightUpdatedEvent weightUpdatedEvent) {
        updateBMI();
    }

    @Subscribe
    public void onHeightUpdatedEvent(HeightUpdatedEvent heightUpdatedEvent) {
        updateBMI();
    }

    private void updateAll() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        if (height > 0 && weight > 0) {
            updateBMI();
        } else {
            BMITitleTextView.setVisibility(View.VISIBLE);
            BMITitle2TextView.setVisibility(View.GONE);
            sideLayout.setVisibility(View.GONE);
        }
    }

    private void updateBMI() {
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();

        double BMI = FitnessCalculator.calculateBMI(weight, height);
        BMITitleTextView.setVisibility(View.GONE);
        BMITitle2TextView.setVisibility(View.VISIBLE);
        sideLayout.setVisibility(View.VISIBLE);


        updateChart(BMI);
    }

    private void updateChart(double BMI) {
        String BMIClassification = "";
        int BMIColor = R.color.paleGreen;
        if (BMI < 18.5) {
            BMIClassification = getString(R.string.bmi_underweight);
            BMIColor = R.color.yellowGreen;
        } else if (BMI < 25) {
            BMIClassification = getString(R.string.bmi_normal);
            BMIColor = R.color.paleGreen;
        } else if (BMI < 30) {
            BMIClassification = getString(R.string.bmi_overweight);
            BMIColor = R.color.mustardOrange;
        } else if (BMI < 40) {
            BMIClassification = getString(R.string.bmi_obese);
            BMIColor = R.color.lightRed;
        } else if (BMI >= 40) {
            BMIClassification = getString(R.string.bmi_morbidly_obese);
            BMIColor = R.color.deepRed;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(decimalFormat.format(BMI));
        pieChartData.setCenterText2(BMIClassification);
        pieChartData.setCenterText1Color(getResources().getColor(R.color.white));
        pieChartData.setCenterText2Color(getResources().getColor(R.color.white));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue((int) Math.min(BMI, 40.0), getResources().getColor(BMIColor)));
        if (BMI < 40) {
            sliceValues.add(new SliceValue((int)(40 - BMI), getResources().getColor(R.color.defaultGrey)));
        }

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) getResources().getDimension(R.dimen.bmi_pie_chart_text_size));
        pieChartData.setCenterText2FontSize((int) getResources().getDimension(R.dimen.bmi_pie_chart_text_size_small));

        chartView.setPieChartData(pieChartData);
    }

}