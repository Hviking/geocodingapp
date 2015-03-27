/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.datasource;

/**
 * 数据源提供类
 *
 * @author
 */
public class DataSource {

    String m_ID="";
    String m_Ip="";
    String m_driver="";

    String m_VirtualNode="";
    private String url="";
    private String name="";
    private String password="";
//    private SimpleDataSource ds;

    public DataSource() {
    }

    public DataSource(String url) {
        this.url = url;
    }

    public DataSource(String url, String name, String password) {
        this.url = url;
        this.name = name;
        this.password = password;
    }
    
    public DataSource(String url, String name, String password,
            String driver, String ip, String virtualNode) {
        this.url = url;
        this.name = name;
        this.password = password;
        m_driver = driver;
        m_Ip = ip;
        m_VirtualNode = virtualNode;
    }

    /**
     *设置数据源标识
     * @param id
     */
    public  void setID(String id)
    {
        m_ID=id;
    }

    /**
     *获取数据源标识
     * @return
     */
    public  String getID()
    {
        return m_ID;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIp(String ip) {
        m_Ip = ip;
    }

    public String getIp() {

        if (!m_Ip.isEmpty()) {
            return m_Ip;
        }
        //从url中解析
        //jdbc:beyondb://192.168.1.127:II7/beijingpoidb;auto=multi;ENCODE=gbk
        String tmpURL = url.split(":")[2];
        return tmpURL.substring(2);
    }

    public void setVirtualNode(String node) {
        m_VirtualNode = node;
    }
    public String getVirtualNode() {
        return m_VirtualNode;
    }
    

    public void setDriver(String driver) {
        m_driver = driver;
    }

    public String getDriver() {
        return m_driver;
    }

    /**
     * 获取数据库名称
     *
     * @return
     */
    public String getDatabaseName() {
        String tmpURL = url.split(";")[0];
        return tmpURL.substring(tmpURL.lastIndexOf("/") + 1);

    }
    
    /**
     * 获取数据库实例名或端口号
     *
     * @return 2个字母的实例名
     */
    public String getInstance() {
        //jdbc:beyondb://192.168.1.127:II7/beijingpoidb;auto=multi;ENCODE=gbk
        String tmpURL = url.split(";")[0];
        return tmpURL.substring(tmpURL.lastIndexOf(":") + 1,tmpURL.lastIndexOf(":") + 3);
    }
    
    @Override
    public String toString()
    {
        return this.getID();
    }

}
