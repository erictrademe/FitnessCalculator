package com.shalskar.fitnesscalculator.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ImageUtil;
import com.shalskar.fitnesscalculator.utils.SnackbarUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class AboutActivity extends AppCompatActivity {

    private static final int POSITION_ONE_DOLLAR = 0;
    private static final int POSITION_TWO_DOLLAR = 1;
    private static final int POSITION_FIVE_DOLLAR = 2;
    private static final int POSITION_TEN_DOLLAR = 3;
    private static final int POSITION_TWENTY_DOLLAR = 4;

    @BindView(R.id.base_layout)
    RelativeLayout relativeLayout;

    @BindView(R.id.chart)
    PieChartView chartView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        loadImage();
        initialiseToolbar();
        updateChart();
    }

    private void initialiseToolbar() {
        toolbar.setTitle(R.string.about);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadImage() {
        int width = (getResources().getConfiguration().screenWidthDp);
        int height = (getResources().getConfiguration().screenHeightDp);
        Drawable drawable = new BitmapDrawable(ImageUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.about_background, width, height));
        relativeLayout.setBackground(drawable);
    }

    private void updateChart() {
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(getResources().getString(R.string.donation));
        pieChartData.setCenterText1Color(getResources().getColor(android.R.color.primary_text_dark));
        pieChartData.setValueLabelTypeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf"));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(15, getResources().getColor(R.color.colorAccent50)).setLabel("$1"));
        sliceValues.add(new SliceValue(20, getResources().getColor(R.color.colorAccent60)).setLabel("$2"));
        sliceValues.add(new SliceValue(25, getResources().getColor(R.color.colorAccent70)).setLabel("$5"));
        sliceValues.add(new SliceValue(30, getResources().getColor(R.color.colorAccent80)).setLabel("$10"));
        sliceValues.add(new SliceValue(35, getResources().getColor(R.color.colorAccent90)).setLabel("$20"));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        pieChartData.setCenterText1FontSize((int) getResources().getDimension(R.dimen.donation_pie_chart_text_size));
        pieChartData.setHasLabels(true);
        pieChartData.setCenterCircleScale(0.5f);
        pieChartData.setSlicesSpacing(4);

        chartView.setOnValueTouchListener(pieChartOnValueSelectListener);
        chartView.setPieChartData(pieChartData);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Chart Listener
     **/

    private PieChartOnValueSelectListener pieChartOnValueSelectListener = new PieChartOnValueSelectListener() {
        @Override
        public void onValueSelected(int position, SliceValue sliceValue) {
            View contentView = findViewById(android.R.id.content);
            switch (position) {
                case POSITION_ONE_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 1);
                    break;
                case POSITION_TWO_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 2);
                    break;
                case POSITION_FIVE_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 5);
                    break;
                case POSITION_TEN_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 10);
                    break;
                case POSITION_TWENTY_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 20);
                    break;
            }
        }

        @Override
        public void onValueDeselected() {

        }
    };

}

