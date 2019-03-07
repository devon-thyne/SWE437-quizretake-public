package quizretakes.testing;

import org.junit.*;
import static org.junit.Assert.*;
import quizretakes.*;
import quizretakes.exceptions.InvalidIdException;
import quizretakes.exceptions.InvalidQuizException;
import quizretakes.exceptions.InvalidTimeException;

import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author Devon Thyne
 *         Date: March 2019
 * @author Walter Portillo
 *         Date: March 2019
 */
public class newQuizTest {

    // Testing Utilities
    //----------------------------------------------------------------------------------------------------

    //store global course variable
    private static courseBean course;
    private static String quizzesFileName;
    private static quizzes quizList;

    /**
     * Test setup method initialize course
     */
    @Before
    public void setUp(){
        LocalDate startSkip = LocalDate.of(2019, 3, 11);
        LocalDate endSkip = LocalDate.of(2019, 3, 17);
        course = new courseBean(
                "swe437",
                "Software testing",
                "14",
                startSkip,
                endSkip,
                ""
        );
        quizzesFileName = "data/testing/testing-quiz-orig-swe437.xml";
    }



    // Test Methods
    //----------------------------------------------------------------------------------------------------


    // User story 1
    //==================================================

    /**
     * Test to check for a valid quiz id if negative number passed
     */
    @Test
    public void valid_quiz_id_test_1(){
        JTextField quiz_id_input = new JTextField("-1"); //invalid (negative)
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with an invalid quiz id input
        //should throw InvalidIdException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), "Quiz id cannot be a negative number");
            return;
        }
        fail();
    }

    /**
     * Test to check for a valid quiz id if zero passed
     */
    @Test
    public void valid_quiz_id_test_2(){
        JTextField quiz_id_input = new JTextField("0"); //invalid (zero)
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with an invalid quiz id input
        //should throw InvalidIdException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), "Quiz id cannot be zero");
            return;
        }
        fail();
    }

    /**
     * Test to check for if no quiz id is passed
     */
    @Test
    public void valid_quiz_id_test_3(){
        JTextField quiz_id_input = new JTextField(""); //invalid (nothing passed)
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with an invalid quiz id input
        //should throw InvalidIdException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidIdException);
            assertEquals(e.getMessage(), "Quiz id cannot be null");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz month passed that is out of range of months < 1-12
     */
    @Test
    public void valid_quiz_month_test_1(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("-1"); //invalid (below range 1-12)
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid month input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz month invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz month passed that is out of range of months > 1-12
     */
    @Test
    public void valid_quiz_month_test_2(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("13"); //invalid (above range 1-12)
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid month input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz month invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if no quiz month is passed
     */
    @Test
    public void valid_quiz_month_test_3(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField(""); //invalid (nothing passed)
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid month input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz month cannot be null");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz day passed that is not a valid day for that month
     */
    @Test
    public void valid_quiz_day_test_1(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("2"); //valid
        JTextField quiz_day_input = new JTextField("30"); //invalid (February does not have day 30)
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid day input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz day invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if quiz day passed is on a weekend
     * No classes occur on the weekends
     */
    @Test
    public void valid_quiz_day_test_2(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("2"); //invalid (3/2/19 is a Saturday)
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid day input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz cannot be scheduled on a weekend");
            return;
        }
        fail();
    }

    /**
     * Test to check for if quiz day passed is during course skip week
     * No classes occur on the weekends
     */
    @Test
    public void valid_quiz_day_test_4(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("12"); //invalid (3/12/19 is during spring break)
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid day input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz cannot be scheduled during course skip week");
            return;
        }
        fail();
    }

    /**
     * Test to check for if no quiz day is passed
     */
    @Test
    public void valid_quiz_day_test_3(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField(""); //invalid (nothing passed)
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid month input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz day cannot be null");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz hour passed that is out of range of hours < 0-23
     */
    @Test
    public void valid_quiz_hour_test_1(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("-1"); //invalid (below range 0-23)
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid hour input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz hour invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz hour passed that is out of range of hours > 0-23
     */
    @Test
    public void valid_quiz_hour_test_2(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("24"); //invalid (above range 0-23)
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid hour input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz hour invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if no quiz hour passed
     */
    @Test
    public void valid_quiz_hour_test_3(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField(""); //invalid (no valid passed)
        JTextField quiz_minute_input = new JTextField("30"); //valid
        //Attempt quiz creation for a quiz with invalid hour input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz hour cannot be null");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz minute passed that is out of range of minutes < 0-59
     */
    @Test
    public void valid_quiz_minute_test_1(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("-1"); //invalid (below range 0-59)
        //Attempt quiz creation for a quiz with invalid minute input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz minute invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if invalid quiz minute passed that is out of range of minutes > 0-59
     */
    @Test
    public void valid_quiz_minute_test_2(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField("60"); //invalid (above range 0-59)
        //Attempt quiz creation for a quiz with invalid minute input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz minute invalid");
            return;
        }
        fail();
    }

    /**
     * Test to check for if no quiz minute passed
     */
    @Test
    public void valid_quiz_minute_test_3(){
        JTextField quiz_id_input = new JTextField("1"); //valid
        JTextField quiz_month_input = new JTextField("3"); //valid
        JTextField quiz_day_input = new JTextField("6"); //valid
        JTextField quiz_hour_input = new JTextField("10"); //valid
        JTextField quiz_minute_input = new JTextField(""); //invalid (no valid passed)
        //Attempt quiz creation for a quiz with invalid minute input
        //should throw InvalidTimeException
        try {
            quizBean new_quiz = create_new_quiz_from_form(quiz_id_input, quiz_month_input, quiz_day_input, quiz_hour_input, quiz_minute_input);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidTimeException);
            assertEquals(e.getMessage(), "Quiz minute cannot be null");
            return;
        }
        fail();
    }


    // User story 2
    //==================================================

    /**
     * Test to check for no duplicate quiz id's already in data file
     */
    @Test
    public void no_duplicate_quiz_id_test_1(){
        quizBean q1 = new quizBean(1, 3, 6, 2, 30); //valid first quiz
        quizList = new quizzes();
        quizList.addQuiz(q1);

        quizBean q2 = new quizBean(1, 3, 7, 2, 30); //invalid (duplicate quiz id to q1)
        try{
            add_new_quiz(q2);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidQuizException);
            assertEquals(e.getMessage(), "Cannot have duplicate quiz id's");
            return;
        }
        fail();
    }

    /**
     * Test to check for no duplicate quiz id's already in data file
     */
    @Test
    public void no_duplicate_quiz_date_test_1(){
        quizBean q1 = new quizBean(1, 3, 6, 2, 30); //valid first quiz
        quizList = new quizzes();
        quizList.addQuiz(q1);

        quizBean q2 = new quizBean(2, 3, 6, 2, 30); //invalid (duplicate date 3/6 to q1)
        try{
            add_new_quiz(q2);
        }
        catch (Exception e){
            assertTrue(e instanceof InvalidQuizException);
            assertEquals(e.getMessage(), "Cannot have two quizzes on the same date");
            return;
        }
        fail();
    }

    /**
     * Test to check valid writing of quizzes to test .xml data file
     * By testing two quiz objects, tests simultaneously that writing can be done appending to existing data file
     */
    @Test
    public void quiz_writer_test_1(){
        quizBean q1 = new quizBean(1, 3, 6, 2, 30); //valid
        quizBean q2 = new quizBean(2, 3, 7, 2, 30); //valid
        quizList = new quizzes();
        quizzes new_quizzes_list = new quizzes();

        try{
            add_new_quiz(q1);
            add_new_quiz(q2);

            quizReader qr = new quizReader();
            new_quizzes_list = qr.read(quizzesFileName);
        }
        catch (Exception e){
            fail();
        }

        String expected_new_quizzes_list_toString = "[1: 2019-03-06: WEDNESDAY: 02:30, 2: 2019-03-07: THURSDAY: 02:30]";

        assertEquals(expected_new_quizzes_list_toString, new_quizzes_list.toString());
    }



    // Method copies being tested
    //----------------------------------------------------------------------------------------------------

    //copied from quizretakes/quizschedule.java for unit testing
    private static quizBean create_new_quiz_from_form(JTextField quiz_id_input, JTextField quiz_month_input, JTextField quiz_day_input, JTextField quiz_hour_input, JTextField quiz_minute_input) throws Exception {
        quizBean new_quiz;

        int quiz_id;
        int month;
        int day;
        int hour;
        int minute;

        //checking ID
        if(quiz_id_input.getText().equals("")){
            throw new InvalidIdException("Quiz id cannot be null");
        }
        else if(quiz_id_input.getText().equals("0")){
            throw new InvalidIdException("Quiz id cannot be zero");
        }
        else{
            try {
                quiz_id = Integer.parseInt(quiz_id_input.getText());
            }
            catch (Exception e){
                throw new InvalidIdException("Quiz id invalid");
            }
            if(quiz_id < 0){
                throw new InvalidIdException("Quiz id cannot be a negative number");
            }
        }

        //checking month
        if(quiz_month_input.getText().equals("")){
            throw new InvalidTimeException("Quiz month cannot be null");
        }
        else if(quiz_month_input.getText().equals("0")){
            throw new InvalidTimeException("Quiz month cannot be zero");
        }
        else{
            try {
                month = Integer.parseInt(quiz_month_input.getText());
            }
            catch (Exception e){
                throw new InvalidIdException("Quiz month invalid");
            }
            if(month < 0){
                throw new InvalidTimeException("Quiz month invalid");
            }
            else if(month > 12){
                throw new InvalidTimeException("Quiz month invalid");
            }
        }

        //checking day
        if(quiz_day_input.getText().equals("")){
            throw new InvalidTimeException("Quiz day cannot be null");
        }
        else if (quiz_day_input.getText().equals("0")){
            throw new InvalidTimeException("Quiz day cannot be zero");
        }
        else{
            try {
                day = Integer.parseInt(quiz_day_input.getText());
            }
            catch (Exception e){
                throw new InvalidIdException("Quiz day invalid");
            }
            if(day < 0){
                throw new InvalidTimeException("Quiz day invalid");
            }
            else{
                LocalDate test_date;
                try {
                    //to test for skip day and legal month day
                    test_date = LocalDate.of(2019, month, day);
                }
                catch(Exception e){
                    throw new InvalidTimeException("Quiz day invalid");
                }

                if(test_date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || test_date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                    throw new InvalidTimeException("Quiz cannot be scheduled on a weekend");
                }
                else if((!(test_date.isBefore(course.getStartSkip())) && !(test_date.isAfter(course.getEndSkip()))) || test_date.isEqual(course.getStartSkip()) || test_date.isEqual(course.getEndSkip())){
                    throw new InvalidTimeException("Quiz cannot be scheduled during course skip week");
                }
            }
        }

        //checking hour
        if(quiz_hour_input.getText().equals("")){
            throw new InvalidTimeException("Quiz hour cannot be null");
        }
        else{
            try {
                hour = Integer.parseInt(quiz_hour_input.getText());
            }
            catch(Exception e){
                throw new InvalidIdException("Quiz hour invalid");
            }
            if(hour < 0){
                throw new InvalidTimeException("Quiz hour invalid");
            }
            else if(hour > 23){
                throw new InvalidTimeException("Quiz hour invalid");
            }
        }

        //checking minute
        if(quiz_minute_input.getText().equals("")){
            throw new InvalidTimeException("Quiz minute cannot be null");
        }
        else{
            try {
                minute = Integer.parseInt(quiz_minute_input.getText());
            }
            catch (Exception e){
                throw new InvalidIdException("Quiz minute invalid");
            }
            if(minute < 0){
                throw new InvalidTimeException("Quiz minute invalid");

            }
            else if(minute > 59){
                throw new InvalidTimeException("Quiz minute invalid");

            }
        }

        new_quiz = new quizBean(quiz_id,month,day,hour,minute);
        return new_quiz;
    }


    //copied from quizretakes/quizschedule.java for unit testing
    private static void add_new_quiz(quizBean new_quiz) throws Exception {
        //traverse existing quiz list to validate new_quiz before adding it to the list
        for(quizBean quiz : quizList){
            if(quiz.getID() == new_quiz.getID()){
                throw new InvalidQuizException("Cannot have duplicate quiz id's");
            }
            if(quiz.dateAsString().equals(new_quiz.dateAsString()) && quiz.timeAsString().equals(new_quiz.timeAsString())){
                throw new InvalidQuizException("Cannot have two quizzes on the same date");
            }
        }

        //if traversal successful, attempt to add new quiz to data file using writer class
        quizList.addQuiz(new_quiz);
        quizWriter qw = new quizWriter();
        qw.write(quizzesFileName, quizList);
    }

}
