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
import com.shalskar.fitnesscalculator.Converter;
import com.shalskar.fitnesscalculator.Parser;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.HeightUpdatedEvent;
import com.shalskar.fitnesscalculator.events.WeightUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class BMIDialog extends DialogFragment {

    @BindView(R.id.edittext_layout_weight)
    TextInputLayout weightLayout;

    @BindView(R.id.edittext_weight)
    EditText weightEditText;

    @BindView(R.id.edittext_height)
    EditText heightEditText;

    @BindView(R.id.edittext_layout_height)
    TextInputLayout heightLayout;

    @BindView(R.id.edittext_height_inches)
    EditText heightInchesEditText;

    @BindView(R.id.height_inches_layout)
    ViewGroup heightInchesLayout;

    @BindView(R.id.bmi_button_unit)
    Button BMIUnitButton;

    private int unit = Constants.UNIT_METRIC;
    private double height = 0;
    private double weight = 0;

    public BMIDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bmi, container);

        ButterKnife.bind(this, view);
        initialiseViews();
        prepopulateFields();

        return view;
    }

    private void initialiseViews() {
        weightEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("BMI");
        addListeners();
    }

    private void prepopulateFields() {
        height = SharedPreferencesManager.getHeight();
        weight = SharedPreferencesManager.getWeight();
        unit = SharedPreferencesManager.getUnit();
        if (unit == Constants.UNIT_IMPERIAL) {
            BMIUnitButton.setText(getString(R.string.imperial));
            heightLayout.setHint(getString(R.string.feet));
            weightLayout.setHint(getString(R.string.pounds));
            heightInchesLayout.setVisibility(View.VISIBLE);
            if (weight > 0)
                weightEditText.setText(String.format("%.0f", Converter.kgsToPounds(weight)));
            if (height > 0) {
                double[] feetAndInches = Converter.cmToFeetAndInches(height);
                heightEditText.setText(String.format("%.0f", feetAndInches[0]));
                heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
            }
        } else if (unit == Constants.UNIT_METRIC) {
            heightLayout.setHint(getString(R.string.centimeters));
            weightLayout.setHint(getString(R.string.kilograms));
            if (weight > 0)
                weightEditText.setText(String.format("%.0f", weight));
            if (height > 0)
                heightEditText.setText(String.format("%.0f", height));
        }
    }


    @OnClick(R.id.bmi_button_unit)
    void onClickBmiUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            BMIUnitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            heightLayout.setHint(getString(R.string.feet));
            weightLayout.setHint(getString(R.string.pounds));
            heightInchesLayout.setVisibility(View.VISIBLE);
        } else if (unit == Constants.UNIT_IMPERIAL) {
            BMIUnitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            heightLayout.setHint(getString(R.string.centimeters));
            weightLayout.setHint(getString(R.string.kilograms));
            heightInchesLayout.setVisibility(View.GONE);
        }
        convertFields();
        addListeners();
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            double convertedWeight = 0;
            if (weight > 0)
                convertedWeight = Converter.kgsToPounds(weight);
            weightEditText.setText(String.format("%.0f", convertedWeight));

            double[] feetAndInches = {0, 0};
            if (heightEditText.getText().length() > 0)
                feetAndInches = Converter.cmToFeetAndInches(height);
            heightEditText.setText(String.format("%.0f", feetAndInches[0]));
            heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
        } else if (unit == Constants.UNIT_METRIC) {
            weightEditText.setText(String.format("%.0f", weight));
            heightEditText.setText(String.format("%.0f", height));
        }
    }

    @OnClick(R.id.bmi_button_ok)
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveWeight(weight);
            EventBus.getDefault().post(new WeightUpdatedEvent(weight));
            SharedPreferencesManager.saveHeight(height);
            EventBus.getDefault().post(new HeightUpdatedEvent(height));
            this.dismiss();
        }
    }

    @OnClick(R.id.bmi_button_cancel)
    void onCancelClick() {
        this.dismiss();
    }

    private boolean validateFields() {
        if (heightEditText.length() > 0 && weightEditText.length() > 0) {
            if (height > 0 && weight > 0) return true;
        }
        return false;
    }

    private void removeListeners(){
        weightEditText.removeTextChangedListener(weightEditTextWatcher);
        heightEditText.removeTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.removeTextChangedListener(heightEditTextWatcher);
    }

    private void addListeners(){
        weightEditText.addTextChangedListener(weightEditTextWatcher);
        heightEditText.addTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.addTextChangedListener(heightEditTextWatcher);
    }

    /**
     * Listeners
     */

    private TextWatcher weightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            weight = Parser.parseDouble(getContext(), weightEditText.getText().toString());
            if (unit == Constants.UNIT_IMPERIAL)
                weight = Converter.poundsToKgs(weight);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher heightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (unit == Constants.UNIT_METRIC) {
                height = Parser.parseDouble(getContext(), heightEditText.getText().toString());
            } else if (unit == Constants.UNIT_IMPERIAL) {
                double feet = 0;
                if (heightEditText.getText().length() > 0)
                    feet = Parser.parseDouble(getContext(), heightEditText.getText().toString());
                double inches = 0;
                if (heightInchesEditText.getText().length() > 0)
                    inches = Parser.parseDouble(getContext(), heightInchesEditText.getText().toString());
                height = Converter.feetAndInchesToCm(feet, inches);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
