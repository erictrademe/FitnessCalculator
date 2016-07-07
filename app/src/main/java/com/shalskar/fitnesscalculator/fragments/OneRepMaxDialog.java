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
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by Vincent on 7/05/2016.
 */
public class OneRepMaxDialog extends BaseDialogFragment {

    @BindView(R.id.spinner_formula)
    Spinner formulaSpinner;

    @BindView(R.id.edittext_layout_weight_lifted)
    TextInputLayout weightLiftedLayout;

    @BindView(R.id.edittext_weight_lifted)
    EditText weightLiftedEditText;

    @BindView(R.id.edittext_layout_reps_lifted)
    TextInputLayout repsLiftedLayout;

    @BindView(R.id.edittext_reps_lifted)
    EditText repsLiftedEditText;

    private int unit = Constants.UNIT_METRIC;
    private int repsLifted = 0;
    private float weightLifted = 0;
    private int oneRepMaxFormula = Constants.ONE_REP_MAX_FORMULA_EPLEY;

    public OneRepMaxDialog() {
    }

    public static OneRepMaxDialog newInstance(@NonNull String title) {
        OneRepMaxDialog oneRepMaxDialog = new OneRepMaxDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_one_rep_max);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.one_rep_max_image);
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
            weightLiftedLayout.setHint(getString(R.string.pounds));
            if (weightLifted > 0)
                weightLiftedEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(weightLifted)));
        } else if (unit == Constants.UNIT_METRIC) {
            weightLiftedLayout.setHint(getString(R.string.kilograms));
            if (weightLifted > 0)
                weightLiftedEditText.setText(String.format("%.0f", weightLifted));
        }
        if (repsLifted > 0)
            repsLiftedEditText.setText(numberFormat.format(repsLifted));
        prepopulateSpinner();
    }

    private void loadFields() {
        repsLifted = SharedPreferencesManager.getRepsLifted();
        weightLifted = SharedPreferencesManager.getWeightLifted();
        unit = SharedPreferencesManager.getUnit();
        oneRepMaxFormula = SharedPreferencesManager.getOneRepMaxFormula();
        if (oneRepMaxFormula == Constants.UNDEFINED) {
            oneRepMaxFormula = Constants.ONE_REP_MAX_FORMULA_EPLEY;
            SharedPreferencesManager.saveOneRepMaxFormula(oneRepMaxFormula);
        }

    }

    private void prepopulateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.one_rep_max_formulas, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        formulaSpinner.setAdapter(adapter);
        formulaSpinner.setOnItemSelectedListener(formulaSpinnerOnItemSelectedListener);
        formulaSpinner.setSelection(getPositionForFormula(oneRepMaxFormula));
    }

    @Override
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            weightLiftedLayout.setHint(getString(R.string.pounds));
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            weightLiftedLayout.setHint(getString(R.string.kilograms));
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (weightLifted > 0 && weightLiftedEditText.length() > 0) {
            if (unit == Constants.UNIT_IMPERIAL)
                weightLiftedEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(weightLifted)));
            else if (unit == Constants.UNIT_METRIC)
                weightLiftedEditText.setText(numberFormat.format(weightLifted));
        }
    }


    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveWeightLifted(weightLifted);
            SharedPreferencesManager.saveRepsLifted(repsLifted);
            SharedPreferencesManager.saveOneRepMaxFormula(oneRepMaxFormula);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_ONE_REP_MAX));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        if (!validateWeightField(repsLiftedLayout, repsLiftedEditText, repsLifted))
            validated = false;
        if (!validateWeightField(weightLiftedLayout, weightLiftedEditText, weightLifted))
            validated = false;

        return validated;
    }

    private void removeListeners() {
        weightLiftedEditText.removeTextChangedListener(weightLiftedEditTextWatcher);
        repsLiftedEditText.removeTextChangedListener(repsLiftedEditTextWatcher);
    }

    private void addListeners() {
        weightLiftedEditText.addTextChangedListener(weightLiftedEditTextWatcher);
        repsLiftedEditText.addTextChangedListener(repsLiftedEditTextWatcher);
    }

    /**
     * Listeners
     */

    private TextWatcher weightLiftedEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (weightLiftedEditText.length() == 0) {
                weightLiftedLayout.setError(" ");
                weightLiftedLayout.setErrorEnabled(true);
            } else {
                weightLiftedLayout.setErrorEnabled(false);
                weightLifted = (float) ParserUtil.parseDouble(getContext(), weightLiftedEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    weightLifted = ConverterUtil.poundsToKgs(weightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher repsLiftedEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (repsLiftedEditText.length() == 0) {
                repsLiftedLayout.setError(" ");
                repsLiftedLayout.setErrorEnabled(true);
            } else {
                repsLiftedLayout.setErrorEnabled(false);
                repsLifted = Integer.parseInt(repsLiftedEditText.getText().toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private AdapterView.OnItemSelectedListener formulaSpinnerOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            oneRepMaxFormula = getFormulaForPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private int getFormulaForPosition(int position){
        switch (position){
            case 0: return Constants.ONE_REP_MAX_FORMULA_EPLEY;
            case 1: return Constants.ONE_REP_MAX_FORMULA_BRZYCKI;
            case 2: return Constants.ONE_REP_MAX_FORMULA_LANDER;
            case 3: return Constants.ONE_REP_MAX_FORMULA_LOMBARDI;
            case 4: return Constants.ONE_REP_MAX_FORMULA_MAYHEW;
            case 5: return Constants.ONE_REP_MAX_FORMULA_OCONNER;
            case 6: return Constants.ONE_REP_MAX_FORMULA_WATHEN;
        }
        return -1;
    }

    private int getPositionForFormula(int oneRepMaxFormula){
        switch (oneRepMaxFormula){
            case Constants.ONE_REP_MAX_FORMULA_EPLEY: return 0;
            case Constants.ONE_REP_MAX_FORMULA_BRZYCKI: return 1;
            case Constants.ONE_REP_MAX_FORMULA_LANDER: return 2;
            case Constants.ONE_REP_MAX_FORMULA_LOMBARDI: return 3;
            case Constants.ONE_REP_MAX_FORMULA_MAYHEW: return 4;
            case Constants.ONE_REP_MAX_FORMULA_OCONNER: return 5;
            case Constants.ONE_REP_MAX_FORMULA_WATHEN: return 6;
        }
        return -1;
    }
}
