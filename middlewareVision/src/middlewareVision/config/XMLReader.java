/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middlewareVision.config;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Config;

/**
 *
 * @author HumanoideFilms
 */
public class XMLReader {

    public static void readXML() {
        File xmlFile = new File("ConfigFiles/Configuration.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement();

            NodeList GeneralConfiguration = doc.getElementsByTagName("Configuration");

            Node property = GeneralConfiguration.item(0);
            if (property.getNodeType() == Node.ELEMENT_NODE) {
                Element propertyElement = (Element) property;

                String port = propertyElement.getElementsByTagName("port").item(0).getTextContent();
                String IP = propertyElement.getElementsByTagName("ip").item(0).getTextContent();
                int device = Integer.parseInt(propertyElement.getElementsByTagName("device").item(0).getTextContent());

                int gaborOrientations = Integer.parseInt(propertyElement.getElementsByTagName("GaborOrientations").item(0).getTextContent());
                int dimensions = Integer.parseInt(propertyElement.getElementsByTagName("dimensions").item(0).getTextContent());
                int motionDimensions = Integer.parseInt(propertyElement.getElementsByTagName("motionDimensions").item(0).getTextContent());

                int LGNMethod = Integer.parseInt(propertyElement.getElementsByTagName("LGNMethod").item(0).getTextContent());

                int NoConcentricCircles = Integer.parseInt(propertyElement.getElementsByTagName("NoConcentricCircles").item(0).getTextContent());
                int NoRadialDivisions = Integer.parseInt(propertyElement.getElementsByTagName("NoRadialDivisions").item(0).getTextContent());
                int NoHeightDivisions = Integer.parseInt(propertyElement.getElementsByTagName("NoHeightDivisions").item(0).getTextContent());

                int dxExpCont = Integer.parseInt(propertyElement.getElementsByTagName("dxExpCont").item(0).getTextContent());
                int dtExpCont = Integer.parseInt(propertyElement.getElementsByTagName("dtExpCont").item(0).getTextContent());
                int dxRotation = Integer.parseInt(propertyElement.getElementsByTagName("dxRotation").item(0).getTextContent());
                int dtRotation = Integer.parseInt(propertyElement.getElementsByTagName("dtRotation").item(0).getTextContent());

                int V1MotionSubs = Integer.parseInt(propertyElement.getElementsByTagName("V1MotionSubs").item(0).getTextContent());
                int MTMotionSubs = Integer.parseInt(propertyElement.getElementsByTagName("MTMotionSubs").item(0).getTextContent());

                Config.port = Integer.parseInt(port);
                Config.IP = IP;
                Config.device = device;

                Config.gaborOrientations = gaborOrientations;
                Config.width = dimensions;
                Config.heigth = dimensions;
                Config.motionWidth = motionDimensions;
                Config.motionHeight = motionDimensions;
                Config.V1MotionSubs = V1MotionSubs;
                Config.MTMotionSubs = MTMotionSubs;

                Config.LGNmethod = LGNMethod;

                Config.NoConcentricCircles = NoConcentricCircles;
                Config.NoRadialDivisions = NoRadialDivisions;
                Config.NoHeightDivisions = NoHeightDivisions;

                Config.dxExpCont = dxExpCont;
                Config.dtExpCont = dtExpCont;
                Config.dxRotation = dxRotation;
                Config.dtRotation = dtRotation;

            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException ex) {
        }
    }

    public static String[] getValuesFromXML(String... keys) {
        String values[] = new String[keys.length];
        File xmlFile = new File("ConfigFiles/Configuration.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement();

            NodeList GeneralConfiguration = doc.getElementsByTagName("Configuration");

            Node property = GeneralConfiguration.item(0);
            if (property.getNodeType() == Node.ELEMENT_NODE) {
                Element propertyElement = (Element) property;
                int i = 0;
                for (String key : keys) {
                    values[i] = propertyElement.getElementsByTagName("" + key).item(0).getTextContent();
                    i++;
                }

            }
        } catch (IOException | ParserConfigurationException | DOMException | SAXException ex) {

        }
        return values;
    }

    public static String getValue(String key) {
        String value = "";
        File xmlFile = new File("ConfigFiles/Configuration.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement();

            NodeList GeneralConfiguration = doc.getElementsByTagName("Configuration");

            Node property = GeneralConfiguration.item(0);
            if (property.getNodeType() == Node.ELEMENT_NODE) {
                Element propertyElement = (Element) property;

                value = propertyElement.getElementsByTagName(key).item(0).getTextContent();

            }
        } catch (Exception ex) {
        }
        return value;
    }

    public static int getIntValue(String key) {
        return Integer.parseInt(getValue(key));
    }

    public static float getFloatValue(String key) {
        return Float.parseFloat(getValue(key));
    }

}
