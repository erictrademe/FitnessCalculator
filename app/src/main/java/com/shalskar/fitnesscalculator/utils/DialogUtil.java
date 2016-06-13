package com.shalskar.fitnesscalculator.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.fragments.BaseDialogFragment;
import com.shalskar.fitnesscalculator.fragments.BaseInfoDialogFragment;
import com.shalskar.fitnesscalculator.fragments.MacroDialog;
import com.shalskar.fitnesscalculator.fragments.OneRepMaxDialog;

/**
 * Created by RachelTeTau on 18/05/16.
 */
public class DialogUtil {

    private static final String TAG_INFO_DIALOG_FRAGMENT = "fragment_dialog_info";

    public static void showMessageDialog(@NonNull FragmentManager fragmentManager, @NonNull String title, @NonNull String content,
                                         int imageResource){
        Fragment frag = fragmentManager.findFragmentByTag(TAG_INFO_DIALOG_FRAGMENT);
        if (frag != null)
            fragmentManager.beginTransaction().remove(frag).commit();

        BaseInfoDialogFragment baseInfoDialogFragment = BaseInfoDialogFragment.newInstance(title, content, imageResource);
        baseInfoDialogFragment.show(fragmentManager, TAG_INFO_DIALOG_FRAGMENT);
    }

    public static boolean validateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        if (editText.length() == 0 && value <= 0) {
            textInputLayout.setError(" ");
            textInputLayout.setErrorEnabled(true);
            return false;
        } else
            return true;
    }

}
