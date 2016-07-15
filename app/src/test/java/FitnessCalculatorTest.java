/**
 * Created by Vincent on 8/07/2016.
 */
import com.shalskar.fitnesscalculator.Constants;
import com.shalskar.fitnesscalculator.FitnessCalculator;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FitnessCalculatorTest{

    /**
     * Suite of tests for the range of fitness calculators
     */

    @Test
    public void fitnessCalculator_OneRepMax_ReturnsTrue() {
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_EPLEY, 5, 100), 116, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_LOMBARDI, 5, 100), 117, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_BRZYCKI, 5, 100), 112, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_MAYHEW, 5, 100), 119, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_OCONNER, 5, 100), 112, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_WATHEN, 5, 100), 116, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_LANDER, 5, 100), 113, 1);

        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_EPLEY, 10, 220), 293, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_LOMBARDI, 10, 220), 276, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_BRZYCKI, 10, 220), 293, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_MAYHEW, 10, 220), 288, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_OCONNER, 10, 220), 275, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_WATHEN, 10, 220), 296, 1);
        assertEquals(FitnessCalculator.calculateRepMax(1, Constants.ONE_REP_MAX_FORMULA_LANDER, 10, 220), 294, 1);
    }

    @Test
    public void fitnessCalculator_BMI_ReturnsTrue() {
        assertEquals(FitnessCalculator.calculateBMI(80, 180), 24.7f, 0.1f);
        assertEquals(FitnessCalculator.calculateBMI(120, 150), 53.3f, 0.1f);
        assertEquals(FitnessCalculator.calculateBMI(60, 200), 15.0f, 0.1f);
    }

    @Test
    public void fitnessCalculator_Wilks_ReturnsTrue() {
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_FEMALE, 80, 540), 494, 1);
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_FEMALE, 45, 230), 319, 1);
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_FEMALE, 120, 440), 352, 1);
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_MALE, 160, 860), 471, 1);
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_MALE, 80, 680), 464, 1);
        assertEquals(FitnessCalculator.calculateWilks(Constants.GENDER_MALE, 90, 330), 211, 1);
    }

    @Test
    public void fitnessCalculator_Bodyfat_ReturnsTrue() {
        // 3 point bodyfat calculator
        assertEquals(FitnessCalculator.calculateBodyfatMale(20, 20, 20, 20), 16.8f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfatMale(15, 15, 15, 40), 14.7f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfatMale(10, 20, 30, 60), 21.4f, 0.1f);

        assertEquals(FitnessCalculator.calculateBodyfatFemale(20, 15, 10, 60), 20.9f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfatFemale(21, 33, 43, 36), 35.4f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfatFemale(15, 11, 12, 22), 16.1f, 0.1f);

        // 7 point bodyfat calculator
        assertEquals(FitnessCalculator.calculateBodyfat(20, 20, 20, 20, 20, 20, 20, Constants.GENDER_MALE, 23), 19.1f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfat(13, 18, 14, 17, 17, 12, 21, Constants.GENDER_MALE, 44), 18.1f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfat(20, 20, 20, 15, 25, 15, 20, Constants.GENDER_MALE, 35), 20, 0.1f);

        assertEquals(FitnessCalculator.calculateBodyfat(32, 21, 26, 22, 27, 22, 17, Constants.GENDER_FEMALE, 27), 30.3f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfat(16, 14, 14, 17, 19, 16, 14, Constants.GENDER_FEMALE, 31), 22.3f, 0.1f);
        assertEquals(FitnessCalculator.calculateBodyfat(19, 11, 15, 12, 14, 14, 11, Constants.GENDER_FEMALE, 66), 22.1f, 0.1f);
    }
}