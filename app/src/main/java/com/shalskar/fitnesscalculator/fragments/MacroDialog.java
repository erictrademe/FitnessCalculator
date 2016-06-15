package com.shalskar.fitnesscalculator.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rey.material.widget.Slider;
import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.listeners.FieldListener;
import com.shalskar.fitnesscalculator.listeners.ValidificationTextWatcher;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vincent on 7/05/2016.
 */
public class MacroDialog extends BaseDialogFragment {

    @BindView(R.id.edittext_layout_calorie_intake)
    TextInputLayout calorieIntakeLayout;

    @BindView(R.id.edittext_calorie_intake)
    EditText calorieIntakeEditText;

    @BindView(R.id.button_calculated)
    Button calculatedButton;

    @BindView(R.id.radio_group_goal)
    RadioGroup radioGroupGoal;

    @BindView(R.id.radio_button_gain_muscle)
    RadioButton gainMuscleRadioButton;

    @BindView(R.id.radio_button_fat_loss)
    RadioButton fatLossRadioButton;

    @BindView(R.id.radio_button_maintain)
    RadioButton maintainRadioButton;

    @BindView(R.id.radio_button_custom)
    RadioButton customRadioButton;

    @BindView(R.id.custom_macro_layout)
    ViewGroup customMacroLayout;

    @BindView(R.id.textview_custom)
    TextView customTextView;

    @BindView(R.id.textview_custom_error)
    TextView customErrorTextView;

    @BindView(R.id.edittext_layout_protein)
    TextInputLayout proteinLayout;

    @BindView(R.id.edittext_protein)
    EditText proteinEditText;

    @BindView(R.id.edittext_layout_carbohydrates)
    TextInputLayout carbohydratesLayout;

    @BindView(R.id.edittext_carbohydrates)
    EditText carbohydratesEditText;

    @BindView(R.id.edittext_layout_fat)
    TextInputLayout fatLayout;

    @BindView(R.id.edittext_fat)
    EditText fatEditText;

    // todo check if unit is necessary
    private int unit = Constants.UNIT_METRIC;
    private int goal;
    private float calorieIntake = 0;
    private float protein = 0;
    private float carbohydrates = 0;
    private float fat = 0;

    public MacroDialog() {

    }

    public static MacroDialog newInstance(@NonNull String title) {
        MacroDialog macroDialog = new MacroDialog();
        Bundle args = new Bundle();
        args.putInt(KEY_LAYOUT, R.layout.dialog_macro);
        args.putString(KEY_TITLE, title);
        args.putInt(KEY_IMAGE, R.drawable.macro_image);
        macroDialog.setArguments(args);
        return macroDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, view);
        addListeners();
        prepopulateFields();
        unitButton.setVisibility(View.GONE);
        if (calculatedCalorieIntakePossible())
            calculatedButton.setVisibility(View.VISIBLE);

