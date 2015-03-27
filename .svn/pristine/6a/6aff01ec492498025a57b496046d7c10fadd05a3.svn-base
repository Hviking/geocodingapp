/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.datasource;

/**
 *
 * @author lbs
 */
public class BydDataSource extends  DataSource{

    public BydDataSource() {
    }
    

    public BydDataSource(String url, String name, String password) {
       super(url, name, password);

    }
    
    public BydDataSource(String url, String name, String password,
            String driver, String ip, String virtualNode) {
        super(url, name, password, driver, ip, virtualNode);
    }
    /**
     *
     * @return
     */
    @Override
    public String getDriver() {
        m_driver="com.beyondb.jdbc.BeyondbDriver";
        return m_driver;
    }
    
}
