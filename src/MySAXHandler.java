/**
 * This program demonstrates parsing an XML document using the SAX-style parser.
 */

//import xml libraries
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/*
we need to extend DefaultHandler which implements the ContentHandler interface
SAXparser expects and will call the callback methods in whichever object you tell it
as long as that object implements the ContentHandler interface
 */
public class MySAXHandler extends DefaultHandler {

    /*
    let's override the "hook" methods in the framework that get called by the parser when
    the various document features are reached. These methods are declared in the ContentHandler interface
    empty, default implementations are provided in DefaultHandler (concrete base class)
     */

    /**
     * this method is called by the XML reader when the start of the document is reached
     *
     */
    public void startDocument() throws SAXException{
        System.out.println("The start of the document was reached");
    }

    public static void main (String[] args){

        //let the user choose an xml file to parse
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        String inputFileName = fileChooser.getSelectedFile().getAbsolutePath();
        System.out.println("user chose to parse file: " + inputFileName);

        /*
        when using the Java SAX api your're supposed ot get an instance of SAXParser from
        the SAXParserFactory. This means we need an instance of the SAXPaserFactory!
        we call a static method that returns a SAXParserFactory instance
        notice this is a common pattern in Java
         */
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        //make the factory namespace aware so that it generates a parser that is also namespace aware
        saxParserFactory.setNamespaceAware(true);
        //use the SAXParserFactory to create a new instance of SAXParser (this may throw exceptions)
        SAXParser saxParser = null;
        try{
            saxParser = saxParserFactory.newSAXParser();
        }catch (SAXException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }

        //we're going to need a helper class called XMLReader to work with our parser and handler classes
        XMLReader xmlReader = null;
        try{
            //the reader comes from our SAXParser instance
            xmlReader = saxParser.getXMLReader();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        //set the XMLReader's handler property to set the handler that will "do something" with elements in the XML document
        //let's instantiate MySAXHandler (we don't have an instance yet) and pass it to setContentHandler
        MySAXHandler mySAXHandler = new MySAXHandler();
        xmlReader.setContentHandler(mySAXHandler);

        //tell the xmlReader to parse the XML document
        try{
            xmlReader.parse(inputFileName);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
