package com.shalskar.fitnesscalculator.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.utils.ImageUtil;
import com.shalskar.fitnesscalculator.utils.SnackbarUtil;

import org.json.JSONException;
import org.json.JSONObject;

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

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.chart)
    PieChartView chartView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private IInAppBillingService service;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        loadImage();
        initialiseToolbar();
        updateChart();
        initialiseServiceConnection();
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
        PieChartData pieChartData = new PieChartData();
        pieChartData.setCenterText1(getResources().getString(R.string.donation));
        pieChartData.setCenterText1Color(ContextCompat.getColor(this, android.R.color.primary_text_dark));
        pieChartData.setValueLabelTypeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf"));
        pieChartData.setCenterText1Typeface(Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf"));
        List<SliceValue> sliceValues = new ArrayList<>();
        sliceValues.add(new SliceValue(15, ContextCompat.getColor(this, R.color.colorAccent50)).setLabel("$1"));
        sliceValues.add(new SliceValue(20, ContextCompat.getColor(this, R.color.colorAccent60)).setLabel("$2"));
        sliceValues.add(new SliceValue(25, ContextCompat.getColor(this, R.color.colorAccent70)).setLabel("$5"));
        sliceValues.add(new SliceValue(30, ContextCompat.getColor(this, R.color.colorAccent80)).setLabel("$10"));
        sliceValues.add(new SliceValue(35, ContextCompat.getColor(this, R.color.colorAccent90)).setLabel("$20"));

        pieChartData.setValues(sliceValues);
        pieChartData.setHasCenterCircle(true);
        chartView.setChartRotationEnabled(false);
        int centerTextSize = (int) (getResources().getDimension(R.dimen.donation_pie_chart_text_size) / getResources().getDisplayMetrics().density);
        pieChartData.setCenterText1FontSize(centerTextSize);
        pieChartData.setHasLabels(true);
        pieChartData.setCenterCircleScale(0.5f);
        pieChartData.setSlicesSpacing(4);

        chartView.setOnValueTouchListener(pieChartOnValueSelectListener);
        chartView.setPieChartData(pieChartData);
    }

    private void initialiseServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Toast.makeText(AboutActivity.this, "Service disconnected", Toast.LENGTH_LONG).show();
                service = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder iBinder) {
                service = IInAppBillingService.Stub.asInterface(iBinder);
            }
        };
        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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
        if (service != null)
            unbindService(serviceConnection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");

                    Toast.makeText(this, "You have bought the " + sku + ". Excellent choice, adventurer !", Toast.LENGTH_LONG);
                } catch (JSONException e) {
                    Toast.makeText(this, "Failed to parse purchase data.", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        }
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
                    SnackbarUtil.showDonationSnackBar(contentView, 1, AboutActivity.this, service);
                    break;
                case POSITION_TWO_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 2, AboutActivity.this, service);
                    break;
                case POSITION_FIVE_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 5, AboutActivity.this, service);
                    break;
                case POSITION_TEN_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 10, AboutActivity.this, service);
                    break;
                case POSITION_TWENTY_DOLLAR:
                    SnackbarUtil.showDonationSnackBar(contentView, 20, AboutActivity.this, service);
                    break;
            }
        }

        @Override
        public void onValueDeselected() {

        }
    };

}

