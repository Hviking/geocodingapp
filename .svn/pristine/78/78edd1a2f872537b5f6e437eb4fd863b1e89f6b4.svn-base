/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.datasource.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 初始化系统参数
 * @author lbs
 */
public  class initSystemParams {
 
    private  static initSystemParams instance=null;
    private static List<String> paramNameList = null;
    private static HashMap<String, Object> params = null;
    private Properties properties;
    private File sysConfigFile;
    private StringBuilder comments;

    public static enum PARAM {
        /**
         * 数据源路径
         */
        PARAM_DATASOURCE_PATH,
        /**
         * 博阳地图数据源(存放在博阳数据库)
         */
        PARAM_BEYONDB_DATASOURCE,
        /**
         * 博阳地图数据源中兴趣点的表名
         */
        PARAM_BEYONDB_POI_DATATABLE,
        /**
         * 对兴趣点的查询语句,精确查询,并返回经纬度
         */
        PARAM_BEYONDB_SQL_POI_LONLAT,
                /**
         * 对兴趣点的查询语句,模糊查询,并返回经纬度
         */
        PARAM_BEYONDB_SQL_POI_LONLAT_FUZZY,
        /**
         * 支持的栅格文件类型
         */
        PARAM_RASTER_FILE_TYPE,
        /**
         * 支持的多媒体文件类型
         */
        PARAM_MULTIMEDIA_FILE_TYPE,
        
        /**
         * 数据编辑工具服务网址
         */
        PARAM_DATAEDIT_TOOL_URL,
        /**
         * 数据发布工具服务网址
         */
        PARAM_DATAPUBLISH_TOOL_URL,
        /**
         * 程序支持并行导入文件的数量，默认是10
         */
        PARAM_TASK_PARALELLIMPORT_NUM,
        PARAM_LOG_RAST,
        PARAM_LOG_GEOM,
        PARAM_LOG_MULTIMEDIA,
    };
    /**
     * 公共成员
     */
    /*地址解析匹配方式*/
    public static final String AddressParseType_BAIDU = "百度地图";
    public static final String AddressParseType_BEYONDB = "博阳地图";
    public static final String AddressParseType_SKYMAP = "天地图";
    /*地址解析匹配方式*/
    
    /*坐标系名称*/
    public static final String CoordinateSys_WGS84  = "WGS84";
    public static final String CoordinateSys_GCJ02  = "GCJ02";
    public static final String CoordinateSys_BaiDu09  = "BaiDu09";
    public static final String CoordinateSys_SkyMap  = "SkyMap";
    public static final String CoordinateSys_Beyondb  = "Beyondb";
    /*坐标系名称*/

    public static final String Table_Column_CoordinateLong_BaiDu = "baidu_longitude";
    public static final String Table_Column_CoordinateLat_BaiDu = "baidu_latitude";
    public static final String Table_Column_CoordinateLong_BeyonDB = "beyondb_longitude";
    public static final String Table_Column_CoordinateLat_BeyonDB = "beyondb_latitude";
    public static final String Table_Column_CoordinateLong_SkyMap = "skymap_longitude";
    public static final String Table_Column_CoordinateLat_SkyMap = "skymap_latitude";
    public static final String Table_Column_ParseStat = "status";
    
    private initSystemParams() {
        params = new HashMap<>();
        paramNameList = new ArrayList<>();

        for (PARAM p : PARAM.values()) {
            paramNameList.add(p.name());
        }

        doInit();
    }

