package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.listeners.FieldListener;
import com.shalskar.fitnesscalculator.listeners.ValidificationTextWatcher;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class BodyfatDialog extends BaseDialogFragment {

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

    @BindView(R.id.button_bodyfat_type)
    Button bodyfatCalculatorTypeButton;

    @BindView(R.id.textview_pectoral)
    TextView pectoralTextView;

    @BindView(R.id.edittext_layout_pectoral)
    TextInputLayout pectoralLayout;

    @BindView(R.id.edittext_pectoral)
    EditText pectoralEditText;

    @BindView(R.id.textview_abdominal)
    TextView abdominalTextView;

    @BindView(R.id.edittext_layout_abdominal)
    TextInputLayout abdominalLayout;

    @BindView(R.id.edittext_abdominal)
    EditText abdominalEditText;

    @BindView(R.id.textview_thigh)
    TextView thighTextView;

    @BindView(R.id.edittext_layout_thigh)
    TextInputLayout thighLayout;

    @BindView(R.id.edittext_thigh)
    EditText thighEditText;

    @BindView(R.id.textview_triceps)
    TextView tricepsTextView;

    @BindView(R.id.edittext_layout_triceps)
    TextInputLayout tricepsLayout;

    @BindView(R.id.edittext_triceps)
    EditText tricepsEditText;

    @BindView(R.id.textview_subscapular)
    TextView subscapularTextView;

    @BindView(R.id.edittext_layout_subscapular)
    TextInputLayout subscapularLayout;

    @BindView(R.id.edittext_subscapular)
    EditText subscapularEditText;

    @BindView(R.id.textview_suprailiac)
    TextView suprailiacTextView;

    @BindView(R.id.edittext_layout_suprailiac)
    TextInputLayout suprailiacLayout;

    @BindView(R.id.edittext_suprailiac)
    EditText suprailiacEditText;

    @BindView(R.id.textview_axilla)
    TextView axillaTextView;

    @BindView(R.id.edittext_layout_axilla)
    TextInputLayout axillaLayout;

    @BindView(R.id.edittext_axilla)
    EditText axillaEditText;

    private int unit = Constants.UNIT_METRIC;
    private int gender = Constants.GENDER_FEMALE;
    private int age = 0;
    private int bodyfatCalculatorType = Constants.BODYFAT_CALCULATOR_TYPE_7_POINT;
    private float pectoralSkinfold = 0;
    private float abdominalSkinfold = 0;
    private float thighSkinfold = 0;
    private float tricepsSkinfold = 0;
    private float subscapularSkinfold = 0;
    private float suprailiacSkinfold = 0;
    private float axillaSkinfold = 0;

    private ValidificationTextWatcher pectoralValidificationTextWatcher;
    private ValidificationTextWatcher abdominalValidificationTextWatcher;
    private ValidificationTextWatcher thighValidificationTextWatcher;
    private ValidificationTextWatcher tricepsValidificationTextWatcher;
    private ValidificationTextWatcher subscapularValidificationTextWatcher;
    private ValidificationTextWatcher suprailiacValidificationTextWatcher;
    private ValidificationTextWatcher axillaValidificationTextWatcher;
    private ValidificationTextWatcher ageValidificationTextWatcher;

    public BodyfatDialog() {

    }

    public static BodyfatDialog newInstance(@NonNull String title) {
        BodyfatDialog bodyfatDialog = new BodyfatDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_bodyfat);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.bodyfat_image);
        bodyfatDialog.setArguments(args);
        return bodyfatDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        initialiseListeners();
        addListeners();
        prepopulateFields();

        return view;
    }


    private void prepopulateFields() {
        age = SharedPreferencesManager.getAge();
        gender = SharedPreferencesManager.getGender();
        unit = SharedPreferencesManager.getUnit();
        bodyfatCalculatorType = SharedPreferencesManager.getBodyfatCalculatorType();
        abdominalSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_ABDOMINAL);
        axillaSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_AXILLA);
        pectoralSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_PECTORAL);
        subscapularSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_SUBSCAPULAR);
        suprailiacSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_SUPRAILIAC);
        thighSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_THIGH);
        tricepsSkinfold = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_TRICEPS);
        updateFieldsVisibility();

        if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_7_POINT)
            bodyfatCalculatorTypeButton.setText(getString(R.string.bf_7_point));
        else if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_3_POINT)
            bodyfatCalculatorTypeButton.setText(getString(R.string.bf_3_pont));

        prepopulateUnit(unit);
        prepopulateAge();
        prepopulateGender();
        prepopulateField(pectoralLayout, pectoralEditText, unit, pectoralSkinfold);
        prepopulateField(abdominalLayout, abdominalEditText, unit, abdominalSkinfold);
        prepopulateField(axillaLayout, axillaEditText, unit, axillaSkinfold);
        prepopulateField(subscapularLayout, subscapularEditText, unit, subscapularSkinfold);
        prepopulateField(suprailiacLayout, suprailiacEditText, unit, suprailiacSkinfold);
        prepopulateField(tricepsLayout, tricepsEditText, unit, tricepsSkinfold);
        prepopulateField(thighLayout, thighEditText, unit, thighSkinfold);
    }

    private void prepopulateAge() {
        ageLayout.setErrorEnabled(false);
        if (age != 0)
            ageEditText.setText(String.format("%d", age));
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

    private void prepopulateField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, int unit, float skinfold) {
        if (skinfold > 0)
            convertField(textInputLayout, editText, unit, skinfold);
    }


    @Override
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
        }
        updateListeners();
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void convertFields() {
        if (pectoralSkinfold > 0 && pectoralEditText.length() > 0)
            convertField(pectoralLayout, pectoralEditText, unit, pectoralSkinfold);
        if (abdominalSkinfold > 0 && abdominalEditText.length() > 0)
            convertField(abdominalLayout, abdominalEditText, unit, abdominalSkinfold);
        if (thighSkinfold > 0 && thighEditText.length() > 0)
            convertField(thighLayout, thighEditText, unit, thighSkinfold);
        if (axillaSkinfold > 0 && axillaEditText.length() > 0)
            convertField(axillaLayout, axillaEditText, unit, axillaSkinfold);
        if (suprailiacSkinfold > 0 && suprailiacEditText.length() > 0)
            convertField(suprailiacLayout, suprailiacEditText, unit, suprailiacSkinfold);
        if (subscapularSkinfold > 0 && subscapularEditText.length() > 0)
            convertField(subscapularLayout, subscapularEditText, unit, subscapularSkinfold);
        if (tricepsSkinfold > 0 && tricepsEditText.length() > 0)
            convertField(tricepsLayout, tricepsEditText, unit, tricepsSkinfold);
    }

    private void convertField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, int unit, float skinfold) {
        if (unit == Constants.UNIT_IMPERIAL) {
            textInputLayout.setHint(getString(R.string.inches));
            editText.setText(numberFormat.format(ConverterUtil.mmToInches(skinfold)));
        } else if (unit == Constants.UNIT_METRIC) {
            textInputLayout.setHint(getString(R.string.millimeters));
            editText.setText(numberFormat.format(skinfold));
        }
    }

    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_ABDOMINAL, abdominalSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_AXILLA, axillaSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_PECTORAL, pectoralSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_SUBSCAPULAR, subscapularSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_SUPRAILIAC, suprailiacSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_THIGH, thighSkinfold);
            SharedPreferencesManager.saveSkinfold(Constants.SKINFOLD_TRICEPS, tricepsSkinfold);
            SharedPreferencesManager.saveGender(gender);
            SharedPreferencesManager.saveAge(age);
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_AGE, Constants.DETAIL_GENDER, Constants.DETAIL_SKINFOLD));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        if (!validateWeightField(pectoralLayout, pectoralEditText, pectoralSkinfold))
            validated = false;

        if (!validateWeightField(abdominalLayout, abdominalEditText, abdominalSkinfold))
            validated = false;

        if (!validateWeightField(suprailiacLayout, suprailiacEditText, suprailiacSkinfold))
            validated = false;

        if (!validateWeightField(subscapularLayout, subscapularEditText, subscapularSkinfold))
            validated = false;

        if (!validateWeightField(thighLayout, thighEditText, thighSkinfold))
            validated = false;

        if (!validateWeightField(tricepsLayout, tricepsEditText, tricepsSkinfold))
            validated = false;

        if (!validateWeightField(axillaLayout, axillaEditText, axillaSkinfold))
            validated = false;

        return validated;
    }

    @OnClick(R.id.button_bodyfat_type)
    void onBodyfatButtonClicked() {
        if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_3_POINT) {
            bodyfatCalculatorType = Constants.BODYFAT_CALCULATOR_TYPE_7_POINT;
            bodyfatCalculatorTypeButton.setText(getString(R.string.bf_7_point));
        } else if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_7_POINT) {
            bodyfatCalculatorType = Constants.BODYFAT_CALCULATOR_TYPE_3_POINT;
            bodyfatCalculatorTypeButton.setText(getString(R.string.bf_3_pont));
        }

        updateFieldsVisibility();
        SharedPreferencesManager.saveBodyfatCalculatorType(bodyfatCalculatorType);
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_BODYFAT_CALCULATOR_TYPE));
    }

    /**
     * Show the relevant fields for combination of gender and bodyfat calculator type.
     **/
    private void updateFieldsVisibility() {
        if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_7_POINT) {
            abdominalLayout.setVisibility(View.VISIBLE);
            pectoralLayout.setVisibility(View.VISIBLE);
            thighLayout.setVisibility(View.VISIBLE);
            subscapularLayout.setVisibility(View.VISIBLE);
            tricepsLayout.setVisibility(View.VISIBLE);
            axillaLayout.setVisibility(View.VISIBLE);

            abdominalTextView.setVisibility(View.VISIBLE);
            pectoralTextView.setVisibility(View.VISIBLE);
            thighTextView.setVisibility(View.VISIBLE);
            subscapularTextView.setVisibility(View.VISIBLE);
            tricepsTextView.setVisibility(View.VISIBLE);
            axillaTextView.setVisibility(View.VISIBLE);
        } else if (gender == Constants.GENDER_FEMALE) {
            thighLayout.setVisibility(View.VISIBLE);
            tricepsLayout.setVisibility(View.VISIBLE);
            abdominalLayout.setVisibility(View.GONE);
            pectoralLayout.setVisibility(View.GONE);
            subscapularLayout.setVisibility(View.GONE);
            axillaLayout.setVisibility(View.GONE);

            tricepsTextView.setVisibility(View.VISIBLE);
            thighTextView.setVisibility(View.VISIBLE);
            abdominalTextView.setVisibility(View.GONE);
            pectoralTextView.setVisibility(View.GONE);
            subscapularTextView.setVisibility(View.GONE);
            axillaTextView.setVisibility(View.GONE);
        } else if (gender == Constants.GENDER_MALE) {
            pectoralLayout.setVisibility(View.VISIBLE);
            abdominalLayout.setVisibility(View.VISIBLE);
            thighLayout.setVisibility(View.GONE);
            tricepsLayout.setVisibility(View.GONE);
            subscapularLayout.setVisibility(View.GONE);
            axillaLayout.setVisibility(View.GONE);

            abdominalTextView.setVisibility(View.VISIBLE);
            pectoralTextView.setVisibility(View.VISIBLE);
            tricepsTextView.setVisibility(View.GONE);
            thighTextView.setVisibility(View.GONE);
            subscapularTextView.setVisibility(View.GONE);
            axillaTextView.setVisibility(View.GONE);
        }
    }


    /**
     * Listeners
     */

    private void initialiseListeners() {
        pectoralValidificationTextWatcher = new ValidificationTextWatcher(pectoralLayout, pectoralEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                pectoralSkinfold = value;
            }
        }, unit, true);
        abdominalValidificationTextWatcher = new ValidificationTextWatcher(abdominalLayout, abdominalEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                abdominalSkinfold = value;
            }
        }, unit, true);
        thighValidificationTextWatcher = new ValidificationTextWatcher(thighLayout, thighEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                thighSkinfold = value;
            }
        }, unit, true);
        tricepsValidificationTextWatcher = new ValidificationTextWatcher(tricepsLayout, tricepsEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                tricepsSkinfold = value;
            }
        }, unit, true);
        subscapularValidificationTextWatcher = new ValidificationTextWatcher(subscapularLayout, subscapularEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                subscapularSkinfold = value;
            }
        }, unit, true);
        suprailiacValidificationTextWatcher = new ValidificationTextWatcher(suprailiacLayout, suprailiacEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                suprailiacSkinfold = value;
            }
        }, unit, true);
        axillaValidificationTextWatcher = new ValidificationTextWatcher(axillaLayout, axillaEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                axillaSkinfold = value;
            }
        }, unit, true);
        ageValidificationTextWatcher = new ValidificationTextWatcher(ageLayout, ageEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                age = (int) value;
            }
        }, unit, false);
    }


    private void updateListeners() {
        pectoralValidificationTextWatcher.setUnit(unit);
        abdominalValidificationTextWatcher.setUnit(unit);
        thighValidificationTextWatcher.setUnit(unit);
        tricepsValidificationTextWatcher.setUnit(unit);
        subscapularValidificationTextWatcher.setUnit(unit);
        suprailiacValidificationTextWatcher.setUnit(unit);
        axillaValidificationTextWatcher.setUnit(unit);
    }

    private void removeListeners() {
        pectoralEditText.removeTextChangedListener(pectoralValidificationTextWatcher);
        abdominalEditText.removeTextChangedListener(abdominalValidificationTextWatcher);
        thighEditText.removeTextChangedListener(thighValidificationTextWatcher);
        tricepsEditText.removeTextChangedListener(tricepsValidificationTextWatcher);
        subscapularEditText.removeTextChangedListener(subscapularValidificationTextWatcher);
        suprailiacEditText.removeTextChangedListener(suprailiacValidificationTextWatcher);
        axillaEditText.removeTextChangedListener(axillaValidificationTextWatcher);
        ageEditText.removeTextChangedListener(ageValidificationTextWatcher);
    }

    private void addListeners() {
        pectoralEditText.addTextChangedListener(pectoralValidificationTextWatcher);
        abdominalEditText.addTextChangedListener(abdominalValidificationTextWatcher);
        thighEditText.addTextChangedListener(thighValidificationTextWatcher);
        tricepsEditText.addTextChangedListener(tricepsValidificationTextWatcher);
        subscapularEditText.addTextChangedListener(subscapularValidificationTextWatcher);
        suprailiacEditText.addTextChangedListener(suprailiacValidificationTextWatcher);
        axillaEditText.addTextChangedListener(axillaValidificationTextWatcher);
        ageEditText.addTextChangedListener(ageValidificationTextWatcher);
        radioGroupGender.setOnCheckedChangeListener(onGenderCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener onGenderCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radio_button_female) gender = Constants.GENDER_FEMALE;
            else if (checkedId == R.id.radio_button_male) gender = Constants.GENDER_MALE;
            updateFieldsVisibility();
        }
    };
}
