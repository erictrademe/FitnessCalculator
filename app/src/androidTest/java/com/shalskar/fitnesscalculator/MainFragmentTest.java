package com.shalskar.fitnesscalculator;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.shalskar.fitnesscalculator.activities.MainActivity;
import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.shalskar.fitnesscalculator.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.not;

/**
 * Created by Vincent on 10/07/2016.
 */
public class MainFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public MainFragmentTest() {
        super(MainActivity.class);
    }

    public MainFragmentTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() {
        SharedPreferencesManager.clearAll();
        mainActivity = getActivity();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    public void testCalorieIntake() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Check dialog doesn't close
        onView(withId(R.id.button_ok)).perform(click());

        // Check unit button
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.imperial)));
        onView(withId(R.id.edittext_height_inches)).check(matches(isDisplayed()));
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.metric)));
        onView(withId(R.id.edittext_height_inches)).check(matches(not(isDisplayed())));

        // Check input fields
        onView(withId(R.id.edittext_age)).perform(typeText("30"), closeSoftKeyboard());
        onView(withId(R.id.edittext_weight)).perform(typeText("180"), closeSoftKeyboard());
        onView(withId(R.id.edittext_height)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.button_ok)).perform(click());

        // Check value is displayed
        onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(1, R.id.chart)).check(matches(isDisplayed()));
    }

    public void testMacros() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        // Shouldn't hide dialog
        onView(withId(R.id.button_ok)).perform(click());

        onView(withId(R.id.edittext_calorie_intake)).perform(typeText("3000"), closeSoftKeyboard());
        onView(withId(R.id.radio_button_custom)).perform(click());
        onView(withId(R.id.custom_macro_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.radio_button_fat_loss)).perform(click());
        onView(withId(R.id.custom_macro_layout)).check(matches(not(isDisplayed())));

        onView(withId(R.id.button_ok)).perform(click());
        onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(2, R.id.chart)).check(matches(isDisplayed()));
    }

    public void testWaterIntake() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));

        // Check dialog doesn't close
        onView(withId(R.id.button_ok)).perform(click());

        // Check unit button
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.imperial)));
        onView(withId(R.id.edittext_height_inches)).check(matches(isDisplayed()));
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.metric)));
        onView(withId(R.id.edittext_height_inches)).check(matches(not(isDisplayed())));

        // Check input fields
        onView(withId(R.id.edittext_age)).perform(typeText("20"), closeSoftKeyboard());
        onView(withId(R.id.edittext_weight)).perform(typeText("190"), closeSoftKeyboard());
        onView(withId(R.id.edittext_height)).perform(typeText("80"), closeSoftKeyboard());
        onView(withId(R.id.button_ok)).perform(click());

        // Check value is displayed
        onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(3, R.id.side_layout)).check(matches(isDisplayed()));
    }

    // todo find out why these 2 tests aren't working
    public void testIdealWeight() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));

        // Check dialog doesn't close
        onView(withId(R.id.button_ok)).perform(click());

        // Check unit button
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.imperial)));
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.metric)));

        // Check input fields
        onView(withId(R.id.edittext_height)).perform(typeText("190"), closeSoftKeyboard());
        onView(withId(R.id.button_ok)).perform(click());

        // Check value is displayed
        onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(4, R.id.side_layout)).check(matches(isDisplayed()));
    }

    public void testBMI() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(5, click()));

        // Check dialog doesn't close
        onView(withId(R.id.button_ok)).perform(click());

        // Check unit button
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.imperial)));
        onView(withId(R.id.edittext_height_inches)).check(matches(isDisplayed()));
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.metric)));
        onView(withId(R.id.edittext_height_inches)).check(matches(not(isDisplayed())));

        // Check input fields
        onView(withId(R.id.edittext_weight)).perform(typeText("180"), closeSoftKeyboard());
        onView(withId(R.id.edittext_height)).perform(typeText("100"), closeSoftKeyboard());
        onView(withId(R.id.button_ok)).perform(click());


        // Check value is displayed]
        //onView(withId(R.id.recycler_view_body)).perform(RecyclerViewActions.scrollToPosition(5));
        //onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(5, R.id.chart)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    public void testBodyfat() throws Exception {
        onView(withId(R.id.recycler_view_body)).perform(
                RecyclerViewActions.actionOnItemAtPosition(6, click()));

        // Check dialog doesn't close
        onView(withId(R.id.button_ok)).perform(click());

        // Check unit button
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.imperial)));
        onView(withId(R.id.button_unit)).perform(click());
        onView(withId(R.id.button_unit)).check(matches(withText(R.string.metric)));

//        // Check body fat type button
//        onView(withId(R.id.button_bodyfat_type)).perform(click());
//        onView(withId(R.id.button_bodyfat_type)).check(matches(withText(R.string.bf_3_point)));
//
//        // For female 3 point bodyfat, thigh, triceps and suprailiac fields should be shown
//        onView(withId(R.id.textview_thigh)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_triceps)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_suprailiac)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_pectoral)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_abdominal)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_subscapular)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_axilla)).check(matches(not(isDisplayed())));
//
//        onView(withId(R.id.radio_button_male)).perform(click());
//
//        // For male 3 point bodyfat, thigh, abdominal and pectoral fields should be shown
//        onView(withId(R.id.textview_thigh)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_abdominal)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_pectoral)).check(matches(isDisplayed()));
//        onView(withId(R.id.textview_triceps)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_suprailiac)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_subscapular)).check(matches(not(isDisplayed())));
//        onView(withId(R.id.textview_axilla)).check(matches(not(isDisplayed())));
//
//        onView(withId(R.id.button_bodyfat_type)).perform(click());
//        onView(withId(R.id.button_bodyfat_type)).check(matches(withText(R.string.bf_7_point)));

        // For male/female 7 point bodyfat, all fields should be shown
        onView(withId(R.id.textview_thigh)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_abdominal)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_pectoral)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_triceps)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_suprailiac)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_subscapular)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.textview_axilla)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // Check input fields
        onView(withId(R.id.edittext_age)).perform(typeText("18"), closeSoftKeyboard());
        onView(withId(R.id.edittext_pectoral)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_abdominal)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_thigh)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_triceps)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_subscapular)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_suprailiac)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.edittext_axilla)).perform(typeText("15"), closeSoftKeyboard());
        onView(withId(R.id.button_ok)).perform(click());


        // Check value is displayed
        //onView(withId(R.id.recycler_view_body)).perform(RecyclerViewActions.scrollToPosition(6));
        //onView(withRecyclerView(R.id.recycler_view_body).atPositionOnView(6, R.id.side_layout)).check(matches(isDisplayed()));
    }
}
