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
public class BaseDialogFragment extends DialogFragment {

    /**
     * Generic base dialog for most of the dialogs used throughout the app.
     * <p/>
     * Any subclass must provide an argument for both the layout and the title.
     * This layout file must include the dialog_header and dialog_footer layouts.
     */

    protected static final String KEY_LAYOUT = "layout";
    protected static final String KEY_TITLE = "title";
    protected static final String KEY_IMAGE = "image";

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.title_textview)
    TextView titleTextView;

    @BindView(R.id.button_unit)
    Button unitButton;

    @BindView(R.id.bottom_divider)
    View dividerView;

    @BindView(R.id.field_layout)
    View fieldLayout;

    protected NumberFormat numberFormat = new DecimalFormat(Constants.FORMAT_NUMBER);


    public BaseDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTheme;

        int layout = getArguments().getInt(KEY_LAYOUT);
        View view = getActivity().getLayoutInflater().inflate(layout, container);
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);

        ButterKnife.bind(this, view);
        titleTextView.setText(getArguments().getString(KEY_TITLE));
        loadImage(getArguments().getInt(KEY_IMAGE));
        initialiseScrollView();
        return view;
    }

    private void loadImage(int imageResource) {
        int width = getDialog().getWindow().getDecorView().getWidth();
        int height = (int) getResources().getDimension(R.dimen.dialog_header_height);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), imageResource, width, height));
    }

    private void initialiseScrollView() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int diff = (fieldLayout.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                if (diff <= 0 && dividerView.getVisibility() == View.VISIBLE) {
                    dividerView.setVisibility(View.INVISIBLE);
                } else if (diff > 10 && dividerView.getVisibility() == View.INVISIBLE) {
                    dividerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected boolean validateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        if (editText.length() == 0 && value <= 0) {
            textInputLayout.setError(" ");
            textInputLayout.setErrorEnabled(true);
            return false;
        } else
            return true;
    }

    protected void prepopulateUnit(int unit){
        if (unit == Constants.UNIT_IMPERIAL)
            unitButton.setText(getString(R.string.imperial));
        else if (unit == Constants.UNIT_METRIC)
            unitButton.setText(getString(R.string.metric));
    }

    @OnClick(R.id.button_unit)
    void onClickUnitButton() {

    }

    @OnClick(R.id.button_ok)
    void onOkClick() {

    }

    @OnClick(R.id.button_cancel)
    void onCancelClick() {
        this.dismiss();
    }
}
