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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rey.material.widget.Slider;
import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class CalorieDialog extends BaseDialogFragment {

    @BindView(R.id.radio_group_gender)
    RadioGroup radioGroupGender;

    @BindView(R.id.radio_button_male)
    RadioButton maleRadioButton;

    @BindView(R.id.radio_button_female)
    RadioButton femaleRadioButton;

    @BindView(R.id.edittext_layout_age)
    TextInputLayout ageLayout;

    @BindView(R.id.edittext_age)
    EditText ageEditText;

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

    @BindView(R.id.slider_activity_level)
    Slider activityLevelSlider;

    @BindView(R.id.textview_activity_level_amount)
    TextView activityLevelAmountTextView;

    @BindView(R.id.button_unit)
    Button unitButton;

    private int unit = Constants.UNIT_METRIC;
    private int gender = Constants.GENDER_FEMALE;
    private int age = 0;
    private double height = 0;
    private double weight = 0;
    private float activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;

    public CalorieDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calorie, container);

        ButterKnife.bind(this, view);
        initialiseViews();
        prepopulateFields();

        return view;
    }

    private void initialiseViews() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().setTitle(getString(R.string.calorie_intake));
        addListeners();
    }

    private void loadFields() {
        gender = SharedPreferencesManager.getGender();
        if (gender == -1) {
            gender = Constants.GENDER_FEMALE;
            SharedPreferencesManager.saveGender(gender);
        }
        age = SharedPreferencesManager.getAge();
        height = SharedPreferencesManager.getHeight();
        weight = SharedPreferencesManager.getWeight();
        unit = SharedPreferencesManager.getUnit();
        activityLevel = SharedPreferencesManager.getActivityLevel();
        if (activityLevel == -1) {
            activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;
            SharedPreferencesManager.saveActivityLevel(activityLevel);
        }
    }

    /**
     * Preopulation methods.
     */

    private void prepopulateFields() {
        loadFields();
        prepopulateGender();
        prepopulateAge();
        prepopulateHeightAndWeight();
        prepopulateActivityLevel();
    }

    private void prepopulateGender() {
        if (gender == Constants.GENDER_FEMALE) {
            femaleRadioButton.setChecked(true);
            maleRadioButton.setChecked(false);
        } else if (gender == Constants.GENDER_MALE) {
            femaleRadioButton.setChecked(false);
            maleRadioButton.setChecked(true);
        }
    }

    private void prepopulateAge() {
        weightLayout.setErrorEnabled(false);
        if (age != 0)
            ageEditText.setText(String.format("%d", age));
    }

    private void prepopulateHeightAndWeight() {
        heightLayout.setErrorEnabled(false);
        weightLayout.setErrorEnabled(false);
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            heightLayout.setHint(getString(R.string.feet));
            weightLayout.setHint(getString(R.string.pounds));
            heightInchesLayout.setVisibility(View.VISIBLE);
            if (weight > 0)
                weightEditText.setText(String.format("%.0f", ConverterUtil.kgsToPounds(weight)));
            if (height > 0) {
                double[] feetAndInches = ConverterUtil.cmToFeetAndInches(height);
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

    private void prepopulateActivityLevel() {
        if (activityLevel == Constants.ACTIVITY_LEVEL_SEDENTARY) {
            activityLevelSlider.setValue(0, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_sedentary));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_LIGHT) {
            activityLevelSlider.setValue(1, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_light));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_MODERATE) {
            activityLevelSlider.setValue(2, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_moderate));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_ACTIVE) {
            activityLevelSlider.setValue(3, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_active));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_EXTREME) {
            activityLevelSlider.setValue(4, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_extreme));
        }
    }


    @OnClick(R.id.button_unit)
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            heightLayout.setHint(getString(R.string.feet));
            weightLayout.setHint(getString(R.string.pounds));
            heightInchesLayout.setVisibility(View.VISIBLE);
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            heightLayout.setHint(getString(R.string.centimeters));
            weightLayout.setHint(getString(R.string.kilograms));
            heightInchesLayout.setVisibility(View.GONE);
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            double convertedWeight = 0;
            if (weight > 0)
                convertedWeight = ConverterUtil.kgsToPounds(weight);
            weightEditText.setText(String.format("%.0f", convertedWeight));

            double[] feetAndInches = {0, 0};
            if (heightEditText.getText().length() > 0)
                feetAndInches = ConverterUtil.cmToFeetAndInches(height);
            heightEditText.setText(String.format("%.0f", feetAndInches[0]));
            heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
        } else if (unit == Constants.UNIT_METRIC) {
            weightEditText.setText(String.format("%.0f", weight));
            heightEditText.setText(String.format("%.0f", height));
        }
    }

    @OnClick(R.id.button_ok)
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveAge(age);
            SharedPreferencesManager.saveGender(gender);
            SharedPreferencesManager.saveWeight(weight);
            SharedPreferencesManager.saveHeight(height);
            SharedPreferencesManager.saveActivityLevel(activityLevel);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_AGE, Constants.DETAIL_GENDER,
                    Constants.DETAIL_HEIGHT, Constants.DETAIL_WEIGHT, Constants.DETAIL_ACTIVITY_LEVEL));
            this.dismiss();
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        this.dismiss();
    }

    private boolean validateFields() {
        boolean validated = true;

        if(!validateWeightField(ageLayout, ageEditText, age))
            validated = false;
        if(!validateWeightField(heightLayout, heightEditText, (float) height))
            validated = false;
        if(!validateWeightField(weightLayout, weightEditText, (float) weight))
            validated = false;

        return validated;
    }

    private void removeListeners() {
        ageEditText.removeTextChangedListener(ageEditTextWatcher);
        weightEditText.removeTextChangedListener(weightEditTextWatcher);
        heightEditText.removeTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.removeTextChangedListener(heightEditTextWatcher);
    }

    private void addListeners() {
        radioGroupGender.setOnCheckedChangeListener(onGenderCheckedChangeListener);
        ageEditText.addTextChangedListener(ageEditTextWatcher);
        weightEditText.addTextChangedListener(weightEditTextWatcher);
        heightEditText.addTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.addTextChangedListener(heightEditTextWatcher);
        activityLevelSlider.setOnPositionChangeListener(activityLevelPositionChangedListener);
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
            if (weightEditText.length() == 0) {
                weightLayout.setError(" ");
                weightLayout.setErrorEnabled(true);
            } else {
                weightLayout.setErrorEnabled(false);
                weight = ParserUtil.parseDouble(getContext(), weightEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    weight = ConverterUtil.poundsToKgs(weight);
            }
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
            if (heightEditText.length() == 0) {
                heightLayout.setError(" ");
                heightLayout.setErrorEnabled(true);
            } else {
                heightLayout.setErrorEnabled(false);
                if (unit == Constants.UNIT_METRIC) {
                    height = ParserUtil.parseDouble(getContext(), heightEditText.getText().toString());
                } else if (unit == Constants.UNIT_IMPERIAL) {
                    double feet = ParserUtil.parseDouble(getContext(), heightEditText.getText().toString());
                    double inches = 0;
                    if (heightInchesEditText.length() > 0)
                        inches = ParserUtil.parseDouble(getContext(), heightInchesEditText.getText().toString());
                    height = ConverterUtil.feetAndInchesToCm(feet, inches);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher ageEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (ageEditText.length() == 0) {
                ageLayout.setError(" ");
                ageLayout.setErrorEnabled(true);
            } else {
                age = Integer.parseInt(ageEditText.getText().toString());
                ageLayout.setErrorEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private RadioGroup.OnCheckedChangeListener onGenderCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radio_button_female) gender = Constants.GENDER_FEMALE;
            else if (checkedId == R.id.radio_button_male) gender = Constants.GENDER_MALE;

        }
    };

    private Slider.OnPositionChangeListener activityLevelPositionChangedListener = new Slider.OnPositionChangeListener() {
        @Override
        public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
            switch (newValue) {
                case 0:
                    activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_sedentary));
                    break;
                case 1:
                    activityLevel = Constants.ACTIVITY_LEVEL_LIGHT;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_light));
                    break;
                case 2:
                    activityLevel = Constants.ACTIVITY_LEVEL_MODERATE;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_moderate));
                    break;
                case 3:
                    activityLevel = Constants.ACTIVITY_LEVEL_ACTIVE;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_active));
                    break;
                case 4:
                    activityLevel = Constants.ACTIVITY_LEVEL_EXTREME;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_extreme));
                    break;
            }
        }
    };
}
