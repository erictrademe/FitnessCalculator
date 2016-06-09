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
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
public class WilksDialog extends BaseDialogFragment {

    @BindView(R.id.radio_group_gender)
    RadioGroup radioGroupGender;

    @BindView(R.id.radio_button_male)
    RadioButton maleRadioButton;

    @BindView(R.id.radio_button_female)
    RadioButton femaleRadioButton;

    @BindView(R.id.edittext_layout_weight)
    TextInputLayout weightLayout;

    @BindView(R.id.edittext_weight)
    EditText weightEditText;

    @BindView(R.id.edittext_layout_squat)
    TextInputLayout squatLayout;

    @BindView(R.id.edittext_squat)
    EditText squatEditText;

    @BindView(R.id.edittext_layout_bench_press)
    TextInputLayout benchPressLayout;

    @BindView(R.id.edittext_bench_press)
    EditText benchPressEditText;

    @BindView(R.id.edittext_layout_deadlift)
    TextInputLayout deadliftLayout;

    @BindView(R.id.edittext_deadlift)
    EditText deadliftEditText;

    private int unit = Constants.UNIT_METRIC;
    private int gender = Constants.GENDER_FEMALE;
    private double weight = 0;
    private float benchPressWeightLifted = 0;
    private float squatWeightLifted = 0;
    private float deadliftWeightLifted = 0;

    public WilksDialog() {

    }

