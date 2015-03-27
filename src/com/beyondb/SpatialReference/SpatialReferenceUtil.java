/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.SpatialReference;

import com.beyondb.geocoding.Coordinate;
import com.beyondb.utils.initSystemParams;
import java.util.ArrayList;

/**
 * 工具类，
 * @author 倪永
 */
public  class SpatialReferenceUtil {
    
    private static final double ee = 0.006693421622965943; //WGS偏心率
    private static final double a = 6378245.0; //WGS长轴半径
    private static final double x_pi = 3.141592653589793 * 3000.0 / 180.0;
    private static final double pi = 3.141592653589793;

    /**
     *中国坐标内
     * @param lat
     * @param lon
     * @return
     */
    private static boolean outofChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    public static Coordinate transformWGS84toGCJ02(double lon, double lat) {
       Coordinate coord =new Coordinate();
        if (outofChina(lat, lon)) {
           coord.Longitude = lon;
            coord.Latitude = lat;
            return coord;
        }
        double dLat = transformLatWGS84toGCJ02(lon - 105.0, lat - 35.0);
        double dLon = transformLonWGS84toGCJ02(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        coord.Longitude = mgLon;
        coord.Latitude = mgLat;
        return coord;
    }

    /**
     *将GCJ02坐标系转成百度09坐标系
     * @param gg_lat
     * @param gg_lon
     * @return
     */
    public static Coordinate transformGCJ02toBaiDu09(double gg_lat, double gg_lon) {
              
        double x = gg_lon;
        double y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 2.0E-5 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 3.0E-6 * Math.cos(x * x_pi);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
    
        Coordinate coord = new Coordinate();

        coord.Longitude = bd_lon;
        coord.Latitude = bd_lat;

        return coord;
    }

    private  static double transformLatWGS84toGCJ02(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320.0 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformLonWGS84toGCJ02(double x, double y) {
        double ret = -300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     *将百度09坐标系转成博阳坐标系
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static Coordinate transformBaiDu09toBeyondb(double bd_lat, double bd_lon) {
        //暂时转到wgs84坐标系
        Coordinate tmpCoord = transformBaiDu09toGCJ02(bd_lat, bd_lon);

        return transformGCJ02toWGS84(tmpCoord.Longitude, tmpCoord.Latitude);
    }
    
    
    /**
     * 将百度09坐标系转成天地图坐标系
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static Coordinate transformBaiDu09toSkyMap(double bd_lat, double bd_lon) {
        //暂时转到wgs84坐标系
        Coordinate tmpCoord = transformBaiDu09toGCJ02(bd_lat, bd_lon);

        return transformGCJ02toWGS84(tmpCoord.Longitude, tmpCoord.Latitude);
    }
    /**
     *将百度09坐标系转成GCJ02坐标系
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static Coordinate transformBaiDu09toGCJ02(double bd_lat, double bd_lon) {
       Coordinate coord = new Coordinate();
        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 2.0E-5 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 3.0E-6 * Math.cos(x * x_pi);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        coord.Longitude = gg_lon;
        coord.Latitude =gg_lat;
        return coord;
    }

    // gcj02-84
    public static Coordinate transformGCJ02toWGS84(double lon, double lat) {
        Coordinate coord = new Coordinate();
        double longitude = lon - ((transformWGS84toGCJ02(lon, lat).Longitude) - lon);
        double latitude = lat - (((transformWGS84toGCJ02(lon, lat)).Latitude) - lat);
        coord.Longitude = longitude;
        coord.Latitude = latitude;
        return coord;
    }
    public  static boolean  hasCoordinateSys(String name)
    {
        ArrayList<String> CoordinateSys = new ArrayList();
        CoordinateSys.add(initSystemParams.CoordinateSys_WGS84);
        CoordinateSys.add(initSystemParams.CoordinateSys_GCJ02);
        CoordinateSys.add(initSystemParams.CoordinateSys_BaiDu09);
        CoordinateSys.add(initSystemParams.CoordinateSys_SkyMap);
        CoordinateSys.add(initSystemParams.CoordinateSys_Beyondb);
        
        for (String sys : CoordinateSys) {
            if (sys.equals(name)) {
                return true;
            }
        }
        return false;
    }
    public static Coordinate transform(String sourceCoordinateSys, String targetCoordinateSys, Coordinate sourceCoordinate) {
        Coordinate coord = null;
        if (hasCoordinateSys(sourceCoordinateSys)
                && hasCoordinateSys(targetCoordinateSys)) {
            if (sourceCoordinateSys.equals(initSystemParams.CoordinateSys_WGS84)) {
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_GCJ02)) {
                    //wgs84->GCJ02
                    coord = transformWGS84toGCJ02(sourceCoordinate.Longitude, sourceCoordinate.Latitude);
                }
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_BaiDu09)) {
                    //wgs84->BaiDu09
                    Coordinate tmpCoord = transformWGS84toGCJ02(sourceCoordinate.Longitude, sourceCoordinate.Latitude);
                    coord = transformGCJ02toBaiDu09(tmpCoord.Latitude, tmpCoord.Longitude);
                }
//                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_Beyondb)) {
//                    //wgs84->Beyondb
//                    coord = transformWGS84toGCJ02(sourceCoordinate.Longitude, sourceCoordinate.Latitude);
//                }
//                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_SkyMap)) {
//                    //wgs84->SkyMap
//                    Coordinate tmpCoord = transformWGS84toGCJ02(sourceCoordinate.Longitude, sourceCoordinate.Latitude);
//                    coord = transformGCJ02toBaiDu09(tmpCoord.Latitude, tmpCoord.Longitude);
//                }
            }
            if (sourceCoordinateSys.equals(initSystemParams.CoordinateSys_GCJ02)) {
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_WGS84)) {
                    //GCJ02->WGS84
                    coord = transformGCJ02toWGS84(sourceCoordinate.Longitude, sourceCoordinate.Latitude);
                }
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_BaiDu09)) {
                    //GCJ02->BaiDu09
                    coord = transformGCJ02toBaiDu09(sourceCoordinate.Latitude, sourceCoordinate.Longitude);
                }
            }
            if (sourceCoordinateSys.equals(initSystemParams.CoordinateSys_BaiDu09)) {
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_WGS84)) {
                    //BaiDu09->WGS84
                    Coordinate tmpCoord = transformBaiDu09toGCJ02(sourceCoordinate.Latitude, sourceCoordinate.Longitude);
                    coord = transformGCJ02toWGS84(tmpCoord.Longitude, tmpCoord.Latitude);
                }
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_GCJ02)) {
                    //BaiDu09->GCJ02
                    coord = transformBaiDu09toGCJ02(sourceCoordinate.Latitude, sourceCoordinate.Longitude);
                }
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_Beyondb)) {
                    //BaiDu09->Beyondb
                    coord = transformBaiDu09toBeyondb(sourceCoordinate.Latitude, sourceCoordinate.Longitude);
                }
                if (targetCoordinateSys.equals(initSystemParams.CoordinateSys_SkyMap)) {
                    //BaiDu09->天地图
                    coord = transformBaiDu09toSkyMap(sourceCoordinate.Latitude, sourceCoordinate.Longitude);
                }
            }

            if (coord != null) {//控制数值精度
                java.math.BigDecimal lon = new java.math.BigDecimal(coord.Longitude);

                String s = String.valueOf(sourceCoordinate.Longitude);
                int scale = 0;
                if (s.indexOf(".") > 0) {

                    scale = s.length() - s.indexOf(".") - 1;
                    coord.Longitude = lon.setScale(scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                java.math.BigDecimal lat = new java.math.BigDecimal(coord.Latitude);
                s = String.valueOf(sourceCoordinate.Longitude);
                if (s.indexOf(".") > 0) {
                    scale = s.length() - s.indexOf(".") - 1;
                    coord.Latitude = lat.setScale(scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
                }

            }
        }
        

        return coord;
    }
    
    

}
