package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by emrahozel on 18.09.2017.
 */
public class config {

    public String elkHost;
    public String indexName;
    public String typeName;
    public int elkPort;
    public int maxLineSize;
    public Properties prop;
    public String configFile = getClass().getResource("/config.properties").getFile();

    public config(){
        readConfigFile();
    }

    public void readConfigFile() {
        try {
            InputStream input = new FileInputStream(configFile);
            prop = new Properties();
            prop.load(input);
            elkHost = prop.getProperty("elkHost");
            elkPort = Integer.parseInt(prop.getProperty("elkPort"));
            indexName = prop.getProperty("indexName");
            typeName = prop.getProperty("typeName");
            maxLineSize = Integer.parseInt(prop.getProperty("maxLineSize"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public void updateConfig(){
//        try {
//            InputStream input = new FileInputStream(configFile);
//            prop = new Properties();
//            prop.load(input);
//            OutputStream output = new FileOutputStream(configFile);
//            prop.setProperty("elkHost", elkHost);
//            prop.setProperty("elkPort", Integer.toString(elkPort));
//            prop.setProperty("indexName", indexName);
//            prop.setProperty("typeName", typeName);
//            prop.setProperty("maxLineSize",Integer.toString(maxLineSize));
//            prop.store(output, null);
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(config.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
