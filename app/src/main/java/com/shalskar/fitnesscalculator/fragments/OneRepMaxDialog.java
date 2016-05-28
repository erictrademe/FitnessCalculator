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
import com.shalskar.fitnesscalculator.viewholders.OneRepMaxViewHolder;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class OneRepMaxDialog extends DialogFragment {

    @BindView(R.id.edittext_layout_weight_lifted)
    TextInputLayout weightLiftedLayout;

    @BindView(R.id.edittext_weight_lifted)
    EditText weightLiftedEditText;

    @BindView(R.id.edittext_layout_reps_lifted)
    TextInputLayout repsLiftedLayout;

    @BindView(R.id.edittext_reps_lifted)
    EditText repsLiftedEditText;

    @BindView(R.id.button_unit)
    Button unitButton;

    private int unit = Constants.UNIT_METRIC;
    private int repsLifted = 0;
    private float weightLifted = 0;

    public OneRepMaxDialog() {
    }

    public static OneRepMaxDialog newInstance() {
        OneRepMaxDialog oneRepMaxDialog = new OneRepMaxDialog();
        return oneRepMaxDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_one_rep_max, container);

        ButterKnife.bind(this, view);
        initialiseViews();
        prepopulateFields();

        return view;
    }

    private void initialiseViews() {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().setTitle(getString(R.string.body_mass_index));
        addListeners();
    }

    private void prepopulateFields() {
        repsLifted = SharedPreferencesManager.getRepsLifted();
        weightLifted = SharedPreferencesManager.getWeightLifted();
        unit = SharedPreferencesManager.getUnit();
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            weightLiftedLayout.setHint(getString(R.string.pounds));
            if (weightLifted > 0)
                weightLiftedEditText.setText(String.format("%.0f", ConverterUtil.kgsToPounds(weightLifted)));
        } else if (unit == Constants.UNIT_METRIC) {
            weightLiftedLayout.setHint(getString(R.string.kilograms));
            if (weightLifted > 0)
                weightLiftedEditText.setText(String.format("%.0f", weightLifted));
        }
        if (repsLifted > 0)
            repsLiftedEditText.setText(String.format("%d", repsLifted));
    }


    @OnClick(R.id.button_unit)
    void onClickBmiUnitButton() {
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
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            double convertedWeight = 0;
            if (weightLifted > 0)
                convertedWeight = ConverterUtil.kgsToPounds(weightLifted);
            weightLiftedEditText.setText(String.format("%.0f", convertedWeight));
        } else if (unit == Constants.UNIT_METRIC) {
            weightLiftedEditText.setText(String.format("%.0f", weightLifted));
        }
    }

    @OnClick(R.id.button_ok)
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveWeightLifted(weightLifted);
            SharedPreferencesManager.saveRepsLifted(repsLifted);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_ONE_REP_MAX));
            this.dismiss();
        }
    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        this.dismiss();
    }

    private boolean validateFields() {
        if (repsLiftedEditText.length() > 0 && weightLiftedEditText.length() > 0) {
            if (repsLifted > 0 && weightLifted > 0) return true;
        }
        return false;
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
                    weightLifted = (float) ConverterUtil.poundsToKgs(weightLifted);
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
}
