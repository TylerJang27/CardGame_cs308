package ooga.data;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLWriter {
}

/**
 * Below file written for use in the simulation project to build an XML file
 * Yes I know it's bad code I didn't know better at the time
 * @author Andrew Krier
 */

/*

package XML;

import cellsociety.Grid;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


/**
 * This class takes in a grid and simData class and saves a corresponding XML file
 * @author Andrew Krier
 */
/*
public class XMLDocumentBuilder {
    private SimData mySimData;
    private Grid myGrid;
    private String myAuthor;
    private String mySim;
    private int mySides;
    private int myRows;
    private int myColumns;
    private double mySimVal;
    private String myRetVals = "";
    private String myWrap;
    private String myNeighbors;
    private String[] myVariables = new String[9];
    private static final String fileLocation = "XML_SAVE_TEST.xml";

    /**
     * Sets all the required data for processing and initiates saving
     * @param simData
     * @param grid
     */ /*
    public XMLDocumentBuilder (SimData simData, Grid grid) {
        mySimData = simData;
        myGrid = grid;
        setVals();
        setValString();
        setVariables();
        saveDoc();
    }

    private void setVals () {
        myAuthor = mySimData.getAuthor();
        mySim = mySimData.getSimType();
        mySides = mySimData.getShape();
        myRows = mySimData.getRows();
        myColumns = mySimData.getColumns();
        mySimVal = mySimData.getSpreadProb();
        myWrap = mySimData.getWrapType();
        myNeighbors = mySimData.getNeighborType();
    }

    private void setValString () {
        for (int i = 0; i < myRows; i++) {
            for (int j = 0; j < myColumns; j++) {
                myRetVals = myRetVals + String.valueOf(myGrid.getListOfCells().get(j).get(i).getCurrentState());
            }
        }
    }

    private void setVariables () {
        myVariables[0] = myAuthor;
        myVariables[1] = mySim;
        myVariables[2] = String.valueOf(mySides);
        myVariables[3] = String.valueOf(myRows);
        myVariables[4] = String.valueOf(myColumns);
        myVariables[5] = String.valueOf(mySimVal);
        myVariables[6] = myRetVals;
        myVariables[7] = myWrap;
        myVariables[8] = myNeighbors;
    }

    private void saveDoc () {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("data");
            document.appendChild(root);

            Attr attribute = document.createAttribute("game");
            attribute.setValue(mySimData.DATA_TYPE);
            root.setAttributeNode(attribute);

            addElement(document, root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(fileLocation));

            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException e) {

        }
    }

    private void addElement (Document document, Element root) {
        for (int i = 0; i < myVariables.length; i++) {
            Element e = document.createElement(mySimData.DATA_FIELDS.get(i));
            e.appendChild(document.createTextNode(myVariables[i]));
            root.appendChild(e);
        }
    }

}

 */
