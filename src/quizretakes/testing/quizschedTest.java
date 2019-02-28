package quizretakes.testing;

import org.junit.*;
import quizretakes.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;


/**
 * This will be testing the quizsched.java class
 * specifically the printQuizScheduleForm() method
 *
 * Note: Using JUnit4
 *
 * @author Walter Portillo
 * @author Devon Thyne
 *
 */
public class quizschedTest {

    // Data files
    private static final String dataLocation = "data/"; /* CLI */
    private static final String separator    = ",";
    private static final String courseBase   = "course";
    private static final String quizzesBase  = "quiz-orig";
    private static final String retakesBase  = "quiz-retakes";
    private static final String apptsBase    = "quiz-appts";

    // Filenames to be built from above and the courseID
    private static String courseFileName;
    private static String quizzesFileName;
    private static String retakesFileName;
    private static String apptsFileName;

    // Stored in course.xml file, default 14
    // Number of days a retake is offered after the quiz is given
    private static int daysAvailable = 14;

    //used for populating the parameters
    //of printScheduleQuizForm
    private quizzes quizList;
    private retakes retakeList;
    private courseBean course;

    //adding since testing System.out.println
    private final PrintStream origOut = System.out;
    private final PrintStream origErr = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();



    // Test Housekeeping
    //----------------------------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception {
        //reconfigure System outputs to be captured by tests
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        buildFileNames("swe437");
        course = readCourseFile("swe437");
        quizList = readQuizzes("swe437");
        retakeList = readRetakes("swe437");
    }


    @After
    public void clockOut(){
        //reconfigure System outputs back to default after test completion
        System.setOut(origOut);
        System.setErr(origErr);
    }



    // Tests
    //----------------------------------------------------------------------------------------------------

    @Test
    public void printFormTest1(){
        //made new quizzes with different times that do not line up with classes to
        //test how it behaves with different times
        quizBean quizOne = new quizBean(1,2,25,12,0); //controllability
        quizBean quizTwo = new quizBean(2,3,1,12,0); //controllability
        quizBean quizThree = new quizBean(3,3,7,10,0); //controllability
        quizzes quizTest = new quizzes();
        quizTest.addQuiz(quizOne);quizTest.addQuiz(quizTwo);quizTest.addQuiz(quizThree);

        //Used a different location to test whether or not it would be added
        //tested with new dates different from XML files
        retakeBean retakeOne = new retakeBean(1,"Robinson A",3,4,15,0); //controllability
        retakeBean retakeTwo = new retakeBean(2,"Buchanan D002",3,6,10,0); //controllability
        retakeBean retakeThree = new retakeBean(3,"ENGR 5431", 3, 7, 12,0); //controllability
        retakeBean retakeFour = new retakeBean(4,"AB 344", 3, 12,15,30); //controllability
        retakeBean retakeFive = new retakeBean(5,"ENGR 1101", 3, 13, 12,30); //controllability
        retakeBean retakeSix = new retakeBean(6,"Buchanan D002",3,14,10,0); //controllability

        retakes retakeTest = new retakes();
        retakeTest.addRetake(retakeOne);retakeTest.addRetake(retakeTwo);retakeTest.addRetake(retakeThree);
        retakeTest.addRetake(retakeFour);retakeTest.addRetake(retakeFive);retakeTest.addRetake(retakeSix);

        quizsched.printQuizScheduleForm(quizTest,retakeTest,course);

        assertThat(outContent.toString(), containsString("Quiz 1 from MONDAY, FEBRUARY 25")); //observability
        assertThat(outContent.toString(), containsString("Quiz 2 from FRIDAY, MARCH 1")); //observability
        assertThat(outContent.toString(), containsString("Quiz 3 from THURSDAY, MARCH 7")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: MONDAY, MARCH 4, at 15:00 in Robinson A")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: WEDNESDAY, MARCH 6, at 10:00 in Buchanan D002")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: THURSDAY, MARCH 7, at 12:00 in ENGR 5431")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: TUESDAY, MARCH 12, at 15:30 in AB 344")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: WEDNESDAY, MARCH 13, at 12:30 in ENGR 1101")); //observability
        assertThat(outContent.toString(), containsString("RETAKE: THURSDAY, MARCH 14, at 10:00 in Buchanan D002")); //observability
    }

    //incase somehow you have an empty XML File
    //this should be accounted for
    @Test
    public void printFormTest2(){
        //empty list of quizzes
        quizzes testQuiz = new quizzes(); //controllability

        //empty list of retakes
        retakes testRetake = new retakes(); //controllability

        quizsched.printQuizScheduleForm(testQuiz,testRetake,course);

        //since nothing should be printed besides the welcome message,
        //looking for keywords RETAKE or Quiz not to be printed
        assertFalse(outContent.toString(), containsString("RETAKE").equals(false)); //observability
        assertFalse(outContent.toString(), containsString("Quiz").equals(false)); //observability
    }

    @Test
    public void skipWeekTest1(){
        //this is being used to switch course specs around
        //without switching the field course around
        courseBean testCourse = course;

        //testing the skip days using the dates of Spring Break as that is a week
        //that needs to be skipped
        LocalDate testStartSkipDate = LocalDate.of(2019,3,11); //controllability
        LocalDate testEndSkipDate = LocalDate.of(2019,3,15); //controllability

        //since skipping a week will add 7 days to the original 14
        //days that are allowed for retake scheduling
        //also helps with testing on a different day
        LocalDate testDate = LocalDate.now().plusDays(21);

        testCourse.setStartSkip(testStartSkipDate); //controllability
        testCourse.setEndSkip(testEndSkipDate); //controllability

        quizsched.printQuizScheduleForm(quizList,retakeList,testCourse);

        //looking at three weeks away
        //formatted so it can be tested on other days
        assertThat(outContent.toString(), containsString(
                "Currently scheduling quizzes for the next two weeks, " +
                        "until " + testDate.getDayOfWeek() + ", " +
                        testDate.getMonth() + " " + testDate.getDayOfMonth())); //observability

    }

    //Testing that if not skipping a week
    //it should be normal 2 weeks instead of 3
    //skip days were automatically set to the first week of school
    @Test
    public void skipWeekTest2(){
        //this is being used to switch course specs around
        //without switching the field course around
        courseBean testCourse = course;

        //rescheduling only goes up until 2 weeks
        LocalDate testDate = LocalDate.now().plusDays(14);

        quizsched.printQuizScheduleForm(quizList,retakeList,testCourse);

        //looking for two weeks away since no skip
        //formatted so it can be tested on other days
        assertThat(outContent.toString(), containsString(
                "Currently scheduling quizzes for the next two weeks, " +
                        "until " + testDate.getDayOfWeek() + ", " +
                        testDate.getMonth() + " " + testDate.getDayOfMonth())); //observability
    }



    // Imported helper methods from quizsched.java
    // Assumed to be properly implemented and tested
    //----------------------------------------------------------------------------------------------------

    private static void buildFileNames(String courseID){
        courseFileName  = dataLocation + courseBase  + "-" + courseID + ".xml";
        quizzesFileName = dataLocation + quizzesBase + "-" + courseID + ".xml";
        retakesFileName = dataLocation + retakesBase + "-" + courseID + ".xml";
        apptsFileName   = dataLocation + apptsBase   + "-" + courseID + ".txt";
    }

    private static courseBean readCourseFile(String courseID) throws Exception{
        courseBean course;
        courseReader cr = new courseReader();
        course          = cr.read(courseFileName);
        return(course);
    }

    private static quizzes readQuizzes(String courseID) throws Exception{
        quizzes quizList = new quizzes();
        quizReader qr    = new quizReader();
        quizList         = qr.read(quizzesFileName);
        return(quizList);
    }

    private static retakes readRetakes(String courseID) throws Exception{
        retakes retakesList = new retakes();
        retakesReader rr    = new retakesReader();
        retakesList         = rr.read(retakesFileName);
        return(retakesList);
    }
}