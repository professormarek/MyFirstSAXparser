/**
 * This program demonstrates parsing an XML document using the SAX-style parser.
 * Exercises:
 * 1) print out only accession numbers encountered as attributes within SequenceLocation elements
 * 2) print out only the values of CitationTextElement
 * 3) count to make sure that the number of element starting tags is the same as element ending tags
 */

//import xml libraries
import org.xml.sax.Attributes;
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
    //declare a variable to store the count of elements that are in the document
    private int elementCounter = 0;
    //a variable to act as a buffer and store text that is encountered between element tags
    private StringBuffer elementTextBuffer = new StringBuffer();

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
        //for now just print a debugging statement, but you can do anything you want here!
        System.out.println("The start of the document was reached");
    }

    /**
     * this method will be called by the XML reader when the end of the document is reached
     * @throws SAXException
     */
    public void endDocument() throws SAXException{
        //perform any actions you need to once the end is reached
        System.out.println("The end of the document was reached");
        System.out.println("We've seen a total of " + elementCounter + " elements! Cool.");
    }

    /**
     * called by the parser whenver the start of an element is reached
     * @param uri
     * @param localName this will store the element's name
     * @param qName
     * @param attributes treat this as a list of Attributes (may be empty!)
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        //let's start by counting the number of elements we've encountered
        elementCounter++;
        //let's log the name of the element that we have reached
        System.out.println("Start of element " + localName + " was reached. This is the " + elementCounter + "th element");
        //empty the buffer
        elementTextBuffer.setLength(0);

        //let's traverse and print the Attributes
        for(int i = 0; i < attributes.getLength(); i++){
            System.out.println("Attribute " + i + " is " + attributes.getLocalName(i) + "="+attributes.getValue(i));
        }
    }

    /**
     * this method is called whenever ordinary text characters are encountered between element tags
     * @param text this is the text itself
     * @param begin indicates the beginning of the text
     * @param length end of the text
     */
    public void characters(char[] text, int begin, int length){
        //handle the characters encountered by storing them in the buffer
        elementTextBuffer.append(text, begin, length);
    }

    /**
     * again we're passed the same information to endElement as startElement minus Attributes
     * because Attributes are only seen in the opening tag, and never the closing tag...
     * @param uri
     * @param localName
     * @param qName
     */
    public void endElement(String uri, String localName, String qName){

        //do some processing if the element contained any text
        //in this case simply print the text, but you can do whatever you imagine
        if(elementTextBuffer.length() > 0){
            System.out.println("Text for element: " + localName + " is " + elementTextBuffer.toString());
        }
    }

    public static void main (String[] args){

        //let the user choose an xml file to parse
        final JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        String inputFileName = fileChooser.getSelectedFile().getAbsolutePath();
        //String inputFileName = "/Users/teaching/desktop/xid-17243045_2.xml";
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
