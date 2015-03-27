package com.beyondb.gdal.jdbc;

import java.util.HashMap;
import java.util.Map;

/**
 * 空间数据类型的int 数值表示，同时提供了数值与空间数据字符窜的映射。
 * @author guanml <guanminglin@gmail.com>
 */
public class GeoTypeMap {
   public static final int LINEARRING = 8;
    
    public static final int GEOMETRY = 0;
    
    /**
     * The OGIS geometry type number for points.
     */
    public static final int POINT = 1;
    /**
     * The OGIS geometry type number for lines.
     */
    public static final int LINESTRING = 2;
    /**
     * The OGIS geometry type number for polygons.
     */
    public static final int POLYGON = 3;
    /**
     * The OGIS geometry type number for aggregate points.
     */
    public static final int MULTIPOINT = 4;
    /**
     * The OGIS geometry type number for aggregate lines.
     */
    public static final int MULTILINESTRING = 5;
    /**
     * The OGIS geometry type number for aggregate polygons.
     */
    public static final int MULTIPOLYGON = 6;
    /**
     * The OGIS geometry type number for feature collections.
     */
    public static final int GEOMETRYCOLLECTION = 7;
    
    public final static Map<Integer, String> CODE_TO_TYPENAME_MAP = new HashMap<Integer, String>() {

        {
            put(GEOMETRY, "GEOMETRY");
            put(POINT, "POINT");
            put(LINESTRING, "LINESTRING");
            put(POLYGON, "POLYGON");
            put(MULTIPOINT, "MULTIPOINT");
            put(MULTILINESTRING, "MULTILINESTRING");
            put(MULTIPOLYGON, "MULTIPOLYGON");
            put(GEOMETRYCOLLECTION, "GEOMETRYCOLLECTION");

        }
    };
    public  final static Map<String, Integer> TYPENAME_TO_CODE_MAP = new HashMap<String, Integer>() {

        {
            put("GEOMETRY",GEOMETRY);
            put("POINT",POINT);
            put("LINESTRING",LINESTRING);
            put("POLYGON",POLYGON);
            put("MULTIPOINT",MULTIPOINT);
            put("MULTILINESTRING",MULTILINESTRING);
            put("MULTIPOLYGON",MULTIPOLYGON);
            put("GEOMETRYCOLLECTION",GEOMETRYCOLLECTION);

        }
    };
    
    /**
     * get geo typecode
     * @param typeName
     * @return 
     */
    public static int getTypeCode(String typeName){
        Integer code = TYPENAME_TO_CODE_MAP.get(typeName.toUpperCase()) ;
        return code !=null? code:-1;
    }
    
    /**
     * get geo typecode name
     * @param code
     * @return 
     */
    public static String getTypeName(int code){
        return CODE_TO_TYPENAME_MAP.get(code);
    }
    
}
