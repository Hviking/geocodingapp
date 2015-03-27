/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.geocoding;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 经纬度坐标
 * @author 倪永
 */
public class Coordinate implements  Cloneable{
        
    /**
     *纬度
     */
    public double Latitude;
    
    /**
     *经度
     */
    public double Longitude;

    /**
     *
     * @return
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
      public Coordinate clone() throws CloneNotSupportedException{
        Coordinate coord=null;   
        try   
         {   
             coord=(Coordinate)super.clone();   
             coord.Latitude=this.Latitude;
             coord.Longitude =this.Longitude;
         }
        catch(CloneNotSupportedException ex)
        {
            Logger.getLogger(Coordinate.class.getName())
                    .log(Level.SEVERE,"不支持Coordinate的复制",ex);
        }
        return coord;
       
      }   
}
