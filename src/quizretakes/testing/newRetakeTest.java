package quizretakes.testing;

import org.junit.*;
import static org.junit.Assert.*;
import quizretakes.*;
import quizretakes.exceptions.*;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Walter Portillo
 *         Date: March 2019
 * @author Devon Thyne
 *         Date: March 2019
 */
public class newRetakeTest {

    // Testing Utilities
    //----------------------------------------------------------------------------------------------------

    //store global course variable
    private courseBean course;
    private static String retakesFileName;
    private static retakes retakesList;

    /**
     * Test setup method initialize course
     */
    @Before
    public void setUp() {
        LocalDate startSkip = LocalDate.of(2019, 3, 11);
        LocalDate endSkip = LocalDate.of(2019, 3, 17);
        this.course = new courseBean(
                "swe437",
                "Software testing",
                "14",
                startSkip,
                endSkip,
                ""
        );
        retakesFileName = "data/testing/testing-quiz-retakes-swe437.xml";
    }


    // Test Methods
    //----------------------------------------------------------------------------------------------------

    // User story 3
    //==================================================

    /**
     * Test to check for a valid retakeid if negative number passed
     */
    @Test
    public void valid_retake_id_test_1() {
        JTextField retake_id_input = new JTextField("-1"); //invalid (negative)
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("15"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with an invalid retakeid input
        //should throw InvalidIdException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), "Retake id cannot be negative");
        }

    }

    /**
     * Test to check for a valid retakeid if zero passed
     */
    @Test
    public void valid_retake_id_test_2() {
        JTextField retake_id_input = new JTextField("0"); //invalid (zero)
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("15"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with an invalid retakeid input
        //should throw InvalidIdException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), ("Retake id cannot be zero"));
        }

    }

    /**
     * Test to check for if no retakeid is passed
     */
    @Test
    public void valid_retake_id_test_3() {
        JTextField retake_id_input = new JTextField(""); //invalid (nothing passed)
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("15"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with an invalid retakeid input
        //should throw InvalidIdException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), ("Retake id cannot be empty or null"));
        }

    }

