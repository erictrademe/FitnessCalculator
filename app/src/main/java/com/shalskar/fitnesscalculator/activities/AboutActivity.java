package com.shalskar.fitnesscalculator.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.iap.IabHelper;
import com.shalskar.fitnesscalculator.iap.IabResult;
import com.shalskar.fitnesscalculator.iap.Purchase;
import com.shalskar.fitnesscalculator.utils.ImageUtil;
import com.shalskar.fitnesscalculator.utils.SnackbarUtil;
import com.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.button_upgrade)
    Button upgradeButton;

    @BindView(R.id.logo)
    SparkButton sparkButton;

    private IabHelper iabHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        loadImage();
        initialiseToolbar();
        updateChart();
        initialiseServiceConnection();
        sparkButton.setChecked(false);
        sparkButton.setEnabled(false);
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
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.about_background, width, height));
    }

    private void updateChart() {
//        PieChartData pieChartData = new PieChartData();
//        pieChartData.setCenterText1(getResources().getString(R.string.donation));
//        pieChartData.setCenterText1Color(ContextCompat.getColor(this, android.R.color.primary_text_dark));
//        pieChartData.setValueLabelTypeface(Typeface.createFromAsset(getAssets(), Constants.DEFAULT_FONT_PATH));
//        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(getAssets(), Constants.DEFAULT_FONT_PATH));
//        List<SliceValue> sliceValues = new ArrayList<>();
//        sliceValues.add(new SliceValue(15, ContextCompat.getColor(this, R.color.colorAccent50)).setLabel("$1"));
//        sliceValues.add(new SliceValue(20, ContextCompat.getColor(this, R.color.colorAccent60)).setLabel("$2"));
//        sliceValues.add(new SliceValue(25, ContextCompat.getColor(this, R.color.colorAccent70)).setLabel("$5"));
//        sliceValues.add(new SliceValue(30, ContextCompat.getColor(this, R.color.colorAccent80)).setLabel("$10"));
//        sliceValues.add(new SliceValue(35, ContextCompat.getColor(this, R.color.colorAccent90)).setLabel("$20"));
//
//        pieChartData.setValues(sliceValues);
//        pieChartData.setHasCenterCircle(true);
//        chartView.setChartRotationEnabled(false);
//        int centerTextSize = (int) (getResources().getDimension(R.dimen.donation_pie_chart_text_size) / getResources().getDisplayMetrics().density);
//        pieChartData.setCenterText1FontSize(centerTextSize);
//        pieChartData.setHasLabels(true);
//        pieChartData.setCenterCircleScale(0.5f);
//        pieChartData.setSlicesSpacing(4);
//
//        chartView.setOnValueTouchListener(pieChartOnValueSelectListener);
//        chartView.setPieChartData(pieChartData);
    }

    private void initialiseServiceConnection() {
        iabHelper = new IabHelper(this, Constants.IAP_KEY);

        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d("About activity", "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d("About activity", "In-app Billing is set up OK");
                }
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        iabHelper = null;
    }

    /**
     * Chart Listener
     **/

    private PieChartOnValueSelectListener pieChartOnValueSelectListener = new PieChartOnValueSelectListener() {
        @Override
        public void onValueSelected(int position, SliceValue sliceValue) {
            donate(getSku(position));
        }


        @Override
        public void onValueDeselected() {

        }
    };

    private String getSku(int position) {
        switch (position) {
            case POSITION_ONE_DOLLAR:
                return Constants.DONATION_ONE_DOLLAR;
            case POSITION_TWO_DOLLAR:
                return Constants.DONATION_TWO_DOLLARS;
            case POSITION_FIVE_DOLLAR:
                return Constants.DONATION_FIVE_DOLLARS;
            case POSITION_TEN_DOLLAR:
                return Constants.DONATION_TEN_DOLLARS;
            case POSITION_TWENTY_DOLLAR:
                return Constants.DONATION_TWENTY_DOLLARS;
        }
        return Constants.DONATION_ONE_DOLLAR;
    }

    private void donate(@NonNull String sku) {
        try {
            iabHelper.launchPurchaseFlow(this, sku, 10001,
                    new IabHelper.OnIabPurchaseFinishedListener() {
                        @Override
                        public void onIabPurchaseFinished(IabResult result, Purchase info) {
                            SnackbarUtil.showMessageSnackbar(AboutActivity.this, getString(R.string.donation_thanks));
                        }
                    });
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
            SnackbarUtil.showMessageSnackbar(AboutActivity.this, getString(R.string.donations_unavailable));
        }
    }

    @OnClick({R.id.button_upgrade, R.id.button_upgrade2})
    void onClickUpgradeButton(){
        sparkButton.setEnabled(true);
        sparkButton.performClick();
        sparkButton.setEnabled(false);
    }


}

