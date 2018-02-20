package com.attivio.javatest;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


import static java.nio.file.Files.probeContentType;

public class CSVToXMLConverter {

    //method to check whether user has provided valid csv file or not
    public String checkValidCSV(File file) throws IOException {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") == -1 || fileName.lastIndexOf(".") == 0)
            return "Kindly enter the file name with extension";
        String ext=  fileName.substring(fileName.lastIndexOf(".")+1);
        if(!"csv".equals(ext))
            return "Provide the file name with .csv extension";
        if(!file.exists())
            return "File Doesn't exists in the directory!";
        else
            return "Valid CSV file";

    }

    //method to accept file as parameter and return the XML content in a document
    public Document convertcsvToXML(File file) throws ParserConfigurationException, IOException {

        ArrayList<String> attivioTestInfo = new ArrayList<String>();
        BufferedReader readFile = null;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();
        try {
            org.w3c.dom.Element root = document.createElement("Attivio"); //giving root name

            document.appendChild(root);
            //readFile = new BufferedReader(new FileReader(file));
            readFile = new BufferedReader(new FileReader(file));
            int line = 0;

            String lineInfo = null;
            while ((lineInfo = readFile.readLine()) != null) {

                String[] rowValues = lineInfo.split(",");

                if (line == 0) {
                    for (String col : rowValues) {  // to extract the column name defined on the first row of CSV
                        attivioTestInfo.add(col);
                    }
                } else {
                    // Element childElement = document.createElement("details");
                    Element childElement = document.createElement("Information"+line);
                    root.appendChild((Node) childElement); //Adding Information tag


                    for (int column = 0; column < attivioTestInfo.size(); column++) {

                        String header = attivioTestInfo.get(column);
                        String value = null;

                        if (column < rowValues.length) {
                            value = rowValues[column];
                        } else {
                            value = " ";
                        }

                        Element current = document.createElement(header); // It will add Column name as tag eg. employeename, gender etc
                        current.appendChild(document.createTextNode(value)); // This will hold value of the tag
                        childElement.appendChild(current); //Adding it to the XML
                    }
                }
                line++;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage() + " Please provide valid CSV file name with extension");
            System.exit(1);
        }
        return document;
    }

    //method to write the XML content conveted into the XML file and save in current directory
    public String writeXML(Document document, String nameOfFile) throws TransformerConfigurationException, FileNotFoundException, UnsupportedEncodingException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer output = new StringWriter();
        try {
            transformer.transform(new DOMSource(document), new StreamResult(output));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        System.out.println("Following is XML file content");
        System.out.println();
        System.out.println(output.toString());
        String writeFile = nameOfFile+".xml"; //Giving XML filename same as CSV filename
        System.out.println(writeFile);
        PrintWriter writer = new PrintWriter(writeFile, "UTF-8");
        writer.println(output.toString());  // writing XML content in the file
        writer.close();
        File file = new File(writeFile);
        if(!file.exists())
        return "Error occured during exporting XML file";

        return "XML file generated Successfully";
    }

    public static void main(String[] args) throws IOException {

    System.out.println("**** CSV TO XML Converter App **** ");


        CSVToXMLConverter converter = new CSVToXMLConverter();
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the CSV file name along with extension present in the directory");
        String fileName=scan.next();    //accepting the file name from the user
        File file = new File(fileName);
        String checkValid= converter.checkValidCSV(file); //checking if the file is valid CSV file

        try{
        if(checkValid.equalsIgnoreCase("Valid CSV file")){
         String nameOfFile=fileName.substring(0, fileName.lastIndexOf("."));  //extracting the file name to generate CSV with same name
         Document doc =   converter.convertcsvToXML(file); // converting CSV content to XML content
         String result = converter.writeXML(doc,nameOfFile); // Saving the XML file in current directory
         System.out.println(result);
        }
        else
            System.out.println(checkValid);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "Please provide valid CSV file name with extension");

        }
    }

}