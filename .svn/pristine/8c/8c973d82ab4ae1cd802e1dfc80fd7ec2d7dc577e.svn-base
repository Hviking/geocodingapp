/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.geocoding;

import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.utils.initSystemParams;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *博阳API，博阳目前没有API，因此坐标的获取实际是从百度API转换成WGS８４坐标
 * @author
 */
public class BeyondbAPI implements  I_GeoCodeAPI{

    private static BeyondbAPI beyondbAPI;



    public static void main(String[] args) throws IOException {
         Coordinate coord =BeyondbAPI.getInstance().getCoordinate("建西苑中里４号楼","");
         if (null != coord) {
            System.out.println("lat:" + coord.Latitude);
            System.out.println("lng:" + coord.Longitude);
        }
        Map<String, Coordinate> places = BeyondbAPI.getInstance().getPlaces("建西苑中里", "");
        if (places != null) {
            for (Map.Entry<String, Coordinate> entry : places.entrySet()) {
                String placeName = entry.getKey();
                Coordinate coordinate = entry.getValue();
                System.out.println("placeName:" + placeName);
                System.out.println("lat:" + coordinate.Latitude);
                System.out.println("lng:" + coordinate.Longitude);
            }
        }
    }

    public static synchronized BeyondbAPI getInstance() {      
        if (beyondbAPI == null) {
            beyondbAPI = new BeyondbAPI();
        }
        return beyondbAPI;
    }
    
    @Override
    public Coordinate getCoordinate(String address, String city) {
        Coordinate coord = null;
        initSystemParams params = initSystemParams.getInstance();
        String sql = (String) params.getSystemParam(initSystemParams.PARAM.PARAM_BEYONDB_SQL_POI_LONLAT);
        DataSource ds = (DataSource) params.getSystemParam(initSystemParams.PARAM.PARAM_BEYONDB_DATASOURCE);
        DataSourceUtils utils = new DataSourceUtils(ds);
        PreparedStatement state = null;
        try {
            boolean openConnection = utils.openConnection();
            if (openConnection) {
                state = utils.createPreparedStatement(sql);
                state.setString(1, address);
                ResultSet rs = state.executeQuery();
                if (rs.next()) {
                    coord = new Coordinate();
                    coord.Longitude = rs.getLong(1);
                    coord.Latitude = rs.getLong(2);
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(BeyondbAPI.class.getName()).log(Level.SEVERE,
                    "读取Beyondb数据库中点位的经纬度坐标时出错", ee);
        } finally {
            utils.closePrepareStatement(state);
            utils.closeConnection();

        }

       return coord;
 
        
    }
    
    /**
     * 对于没有明确解析出地理坐标的地名
     *获取推荐的的地名和相应的坐标
     * @param address
     * @param city
     * @return
     */
    public Map<String,Coordinate> getPlaces(String address,String city)
    {
        Map<String, Coordinate> map = null;
  
        initSystemParams params = initSystemParams.getInstance();
        String sql = (String) params.getSystemParam(initSystemParams.PARAM.PARAM_BEYONDB_SQL_POI_LONLAT_FUZZY);
        DataSource ds = (DataSource) params.getSystemParam(initSystemParams.PARAM.PARAM_BEYONDB_DATASOURCE);
        DataSourceUtils utils = new DataSourceUtils(ds);
        Statement state = null;
        try {
            boolean openConnection = utils.openConnection();
            if (openConnection) {

                state = utils.createStatement();
                sql = sql.replace("?", "'%" + address + "%'");

               
                ResultSet rs = state.executeQuery(sql);
            
                while(rs.next()) {
                    if(map==null)
                    {
                       map= new HashMap<>();
                    }
                    Coordinate coord = new Coordinate();
                    coord.Longitude = rs.getLong(1);
                    coord.Latitude = rs.getLong(2);
                    map.put( rs.getString(3), coord);
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(BeyondbAPI.class.getName()).log(Level.SEVERE,
                    "读取Beyondb数据库中点位的经纬度坐标时出错", ee);
        } finally {
            utils.closeStatement(state);
            utils.closeConnection();

        }

       return map;
 
    }
    @Override
    public String getApiType()
    {
        return this.getClass().getName();
    }
}
