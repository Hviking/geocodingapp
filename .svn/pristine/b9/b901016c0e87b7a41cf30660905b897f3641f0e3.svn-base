/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

/**
 *几何基类，，不同于gdal中的geometry类型，此geometry类是针对beyond入库函数的
 * 
 * @author 倪永
 */
public  abstract class Geometry {
    private  int srid=4326;
    
    public  void setSRID(int id )
    {
        srid = id;
    }
    public int getSRID()
    {
        return srid;
    }
    
    /**
     *暂列几个几何类型
     */
    public static  enum  GeometryType{
        Point,Polygon,Line,MultiPoint,MultiLine,MultiPolygon
    }
    
    @Override
     abstract  public  String toString();
  
}
