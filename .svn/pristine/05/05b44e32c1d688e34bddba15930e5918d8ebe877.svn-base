/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Beyondb 数据字段
 * @author 倪永
 */
public class BydField implements  Cloneable{
    
     /**
     * 字段类型 integer,float8,varchar,date,timestamp,time,long_varchar,Long_Byte,Geometry
     */
    public static enum FieldType {
        integer, float8, varchar, date, timestamp, time, long_varchar, Long_Byte,Geometry
    }
    public static enum RasterOrVector{

        /**
         *字段即不是矢量类型，也不是栅格类型
         */
        txt,raster,vector
    }
   public  String Name;
   public  Object Value;
   public FieldType Type;
   public RasterOrVector rov=RasterOrVector.txt;
   
   @Override
   public BydField clone() throws CloneNotSupportedException   
     {   
         BydField field=null;   
        try   
         {   
             field=(BydField)super.clone();   
         }   
        catch(CloneNotSupportedException e)   
         {   
               Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, 
                       "不支持Feature深复制", e);
         }   
        return field;   
     }    
}
