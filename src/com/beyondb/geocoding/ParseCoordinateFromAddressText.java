/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.geocoding;

import com.beyondb.utils.initSystemParams;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 根据地址文本解析出对应的经纬度坐标
 * 
 * future: 未来通过多个API来获取结果，开启多个线程，
 * 对同一查询内容都有返回结果或部分有返回结果时，以返回内容最快的API为准
 * 
 *修改 ：倪永
 * @author ZhangShuo
 */
public class ParseCoordinateFromAddressText {

    private  static String classTag = ParseCoordinateFromAddressText.class.getName();
 
    /**
     *
     * @param parseType  解析类型 {"百度地图","天地图","博阳地图"}中的一种;
    
     * @param address 根据指定地址进行坐标的反定向解析，如北京市海淀区上地十街10号 
     * @param city 地址所在的城市名 非必须
     * @return
     */
    public static Coordinate parseCoordinate(String parseType,String address, String city) {
        Coordinate coord = null;
        try {

            switch (parseType) {
                case initSystemParams.AddressParseType_BAIDU:
                    coord = BaiduAPI.getInstance().getCoordinate(address, city);
                    break;
                case initSystemParams.AddressParseType_BEYONDB:
                    coord = BeyondbAPI.getInstance().getCoordinate(address, city);
                    break;
                case initSystemParams.AddressParseType_SKYMAP:  
                    coord = SkyMapAPI.getInstance().getCoordinate(address, city);
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "地址解析出错", ex);
        }

        return coord;
    }
        
    /**
     *对于不能明确解析的地址给出相近的地址及地理坐标
     * @param parseType  解析类型 {"百度地图","天地图","博阳地图"}中的一种;
     * @param address
     * @param city
     * @return
     */
    public static Map<String,Coordinate> parseSuggestionPlace(String parseType,String address, String city) {
      Map<String,Coordinate> map=null;
        try {
 
             switch (parseType) {
                case initSystemParams.AddressParseType_BAIDU:
                    map = BaiduAPI.getInstance().getPlaces(address, city);
                    break;
                case initSystemParams.AddressParseType_BEYONDB:
                    map = BeyondbAPI.getInstance().getPlaces(address, city);
                    break;
                case initSystemParams.AddressParseType_SKYMAP:  
                    map = SkyMapAPI.getInstance().getPlaces(address, city);
                    break;
            }

          
        } catch (Exception ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "相似地址解析出错", ex);
        }
        return map;
    }
        
   
}
