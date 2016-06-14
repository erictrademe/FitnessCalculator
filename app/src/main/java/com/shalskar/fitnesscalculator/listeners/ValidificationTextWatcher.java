package com.shalskar.fitnesscalculator.listeners;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

/**
 * Created by Vincent on 12/06/2016.
 */
public class ValidificationTextWatcher implements TextWatcher {

    private int unit;
    private boolean conversion;
    private TextInputLayout textInputLayout;
    private EditText editText;
    private FieldListener fieldListener;

    public ValidificationTextWatcher(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, @NonNull FieldListener fieldListener,
                                     int unit, boolean conversion) {
        this.textInputLayout = textInputLayout;
        this.editText = editText;
        this.fieldListener = fieldListener;
        this.unit = unit;
        this.conversion = conversion;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        float value = 0;
        if (editText.length() == 0) {
            textInputLayout.setError(" ");
            textInputLayout.setErrorEnabled(true);
        } else {
            textInputLayout.setErrorEnabled(false);
            value = ParserUtil.parseFloat(textInputLayout.getContext(), editText.getText().toString());
            if (unit == Constants.UNIT_IMPERIAL && conversion)
                value = ConverterUtil.inchesToCm(value);
        }
        fieldListener.fieldChanged(value);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
