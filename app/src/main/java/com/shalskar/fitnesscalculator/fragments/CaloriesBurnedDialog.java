package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.model.MetabolicEquivalent;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Vincent on 7/05/2016.
 */
public class CaloriesBurnedDialog extends BaseDialogFragment {

    @BindView(R.id.spinner_activity)
    Spinner exerciseSpinner;

    @BindView(R.id.edittext_layout_bodyweight)
    TextInputLayout bodyweightLayout;

    @BindView(R.id.edittext_bodyweight)
    EditText bodyweightEditText;

    @BindView(R.id.edittext_layout_duration)
    TextInputLayout durationLayout;

    @BindView(R.id.edittext_duration)
    EditText durationEditText;


    private int unit = Constants.UNIT_METRIC;
    private int duration = 0;
    private float bodyweight = 0;
    private MetabolicEquivalent MET = MetabolicEquivalent.MET_ACTIVITIES[0];

    public CaloriesBurnedDialog() {
    }

    public static CaloriesBurnedDialog newInstance(@NonNull String title) {
        CaloriesBurnedDialog oneRepMaxDialog = new CaloriesBurnedDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_calories_burned);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.calories_burned_image);
        oneRepMaxDialog.setArguments(args);
        return oneRepMaxDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addListeners();
        prepopulateFields();

        return view;
    }

    private void prepopulateFields() {
        loadFields();
        prepopulateUnit(unit);
        if (unit == Constants.UNIT_IMPERIAL) {
            bodyweightLayout.setHint(getString(R.string.pounds));
            if (bodyweight > 0)
                bodyweightEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(bodyweight)));
        } else if (unit == Constants.UNIT_METRIC) {
            bodyweightLayout.setHint(getString(R.string.kilograms));
            if (bodyweight > 0)
                bodyweightEditText.setText(String.format("%.0f", bodyweight));
        }
        if (duration > 0)
            durationEditText.setText(String.format("%d", duration));

        prepopulateSpinner();
    }

    private void loadFields() {
        duration = SharedPreferencesManager.getActivityDuration();
        bodyweight = SharedPreferencesManager.getWeight();
        unit = SharedPreferencesManager.getUnit();
        MET = SharedPreferencesManager.getMET();
        if (MET == null) {
            MET = MetabolicEquivalent.MET_ACTIVITIES[0];
            SharedPreferencesManager.saveMET(MET);
        }

    }

    private void prepopulateSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, MetabolicEquivalent.getMETSasStringArray());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);
        exerciseSpinner.setOnItemSelectedListener(formulaSpinnerOnItemSelectedListener);
        exerciseSpinner.setSelection(MetabolicEquivalent.getPositionForMET(MET));
    }

    @Override
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            bodyweightLayout.setHint(getString(R.string.pounds));
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            bodyweightLayout.setHint(getString(R.string.kilograms));
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (bodyweight > 0 && bodyweightEditText.length() > 0) {
            if (unit == Constants.UNIT_IMPERIAL)
                bodyweightEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(bodyweight)));
            else if (unit == Constants.UNIT_METRIC)
                bodyweightEditText.setText(numberFormat.format(bodyweight));
        }
    }


    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveWeight(bodyweight);
            SharedPreferencesManager.saveActivityDuration(duration);
            SharedPreferencesManager.saveMET(MET);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_WEIGHT, Constants.DETAIL_MET));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;
        if (!validateWeightField(bodyweightLayout, bodyweightEditText, bodyweight))
            validated = false;
        if (!validateWeightField(durationLayout, durationEditText, duration))
            validated = false;
        return validated;
    }

    private void removeListeners() {
        bodyweightEditText.removeTextChangedListener(bodyweightEditTextWatcher);
        durationEditText.removeTextChangedListener(durationEditTextWatcher);
    }

    private void addListeners() {
        bodyweightEditText.addTextChangedListener(bodyweightEditTextWatcher);
        durationEditText.addTextChangedListener(durationEditTextWatcher);
    }

    /**
     * Listeners
     */

    private TextWatcher bodyweightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (bodyweightEditText.length() == 0) {
                bodyweightLayout.setError(" ");
                bodyweightLayout.setErrorEnabled(true);
            } else {
                bodyweightLayout.setErrorEnabled(false);
                bodyweight = (float) ParserUtil.parseDouble(getContext(), bodyweightEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    bodyweight = ConverterUtil.poundsToKgs(bodyweight);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher durationEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (durationEditText.length() == 0) {
                durationLayout.setError(" ");
                durationLayout.setErrorEnabled(true);
            } else {
                durationLayout.setErrorEnabled(false);
                duration = Integer.parseInt(durationEditText.getText().toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private AdapterView.OnItemSelectedListener formulaSpinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            MET = MetabolicEquivalent.MET_ACTIVITIES[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

//    private Slider.OnPositionChangeListener durationOnPositionChangedListener = new Slider.OnPositionChangeListener() {
//        @Override
//        public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
//            duration = (newValue + 1) * 15;
//            setDurationAmountTextView();
//        }
//    };

//    private void setDurationAmountTextView(){
//        String hoursString = getContext().getString(R.string.hours);
//        String hourString = getContext().getString(R.string.hour);
//        String minutesString = getContext().getString(R.string.minutes);
//
//        int hours = duration / 60;
//        int minutes = duration % 60;
//        if(hours == 1 && minutes > 0)
//            durationAmountTextView.setText(String.format("%d %s %d %s", hours, hourString, minutes, minutesString));
//        else if(hours > 1 && minutes > 0)
//            durationAmountTextView.setText(String.format("%d %s %d %s", hours, hoursString, minutes, minutesString));
//        else if(hours == 1 && minutes == 0)
//            durationAmountTextView.setText(String.format("%d %s", hours, hourString));
//        else if(hours > 1 && minutes == 0)
//            durationAmountTextView.setText(String.format("%d %s", hours, hoursString));
//        else if(hours == 0 && minutes > 0)
//            durationAmountTextView.setText(String.format("%d %s", minutes, minutesString));
//    }
}
