package com.shalskar.fitnesscalculator.viewholders;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.Physique;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.BodyAdapter;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 11/05/2016.
 */
public class IdealPhysiqueViewHolder extends RecyclerView.ViewHolder {

    private BodyAdapter bodyAdapter;

    private View baseView;

    @BindView(R.id.card_view)
    View cardView;

    @BindView(R.id.side_layout)
    View sideLayout;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.chest_value_textview)
    TextView chestValueTextView;

    @BindView(R.id.shoulders_value_textview)
    TextView shouldersValueTextView;

    @BindView(R.id.neck_value_textview)
    TextView neckValueTextView;

    @BindView(R.id.waist_value_textview)
    TextView waistValueTextView;

    @BindView(R.id.arm_value_textview)
    TextView armValueTextView;

    @BindView(R.id.forearm_value_textview)
    TextView forearmValueTextView;

    @BindView(R.id.thigh_value_textview)
    TextView thighValueTextView;

    @BindView(R.id.calf_value_textview)
    TextView calfValueTextView;

    /**
     * We have 2 title text views in 2 different positions
     **/

    @BindView(R.id.title2_textview)
    TextView title2TextView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    public IdealPhysiqueViewHolder(@NonNull BodyAdapter bodyAdapter, @NonNull View baseView) {
        super(baseView);
        this.bodyAdapter = bodyAdapter;
        this.baseView = baseView;
        ButterKnife.bind(this, baseView);
    }

    public void initialiseViews() {
        titleTextView.setText(baseView.getContext().getString(R.string.ideal_physique));
        title2TextView.setText(baseView.getContext().getString(R.string.ideal_physique));
        loadImage();
        updateAll();
    }

    private void loadImage() {
        float bucketSize = baseView.getResources().getDisplayMetrics().density;
        int width = (int) (baseView.getResources().getDimension(R.dimen.ideal_physique_viewholder_width) / bucketSize);
        int height = (int) (baseView.getResources().getDimension(R.dimen.ideal_physique_viewholder_height) / bucketSize);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(baseView.getResources(), R.drawable.ideal_physique_image, width, height));
    }

    public void updateAll() {
        float wristMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_WRIST);
        float ankleMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_ANKLE);
        if (wristMeasurement > 0 && ankleMeasurement > 0) {
            updateIdealPhysique(wristMeasurement, ankleMeasurement);
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

    private void updateIdealPhysique(float wristMeasurement, float ankleMeasurement) {
        Physique physique = FitnessCalculator.calculateIdealPhysique(wristMeasurement, ankleMeasurement);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        int unit = SharedPreferencesManager.getUnit();

        updateValueTextView(chestValueTextView, decimalFormat, unit, physique.getChest());
        updateValueTextView(shouldersValueTextView, decimalFormat, unit, physique.getShoulders());
        updateValueTextView(neckValueTextView, decimalFormat, unit, physique.getNeck());
        updateValueTextView(waistValueTextView, decimalFormat, unit, physique.getWaist());
        updateValueTextView(armValueTextView, decimalFormat, unit, physique.getArm());
        updateValueTextView(forearmValueTextView, decimalFormat, unit, physique.getForearm());
        updateValueTextView(thighValueTextView, decimalFormat, unit, physique.getThigh());
        updateValueTextView(calfValueTextView, decimalFormat, unit, physique.getCalf());
    }

    private void updateValueTextView(@NonNull TextView textView, @NonNull DecimalFormat decimalFormat, int unit, float value){
        if(unit == Constants.UNIT_IMPERIAL){
            textView.setText(decimalFormat.format(ConverterUtil.cmToInches(value)) + " in");
        } else if(unit == Constants.UNIT_METRIC) {
            textView.setText(decimalFormat.format(value) + " cm");
        }
    }

    @OnClick(R.id.card_view)
    void showIdealPhysiqueDialog() {
        bodyAdapter.showIdealPhysiqueDialog();
    }

//    @OnClick(R.id.info_button)
//    void showIdealPhysiqueInfoDialog() {
//        bodyAdapter.showIdealPhysiqueInfoDialog();
//    }


}
