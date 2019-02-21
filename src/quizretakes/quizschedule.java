package quizretakes;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import java.time.*;
import java.lang.Long;
import java.lang.String;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * @author Jeff Offutt
 *         Date: January, 2019
 * @author Devon Thyne
 *         Date: January 2019
 * @author Walter Portillo
 *         Date: January 2019
 *
 * Wiring the pieces together:
 *    quizschedule.java -- Servlet entry point for students to schedule quizzes
 *    quizReader.java -- reads XML file and stores in quizzes.
                             Used by quizschedule.java
 *    quizzes.java -- A list of quizzes from the XML file
 *                    Used by quizschedule.java
 *    quizBean.java -- A simple quiz bean
 *                      Used by quizzes.java and readQuizzesXML.java
 *    retakesReader.java -- reads XML file and stores in retakes.
                             Used by quizschedule.java
 *    retakes.java -- A list of retakes from the XML file
 *                    Used by quizschedule.java
 *    retakeBean.java -- A simple retake bean
 *                      Used by retakes.java and readRetakesXML.java
 *    apptBean.java -- A bean to hold appointments

 *    quizzes.xml -- Data file of when quizzes were given
 *    retakes.xml -- Data file of when retakes are given
 */
public class quizschedule extends JFrame implements ActionListener{

    //flag to turn debugging features on/off
    private Boolean DEBUG = false;

    //pre-defined program data
    private static final String dataLocation = "data/";
    static private final String separator = ",";
    private static final String courseBase = "course";
    private static final String quizzesBase = "quiz-orig";
    private static final String retakesBase = "quiz-retakes";
    private static final String apptsBase = "quiz-appts";
    // Passed as parameter and stored in course.xml file (format: "swe437")
    private String courseID = "swe437";
    // Stored in course.xml file, default 14
    // Number of days a retake is offered after the quiz is given
    private int daysAvailable = 14;

    // Filenames to be built from above and the courseID parameter
    private String courseFileName;
    private String quizzesFileName;
    private String retakesFileName;
    private String apptsFileName;

    //stored information gathered from .xml files
    private courseBean course;
    private quizzes quizList;
    private retakes retakesList;

    //critical GUI components and gathered information
    private JTextField student_name_input_field;
    private ArrayList<JCheckBox> quiz_retake_selection = new ArrayList<>();
    private ArrayList<String> quiz_retake_selection_ids = new ArrayList<>();


    /**
     * Java main method
     *
     * @param args passed command line arguments to program should be empty
     */
    public static void main(String[] args){
        new quizschedule().setVisible(true);
    }


    /**
     * Constructs JFrame GUI for program
     */
    private quizschedule(){
        super("Quiz Retakes");
        setSize(1000, 750);
        setResizable(false);
        setLocation(100,50);

        //terminate java program on gui exit
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //populate class fields with necessary data to populate GUI
        get_data();

        //header and intro information
        JLabel intro_label = new JLabel(String.format("GMU quiz retake scheduler for class %s", course.getCourseTitle()));
        intro_label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(intro_label, BorderLayout.PAGE_START);

        //student quiz retake selection form
        JLabel student_form_label = new JLabel("Retake Selection Form:");
        student_form_label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel student_form = new JPanel();
        populate_student_form(student_form);
        JScrollPane student_form_scroll_pane = new JScrollPane(student_form);
        student_form_scroll_pane.setPreferredSize(new Dimension(400, 625));
        student_form_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        student_form_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel gui_left_panel = new JPanel();
        gui_left_panel.add(student_form_label, BorderLayout.PAGE_START);
        gui_left_panel.add(student_form_scroll_pane, BorderLayout.CENTER);

        //list of all available quiz retakes
        JLabel retake_list_label = new JLabel("List of all quiz retakes:");
        retake_list_label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel retake_list = new JPanel();
        populate_retakes_list(retake_list);
        JScrollPane retake_list_scroll_pane = new JScrollPane(retake_list);
        retake_list_scroll_pane.setPreferredSize(new Dimension(400, 625));
        retake_list_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        retake_list_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel gui_right_panel = new JPanel();
        gui_right_panel.add(retake_list_label, BorderLayout.PAGE_START);
        gui_right_panel.add(retake_list_scroll_pane, BorderLayout.CENTER);

        //split content of GUI horizontally
        JPanel content_panel = new JPanel();
        content_panel.setLayout(new GridLayout(1, 2));
        content_panel.add(gui_left_panel);
        content_panel.add(gui_right_panel);
        getContentPane().add(content_panel);

        //bottom button panel
        JButton submit_button = new JButton("Submit");
        submit_button.setActionCommand("student_form_submit");
        submit_button.addActionListener(this);
        submit_button.setHorizontalAlignment(SwingConstants.CENTER);

        JButton close_button = new JButton("Close");
        close_button.setActionCommand("close_form");
        close_button.addActionListener(this);
        close_button.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottom_button_panel = new JPanel();
        bottom_button_panel.add(submit_button, BorderLayout.CENTER);
        bottom_button_panel.add(close_button, BorderLayout.CENTER);
        getContentPane().add(bottom_button_panel, BorderLayout.PAGE_END);
    }


