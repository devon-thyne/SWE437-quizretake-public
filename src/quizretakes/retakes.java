package quizretakes;

import java.util.*;

/**
 * This class holds a collection of retakes

 * @author Jeff Offutt
 * @author Devon Thyne - Edited 2019.02.12
 */
public class retakes implements Iterable<retakeBean>{

    private ArrayList<retakeBean> retakes;

   // Constructors
   public retakes(){
      retakes = new ArrayList<retakeBean>();
   }

   public retakes(int ID, String location, int month, int day, int hour, int minute){
      retakes = new ArrayList<retakeBean>();
      retakeBean qr = new retakeBean(ID, location, month, day, hour, minute);
      retakes.add (qr);
   }

   public retakes(retakeBean qr){
      retakes = new ArrayList<retakeBean>();
      retakes.add(qr);
   }


   // sorting and iterating
   public void sort(){
      Collections.sort(retakes);
   }

   @Override
   public Iterator<retakeBean> iterator(){
       return retakes.iterator();
   }


   // adders & getters
   public void addRetake(retakeBean qr){
      retakes.add(qr);
   }

   public retakeBean getRetake(int retakeID){
      for(retakeBean r: retakes){
         if(r.getID() == retakeID){
            return r;
         }
      }
      return null;
   }

   public String toString(){
      return (Arrays.toString(retakes.toArray()));
   }

}
