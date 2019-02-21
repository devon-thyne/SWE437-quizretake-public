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
    private Boolean DEBUG = true;

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
    private appointments appointmentList;

    //critical GUI components and gathered information
    private JPanel schedule_retake_form_panel;
    private JPanel view_appointments_form_panel;
    private JTextField student_name_input_field;
    private ArrayList<JCheckBox> quiz_retake_selection;
    private ArrayList<String> quiz_retake_selection_ids;
    private boolean appts_sort_by_name_toggle = true;
    private boolean appts_sort_by_quizID_toggle = true;
    private boolean appts_sort_by_retakeID_toggle = true;


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
        setSize(1000, 785);
        setResizable(false);
        setLocation(100,50);

        //terminate java program on gui exit
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //populate class fields with necessary data to populate GUI
        get_data();

        //build menu bar for GUI
        JMenuBar menu_bar = new JMenuBar();


        //Form options toggle between different program functionality
        JMenu form_options_menu = new JMenu("Form Options");

        JMenuItem schedule_retake_form = new JMenuItem("Schedule Retake");
        schedule_retake_form.setActionCommand("show_schedule_retake_form");
        schedule_retake_form.addActionListener(this);

        JMenuItem view_appointments_form = new JMenuItem("View Scheduled Appointments");
        view_appointments_form.setActionCommand("show_view_appointments_form");
        view_appointments_form.addActionListener(this);

        form_options_menu.add(schedule_retake_form);
        form_options_menu.add(view_appointments_form);

        menu_bar.add(form_options_menu);
        setJMenuBar(menu_bar);

        //build and add form panel components and other gui content
        schedule_retake_form_panel = new JPanel();
        schedule_retake_form_panel.setPreferredSize(new Dimension(1000, 750));
        build_schedule_retake_form();

        view_appointments_form_panel = new JPanel();
        view_appointments_form_panel.setPreferredSize(new Dimension(1000, 750));
        build_view_appointments_form();

        getContentPane().add(schedule_retake_form_panel, BorderLayout.CENTER);
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
        apptsReader ar = new apptsReader();
        try {
            quizList = qr.read (quizzesFileName);
            retakesList = rr.read (retakesFileName);
            appointmentList = ar.read(apptsFileName);
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
            System.out.format("\nCourse information read from file '%s':\n", courseFileName);
            System.out.format("%s\n", course.toString());

            System.out.format("\nList of quizzes read from file '%s':\n", quizzesFileName);
            for (quizBean q : quizList) {
                System.out.format("%s\n", q.toString());
            }

            System.out.format("\nList of retakes read from file '%s':\n", retakesFileName);
            for (retakeBean r : retakesList) {
                System.out.format("%s\n", r.toString());
            }

            System.out.format("\nList of appointments read from file '%s':\n", apptsFileName);
            for (apptBean a : appointmentList) {
                System.out.format("%s\n", a.toString());
            }

            System.out.format("\n");
        }
    }


    /**
     * Method to build schedule retake form (asst2)
     */
    private void build_schedule_retake_form(){
        //clear former contents before building
        schedule_retake_form_panel = new JPanel();

        //header and intro information
        JLabel intro_label = new JLabel(String.format("GMU quiz retake scheduler for class %s", course.getCourseTitle()));
        intro_label.setHorizontalAlignment(SwingConstants.CENTER);
        schedule_retake_form_panel.add(intro_label, BorderLayout.PAGE_START);

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
        gui_left_panel.setPreferredSize(new Dimension(500, 650));
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
        gui_right_panel.setPreferredSize(new Dimension(500, 650));
        gui_right_panel.add(retake_list_label, BorderLayout.PAGE_START);
        gui_right_panel.add(retake_list_scroll_pane, BorderLayout.CENTER);

        //split content of GUI horizontally
        JPanel content_panel = new JPanel();
        content_panel.setLayout(new GridLayout(1, 2));
        content_panel.add(gui_left_panel);
        content_panel.add(gui_right_panel);
        schedule_retake_form_panel.add(content_panel);

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
        schedule_retake_form_panel.add(bottom_button_panel, BorderLayout.PAGE_END);
    }


    /**
     * Method to populate contents of student quiz retake selection form
     *
     * @param student_form_panel JPanel where the quiz retake selection form will be put
     */
    private void populate_student_form(JPanel student_form_panel){
        //set BoxLayout so each item can be placed below the next
        student_form_panel.setLayout(new BoxLayout(student_form_panel, BoxLayout.Y_AXIS));

        JLabel form_output_label;
        JLabel line_break_label;

        form_output_label = new JLabel("You can sign up for quiz retakes within the next two weeks. ");
        student_form_panel.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form_panel.add(line_break_label);

        form_output_label = new JLabel("Enter your name (as it appears on the class roster), then select");
        student_form_panel.add(form_output_label);

        form_output_label = new JLabel("which date, time, and quiz you wish to retake from the following list.");
        student_form_panel.add(form_output_label);

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
        student_form_panel.add(line_break_label);

        form_output_label = new JLabel(String.format("Today is %s, %s %s.", today.getDayOfWeek(), today.getMonth(), today.getDayOfMonth()));
        student_form_panel.add(form_output_label);

        form_output_label = new JLabel("Currently scheduling quizzes for the next two weeks, until");
        student_form_panel.add(form_output_label);

        form_output_label = new JLabel(String.format("%s, %s %s.", endDay.getDayOfWeek(), endDay.getMonth(), endDay.getDayOfMonth()));
        student_form_panel.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form_panel.add(line_break_label);

        form_output_label = new JLabel("Name:");
        student_form_panel.add(form_output_label);

        student_name_input_field = new JTextField();
        student_name_input_field.setMaximumSize(new Dimension(500, 30));
        student_form_panel.add(student_name_input_field);

        line_break_label = new JLabel(" ");
        student_form_panel.add(line_break_label);

        form_output_label = new JLabel("Quiz retakes available:");
        student_form_panel.add(form_output_label);

        line_break_label = new JLabel(" ");
        student_form_panel.add(line_break_label);

        quiz_retake_selection = new ArrayList<>();
        quiz_retake_selection_ids = new ArrayList<>();

        for(retakeBean r: retakesList) {
            LocalDate retakeDay = r.getDate();
            if (!(retakeDay.isBefore (today)) && !(retakeDay.isAfter (endDay))) {
                // if skip && retakeDay is after the skip week, print a white bg message
                if (skip && retakeDay.isAfter(origEndDay)) {  // A "skip" week such as spring break.
                    form_output_label = new JLabel("Skipping a week, no quiz or retakes.");
                    student_form_panel.add(form_output_label);
                    // Just print for the FIRST retake day after the skip week
                    skip = false;
                }
                retakePrinted = true;
                // format: Friday, January 12, at 10:00am in EB 4430
                form_output_label = new JLabel(String.format("%s, %s %s, at %s in %s", retakeDay.getDayOfWeek(), retakeDay.getMonth(), retakeDay.getDayOfMonth(), r.timeAsString(), r.getLocation()));
                student_form_panel.add(form_output_label);

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
                        student_form_panel.add(temp_checkbok);
                    }
                }

                line_break_label = new JLabel(" ");
                student_form_panel.add(line_break_label);
            }
            if (retakePrinted) {
                retakePrinted = false;
            }
        }
    }


    /**
     * Method to populate master list of all quiz retake opportunities in .xml file
     *
     * @param retake_list_panel JPanel where the quiz retakes list will be put
     */
    private void populate_retakes_list(JPanel retake_list_panel){
        //creates a vertical box to allow for text to be stacked
        Box list_box = Box.createVerticalBox();
        JLabel label;
        JPanel panel;
        for (retakeBean retake : retakesList) {
            label = new JLabel(retake.toString());
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(350, 20));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(label);
            list_box.add(panel);
            retake_list_panel.add(list_box);
        }
    }


    /**
     * Method to build the view scheduled appointments form (asst3)
     */
    private void build_view_appointments_form(){
        //clear former contents before building
        view_appointments_form_panel = new JPanel();
        view_appointments_form_panel.setPreferredSize(new Dimension(900, 700));
        view_appointments_form_panel.setLayout(new BoxLayout(view_appointments_form_panel, BoxLayout.Y_AXIS));

        //header and intro information
        JLabel intro_label = new JLabel(String.format("GMU quiz retake appointments scheduled for class %s", course.getCourseTitle()));
        intro_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        view_appointments_form_panel.add(intro_label);
        JLabel line_break = new JLabel(" ");
        view_appointments_form_panel.add(line_break);

        //sort options for below list of appointments
        Box sort_by_options_box = Box.createHorizontalBox();
        JLabel sort_by_label = new JLabel("Sort By: ");
        sort_by_options_box.add(sort_by_label);

        JButton name_button = new JButton("Name");
        name_button.setActionCommand("sort_appointments_by_namme");
        name_button.addActionListener(this);
        JButton quizID_button = new JButton("Quiz ID");
        quizID_button.setActionCommand("sort_appointments_by_quizID");
        quizID_button.addActionListener(this);
        JButton retakeID_button = new JButton("Retake ID");
        retakeID_button.setActionCommand("sort_appointments_by_retakeID");
        retakeID_button.addActionListener(this);

        sort_by_options_box.add(name_button);
        sort_by_options_box.add(quizID_button);
        sort_by_options_box.add(retakeID_button);

        view_appointments_form_panel.add(sort_by_options_box);

        //list of all scheduled quiz retake appointments
        JLabel appointments_list_label = new JLabel("List of all scheduled quiz retakes:");
        appointments_list_label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel appointment_list = new JPanel();
        populate_appointments_list(appointment_list);
        JScrollPane appointment_list_scroll_pane = new JScrollPane(appointment_list);
        appointment_list_scroll_pane.setPreferredSize(new Dimension(800, 575));
        appointment_list_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        appointment_list_scroll_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel gui_center_panel = new JPanel();
        gui_center_panel.setPreferredSize(new Dimension(800, 650));
        gui_center_panel.add(appointments_list_label, BorderLayout.PAGE_START);
        gui_center_panel.add(appointment_list_scroll_pane, BorderLayout.CENTER);

        JPanel content_panel = new JPanel();
        content_panel.setPreferredSize(new Dimension(800, 650));
        content_panel.add(gui_center_panel);
        view_appointments_form_panel.add(content_panel);

        //bottom button panel
        JButton close_button = new JButton("Close");
        close_button.setActionCommand("close_form");
        close_button.addActionListener(this);
        close_button.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel bottom_button_panel = new JPanel();
        bottom_button_panel.add(close_button, BorderLayout.CENTER);
        view_appointments_form_panel.add(bottom_button_panel);
    }


    /**
     * Method to populate master list of all scheduled quiz retake appointments in .txt file
     *
     * @param appointment_list_panel JPanel where the appointment list will be put
     */
    private void populate_appointments_list(JPanel appointment_list_panel){
        //creates a vertical box to allow for text to be stacked
        Box list_box = Box.createVerticalBox();

        Box row_box = Box.createHorizontalBox();

        JLabel label;
        JPanel panel;

        quizBean quiz;
        retakeBean retake;
        //String appointment_string;

        for (apptBean appt : appointmentList){
            quiz = quizList.getQuiz(appt.getQuizID());
            retake = retakesList.getRetake(appt.getRetakeID());

            label = new JLabel(appt.getName());
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(150, 20));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(label);
            row_box.add(panel);

            label = new JLabel(String.format("Quiz #%s", quiz.toString()));
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(250, 20));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(label);
            row_box.add(panel);

            label = new JLabel(String.format("Retake #%s", retake.toString()));
            panel = new JPanel();
            panel.setPreferredSize(new Dimension(350, 20));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(label);
            row_box.add(panel);

            list_box.add(row_box);
            row_box = Box.createHorizontalBox();
        }
        appointment_list_panel.add(list_box);
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
                submit_retake_selection_form();
                break;
            case ("close_form"):
                if(DEBUG){
                    System.out.format("Close form button pressed.\n");
                }
                System.exit(0);
                break;
            case("show_schedule_retake_form"):
                if(DEBUG){
                    System.out.format("Show schedule retake form menu option selected.\n");
                }
                schedule_retake_form_panel.setVisible(false);
                view_appointments_form_panel.setVisible(false);
                get_data();
                build_schedule_retake_form();
                getContentPane().add(schedule_retake_form_panel, BorderLayout.CENTER);
                schedule_retake_form_panel.setVisible(true);
                break;
            case("show_view_appointments_form"):
                if(DEBUG){
                    System.out.format("View appointments form menu option selected.\n");
                }
                view_appointments_form_panel.setVisible(false);
                schedule_retake_form_panel.setVisible(false);
                get_data();
                build_view_appointments_form();
                getContentPane().add(view_appointments_form_panel, BorderLayout.CENTER);
                view_appointments_form_panel.setVisible(true);
                break;
            case("sort_appointments_by_namme"):
                view_appointments_form_panel.setVisible(false);
                if(appts_sort_by_name_toggle){
                    if(DEBUG){
                        System.out.format("Sorting appointments by name in alphabetical order.\n");
                    }
                    appointmentList.sortByNameAsc();
                    appts_sort_by_name_toggle = false;
                }
                else{
                    if(DEBUG){
                        System.out.format("Sorting appointments by name in reverse alphabetical order.\n");
                    }
                    appointmentList.sortByNameDesc();
                    appts_sort_by_name_toggle = true;
                }
                build_view_appointments_form();
                getContentPane().add(view_appointments_form_panel, BorderLayout.CENTER);
                view_appointments_form_panel.setVisible(true);
                break;
            case("sort_appointments_by_quizID"):
                view_appointments_form_panel.setVisible(false);
                if(appts_sort_by_quizID_toggle){
                    if(DEBUG){
                        System.out.format("Sorting appointments by quiz ID in order low to high.\n");
                    }
                    appointmentList.sortByQuizIDAsc();
                    appts_sort_by_quizID_toggle = false;
                }
                else{
                    if(DEBUG){
                        System.out.format("Sorting appointments by quiz ID in order high to low.\n");
                    }
                    appointmentList.sortByQuizIDDesc();
                    appts_sort_by_quizID_toggle = true;
                }
                build_view_appointments_form();
                getContentPane().add(view_appointments_form_panel, BorderLayout.CENTER);
                view_appointments_form_panel.setVisible(true);
                break;
            case("sort_appointments_by_retakeID"):
                view_appointments_form_panel.setVisible(false);
                if(appts_sort_by_retakeID_toggle){
                    if(DEBUG){
                        System.out.format("Sorting appointments by retake ID in order low to high.\n");
                    }
                    appointmentList.sortByRetakeIDAsc();
                    appts_sort_by_retakeID_toggle = false;
                }
                else{
                    if(DEBUG){
                        System.out.format("Sorting appointments by retake ID in order high to low.\n");
                    }
                    appointmentList.sortByRetakeIDDesc();
                    appts_sort_by_retakeID_toggle = true;
                }
                build_view_appointments_form();
                getContentPane().add(view_appointments_form_panel, BorderLayout.CENTER);
                view_appointments_form_panel.setVisible(true);
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
    private void submit_retake_selection_form(){
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
                    clear_retake_selection_form();
                }
                else {
                    String message = studentName + ", your appointments have been scheduled.\n" +
                            "Please arrive in time to finish the quiz before the end of the retake period.\n" +
                            "If you cannot make it, please cancel by sending email to your professor.";
                    if(DEBUG){
                        System.out.format("%s\n", IOerrMessage);
                    }
                    alert_user(message);
                    clear_retake_selection_form();
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
     * Clears contents of retake selection form
     */
    private void clear_retake_selection_form(){
        student_name_input_field.setText(""); //clear student name input

        //traverse all checkboxes and uncheck them
        for (JCheckBox ckbx : quiz_retake_selection) {
            ckbx.setSelected(false);
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
