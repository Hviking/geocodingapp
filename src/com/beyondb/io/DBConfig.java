/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.io;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.utils.JarUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 *读取数据库配置文件 
 * @author lbs
 */
public class DBConfig {
    private static final String Node_database = "database";
    private static final String Attribute_id = "id";
    private static final String Attribute_jdbcurl = "jdbcurl";
    private static final String Attribute_username = "username";
    private static final String Attribute_password = "password";
    private static final String Attribute_virtualNode = "virtualNode";

    
    public static void main(String[] args) {
       String  path
            =  JarUtils.findFile("dbConfig.xml");
        DataSource ds = DBConfig.readDBConfig(path);
        ArrayList<DataSource> dsList = DBConfig.readDBConfig1(path);
        
        
        System.out.println(ds.getUrl());
        System.out.println(ds.getName());
        
        DataSource ds1 = new BydDataSource("jdbc:beyondb://192.168.1.115:II7/beijingpoidb", "sry", "sry");
        ds1.setID("ds1");
        
        DataSource ds2 = new BydDataSource("jdbc:beyondb://192.168.1.116:II7/beijingpoidb", "sry", "sry");
        ds2.setID("ds2");
        dsList.clear();
        dsList.add(ds1);
        dsList.add(ds2);
        DBConfig.saveDBConfig(path,dsList);
    }
    
    /**
     *测试数据源连接是否正常
     * @param ds
     * @return
     * @throws SQLException
     */
    public static boolean testConnect(DataSource ds) throws SQLException {
        boolean succeed = false;
        DataSourceUtils DsUtils = new DataSourceUtils(ds);
        boolean openConnection = DsUtils.openConnection();
        if (openConnection) {
            succeed = true;
        }
        DsUtils.closeConnection();
        return succeed;
    }
    /**
     *默认读第一个数据源
     * @param path
     * @return
     */
    public  static DataSource  readDBConfig(String path)
      {
        DataSource ds = null;

        try {
         
            SAXReader reader = new SAXReader();
            File file = new File(path);

            if (file.exists()) {
                Document  doc = reader.read(file);

                Element datasource = doc.getRootElement();
                Element dbElement = datasource.element(Node_database);
                if (dbElement != null) {
                    String id = dbElement.attribute(Attribute_id).getValue();
                    String username = dbElement.attribute(Attribute_username).getValue();
                    String password = dbElement.attribute(Attribute_password).getValue();
                    String virtualNode = dbElement.attribute(Attribute_virtualNode).getValue();
                    String url = dbElement.elementText(Attribute_jdbcurl);
                    ds = new BydDataSource(url, username, password);
                    ds.setVirtualNode(virtualNode);
                    ds.setID(id);                    
                }
                
            }

        } catch (DocumentException ex) {

            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE,
                    "读取数据库配置文件出错", ex);
        }
        return ds;
    }
    
    /**
     *读多个数据源
     * @param path
     * @return
     */
    public  static ArrayList<DataSource>  readDBConfig1(String path)
    {
        ArrayList<DataSource> dslist = new ArrayList<>();

        try {
         
            SAXReader reader = new SAXReader();
            File file = new File(path);

            if (file.exists()) {
                 Document doc = reader.read(file);

                Element datasource = doc.getRootElement();
                Iterator it = datasource.elementIterator(Node_database);
                while (it.hasNext()) {
                    Element dbElement = (Element) it.next();
                    String id = dbElement.attribute(Attribute_id).getValue();
                    String username = dbElement.attribute(Attribute_username).getValue();
                    String password = dbElement.attribute(Attribute_password).getValue();
                    String virtualNode = dbElement.attribute(Attribute_virtualNode).getValue();
                    String url = dbElement.elementText(Attribute_jdbcurl);

                    DataSource ds = new BydDataSource(url, username, password);
                    ds.setVirtualNode(virtualNode);
                    ds.setID(id);
                    dslist.add(ds);
                }
    
            }

        } catch (DocumentException ex) {

            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE,
                    "读取数据库配置文件出错", ex);
        }
        return dslist;
    }
    
    private static  void createXML(File file,ArrayList<DataSource> dslist)
    {
         try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document newDocument = builder.newDocument();
              org.w3c.dom.Element root = newDocument.createElement("datasource");
                 newDocument.appendChild(root);
             for (DataSource ds : dslist) {
                 org.w3c.dom.Element database = newDocument.createElement(Node_database);
                 root.appendChild(database);
                 database.setAttribute(Attribute_id, ds.getID());
                 database.setAttribute(Attribute_username, ds.getName());
                 database.setAttribute(Attribute_password, ds.getPassword());
                 database.setAttribute(Attribute_virtualNode, ds.getVirtualNode());

                 org.w3c.dom.Element jdbcurl = newDocument.createElement(Attribute_jdbcurl);
                 jdbcurl.setTextContent(ds.getUrl());
                 database.appendChild(jdbcurl);
             }
           
           
            TransformerFactory tf = TransformerFactory.newInstance();
            try {
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(newDocument);
                transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                PrintWriter pw = new PrintWriter(new FileOutputStream(file));
                StreamResult result = new StreamResult(pw);
                transformer.transform(source, result);
            } catch (TransformerConfigurationException | IllegalArgumentException | FileNotFoundException e) {
                Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, e);
            } catch (TransformerException ex) {
                Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void saveDBConfig(String path, DataSource ds) {
        ArrayList<DataSource> list = new ArrayList<>();
        list.add(ds);
        saveDBConfig(path, list);
    }
    
    public static void saveDBConfig(String path, ArrayList<DataSource> dslist) {
        try {

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            createXML(file, dslist);

        } catch (IOException ex) {
            Logger.getLogger(DBConfig.class.getName()).log(Level.SEVERE,
                    "读写数据库配置文件出错", ex);
        }
    }
}
