package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
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
public class IdealWeightDialog extends DialogFragment {

    @BindView(R.id.edittext_height)
    EditText heightEditText;

    @BindView(R.id.edittext_layout_height)
    TextInputLayout heightLayout;

    @BindView(R.id.edittext_height_inches)
    EditText heightInchesEditText;

    @BindView(R.id.height_inches_layout)
    ViewGroup heightInchesLayout;

    @BindView(R.id.button_unit)
    Button unitButton;

    private int unit = Constants.UNIT_METRIC;
    private double height = 0;

    public IdealWeightDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ideal_weight, container);

        ButterKnife.bind(this, view);
        initialiseViews();
        prepopulateFields();

        return view;
    }

    private void initialiseViews() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().setTitle(getString(R.string.ideal_weight));
        addListeners();
    }

    private void prepopulateFields() {
        height = SharedPreferencesManager.getHeight();
        unit = SharedPreferencesManager.getUnit();
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            heightLayout.setHint(getString(R.string.feet));
            heightInchesLayout.setVisibility(View.VISIBLE);
            if (height > 0) {
                double[] feetAndInches = ConverterUtil.cmToFeetAndInches(height);
                heightEditText.setText(String.format("%.0f", feetAndInches[0]));
                heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
            }
        } else if (unit == Constants.UNIT_METRIC) {
            heightLayout.setHint(getString(R.string.centimeters));
            if (height > 0)
                heightEditText.setText(String.format("%.0f", height));
        }
    }


    @OnClick(R.id.button_unit)
    void onClickBmiUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            heightLayout.setHint(getString(R.string.feet));
            heightInchesLayout.setVisibility(View.VISIBLE);
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            heightLayout.setHint(getString(R.string.centimeters));
            heightInchesLayout.setVisibility(View.GONE);
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            double[] feetAndInches = {0, 0};
            if (heightEditText.getText().length() > 0)
                feetAndInches = ConverterUtil.cmToFeetAndInches(height);
            heightEditText.setText(String.format("%.0f", feetAndInches[0]));
            heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
        } else if (unit == Constants.UNIT_METRIC) {
            heightEditText.setText(String.format("%.0f", height));
        }
    }

    @OnClick(R.id.button_ok)
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveHeight(height);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_HEIGHT));
            this.dismiss();
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        this.dismiss();
    }

    private boolean validateFields() {
        if (heightEditText.length() > 0) {
            if (height > 0) return true;
        }
        return false;
    }

    private void removeListeners(){
        heightEditText.removeTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.removeTextChangedListener(heightEditTextWatcher);
    }

    private void addListeners(){
        heightEditText.addTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.addTextChangedListener(heightEditTextWatcher);
    }

    /**
     * Listeners
     */

    private TextWatcher heightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (heightEditText.length() == 0) {
                heightLayout.setError(" ");
                heightLayout.setErrorEnabled(true);
            } else {
                heightLayout.setErrorEnabled(false);
                if (unit == Constants.UNIT_METRIC) {
                    height = ParserUtil.parseDouble(getContext(), heightEditText.getText().toString());
                } else if (unit == Constants.UNIT_IMPERIAL) {
                    double feet = ParserUtil.parseDouble(getContext(), heightEditText.getText().toString());
                    double inches = ParserUtil.parseDouble(getContext(), heightInchesEditText.getText().toString());
                    height = ConverterUtil.feetAndInchesToCm(feet, inches);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