    /**
     * Method to gather in all necessary data from .xml files and populate global fields
     */
    private void get_data(){
        //get course information form .xml file
        courseReader cr = new courseReader();
        courseFileName = dataLocation + courseBase + "-" + courseID + ".xml";
        try {
            course = cr.read(courseFileName);
        }
        catch (Exception e) {
            String message = String.format("Can't find the course file (%s) for course ID '%s'. Current working directory is: %s\n", courseFileName, course, System.getProperty("user.dir"));
            if(DEBUG){
                System.out.format("ERROR: %s", message);
            }
            alert_user(message);
            throw new RuntimeException(message);
        }
        daysAvailable = Integer.parseInt(course.getRetakeDuration());

        // Filenames to be built from above and the courseID
        quizzesFileName = dataLocation + quizzesBase + "-" + courseID + ".xml";
        retakesFileName = dataLocation + retakesBase + "-" + courseID + ".xml";
        apptsFileName   = dataLocation + apptsBase   + "-" + courseID + ".txt";

        // Load the quizzes and the retake times from disk
        quizReader qr = new quizReader();
        retakesReader rr = new retakesReader();
        try {
            quizList = qr.read (quizzesFileName);
            retakesList = rr.read (retakesFileName);
        }
        catch (Exception e) {
            String message = String.format("Can't find the data files (%s, %s) for course ID '%s'. Current working directory is: %s\n", quizzesFileName, retakesFileName, course, System.getProperty("user.dir"));
            if(DEBUG){
                System.out.format("ERROR: %s", message);
            }
            alert_user(message);
            throw new RuntimeException(message);
        }

        if(DEBUG){
            //output all gathered data to console for debug purposes
            System.out.format("Course information read from file '%s':\n", courseFileName);
            System.out.format("%s\n", course.toString());

            System.out.format("List of quizzes read from file '%s':\n", quizzesFileName);
            for (quizBean q : quizList) {
                System.out.format("%s\n", q.toString());
            }

            System.out.format("List of retakes read from file '%s':\n", retakesFileName);
            for (retakeBean r : retakesList) {
                System.out.format("%s\n", r.toString());
            }
        }
    }


    /**
     * Method to populate contents of student quiz retake selection form
     *
     * @param student_form JPanel where the quiz retake selection form will be put
     */
    private void populate_student_form(JPanel student_form){
        //set BoxLayout so each item can be placed below the next
        student_form.setLayout(new BoxLayout(student_form, BoxLayout.Y_AXIS));

        JLabel form_output_label;
        JLabel line_break_label;

        form_output_label = new JLabel("You can sign up for quiz retakes within the next two weeks. ");
        student_form.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form.add(line_break_label);

        form_output_label = new JLabel("Enter your name (as it appears on the class roster), then select");
        student_form.add(form_output_label);

        form_output_label = new JLabel("which date, time, and quiz you wish to retake from the following list.");
        student_form.add(form_output_label);

        // Check for a week to skip
        boolean skip = false;
        LocalDate startSkip = course.getStartSkip();
        LocalDate endSkip   = course.getEndSkip();
        boolean retakePrinted = false;

        LocalDate today  = LocalDate.now();
        LocalDate endDay = today.plusDays(new Long(daysAvailable));
        LocalDate origEndDay = endDay;
        // if endDay is between startSkip and endSkip, add 7 to endDay
        if (!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip))
        {  // endDay is in a skip week, add 7 to endDay
            endDay = endDay.plusDays(new Long(7));
            skip = true;
        }

