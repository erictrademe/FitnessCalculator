package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class IdealPhysiqueDialog extends BaseDialogFragment {

    @BindView(R.id.edittext_layout_wrist)
    TextInputLayout wristLayout;

    @BindView(R.id.edittext_wrist)
    EditText wristEditText;

    @BindView(R.id.edittext_ankle)
    EditText ankleEditText;

    @BindView(R.id.edittext_layout_ankle)
    TextInputLayout ankleLayout;

    private int unit = Constants.UNIT_METRIC;
    private float wristMeasurement = 0;
    private float ankleMeasurement = 0;

    public IdealPhysiqueDialog() {

    }

    public static IdealPhysiqueDialog newInstance(@NonNull String title){
        IdealPhysiqueDialog idealPhysiqueDialog = new IdealPhysiqueDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_ideal_physique);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.ideal_physique_image);
        idealPhysiqueDialog.setArguments(args);
        return idealPhysiqueDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        addListeners();
        prepopulateFields();

        return view;
    }

    private void prepopulateFields() {
        wristMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_WRIST);
        ankleMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_ANKLE);
        unit = SharedPreferencesManager.getUnit();
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            ankleLayout.setHint(getString(R.string.inches));
            wristLayout.setHint(getString(R.string.inches));
            if (wristMeasurement > 0)
                wristEditText.setText(numberFormat.format(ConverterUtil.cmToInches(wristMeasurement)));
            if (ankleMeasurement > 0) {
                ankleEditText.setText(numberFormat.format(ConverterUtil.cmToInches(ankleMeasurement)));
            }
        } else if (unit == Constants.UNIT_METRIC) {
            ankleLayout.setHint(getString(R.string.centimeters));
            wristLayout.setHint(getString(R.string.centimeters));
            if (wristMeasurement > 0)
                wristEditText.setText(numberFormat.format(wristMeasurement));
            if (ankleMeasurement > 0)
                ankleEditText.setText(numberFormat.format(ankleMeasurement));
        }
    }


    @Override
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            ankleLayout.setHint(getString(R.string.inches));
            wristLayout.setHint(getString(R.string.inches));
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            ankleLayout.setHint(getString(R.string.centimeters));
            wristLayout.setHint(getString(R.string.centimeters));
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            if (wristMeasurement > 0 && wristEditText.length() > 0)
                wristEditText.setText(numberFormat.format(ConverterUtil.cmToInches(wristMeasurement)));

            if (ankleMeasurement > 0 && ankleEditText.length() > 0)
                ankleEditText.setText(numberFormat.format(ConverterUtil.cmToInches(ankleMeasurement)));
        } else if (unit == Constants.UNIT_METRIC) {
            if (wristMeasurement > 0 && wristEditText.length() > 0)
                wristEditText.setText(numberFormat.format(wristMeasurement));

            if (ankleMeasurement > 0 && ankleEditText.length() > 0)
                ankleEditText.setText(numberFormat.format(ankleMeasurement));
        }
    }

    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveMeasurement(Constants.BODY_PART_ANKLE, ankleMeasurement);
            SharedPreferencesManager.saveMeasurement(Constants.BODY_PART_WRIST, wristMeasurement);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_MEASUREMENT));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        if(!validateWeightField(ankleLayout, ankleEditText, ankleMeasurement))
            validated = false;
        if(!validateWeightField(wristLayout, wristEditText, wristMeasurement))
            validated = false;

        return validated;
    }

    private void removeListeners() {
        wristEditText.removeTextChangedListener(wristMeasurementEditTextWatcher);
        ankleEditText.removeTextChangedListener(ankleMeasurementEditTextWatcher);
    }

    private void addListeners() {
        wristEditText.addTextChangedListener(wristMeasurementEditTextWatcher);
        ankleEditText.addTextChangedListener(ankleMeasurementEditTextWatcher);
    }

    /**
     * Listeners
     */

    private TextWatcher wristMeasurementEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (wristEditText.length() == 0) {
                wristLayout.setError(" ");
                wristLayout.setErrorEnabled(true);
            } else {
                wristLayout.setErrorEnabled(false);
                wristMeasurement = ParserUtil.parseFloat(getContext(), wristEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    wristMeasurement = ConverterUtil.inchesToCm(wristMeasurement);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher ankleMeasurementEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (ankleEditText.length() == 0) {
                ankleLayout.setError(" ");
                ankleLayout.setErrorEnabled(true);
            } else {
                ankleLayout.setErrorEnabled(false);
                ankleMeasurement = ParserUtil.parseFloat(getContext(), ankleEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    ankleMeasurement = ConverterUtil.inchesToCm(ankleMeasurement);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
