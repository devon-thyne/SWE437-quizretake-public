// JO, 3-Jan-2019
// Checking XML and reading with DOM parser
// No xsd or validation as yet

package quizretakes;

import java.lang.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

/**
 * @author Walter Portillo
 *         Date: March 2019
 * @author Devon Thyne
 *         Date: March 2019
 */
public class retakeWriter
{

    public void write (String filename, retakes retakesList) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        //root element
        Element root_element = document.createElement("retakes");
        document.appendChild(root_element);

        for (retakeBean retake : retakesList) {
            Element retake_element = document.createElement("retake");
            root_element.appendChild(retake_element);

                Element retake_id_element = document.createElement("id");
                retake_id_element.appendChild(document.createTextNode(retake.getIDStr()));
                retake_element.appendChild(retake_id_element);

                Element retake_location_element = document.createElement("location");
                retake_location_element.appendChild(document.createTextNode(retake.getLocation()));
                retake_element.appendChild(retake_location_element);

                Element retake_date_given_element = document.createElement("dateGiven");
                retake_element.appendChild(retake_date_given_element);

                    Element month_element = document.createElement("month");
                    month_element.appendChild(document.createTextNode(retake.getMonthStr()));
                    retake_date_given_element.appendChild(month_element);

                    Element day_element = document.createElement("day");
                    day_element.appendChild(document.createTextNode(retake.getDayStr()));
                    retake_date_given_element.appendChild(day_element);

                    Element hour_element = document.createElement("hour");
                    hour_element.appendChild(document.createTextNode(retake.getHourStr()));
                    retake_date_given_element.appendChild(hour_element);

                    Element minute_element = document.createElement("minute");
                    minute_element.appendChild(document.createTextNode(retake.getMinuteStr()));
                    retake_date_given_element.appendChild(minute_element);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filename));
        transformer.transform(source, result);
    }

} // end class
