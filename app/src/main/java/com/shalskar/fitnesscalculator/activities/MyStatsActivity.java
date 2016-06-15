package com.shalskar.fitnesscalculator.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rey.material.widget.Slider;
import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.events.DetailsUpdatedEvent;
import com.shalskar.fitnesscalculator.events.MyStatsSavedEvent;
import com.shalskar.fitnesscalculator.listeners.FieldListener;
import com.shalskar.fitnesscalculator.listeners.ValidificationTextWatcher;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.utils.ConverterUtil;
import com.shalskar.fitnesscalculator.utils.ImageUtil;
import com.shalskar.fitnesscalculator.utils.ParserUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MyStatsActivity extends AppCompatActivity {

    // todo add bodyfat and macros

    @BindView(R.id.image)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.button_unit)
    Button unitButton;

    /**
     * Body
     **/

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

    @BindView(R.id.edittext_layout_weight)
    TextInputLayout weightLayout;

    @BindView(R.id.edittext_weight)
    EditText weightEditText;

    @BindView(R.id.edittext_height)
    EditText heightEditText;

    @BindView(R.id.edittext_layout_height)
    TextInputLayout heightLayout;

    @BindView(R.id.edittext_height_inches)
    EditText heightInchesEditText;

    @BindView(R.id.height_inches_layout)
    TextInputLayout heightInchesLayout;

    @BindView(R.id.edittext_layout_wrist)
    TextInputLayout wristLayout;

    @BindView(R.id.edittext_wrist)
    EditText wristEditText;

    @BindView(R.id.edittext_ankle)
    EditText ankleEditText;

    @BindView(R.id.edittext_layout_ankle)
    TextInputLayout ankleLayout;

    @BindView(R.id.slider_activity_level)
    Slider activityLevelSlider;

    @BindView(R.id.textview_activity_level_amount)
    TextView activityLevelAmountTextView;

    @BindView(R.id.button_calculated)
    Button calculatedButton;

    @BindView(R.id.edittext_layout_calorie_intake)
    TextInputLayout calorieIntakeLayout;

    @BindView(R.id.edittext_calorie_intake)
    EditText calorieIntakeEditText;

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

    /**
     * Strength
     **/

    @BindView(R.id.edittext_layout_squat)
    TextInputLayout squatLayout;

    @BindView(R.id.edittext_squat)
    EditText squatEditText;

    @BindView(R.id.edittext_layout_bench_press)
    TextInputLayout benchPressLayout;

    @BindView(R.id.edittext_bench_press)
    EditText benchPressEditText;

    @BindView(R.id.edittext_layout_deadlift)
    TextInputLayout deadliftLayout;

    @BindView(R.id.edittext_deadlift)
    EditText deadliftEditText;

    private ValidificationTextWatcher calorieIntakeValidationTextWatcher;
    private ValidificationTextWatcher proteinValidationTextWatcher;
    private ValidificationTextWatcher carbohydratesValidationTextWatcher;
    private ValidificationTextWatcher fatIntakeValidationTextWatcher;

    protected NumberFormat numberFormat = new DecimalFormat(Constants.FORMAT_NUMBER);

    private int unit;
    private int gender;
    private int age;
    private double height;
    private double weight;
    private float wristMeasurement;
    private float ankleMeasurement;
    private float activityLevel;
    private int goal;
    private float calorieIntake = 0;
    private float protein = 0;
    private float carbohydrates = 0;
    private float fat = 0;

    private float benchPressWeightLifted;
    private float squatWeightLifted;
    private float deadliftWeightLifted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_my_stats);
        ButterKnife.bind(this);

        initialiseListeners();
        addListeners();
        loadImage();
        initialiseToolbar();
        initialiseStats();
        prepopulateFields();
    }

    private void initialiseToolbar() {
        toolbar.setTitle(R.string.my_stats);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void loadImage() {
        int width = (getResources().getConfiguration().screenWidthDp);
        int height = (getResources().getConfiguration().screenHeightDp);
        imageView.setImageBitmap(ImageUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.my_stats_background, width, height));
    }

    private void initialiseListeners(){
        calorieIntakeValidationTextWatcher = new ValidificationTextWatcher(calorieIntakeLayout, calorieIntakeEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                calorieIntake = value;
            }
        }, unit, false);
        proteinValidationTextWatcher = new ValidificationTextWatcher(proteinLayout, proteinEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                protein = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
                else customErrorTextView.setVisibility(View.VISIBLE);
            }
        }, unit, false);
        carbohydratesValidationTextWatcher = new ValidificationTextWatcher(carbohydratesLayout, carbohydratesEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                carbohydrates = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
                else customErrorTextView.setVisibility(View.VISIBLE);
            }
        }, unit, false);
        fatIntakeValidationTextWatcher = new ValidificationTextWatcher(fatLayout, fatEditText, new FieldListener() {
            @Override
            public void fieldChanged(float value) {
                fat = value;
                if (checkMacroRatios()) customErrorTextView.setVisibility(View.INVISIBLE);
                else customErrorTextView.setVisibility(View.VISIBLE);
            }
        }, unit, false);
    }

    /**
     * Preopulation methods.
     */

    private void initialiseStats(){
        unit = Constants.UNIT_METRIC;
        gender = Constants.GENDER_FEMALE;
        age = 0;
        height = 0;
        weight = 0;
        wristMeasurement = 0;
        ankleMeasurement = 0;
        activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;
        goal = Constants.GOAL_GAIN_MUSCLE;

        benchPressWeightLifted = 0;
        squatWeightLifted = 0;
        deadliftWeightLifted = 0;
    }

    private void prepopulateFields() {
        loadFields();
        prepopulateGender();
        prepopulateAge();
        prepopulateHeightAndWeight();
        prepopulateWristAndAnkle();
        prepopulateActivityLevel();
        prepopulateGoal();
        prepopulateCustomMacros();
        prepopulateLifts();
        checkIfCalculateCalorieIntakeAvailable();
    }

    private void loadFields() {
        unit = SharedPreferencesManager.getUnit();
        gender = SharedPreferencesManager.getGender();
        if (gender == -1) {
            gender = Constants.GENDER_FEMALE;
            SharedPreferencesManager.saveGender(gender);
        }
        age = SharedPreferencesManager.getAge();
        height = SharedPreferencesManager.getHeight();
        weight = SharedPreferencesManager.getWeight();
        wristMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_WRIST);
        ankleMeasurement = SharedPreferencesManager.getMeasurement(Constants.BODY_PART_ANKLE);
        activityLevel = SharedPreferencesManager.getActivityLevel();
        if (activityLevel == -1) {
            activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;
            SharedPreferencesManager.saveActivityLevel(activityLevel);
        }
        calorieIntake = SharedPreferencesManager.getCalorieIntake();
        goal = SharedPreferencesManager.getGoal();
        if (goal == -1) {
            goal = Constants.GOAL_GAIN_MUSCLE;
            SharedPreferencesManager.saveGoal(goal);
        }
        protein = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_PROTEIN);
        carbohydrates = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES);
        fat = SharedPreferencesManager.getMacronutrient(Constants.MACRONUTRIENT_FAT);
        squatWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_SQUAT);
        benchPressWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_BENCH_PRESS);
        deadliftWeightLifted = SharedPreferencesManager.getWeightLifted(Constants.EXERCISE_DEADLIFT);
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

    private void prepopulateAge() {
        weightLayout.setErrorEnabled(false);
        if (age != 0)
            ageEditText.setText(String.format("%d", age));
    }

    private void prepopulateHeightAndWeight() {
        heightLayout.setErrorEnabled(false);
        weightLayout.setErrorEnabled(false);
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            heightLayout.setHint(getString(R.string.feet));
            weightLayout.setHint(getString(R.string.pounds));
            heightInchesLayout.setVisibility(View.VISIBLE);
            if (weight > 0)
                weightEditText.setText(String.format("%.0f", ConverterUtil.kgsToPounds(weight)));
            if (height > 0) {
                double[] feetAndInches = ConverterUtil.cmToFeetAndInches(height);
                heightEditText.setText(String.format("%.0f", feetAndInches[0]));
                heightInchesEditText.setText(String.format("%.0f", feetAndInches[1]));
            }
        } else if (unit == Constants.UNIT_METRIC) {
            heightLayout.setHint(getString(R.string.centimeters));
            weightLayout.setHint(getString(R.string.kilograms));
            if (weight > 0)
                weightEditText.setText(String.format("%.0f", weight));
            if (height > 0)
                heightEditText.setText(String.format("%.0f", height));
        }
    }

    private void prepopulateWristAndAnkle() {
        if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.imperial));
            ankleLayout.setHint(getString(R.string.inches));
            wristLayout.setHint(getString(R.string.inches));
            if (wristMeasurement > 0)
                wristEditText.setText(numberFormat.format(ConverterUtil.cmToInches(wristMeasurement)));
            if (ankleMeasurement > 0) {
                ankleEditText.setText(numberFormat.format(ConverterUtil.cmToInches(ankleMeasurement)));
            }
        } else if (unit == Constants.UNIT_METRIC) {
            ankleLayout.setHint(getString(R.string.centimeters));
            wristLayout.setHint(getString(R.string.centimeters));
            if (wristMeasurement > 0)
                wristEditText.setText(numberFormat.format(wristMeasurement));
            if (ankleMeasurement > 0)
                ankleEditText.setText(numberFormat.format(ankleMeasurement));
        }
    }

    private void prepopulateActivityLevel() {
        if (activityLevel == Constants.ACTIVITY_LEVEL_SEDENTARY) {
            activityLevelSlider.setValue(0, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_sedentary));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_LIGHT) {
            activityLevelSlider.setValue(1, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_light));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_MODERATE) {
            activityLevelSlider.setValue(2, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_moderate));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_ACTIVE) {
            activityLevelSlider.setValue(3, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_active));
        } else if (activityLevel == Constants.ACTIVITY_LEVEL_EXTREME) {
            activityLevelSlider.setValue(4, false);
            activityLevelAmountTextView.setText(getString(R.string.activity_level_extreme));
        }
    }

    private void prepopulateGoal() {
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
        } else if (goal == Constants.GOAL_CUSTOM) {
            gainMuscleRadioButton.setChecked(false);
            fatLossRadioButton.setChecked(false);
            maintainRadioButton.setChecked(false);
            customRadioButton.setChecked(true);
            customMacroLayout.setVisibility(View.VISIBLE);
            customTextView.setVisibility(View.VISIBLE);
        }
    }

    private void prepopulateCustomMacros(){
        prepopulateField(calorieIntakeLayout, calorieIntakeEditText, calorieIntake);
        prepopulateField(proteinLayout, proteinEditText, protein);
        prepopulateField(carbohydratesLayout, carbohydratesEditText, carbohydrates);
        prepopulateField(fatLayout, fatEditText, fat);
    }

    private void prepopulateLifts() {
        prepopulateWeightField(squatLayout, squatEditText, squatWeightLifted);
        prepopulateWeightField(benchPressLayout, benchPressEditText, benchPressWeightLifted);
        prepopulateWeightField(deadliftLayout, deadliftEditText, deadliftWeightLifted);
    }

    private void prepopulateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        textInputLayout.setErrorEnabled(false);
        if (unit == Constants.UNIT_IMPERIAL) {
            textInputLayout.setHint(getString(R.string.pounds));
            if (value > 0)
                editText.setText(numberFormat.format(ConverterUtil.kgsToPounds(value)));
        } else if (unit == Constants.UNIT_METRIC) {
            textInputLayout.setHint(getString(R.string.kilograms));
            if (value > 0)
                editText.setText(numberFormat.format(value));
        }
    }

    private void prepopulateField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        textInputLayout.setErrorEnabled(false);
        if (value > 0)
            editText.setText(numberFormat.format(value));
    }

    @OnClick(R.id.button_unit)
    void onClickUnitButton() {
        removeListeners();
        if (unit == Constants.UNIT_METRIC) {
            unitButton.setText(getString(R.string.imperial));
            SharedPreferencesManager.saveUnit(Constants.UNIT_IMPERIAL);
            unit = Constants.UNIT_IMPERIAL;
            heightLayout.setHint(getString(R.string.feet));
            setHintsWeight(getString(R.string.pounds));
            setHintsLength(getString(R.string.inches));
            heightInchesLayout.setVisibility(View.VISIBLE);
        } else if (unit == Constants.UNIT_IMPERIAL) {
            unitButton.setText(getString(R.string.metric));
            SharedPreferencesManager.saveUnit(Constants.UNIT_METRIC);
            unit = Constants.UNIT_METRIC;
            heightLayout.setHint(getString(R.string.centimeters));
            setHintsWeight(getString(R.string.kilograms));
            setHintsLength(getString(R.string.centimeters));
            heightInchesLayout.setVisibility(View.GONE);
        }
        convertFields();
        addListeners();
        EventBus.getDefault().post(new DetailsUpdatedEvent(Constants.DETAIL_UNIT));
    }

    private void setHintsWeight(@NonNull String unit) {
        weightLayout.setHint(unit);
        squatLayout.setHint(unit);
        benchPressLayout.setHint(unit);
        deadliftLayout.setHint(unit);
    }

    private void setHintsLength(@NonNull String unit) {
        wristLayout.setHint(unit);
        ankleLayout.setHint(unit);
    }

    private void convertFields() {
        if (unit == Constants.UNIT_IMPERIAL) {
            if (weight > 0 && weightEditText.length() > 0)
                weightEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(weight)));
            if (height > 0 && heightEditText.length() > 0) {
                double[] feetAndInches = ConverterUtil.cmToFeetAndInches(height);
                heightEditText.setText(numberFormat.format(feetAndInches[0]));
                heightInchesEditText.setText(numberFormat.format(feetAndInches[1]));
            }
            if (wristMeasurement > 0 && wristEditText.length() > 0)
                wristEditText.setText(numberFormat.format(ConverterUtil.cmToInches(wristMeasurement)));
            if (ankleMeasurement > 0 && ankleEditText.length() > 0)
                ankleEditText.setText(numberFormat.format(ConverterUtil.cmToInches(ankleMeasurement)));
            if (squatWeightLifted > 0 && squatEditText.length() > 0)
                squatEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(squatWeightLifted)));
            if (benchPressWeightLifted > 0 && benchPressEditText.length() > 0)
                benchPressEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(benchPressWeightLifted)));
            if (deadliftWeightLifted > 0 && deadliftEditText.length() > 0)
                deadliftEditText.setText(numberFormat.format(ConverterUtil.kgsToPounds(deadliftWeightLifted)));
        } else if (unit == Constants.UNIT_METRIC) {
            if (weight > 0 && weightEditText.length() > 0)
                weightEditText.setText(numberFormat.format(weight));
            if (height > 0 && heightEditText.length() > 0)
                heightEditText.setText(numberFormat.format(height));
            if (wristMeasurement > 0 && wristEditText.length() > 0)
                wristEditText.setText(numberFormat.format(wristMeasurement));
            if (ankleMeasurement > 0 && ankleEditText.length() > 0)
                ankleEditText.setText(numberFormat.format(ankleMeasurement));
            if (squatWeightLifted > 0 && squatEditText.length() > 0)
                squatEditText.setText(numberFormat.format(squatWeightLifted));
            if (benchPressWeightLifted > 0 && benchPressEditText.length() > 0)
                benchPressEditText.setText(numberFormat.format(benchPressWeightLifted));
            if (deadliftWeightLifted > 0 && deadliftEditText.length() > 0)
                deadliftEditText.setText(numberFormat.format(deadliftWeightLifted));
        }
    }

    protected boolean validateMeasurementField(@NonNull EditText editText, float value) {
        if (editText.length() == 0 && value <= 0) return false;
        else return true;
    }

    private boolean validateCustomMacros(){
        if(!validateMeasurementField(proteinEditText, protein)) return false;
        if(!validateMeasurementField(carbohydratesEditText, carbohydrates)) return false;
        if(!validateMeasurementField(fatEditText, fat)) return false;
        if(!checkMacroRatios()) return false;
        return true;
    }

    private boolean checkMacroRatios(){
        return (protein + carbohydrates + fat) == 100;
    }

    @OnClick(R.id.button_clear)
    void onClickClearButton(){
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, R.string.confirm_clear, Snackbar.LENGTH_LONG)
                .setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearStats();
                    }
                })
                .show();
    }

    private void clearStats(){
        SharedPreferencesManager.clearAll();
        initialiseStats();
        clearViews();
        calculatedButton.setVisibility(View.INVISIBLE);
    }

    private void clearViews(){
        removeListeners();
        prepopulateGender();
        ageEditText.setText(null);
        weightEditText.setText(null);
        heightEditText.setText(null);
        wristEditText.setText(null);
        ankleEditText.setText(null);
        prepopulateActivityLevel();
        calorieIntakeEditText.setText(null);
        prepopulateGoal();

        squatEditText.setText(null);
        benchPressEditText.setText(null);
        deadliftEditText.setText(null);
        addListeners();
    }

    @OnClick(R.id.button_save)
    void onClickSaveButton() {
        List<Integer> detailsUpdated = new ArrayList<>();
        if (validateMeasurementField(ageEditText, age)) {
            detailsUpdated.add(Constants.DETAIL_AGE);
            SharedPreferencesManager.saveAge(age);
        }
        if (validateMeasurementField(heightEditText, (float) height)) {
            detailsUpdated.add(Constants.DETAIL_HEIGHT);
            SharedPreferencesManager.saveHeight(height);
        }
        if (validateMeasurementField(weightEditText, (float) weight)) {
            detailsUpdated.add(Constants.DETAIL_WEIGHT);
            SharedPreferencesManager.saveWeight(weight);
        }
        if (validateMeasurementField(wristEditText, wristMeasurement)) {
            detailsUpdated.add(Constants.DETAIL_MEASUREMENT);
            SharedPreferencesManager.saveMeasurement(Constants.BODY_PART_WRIST, wristMeasurement);
        }
        if (validateMeasurementField(ankleEditText, ankleMeasurement)) {
            detailsUpdated.add(Constants.DETAIL_MEASUREMENT);
            SharedPreferencesManager.saveMeasurement(Constants.BODY_PART_ANKLE, ankleMeasurement);
        }
        if (validateMeasurementField(calorieIntakeEditText, calorieIntake)) {
            detailsUpdated.add(Constants.DETAIL_CALORIE_INTAKE);
            SharedPreferencesManager.saveCalorieIntake(calorieIntake);
        }
        if(customRadioButton.isChecked() && validateCustomMacros()){
            detailsUpdated.add(Constants.DETAIL_MACRONUTRIENT);
            SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_PROTEIN, protein);
            SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_CARBOHYDRATES, carbohydrates);
            SharedPreferencesManager.saveMacronutrient(Constants.MACRONUTRIENT_FAT, fat);
        }
        if (validateMeasurementField(squatEditText, squatWeightLifted)) {
            detailsUpdated.add(Constants.DETAIL_EXERCISE);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_SQUAT, squatWeightLifted);
        }
        if (validateMeasurementField(benchPressEditText, benchPressWeightLifted)) {
            detailsUpdated.add(Constants.DETAIL_EXERCISE);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_BENCH_PRESS, benchPressWeightLifted);
        }
        if (validateMeasurementField(deadliftEditText, deadliftWeightLifted)) {
            detailsUpdated.add(Constants.DETAIL_EXERCISE);
            SharedPreferencesManager.saveWeightLifted(Constants.EXERCISE_DEADLIFT, deadliftWeightLifted);
        }
        detailsUpdated.add(Constants.DETAIL_GENDER);
        SharedPreferencesManager.saveGender(gender);
        detailsUpdated.add(Constants.DETAIL_ACTIVITY_LEVEL);
        SharedPreferencesManager.saveActivityLevel(activityLevel);
        detailsUpdated.add(Constants.DETAIL_GOAL);
        SharedPreferencesManager.saveGoal(goal);

        EventBus.getDefault().post(new DetailsUpdatedEvent(detailsUpdated));
        EventBus.getDefault().post(new MyStatsSavedEvent());
        finish();
    }


    private void checkIfCalculateCalorieIntakeAvailable(){
        if(weight > 0 && height > 0 && age > 0)
            calculatedButton.setVisibility(View.VISIBLE);
        else
            calculatedButton.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }


    /**
     * Listeners
     */

    private void removeListeners() {
        ageEditText.removeTextChangedListener(ageEditTextWatcher);
        weightEditText.removeTextChangedListener(weightEditTextWatcher);
        heightEditText.removeTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.removeTextChangedListener(heightEditTextWatcher);
        wristEditText.removeTextChangedListener(wristMeasurementEditTextWatcher);
        ankleEditText.removeTextChangedListener(ankleMeasurementEditTextWatcher);
        calorieIntakeEditText.removeTextChangedListener(calorieIntakeValidationTextWatcher);
        proteinEditText.removeTextChangedListener(proteinValidationTextWatcher);
        carbohydratesEditText.removeTextChangedListener(carbohydratesValidationTextWatcher);
        fatEditText.removeTextChangedListener(fatIntakeValidationTextWatcher);

        squatEditText.removeTextChangedListener(squatEditTextWatcher);
        benchPressEditText.removeTextChangedListener(benchPressEditTextWatcher);
        deadliftEditText.removeTextChangedListener(deadliftEditTextWatcher);
    }

    private void addListeners() {
        radioGroupGender.setOnCheckedChangeListener(onGenderCheckedChangeListener);
        radioGroupGoal.setOnCheckedChangeListener(onGoalCheckedChangeListener);
        ageEditText.addTextChangedListener(ageEditTextWatcher);
        weightEditText.addTextChangedListener(weightEditTextWatcher);
        heightEditText.addTextChangedListener(heightEditTextWatcher);
        heightInchesEditText.addTextChangedListener(heightEditTextWatcher);
        wristEditText.addTextChangedListener(wristMeasurementEditTextWatcher);
        ankleEditText.addTextChangedListener(ankleMeasurementEditTextWatcher);
        activityLevelSlider.setOnPositionChangeListener(activityLevelPositionChangedListener);
        calorieIntakeEditText.addTextChangedListener(calorieIntakeValidationTextWatcher);
        proteinEditText.addTextChangedListener(proteinValidationTextWatcher);
        carbohydratesEditText.addTextChangedListener(carbohydratesValidationTextWatcher);
        fatEditText.addTextChangedListener(fatIntakeValidationTextWatcher);

        squatEditText.addTextChangedListener(squatEditTextWatcher);
        benchPressEditText.addTextChangedListener(benchPressEditTextWatcher);
        deadliftEditText.addTextChangedListener(deadliftEditTextWatcher);
    }

    @OnClick(R.id.button_calculated)
    void onCalculatedButtonClicked() {
        calorieIntake = FitnessCalculator.calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);
        calorieIntakeEditText.setText(numberFormat.format(calorieIntake));
    }

    private TextWatcher weightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (weightEditText.length() != 0) {
                weight = ParserUtil.parseDouble(MyStatsActivity.this, weightEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    weight = ConverterUtil.poundsToKgs(weight);
            } else weight = 0;
            checkIfCalculateCalorieIntakeAvailable();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher heightEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (heightEditText.length() != 0) {
                if (unit == Constants.UNIT_METRIC) {
                    height = ParserUtil.parseDouble(MyStatsActivity.this, heightEditText.getText().toString());
                } else if (unit == Constants.UNIT_IMPERIAL) {
                    double feet = ParserUtil.parseDouble(MyStatsActivity.this, heightEditText.getText().toString());
                    double inches = 0;
                    if (heightInchesEditText.length() > 0)
                        inches = ParserUtil.parseDouble(MyStatsActivity.this, heightInchesEditText.getText().toString());
                    height = ConverterUtil.feetAndInchesToCm(feet, inches);
                }
            } else height = 0;
            checkIfCalculateCalorieIntakeAvailable();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher ageEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (ageEditText.length() != 0)
                age = Integer.parseInt(ageEditText.getText().toString());
            else age = 0;
            checkIfCalculateCalorieIntakeAvailable();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private RadioGroup.OnCheckedChangeListener onGenderCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.radio_button_female) gender = Constants.GENDER_FEMALE;
            else if (checkedId == R.id.radio_button_male) gender = Constants.GENDER_MALE;

        }
    };

    private TextWatcher wristMeasurementEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (wristEditText.length() != 0) {
                wristMeasurement = ParserUtil.parseFloat(MyStatsActivity.this, wristEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    wristMeasurement = ConverterUtil.inchesToCm(wristMeasurement);
            } else wristMeasurement = 0;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher ankleMeasurementEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (ankleEditText.length() != 0) {
                ankleMeasurement = ParserUtil.parseFloat(MyStatsActivity.this, ankleEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    ankleMeasurement = ConverterUtil.inchesToCm(ankleMeasurement);
            } else ankleMeasurement = 0;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private Slider.OnPositionChangeListener activityLevelPositionChangedListener = new Slider.OnPositionChangeListener() {
        @Override
        public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
            switch (newValue) {
                case 0:
                    activityLevel = Constants.ACTIVITY_LEVEL_SEDENTARY;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_sedentary));
                    break;
                case 1:
                    activityLevel = Constants.ACTIVITY_LEVEL_LIGHT;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_light));
                    break;
                case 2:
                    activityLevel = Constants.ACTIVITY_LEVEL_MODERATE;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_moderate));
                    break;
                case 3:
                    activityLevel = Constants.ACTIVITY_LEVEL_ACTIVE;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_active));
                    break;
                case 4:
                    activityLevel = Constants.ACTIVITY_LEVEL_EXTREME;
                    activityLevelAmountTextView.setText(getString(R.string.activity_level_extreme));
                    break;
            }
        }
    };

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

    private TextWatcher squatEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (squatEditText.length() != 0) {
                squatWeightLifted = ParserUtil.parseFloat(MyStatsActivity.this, squatEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    squatWeightLifted = ConverterUtil.poundsToKgs(squatWeightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher benchPressEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (benchPressEditText.length() != 0) {
                benchPressWeightLifted = ParserUtil.parseFloat(MyStatsActivity.this, benchPressEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    benchPressWeightLifted = ConverterUtil.poundsToKgs(benchPressWeightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private TextWatcher deadliftEditTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (deadliftEditText.length() != 0) {
                deadliftWeightLifted = ParserUtil.parseFloat(MyStatsActivity.this, deadliftEditText.getText().toString());
                if (unit == Constants.UNIT_IMPERIAL)
                    deadliftWeightLifted = ConverterUtil.poundsToKgs(deadliftWeightLifted);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

}