        line_break_label = new JLabel(" ");
        student_form.add(line_break_label);

        form_output_label = new JLabel(String.format("Today is %s, %s %s.", today.getDayOfWeek(), today.getMonth(), today.getDayOfMonth()));
        student_form.add(form_output_label);

        form_output_label = new JLabel("Currently scheduling quizzes for the next two weeks, until");
        student_form.add(form_output_label);

        form_output_label = new JLabel(String.format("%s, %s %s.", endDay.getDayOfWeek(), endDay.getMonth(), endDay.getDayOfMonth()));
        student_form.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form.add(line_break_label);

        form_output_label = new JLabel("Name:");
        student_form.add(form_output_label);

        student_name_input_field = new JTextField();
        student_name_input_field.setMaximumSize(new Dimension(500, 30));
        student_form.add(student_name_input_field);

        line_break_label = new JLabel(" ");
        student_form.add(line_break_label);

        form_output_label = new JLabel("Quiz retakes available:");
        student_form.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form.add(line_break_label);

        for(retakeBean r: retakesList) {
            LocalDate retakeDay = r.getDate();
            if (!(retakeDay.isBefore (today)) && !(retakeDay.isAfter (endDay))) {
                // if skip && retakeDay is after the skip week, print a white bg message
                if (skip && retakeDay.isAfter(origEndDay)) {  // A "skip" week such as spring break.
                    form_output_label = new JLabel("Skipping a week, no quiz or retakes.");
                    student_form.add(form_output_label);
                    // Just print for the FIRST retake day after the skip week
                    skip = false;
                }
                retakePrinted = true;
                // format: Friday, January 12, at 10:00am in EB 4430
                form_output_label = new JLabel(String.format("%s, %s %s, at %s in %s", retakeDay.getDayOfWeek(), retakeDay.getMonth(), retakeDay.getDayOfMonth(), r.timeAsString(), r.getLocation()));
                student_form.add(form_output_label);

                for(quizBean q: quizList) {
                    LocalDate quizDay = q.getDate();
                    LocalDate lastAvailableDay = quizDay.plusDays(new Long(daysAvailable));
                    // To retake a quiz on a given retake day, the retake day must be within two ranges:
                    // quizDay <= retakeDay <= lastAvailableDay --> (!quizDay > retakeDay) && !(retakeDay > lastAvailableDay)
                    // today <= retakeDay <= endDay --> !(today > retakeDay) && !(retakeDay > endDay)
                    if (!quizDay.isAfter(retakeDay) && !retakeDay.isAfter(lastAvailableDay) && !today.isAfter(retakeDay) && !retakeDay.isAfter(endDay)) {
                        JCheckBox temp_checkbok = new JCheckBox(String.format("Quiz %s from %s, %s %s", q.getID(), quizDay.getDayOfWeek(), quizDay.getMonth(), quizDay.getDayOfMonth()));
                        quiz_retake_selection.add(temp_checkbok);
                        quiz_retake_selection_ids.add(r.getID() + separator + q.getID());
                        student_form.add(temp_checkbok);
                    }
                }

                line_break_label = new JLabel(" ");
                student_form.add(line_break_label);
            }
            if (retakePrinted) {
                retakePrinted = false;
            }
        }
    }


    /**
     * Method to populate master list of all quiz retake opportunities in .xml files
     *
     * @param retake_list JPanel where the quiz retakes list will be put
     */
    private void populate_retakes_list(JPanel retake_list){
        //creates a vertical box to allow for text to be stacked
        Box box = Box.createVerticalBox();
        JLabel label;
        for (retakeBean r : retakesList) {
            label = new JLabel(r.toString());
            box.add(label);
            retake_list.add(box);
        }
    }


    /**
     * Serves as the mediator between all GUI components and their respective methods to perform their necessary actions
     *
     * @param event the action performed by the user
     */
    @Override
    public void actionPerformed(ActionEvent event){
        String action = event.getActionCommand();
        switch (action){
            case ("student_form_submit"):
                if(DEBUG){
                    System.out.format("Student form submit button pressed.\n");
                    System.out.format("Student Name: %s\n", student_name_input_field.getText());
                    System.out.format("Execute form submission function.\n");
                }
                submit_form();
                break;
            case ("close_form"):
                if(DEBUG){
                    System.out.format("Close form button pressed\n");
                }
                System.exit(0);
                break;
            default:
                if(DEBUG){
                    System.out.format("User performed unsupported action: '%s'\n", action);
                }
        }
    }


    /**
     * Saves selected appointments in a file and displays an acknowledgement
     */
    private void submit_form(){
        // No saving if IOException
        boolean IOerrFlag = false;
        String IOerrMessage = "";

        // Filename to be built from above and the courseID
        String apptsFileName   = dataLocation + apptsBase + "-" + courseID + ".txt";

        // Get name and list of retake requests from parameters
        String studentName = student_name_input_field.getText();
        String[] allIDs = get_retake_ids();

        if(allIDs != null && studentName != null && studentName.length() > 0) {
            // Append the new appointment to the file
            try {
                File file = new File(apptsFileName);
                synchronized(file) { // Only one student should touch this file at a time.
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
                    BufferedWriter bw = new BufferedWriter(fw);

                    for(String oneIDPair : allIDs) {
                        bw.write(oneIDPair + separator + studentName + "\n");
                    }

                    bw.flush();
                    bw.close();
                } // end synchronize block
            } catch (IOException e) {
                IOerrFlag = true;
                IOerrMessage = "I failed and could not save your appointment.\n" + e;
            }

            // Respond to the student
            if (IOerrFlag) {
                if(DEBUG){
                    System.out.format("%s\n", IOerrMessage);
                }
                alert_user(IOerrMessage);
            }
            else {
                if (allIDs.length == 1) {
                    String message = studentName + ", your appointment has been scheduled.";
                    if(DEBUG){
                        System.out.format("%s\n", IOerrMessage);
                    }
                    alert_user(message);
                }
                else {
                    String message = studentName + ", your appointments have been scheduled.\n" +
                            "Please arrive in time to finish the quiz before the end of the retake period.\n" +
                            "If you cannot make it, please cancel by sending email to your professor.";
                    if(DEBUG){
                        System.out.format("%s\n", IOerrMessage);
                    }
                    alert_user(message);
                }
            }

        }
        else { // allIDs == null or name is null
            if(allIDs == null) {
                String message = "You didn't choose any quizzes to retake.";
                if(DEBUG){
                    System.out.format("%s\n", message);
                }
                alert_user(message);
            }
            if(studentName == null || studentName.length() == 0) {
                String message = "You didn't give a name ... no anonymous quiz retakes.";
                if(DEBUG){
                    System.out.format("%s\n", message);
                }
                alert_user(message);
            }
        }
    }


    /**
     * Returns a string array of all retake ideas in ArrayList of checked off retakes upon form submission
     */
    private String[] get_retake_ids(){
        if(DEBUG){
            System.out.format("Checked retakes:\n");
        }

        //first count the number of retakes are checked off in the form
        int num_checked = 0;
        for(JCheckBox ckbx : quiz_retake_selection) {
            if(ckbx.isSelected()){
                num_checked++;
            }
        }

        //if no retakes selected return null
        if(num_checked == 0){
            return null;
        }
        else {
            //construct string array of retake_id and quiz_id pairings of only selected retakes
            String[] retake_ids = new String[num_checked];
            int count = 0;
            String ids_string;
            for (int i = 0; i < quiz_retake_selection.size(); i++) {
                ids_string = quiz_retake_selection_ids.get(i);
                if (quiz_retake_selection.get(i).isSelected()) {
                    retake_ids[count] = ids_string;
                    count++;
                    if (DEBUG) {
                        System.out.format("%s\n", ids_string);
                    }
                }
            }
            return retake_ids;
        }
    }


    /**
     * Alerts the user of passed message through popup dialog
     *
     * @param message message to be displayed in the popup to the user
     */
    private void alert_user(String message){
        JOptionPane.showMessageDialog(null, message);
    }

} // end quizschedule class
