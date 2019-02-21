package quizretakes;

import java.util.*;

/**
 * This class holds a collection of appointments

 * @author Devon Thyne
 */
public class appointments implements Iterable<apptBean>{

    private ArrayList<apptBean> appoitments;

    // Constructors
    public appointments(){
        appoitments = new ArrayList<apptBean>();
    }

    public appointments(int retakeID, int quizID, String name){
        appoitments = new ArrayList<apptBean>();
        apptBean appointment = new apptBean(retakeID, quizID, name);
        appoitments.add(appointment);
    }

    public appointments(apptBean appointment){
        appoitments = new ArrayList<apptBean>();
        appoitments.add(appointment);
    }


    // Methods

    /**
     * Sorts the appointments by name in alphabetical order
     */
    public void sortByNameAsc() {
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt1.getName().compareTo(appt2.getName());
            }
        });
    }

    /**
     * Sorts the appointments by name in reverse alphabetical order
     */
    public void sortByNameDesc() {
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt2.getName().compareTo(appt1.getName());
            }
        });
    }

    /**
     * Sort by the RetakeID from low to high
     */
   public void sortByRetakeIDAsc(){
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt1.getRetakeID() - appt2.getRetakeID();
            }
        });
   }

    /**
     * Sort by the RetakeID from high to low
     */
    public void sortByRetakeIDDesc(){
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt2.getRetakeID() - appt1.getRetakeID();
            }
        });
    }

    /**
     * Sort by the quiz ID  from low to high
     */
   public void sortByQuizIDAsc(){
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt1.getQuizID() - appt2.getQuizID();
            }
        });
   }

    /**
     * Sort by the quiz ID  from high to low
     */
    public void sortByQuizIDDesc(){
        Collections.sort(appoitments, new Comparator<apptBean>() {
            @Override
            public int compare(apptBean appt1, apptBean appt2) {
                return appt2.getQuizID() - appt1.getQuizID();
            }
        });
    }

    @Override
    public Iterator<apptBean> iterator(){
        return appoitments.iterator();
    }

    // adders & getters
    public void addAppointment(apptBean appointment){
        appoitments.add(appointment);
    }

    public String toString(){
        return (Arrays.toString(appoitments.toArray()));
    }

}
