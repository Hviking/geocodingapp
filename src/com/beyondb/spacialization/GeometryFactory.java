/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

/**
 * 工厂类
 * @author 倪永
 */
public   class GeometryFactory {
    

    
       /**
     * 将beyondb空间类型转化成文本
     *
     * @param column
     * @param alias
     * @return
     */
    public static String parseGeomToText(String column, String alias) {
        return "ST_AsText(" + column + ") as " + alias;
    }

    public static String parseGeomToText(String column) {
        return parseGeomToText(column, column);
    }
}
