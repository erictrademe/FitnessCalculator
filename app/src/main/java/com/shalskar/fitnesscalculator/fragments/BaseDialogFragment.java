package com.shalskar.fitnesscalculator.fragments;

import android.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by Vincent on 3/06/2016.
 */
public class BaseDialogFragment extends DialogFragment {

    protected boolean validateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        if (editText.length() == 0 && value <= 0) {
            textInputLayout.setError(" ");
            textInputLayout.setErrorEnabled(true);
            return false;
        } else
            return true;
    }
}
