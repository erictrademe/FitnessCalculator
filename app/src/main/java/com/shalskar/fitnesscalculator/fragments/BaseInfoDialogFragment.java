package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.utils.ImageUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 3/06/2016.
 */
public class BaseInfoDialogFragment extends DialogFragment {

    /**
     * Generic base dialog for most of the info dialogs used throughout the app.
     * <p/>
     * Any subclass must provide an argument for both the layout and the title.
     * This layout file must include the dialog_header layout.
     */

    protected static final String KEY_LAYOUT = "layout";
    protected static final String KEY_TITLE = "title";
    protected static final String KEY_IMAGE = "image";
    protected static final String KEY_CONTENT = "content";

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    @BindView(R.id.textview_content)
    TextView contentTextView;

    public BaseInfoDialogFragment() {

    }

    public static BaseInfoDialogFragment newInstance(@NonNull String title, @NonNull String content, int imageResource){
        BaseInfoDialogFragment baseInfoDialogFragment = new BaseInfoDialogFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_info_base);
        args.putString(KEY_TITLE, title);
        args.putString(KEY_CONTENT, content);
        args.putInt(KEY_IMAGE, imageResource);
        baseInfoDialogFragment.setArguments(args);
        return baseInfoDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTheme;

        int layout = getArguments().getInt(KEY_LAYOUT);
        View view = getActivity().getLayoutInflater().inflate(layout, container);

        ButterKnife.bind(this, view);
        titleTextView.setText(getArguments().getString(KEY_TITLE));
        contentTextView.setText(getArguments().getString(KEY_CONTENT));
        loadImage(getArguments().getInt(KEY_IMAGE));
        return view;
    }

    private void loadImage(int imageResource) {
        int width = getDialog().getWindow().getDecorView().getWidth();
        int height = (int) getResources().getDimension(R.dimen.dialog_header_height);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), imageResource, width, height));
    }
}
