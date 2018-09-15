package com.github.brantpastore.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * FileManager:
 *  Is responsible for loading and parsing the config file, and any other necessary files.
 */
public class FileManager {
    private final String fHandlerString = "[FileHandler]: ";
    private FileInputStream io;
    private String configFile = "C:/Users/brant/OneDrive/Desktop/settings.conf";

    public static Map<Enum, String> dbSettings = new HashMap<Enum, String>();


    public FileManager() {
        ReadConfigFile();
    }

    public static Map getdbMap() {
        return dbSettings;
    }

    public void ReadConfigFile() {
        try {
            File inputFile = new File(this.configFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println(fHandlerString + "parsing config file (" + configFile + ")");

            NodeList nList = doc.getElementsByTagName("dbinfo");

            for (int index = 0; index < nList.getLength(); index++) {
                Node nNode = nList.item(index);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    dbSettings.put(dbInfo.driver, eElement.getElementsByTagName("driver").item(0).getTextContent());
                    dbSettings.put(dbInfo.host, eElement.getElementsByTagName("host").item(0).getTextContent());
                    dbSettings.put(dbInfo.port, eElement.getElementsByTagName("port").item(0).getTextContent());
                    dbSettings.put(dbInfo.dbname, eElement.getElementsByTagName("dbname").item(0).getTextContent());
                    dbSettings.put(dbInfo.user, eElement.getElementsByTagName("user").item(0).getTextContent());
                    dbSettings.put(dbInfo.password, eElement.getElementsByTagName("password").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
