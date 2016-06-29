package com.shalskar.fitnesscalculator.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.shalskar.fitnesscalculator.fragments.BaseInfoDialogFragment;

/**
 * Created by Vincent Te Tau on 18/05/16.
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
}
