/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * 模块二：将xls行文本类型转化为Beyondb空间数据类型
 * 含地名和坐标的EXCEL数据空间化入库
 * @author 倪永
 */
public class Point extends  Geometry{
    
     private  double m_lng =0;
     private  double m_latitude=0;
     private static final String classTag = Point.class.getName();
     public  Point(double  longitude, double latitude)
     {
        setPoint(longitude,latitude);
     }
     
     public final void setPoint(double  longitude, double latitude)
     {
           if (longitude>180 ||longitude<-180) {
               Logger.getLogger(classTag).log(Level.WARNING, "参数取值范围有误");
            return ;
        }
          if (latitude>90||latitude<-90) {
              Logger.getLogger(classTag).log(Level.WARNING, "参数取值范围有误");
            return ;
        }
         m_lng =longitude;
         m_latitude = latitude;
     }
     
     @Override
     public  String toString()
     {
         String point = "POINT(" + m_lng + " " + m_latitude + ")";
         String geoTxt = "st_geomfromtext('%s',%d)";
         return String.format(geoTxt, point, getSRID());
        
     }
   


 
    
}