    /**
     * Test to check for if invalid retakemonth passed that is out of range of months < 1-12
     */
    @Test
    public void valid_retake_month_test_1() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("-1"); //invalid (below range 1-12)
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("15"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();

        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake month cannot be negative"));
        }

    }

    /**
     * Test to check for if invalid retakemonth passed that is out of range of months > 1-12
     */
    @Test
    public void valid_retake_month_test_2() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("13"); //invalid (above range 1-12)
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("15"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Retake month must be between 1 and 12");
        }

    }

    /**
     * Test to check for if no retake month is passed
     */
    @Test
    public void valid_retake_month_test_3() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField(""); //invalid (nothing passed)
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake month cannot be empty or null"));
        }

    }

    /**
     * Test to check if no day is passed
     */
    @Test
    public void valid_retake_day_test_1() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField(""); //invalid (nothing passed)
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake day cannot be empty or null"));
        }

    }

    /**
     * Test to check for valid positive day
     */
    @Test
    public void valid_retake_day_test_2() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("-1"); //invalid (day is negative)
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Invalid retake day"));
        }

    }

    /**
     * check for day past 31
     */
    @Test
    public void valid_retake_day_test_3() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("32"); //invalid (day is past 31)
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Invalid retake day"));
        }

    }

    /**
     * check for hour if nothing passed
     */
    @Test
    public void valid_retake_time_test_1() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField(""); //invalid (time is null)
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake hour cannot be empty or null"));
        }

    }

    /**
     * time needs to be positive
     */
    @Test
    public void valid_retake_time_test_2() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("-1"); //invalid (time is negative)
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake hour cannot be negative"));
        }

    }

    /**
     * Test for time between 0 and 23
     */
    @Test
    public void valid_retake_time_test_3() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("25"); //invalid (time is not between 0 and 23)
        JTextField retake_minute_input = new JTextField("30"); //valid
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake hour must be between 0 and 23"));
        }

    }

    /**
     * Tests for null input
     */

    @Test
    public void valid_retake_time_test_4() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField(""); //invalid (time is null)
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake minute cannot be empty or null"));
        }

    }

    /**
     * Tests for invalid negative time
     */

    @Test
    public void valid_retake_time_test_5() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("-2"); //invalid (time is negative)
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake minute cannot be negative"));
        }

    }

    /**
     * Tests for time being outside 0 and 59
     */
    @Test
    public void valid_retake_time_test_6() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("60"); //invalid (time is not between 0 and 59)
        JTextField retake_location_input = new JTextField("ENGR 1101");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake minute must be between 0 and 59"));
        }

    }

    /**
     * Tests for date not being in a skip week
     */
    @Test
    public void valid_retake_date_test_1() {


    JTextField retake_id_input = new JTextField("1"); //valid
    JTextField retake_month_input = new JTextField("3"); //valid
    JTextField retake_day_input = new JTextField("11"); //invalid (Spring break)
    JTextField retake_hour_input = new JTextField("10"); //valid
    JTextField retake_minute_input = new JTextField("00"); //valid (time is not between 0 and 59)
    JTextField retake_location_input = new JTextField("ENGR 1101");
    //Attempt retake creation for a retake with invalid month input
    //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input, retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        }
        catch(Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), ("Retake cannot be scheduled during course skip week"));
        }

    }


    /**
     * Tests for invalid location
     */
    @Test
    public void valid_location_test_1() {
        JTextField retake_id_input = new JTextField("1"); //valid
        JTextField retake_month_input = new JTextField("3"); //valid
        JTextField retake_day_input = new JTextField("6"); //valid
        JTextField retake_hour_input = new JTextField("10"); //valid
        JTextField retake_minute_input = new JTextField("60"); //invalid (time is negative)
        JTextField retake_location_input= new JTextField("");
        //Attempt retake creation for a retake with invalid month input
        //should throw InvalidTimeException
        try {
            retakeBean new_retake = create_new_retake_from_form(retake_id_input,retake_location_input, retake_month_input, retake_day_input, retake_hour_input, retake_minute_input);
            fail();
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidLocationException);
            assertEquals(e.getMessage(),("Location cannot be null or empty"));
        }

    }


    // User story 4
    //==================================================

    /**
     * Testing to check for a duplicate retake ID in data file
     */
    @Test
    public void no_duplicate_retake_id_test_1(){
        retakeBean retake_test = new retakeBean(1,"Buchanan D023",3,7,10,0);
        retakesList = new retakes();
        retakesList.addRetake(retake_test);

        retakeBean invalid_retake = new retakeBean(1,"ENGR 1101", 3, 21, 15, 0);
        try{
            add_new_retake(invalid_retake);
        }
        catch(Exception e){
            assertTrue(e instanceof InvalidRetakeException);
            assertEquals(e.getMessage(), "Cannot have duplicate retake id's");
            return;
        }
        fail();
    }

    /**
     * Testing to check for a duplicate retake date in data file
     */
    @Test
    public void no_duplicate_retake_date_test_1(){
        retakeBean retake_test = new retakeBean(1,"Buchanan D023",3,7,10,0);
        retakesList = new retakes();
        retakesList.addRetake(retake_test);

        retakeBean invalid_retake = new retakeBean(2, "ENGR 1101",3,7,10,0);
        try{
            add_new_retake(invalid_retake);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidRetakeException);
            assertEquals(e.getMessage(), "Cannot have two retakes on the same date at same time");
            return;
        }
        fail();
    }

    /**
     * Test to check valid writing of retakes to test .xml data file
     * By testing two retake objects, tests simultaneously that writing can be done appending to existing data file
     */
    @Test
    public void retake_writer_test_1(){
        retakeBean retake_1 = new retakeBean(1,"Buchanan D023", 3,7,10,0);
        retakeBean retake_2 = new retakeBean(2,"ENGR 5431",3,21,10,0);
        retakesList = new retakes();
        retakes new_retakes_list = new retakes();

        try{
            add_new_retake(retake_1);
            add_new_retake(retake_2);

            retakesReader rr = new retakesReader();
            new_retakes_list = rr.read(retakesFileName);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail();
        }

        String expected_new_retakes_list_toString = "[1: Buchanan D023: 2019-03-07: THURSDAY: 10:00, 2: ENGR 5431: 2019-03-21: THURSDAY: 10:00]";

        assertEquals(expected_new_retakes_list_toString, new_retakes_list.toString());
    }



    // Method copies being tested
    //----------------------------------------------------------------------------------------------------

    //copied from quizretakes/quizschedule.java for unit testing
    private retakeBean create_new_retake_from_form(JTextField retake_id_input, JTextField retake_location_input, JTextField retake_month_input, JTextField retake_day_input, JTextField retake_hour_input, JTextField retake_minute_input) throws Exception {
        retakeBean new_retake;

        int retake_id;
        String location;
        int month;
        int day;
        int hour;
        int minute;

        //checking retake id
        if(retake_id_input.getText().equals("")){
            throw new InvalidIdException("Retake id cannot be empty or null");
        }
        else if(retake_id_input.getText().equals("0")){
            throw new InvalidIdException("Retake id cannot be zero");
        }
        else{
            try {
                retake_id = Integer.parseInt(retake_id_input.getText());
            }
            catch(Exception e){
                throw new InvalidIdException("Retake id invalid");
            }

            if(retake_id < 0){
                throw new InvalidIdException("Retake id cannot be negative");
            }

        }
        //checking location
        if(retake_location_input.getText().equals("")){
            throw new InvalidLocationException("Location cannot be null or empty");
        }
        else{
            location = retake_location_input.getText();
        }

        //checking month
        if(retake_month_input.getText().equals("")){
            throw new InvalidTimeException("Retake month cannot be empty or null");
        }
        else if(retake_month_input.getText().equals("0")){
            throw new InvalidTimeException("Retake month cannot be zero");
        }
        else{
            try {
                month = Integer.parseInt(retake_month_input.getText());
            }
            catch(Exception e){
                throw new InvalidTimeException("Invalid retake month");
            }

            if(month < 0){
                throw new InvalidTimeException("Retake month cannot be negative");
            }
            else if(month > 12){
                throw new InvalidTimeException("Retake month must be between 1 and 12");
            }
        }

        //checking day
        if(retake_day_input.getText().equals("")){
            throw new InvalidTimeException("Retake day cannot be empty or null");
        }
        else if(retake_day_input.getText().equals("0")){
            throw new InvalidTimeException("Retake day cannot be zero");
        }
        else{
            LocalDate test_date;
            try {
                day = Integer.parseInt(retake_day_input.getText());
                if (day < 0) {
                    throw new InvalidTimeException("Retake day cannot be negative");
                } else if (day > 31) {
                    throw new InvalidTimeException("Retake day must be between 1 and 31");
                }
                test_date = LocalDate.of(2019,month,day);
            }
            catch (Exception e){

                throw new InvalidTimeException("Invalid retake day");
            }
            if(test_date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || test_date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                throw new InvalidTimeException("Retake cannot be scheduled on a weekend");
            }
            else if((!(test_date.isBefore(course.getStartSkip())) && !(test_date.isAfter(course.getEndSkip()))) || test_date.isEqual(course.getStartSkip()) || test_date.isEqual(course.getEndSkip())){
                throw new InvalidTimeException("Retake cannot be scheduled during course skip week");
            }

        }

        //checking hour
        if(retake_hour_input.getText().equals("")){
            throw new InvalidTimeException("Retake hour cannot be empty or null");
        }
        else{
            try {
                hour = Integer.parseInt(retake_hour_input.getText());
            }
            catch (Exception e){
                throw new InvalidTimeException("Invalid retake hour");
            }
            if (hour < 0) {
                throw new InvalidTimeException("Retake hour cannot be negative");
            } else if (hour > 23) {
                throw new InvalidTimeException("Retake hour must be between 0 and 23");
            }

        }

        //checking minute
        if(retake_minute_input.getText().equals("")){
            throw new InvalidTimeException("Retake minute cannot be empty or null");
        }
        else{
            try {
                minute = Integer.parseInt(retake_minute_input.getText());

            }
            catch (Exception e){
                throw new InvalidTimeException("Invalid retake minute");
            }
            if (minute < 0) {
                throw new InvalidTimeException("Retake minute cannot be negative");
            } else if (minute >= 60) {
                throw new InvalidTimeException("Retake minute must be between 0 and 59");
            }

        }

        new_retake = new retakeBean(retake_id,location,month,day,hour,minute);
        return new_retake;
    }


    //copied from quizretakes/quizschedule.java for unit testing
    private static void add_new_retake(retakeBean new_retake) throws Exception{
        for(retakeBean retake : retakesList){
            if(retake.getID() == new_retake.getID()){
                throw new InvalidRetakeException("Cannot have duplicate retake id's");
            }
            if(retake.dateAsString().equals(new_retake.dateAsString())){
                throw new InvalidRetakeException("Cannot have two retakes on the same date at same time");
            }
        }

        //if traversal successful, attempt to add new retake to data file using writer class
        retakesList.addRetake(new_retake);
        retakeWriter rw = new retakeWriter();
        rw.write(retakesFileName, retakesList);
    }

}
