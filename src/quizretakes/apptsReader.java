// JO, 6-Jan-2019
// Readsappointments
// Stores in a ArrayList and returns

package quizretakes;

import java.lang.*;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * @author Jeff Offutt - Original
 * @autho Devon Thyne - Edited 2019.02.12
 */

public class apptsReader{

    static private final String separator = ",";

    public appointments read(String filename) throws IOException{
        // read appointments file
        appointments appts = new appointments();
        apptBean a;
        File file = new File(filename);
        if(!file.exists()){
//            throw new IOException ("No appointments to read.");
        }
        else{
            FileReader fw = new FileReader(file.getAbsoluteFile());
            BufferedReader bw = new BufferedReader(fw);

            String line;
            while((line = bw.readLine()) != null){
                String[] s = line.split(separator);
                a = new apptBean (Integer.parseInt(s[0]), Integer.parseInt(s[1]), s[2]);
                appts.addAppointment(a);
            }
            bw.close();
        }
        return (appts);
    } // end read

} // end class