    /**
     * 完成初始化赋值
     */
    private void doInit() {
        try {
            String rootPath = JarUtils.getJarUtilPath() + File.separatorChar;

            sysConfigFile = new File(rootPath+ "sys.properties");
             properties = new Properties();
             comments =new StringBuilder();
            //读取配置文件
            if (!sysConfigFile.exists()) {
                sysConfigFile.createNewFile();
                try (OutputStream output = new FileOutputStream(sysConfigFile)) {

                    comments.append("配置文件").append("\n");
                    //初始配置，并保存
                    comments.append("tool.data.edit.url 数据编辑工具的网址").append("\n");
                    properties.setProperty("tool.data.edit.url", "");
                    comments.append("tool.data.publish.url 数据发布工具的网址").append("\n");
                    properties.setProperty("tool.data.publish.url", "");

                    comments.append("datasource.map.beyondb.url 博阳地图数据源数据库连接字符串").append("\n");
                    properties.setProperty("datasource.map.beyondb.url", "jdbc:beyondb://192.168.1.25:II7/bj");
                    comments.append("datasource.map.beyondb.username 博阳地图数据源数据库访问用户").append("\n");
                    properties.setProperty("datasource.map.beyondb.username", "beyondb");
                    comments.append("datasource.map.beyondb.password 博阳地图数据源数据库访问密码").append("\n");
                    properties.setProperty("datasource.map.beyondb.password", "123456");

                    comments.append("datasource.map.beyondb.sql.poi.lonlat 博阳地图数据源数据库中兴趣点经纬度坐标的sql精确查询语句").append("\n");
                    properties.setProperty("datasource.map.beyondb.sql.poi.lonlat", "select x,y from navi_point where name =? ");
                    comments.append("datasource.map.beyondb.sql.poi.lonlat.fuzzy 博阳地图数据源数据库中兴趣点经纬度坐标的sql模糊查询语句").append("\n");
                    properties.setProperty("datasource.map.beyondb.sql.poi.lonlat.fuzzy", "select x,y,name from navi_point where name like ? ");


                    OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
                    properties.store(writer, comments.toString());
                }
            } else {
                try (InputStream input = new FileInputStream(sysConfigFile)) {
                    InputStreamReader reader = new InputStreamReader(input, "UTF-8");
                    properties.load(reader);
                }
            }
            setInitSystemParam(PARAM.PARAM_DATASOURCE_PATH, rootPath + "dbConfig.xml");
            DataSource ds = new BydDataSource(
                    properties.getProperty("datasource.map.beyondb.url"),
                    properties.getProperty("datasource.map.beyondb.username"),
                    properties.getProperty("datasource.map.beyondb.password")
            );
            setInitSystemParam(PARAM.PARAM_BEYONDB_DATASOURCE, ds);
            setInitSystemParam(PARAM.PARAM_BEYONDB_SQL_POI_LONLAT,
                    properties.getProperty("datasource.map.beyondb.sql.poi.lonlat"));
            setInitSystemParam(PARAM.PARAM_BEYONDB_SQL_POI_LONLAT_FUZZY,
                    properties.getProperty("datasource.map.beyondb.sql.poi.lonlat.fuzzy"));
            String[] fileTypes = {"tiff", "tif", "img", "bmp", "egc", "jpg", "jpeg"};
            setInitSystemParam(PARAM.PARAM_RASTER_FILE_TYPE, fileTypes);
            String[]  mediafileTypes = {"MPG", "AVI", "WMV", "MOV", "FLV", "RMVB", "MKV", "MP4", "MP3"};
           setInitSystemParam(PARAM.PARAM_MULTIMEDIA_FILE_TYPE,mediafileTypes );
            
            
            setInitSystemParam(PARAM.PARAM_LOG_RAST, "LOG_RAST");
            setInitSystemParam(PARAM.PARAM_LOG_GEOM, "LOG_GEOM");
            setInitSystemParam(PARAM.PARAM_LOG_MULTIMEDIA, "LOG_MULTIMEDIA");

            setInitSystemParam(PARAM.PARAM_DATAEDIT_TOOL_URL, properties.getProperty("tool.data.edit.url"));
            setInitSystemParam(PARAM.PARAM_DATAPUBLISH_TOOL_URL, properties.getProperty("tool.data.publish.url"));
            setInitSystemParam(PARAM.PARAM_TASK_PARALELLIMPORT_NUM, properties.getProperty("raster.import.parallel.num"));
        } catch (IOException ex) {
            Logger.getLogger(initSystemParams.class.getName()).log(Level.SEVERE, null, ex);
        }

         
    }

    /**
     *保存参数
     */
    public void saveParams()
    {
        try {
            try (OutputStream output = new FileOutputStream(sysConfigFile)) {
                OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
                properties.setProperty("tool.data.edit.url", String.valueOf(getSystemParam(PARAM.PARAM_DATAEDIT_TOOL_URL)));
                properties.setProperty("tool.data.publish.url", String.valueOf(getSystemParam(PARAM.PARAM_DATAPUBLISH_TOOL_URL)));
              
                properties.store(writer, comments.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(initSystemParams.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    /**
     * 为参数赋值
     *
     * @param p 参数名
     * @param value 值
     */
    public void setInitSystemParam(PARAM p, Object value) {
        params.put(p.name(), value);
    }

    public static initSystemParams getInstance() {
        if (instance == null) {
            instance = new initSystemParams();
        }
        return instance;
    }

    public Object getSystemParam(PARAM p) {
        return params.get(p.name());
    }
}