        return view;
    }

    private void loadFields() {
        calorieIntake = SharedPreferencesManager.getCalorieIntake();

        unit = SharedPreferencesManager.getUnit();
        goal = SharedPreferencesManager.getGoal();
        if (goal == -1) {
            goal = Constants.GOAL_GAIN_MUSCLE;
            SharedPreferencesManager.saveGoal(goal);
        }

        protein = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_PROTEIN);
        carbohydrates = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES);
        fat = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_FAT);
    }

    /**
     * Preopulation methods.
     */

    private void prepopulateFields() {
        loadFields();

        prepopulateField(calorieIntakeLayout, calorieIntakeEditText, calorieIntake);
        prepopulateField(proteinLayout, proteinEditText, protein);
        prepopulateField(carbohydratesLayout, carbohydratesEditText, carbohydrates);
        prepopulateField(fatLayout, fatEditText, fat);

        prepopulateGoal();
    }

    private void prepopulateField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        textInputLayout.setErrorEnabled(false);
        if (value > 0)
            editText.setText(numberFormat.format(value));
    }

    private void prepopulateGoal() {
        if (goal == Constants.GOAL_CUSTOM) {
            customMacroLayout.setVisibility(View.VISIBLE);
            customTextView.setVisibility(View.VISIBLE);
            customRadioButton.setChecked(true);
            gainMuscleRadioButton.setChecked(false);
            fatLossRadioButton.setChecked(false);
            maintainRadioButton.setChecked(false);
            return;
        } else {
            customMacroLayout.setVisibility(View.GONE);
            customTextView.setVisibility(View.GONE);
        }
        if (goal == Constants.GOAL_GAIN_MUSCLE) {
            gainMuscleRadioButton.setChecked(true);
            fatLossRadioButton.setChecked(false);
            maintainRadioButton.setChecked(false);
            customRadioButton.setChecked(false);
        } else if (goal == Constants.GOAL_FAT_LOSS) {
            gainMuscleRadioButton.setChecked(false);
            fatLossRadioButton.setChecked(true);
            maintainRadioButton.setChecked(false);
            customRadioButton.setChecked(false);
        } else if (goal == Constants.GOAL_MAINTAIN) {
            gainMuscleRadioButton.setChecked(false);
            fatLossRadioButton.setChecked(false);
            maintainRadioButton.setChecked(true);
            customRadioButton.setChecked(false);
        }
    }

    @Override
    void onOkClick() {
        if (validateFields()) {
            SharedPreferencesManager.saveCalorieIntake(calorieIntake);
            SharedPreferencesManager.saveGoal(goal);
            if (customMacroLayout.getVisibility() == View.VISIBLE) {
                SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_PROTEIN, protein);
                SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES, carbohydrates);
                SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_FAT, fat);
            }
            EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_GOAL, Constants.DETAIL_CALORIE_INTAKE, Constants.DETAIL_MACRONUTRIENT));
            this.dismiss();
        }
    }

    private boolean validateFields() {
        boolean validated = true;

        if (!validateWeightField(calorieIntakeLayout, calorieIntakeEditText, calorieIntake))
            validated = false;

        if (customMacroLayout.getVisibility() == View.VISIBLE) {
            if (!validateWeightField(proteinLayout, proteinEditText, protein))
                validated = false;
            if (!validateWeightField(carbohydratesLayout, carbohydratesEditText, carbohydrates))
                validated = false;
            if (!validateWeightField(fatLayout, fatEditText, fat))
                validated = false;

            // Percentages must equal to 100
            if (checkMacroRatios())
                customErrorTextView.setVisibility(View.INVISIBLE);
            else {
                customErrorTextView.setVisibility(View.VISIBLE);
                validated = false;
            }

        }

        return validated;
    }

    private boolean checkMacroRatios() {
        return (protein + carbohydrates + fat) == 100;
    }

    /**
     * Listeners
     */

    @OnClick(R.id.button_calculated)
    void onCalculatedButtonClicked() {
        int gender = SharedPreferencesManager.getGender();
        int age = SharedPreferencesManager.getAge();
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        float activityLevel = SharedPreferencesManager.getActivityLevel();

        calorieIntake = FitnessCalculator.calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);
        calorieIntakeEditText.setText(numberFormat.format(calorieIntake));
    }

    private boolean calculatedCalorieIntakePossible() {
        int gender = SharedPreferencesManager.getGender();
        int age = SharedPreferencesManager.getAge();
        double height = SharedPreferencesManager.getHeight();
        double weight = SharedPreferencesManager.getWeight();
        float activityLevel = SharedPreferencesManager.getActivityLevel();
        if (gender != -1 && age > 0 && height > 0 && weight > 0 && activityLevel != -1)
            return true;
        else
            return false;
    }

    private void addListeners() {
        radioGroupGoal.setOnCheckedChangeListener(onGoalCheckedChangeListener);
        calorieIntakeEditText.addTextChangedListener(new ValidificationTextWatcher(calorieIntakeLayout, calorieIntakeEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                calorieIntake = value;
            }
        }, unit, false));

        proteinEditText.addTextChangedListener(new ValidificationTextWatcher(proteinLayout, proteinEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                protein = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
            }
        }, unit, false));

        carbohydratesEditText.addTextChangedListener(new ValidificationTextWatcher(carbohydratesLayout, carbohydratesEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                carbohydrates = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
            }
        }, unit, false));

        fatEditText.addTextChangedListener(new ValidificationTextWatcher(fatLayout, fatEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                fat = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
            }
        }, unit, false));
    }

    private RadioGroup.OnCheckedChangeListener onGoalCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radio_button_custom) {
                goal = Constants.GOAL_CUSTOM;
                customMacroLayout.setVisibility(View.VISIBLE);
                customTextView.setVisibility(View.VISIBLE);
                return;
            } else {
                customMacroLayout.setVisibility(View.GONE);
                customErrorTextView.setVisibility(View.INVISIBLE);
                customTextView.setVisibility(View.GONE);
            }

            if (checkedId == R.id.radio_button_gain_muscle) goal = Constants.GOAL_GAIN_MUSCLE;
            else if (checkedId == R.id.radio_button_fat_loss) goal = Constants.GOAL_FAT_LOSS;
            else if (checkedId == R.id.radio_button_maintain) goal = Constants.GOAL_MAINTAIN;

        }
    };
}
