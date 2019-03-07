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
 * @author Devon Thyne
 *         Date: March 2019
 * @author Walter Portillo
 *         Date: March 2019
 */
public class quizWriter {

    public void write (String filename, quizzes quizList) throws Exception {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        //root element
        Element root_element = document.createElement("quizzes");
        document.appendChild(root_element);

        //add each new quiz in quizList to .xml file
        for(quizBean quiz : quizList) {
            Element quiz_element = document.createElement("quiz");
            root_element.appendChild(quiz_element);

                Element id_element = document.createElement("id");
                id_element.appendChild(document.createTextNode(quiz.getIDStr()));
                quiz_element.appendChild(id_element);

                Element dateGiven_element = document.createElement("dateGiven");
                quiz_element.appendChild(dateGiven_element);

                    Element month_element = document.createElement("month");
                    month_element.appendChild(document.createTextNode(quiz.getMonthStr()));
                    dateGiven_element.appendChild(month_element);

                    Element day_element = document.createElement("day");
                    day_element.appendChild(document.createTextNode(quiz.getDayStr()));
                    dateGiven_element.appendChild(day_element);

                    Element hour_element = document.createElement("hour");
                    hour_element.appendChild(document.createTextNode(quiz.getHourStr()));
                    dateGiven_element.appendChild(hour_element);

                    Element minute_element = document.createElement("minute");
                    minute_element.appendChild(document.createTextNode(quiz.getMinuteStr()));
                    dateGiven_element.appendChild(minute_element);
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
