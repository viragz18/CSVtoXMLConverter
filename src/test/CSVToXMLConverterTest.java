package test;

import com.attivio.javatest.CSVToXMLConverter;
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

public class CSVToXMLConverterTest {

    CSVToXMLConverter converter = new CSVToXMLConverter();

    @Test
    public void testCheckValidCSV() throws IOException {

    //Test case to check the functionality of checkValidCSV mehtod

    String fileName1="abc"; //scenario 1: File name without extension
        File file1 = new File(fileName1);
        String actual1=converter.checkValidCSV(file1);
        Assert.assertEquals("Kindly enter the file name with extension",actual1);

    String fileName2 = "abc.yml"; // Scenario 2: File name with different extension other than csv
        File file2 = new File(fileName2);
        String actual2=converter.checkValidCSV(file2);
        Assert.assertEquals("Provide the file name with .csv extension",actual2);

    String fileName3 = "abc.csv"; // Scenario 3: File name with valid csv extenstion but File not present in dir
        File file3 = new File(fileName3);
        String actual3=converter.checkValidCSV(file3);
        Assert.assertEquals("File Doesn't exists in the directory!",actual3);

    String fileName4 = "employee.csv"; // Secnario 4: Valid CSV file name present in the directory
        File file4 = new File(fileName4);
        String actual4=converter.checkValidCSV(file4);
        Assert.assertEquals("Valid CSV file",actual4);

    }

    @Test
    public void testconvertcsvToXML() throws IOException, ParserConfigurationException {

        //Test case to validate the functionality of convertcsvToXML method

        String fileName = "employee.csv";

        File file = new File(fileName);
        String result=null;
        Document document = converter.convertcsvToXML(file);
        if(document!=null) // if the returned document is null; It implies it could not convert in valid XML content
             result = "File converted to XML Successfully";
        else
            result = "Error";
        Assert.assertEquals("File converted to XML Successfully",result);

    }

    @Test
    public void testWriteXML() throws ParserConfigurationException, IOException, TransformerConfigurationException {

        // Test case to validate the functionality of writeXML method

        String fileName= "employee.csv";
        File file = new File(fileName);

        Document document = converter.convertcsvToXML(file);
        String result = converter.writeXML(document,"employee"); // result will store the outcome of writeXML method
        Assert.assertEquals("XML file generated Successfully",result);
    }

    @Test
    public void testCSVToXMLConverter() throws IOException, ParserConfigurationException, TransformerConfigurationException {

        //Final Test case to chech the end-to-end functionality of the app

        String fileName = "employee.csv";

        File file = new File(fileName);
        String actual4=converter.checkValidCSV(file);
        Assert.assertEquals("Valid CSV file",actual4);

        Document document = converter.convertcsvToXML(file);
        String result = converter.writeXML(document,"employee");

        Assert.assertEquals("XML file generated Successfully",result);
    }
}