    public static WilksDialog newInstance(@NonNull String title){
        WilksDialog wilksDialog = new WilksDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_wilks);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.wilks_image);
        wilksDialog.setArguments(args);
        return wilksDialog;
    }

    /**
     * Alternative builder for using alternative images (this is used for strength standards,
     * which uses the exact same parameters.)
     */
    public static WilksDialog newInstance(@NonNull String title, int imageResource){
        WilksDialog wilksDialog = new WilksDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_wilks);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, imageResource);
        wilksDialog.setArguments(args);
        return wilksDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addListeners();
        prepopulateFields();

        return view;
    }

    private void loadFields() {
        gender = SharedPreferencesManager.getGender();
        if (gender == -1) {
            gender = Constants.GENDER_FEMALE;
            SharedPreferencesManager.saveGender(gender);
        }
        weight = SharedPreferencesManager.getWeight();
        squatWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_SQUAT);
        benchPressWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_BENCH_PRESS);
        deadliftWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_DEADLIFT);
        unit = SharedPreferencesManager.getUnit();
    }

    /**
     * Preopulation methods.
     */

    private void prepopulateFields() {
        loadFields();
        prepopulateGender();
        prepopulateWeightField(weightLayout, weightEditText, (float) weight);
        prepopulateWeightField(squatLayout, squatEditText, squatWeightLifted);
        prepopulateWeightField(benchPressLayout, benchPressEditText, benchPressWeightLifted);
        prepopulateWeightField(deadliftLayout, deadliftEditText, deadliftWeightLifted);
    }


    private void prepopulateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        textInputLayout.setErrorEnabled(false);
        if (unit == Constants.UNIT_IMPERIAL) {
            textInputLayout.setHint(getString(R.string.pounds));
            if (value > 0)
                editText.setText(numberFormat.format(ConverterUtil.kgsToPounds(value)));
        } else if (unit == Constants.UNIT_METRIC) {
            textInputLayout.setHint(getString(R.string.kilograms));
            if (value > 0)
                editText.setText(numberFormat.format(value));
        }
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


    @Override
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            setHints(getString(R.string.pounds));
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            setHints(getString(R.string.kilograms));
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void setHints(@NonNull String unit){
        weightLayout.setHint(unit);
        squatLayout.setHint(unit);
        benchPressLayout.setHint(unit);
        deadliftLayout.setHint(unit);
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            if (weight > 0 && weightEditText.length() > 0)
                weightEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(weight)));
            if (squatWeightLifted > 0 && squatEditText.length() > 0)
                squatEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(squatWeightLifted)));
            if (benchPressWeightLifted > 0 && benchPressEditText.length() > 0)
                benchPressEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(benchPressWeightLifted)));
            if (deadliftWeightLifted > 0 && deadliftEditText.length() > 0)
                deadliftEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(deadliftWeightLifted)));
        } else if (unit == Constants.UNIT_METRIC) {
            if (weight > 0 && weightEditText.length() > 0)
                weightEditText.setText(numberFormat.format(weight));
            if (squatWeightLifted > 0 && squatEditText.length() > 0)
                squatEditText.setText(numberFormat.format(squatWeightLifted));
            if (benchPressWeightLifted > 0 && benchPressEditText.length() > 0)
                benchPressEditText.setText(numberFormat.format(benchPressWeightLifted));
            if (deadliftWeightLifted > 0 && deadliftEditText.length() > 0)
                deadliftEditText.setText(numberFormat.format(deadliftWeightLifted));
        }
    }

    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveGender(gender);
            SharedPreferencesManager.saveWeight(weight);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_SQUAT, squatWeightLifted);
            SharedPreferencesManager.saveRepsLifted(Constants.EXERCISE_SQUAT, 1);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_BENCH_PRESS, benchPressWeightLifted);
            SharedPreferencesManager.saveRepsLifted(Constants.EXERCISE_BENCH_PRESS, 1);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_DEADLIFT, deadliftWeightLifted);
            SharedPreferencesManager.saveRepsLifted(Constants.EXERCISE_DEADLIFT, 1);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_GENDER, Constants.DETAIL_WEIGHT, Constants.DETAIL_EXERCISE));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        if(!validateWeightField(weightLayout, weightEditText, (float) weight))
            validated = false;
        if(!validateWeightField(squatLayout, squatEditText, squatWeightLifted))
            validated = false;
        if(!validateWeightField(benchPressLayout, benchPressEditText, benchPressWeightLifted))
            validated = false;
        if(!validateWeightField(deadliftLayout, deadliftEditText, deadliftWeightLifted))
            validated = false;

        return validated;
    }

    private void removeListeners() {
        weightEditText.removeTextChangedListener(weightEditTextWatcher);
        squatEditText.removeTextChangedListener(squatEditTextWatcher);
        benchPressEditText.removeTextChangedListener(benchPressEditTextWatcher);
        deadliftEditText.removeTextChangedListener(deadliftEditTextWatcher);
    }

    private void addListeners() {
        radioGroupGender.setOnCheckedChangeListener(onGenderCheckedChangeListener);
        weightEditText.addTextChangedListener(weightEditTextWatcher);
        squatEditText.addTextChangedListener(squatEditTextWatcher);
        benchPressEditText.addTextChangedListener(benchPressEditTextWatcher);
        deadliftEditText.addTextChangedListener(deadliftEditTextWatcher);
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

    private TextWatcher squatEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (squatEditText.length() == 0) {
                squatLayout.setError(" ");
                squatLayout.setErrorEnabled(true);
            } else {
                squatLayout.setErrorEnabled(false);
                squatWeightLifted = ParserUtil.parseFloat(getContext(), squatEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    squatWeightLifted = ConverterUtil.poundsToKgs(squatWeightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher benchPressEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (benchPressEditText.length() == 0) {
                benchPressLayout.setError(" ");
                benchPressLayout.setErrorEnabled(true);
            } else {
                benchPressLayout.setErrorEnabled(false);
                benchPressWeightLifted = ParserUtil.parseFloat(getContext(), benchPressEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    benchPressWeightLifted = ConverterUtil.poundsToKgs(benchPressWeightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher deadliftEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (deadliftEditText.length() == 0) {
                deadliftLayout.setError(" ");
                deadliftLayout.setErrorEnabled(true);
            } else {
                deadliftLayout.setErrorEnabled(false);
                deadliftWeightLifted = ParserUtil.parseFloat(getContext(), deadliftEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    deadliftWeightLifted = ConverterUtil.poundsToKgs(deadliftWeightLifted);
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

}
