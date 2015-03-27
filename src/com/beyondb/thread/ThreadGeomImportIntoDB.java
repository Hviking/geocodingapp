/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.thread;

import com.alibaba.druid.pool.DruidDataSource;
import com.beyondb.bean.ImportTaskInfo;
import com.beyondb.bean.TaskInfoBase;
import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.gdal.jdbc.OgrDataImport;
import com.beyondb.task.Task;
import com.beyondb.task.geom.GeomTask;
import com.beyondb.utils.LoggerUtil;
import com.beyondb.utils.TimeUtil;
import com.beyondb.utils.initSystemParams;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.nutz.dao.impl.NutDao;

/**
 *单个矢量文件完成入库
 * 因为调用官明霖的矢量入库工具类，他选择的nutz工具，这里要重新封装数据源
 * @author lbs
 */
public class ThreadGeomImportIntoDB  extends  ThreadImportIntoDB{
    private static Logger m_logger = Logger.getLogger(ThreadGeomImportIntoDB.class.getName());
    
    private GeomTask m_geomTask = null;
    NutDao dao = null;

    int srid = 4326;
    boolean overWrite;
    public ThreadGeomImportIntoDB(GeomTask task) {
        LoggerUtil.setLogingProperties(m_logger, Level.ALL,
                (String) initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_LOG_GEOM));
        this.setName("导入单个矢量文件的任务线程");
        
        m_geomTask = task;
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName("com.beyondb.jdbc.BeyondbDriver");
        dds.setUrl(task.targetDataSource.getUrl());
        dds.setMaxActive(5);
        dds.setInitialSize(2);
        dds.setUsername(task.targetDataSource.getName());
        dds.setPassword(task.targetDataSource.getPassword());
        dao = new NutDao(dds);
        
        ogr.RegisterAll();
        
//        gdal.SetConfigOption("SHAPE_ENCODING", "GBK");
//        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    }
    
     /**
     * 初始化表格
     */
    public void initGeomTable() {
        BydOperator bydOperator = new BydOperator(new DataSourceUtils(m_geomTask.targetDataSource));

        DataSource poDS = ogr.Open(m_geomTask.importFile.getPath(), 0);
        if (poDS == null) {
            String msg = "打开数据源 " + m_geomTask.importFile.getAbsolutePath() + " 出错！";
            m_logger.severe(msg);
 
            Thread.interrupted();
        }

        try {
            OgrDataImport dataImport = new OgrDataImport(dao, srid);

            for (int i = 0; i < poDS.GetLayerCount(); i++) {
                long starttime = System.currentTimeMillis();
                Layer layer = poDS.GetLayer(i);
                String layerName = layer.GetName().toLowerCase();
                int typeCode = layer.GetGeomType();
                String createTable = dataImport.encodeCreateTableSQL(layer, layerName,m_geomTask.GeomColumnName);
                if (m_geomTask.taskIsOverWrite) {//是否改写表
                    //不存在指定的表名
                    if (!bydOperator.hasTable(layerName)) {
                        //存在对应的序列
                        if (!bydOperator.hasSequence(layerName)) {
                            bydOperator.createSequence(layerName);
                        } else {
                            bydOperator.dropSequence(layerName);
                            bydOperator.createSequence(layerName);
                        }
                        //创建表
                        bydOperator.executeSql(createTable);
                        String regGeomColumnSql = bydOperator.produceRegisterGeomColumnSql(layerName,m_geomTask.GeomColumnName,typeCode, srid);
                        bydOperator.executeSql(regGeomColumnSql);
                    } else {
                        //存在指定的表名
                        //先删除表,任何类型都删除
                        bydOperator.dropAllTypeTable(layerName);

                        //存在对应的序列
                        if (!bydOperator.hasSequence(layerName)) {
                            bydOperator.createSequence(layerName);
                        } else {
                            bydOperator.dropSequence(layerName);
                            bydOperator.createSequence(layerName);
                        }
                        //创建表
                        bydOperator.executeSql(createTable);
                        String regGeomColumnSql = bydOperator.produceRegisterGeomColumnSql(layerName,m_geomTask.GeomColumnName, typeCode, srid);
                        bydOperator.executeSql(regGeomColumnSql);
                    }
                } else {
                    //使用append模式，如果不存在表名，则创建指定名称的表。
                    if (!bydOperator.hasTable(layerName)) {

                        //存在对应的序列
                        if (!bydOperator.hasSequence(layerName)) {
                            bydOperator.createSequence(layerName);
                        } else {
                            bydOperator.dropSequence(layerName);
                            bydOperator.createSequence(layerName);
                        }
                        //创建表
                        bydOperator.executeSql(createTable);
                        String regGeomColumnSql = bydOperator.produceRegisterGeomColumnSql(layerName,m_geomTask.GeomColumnName, typeCode, srid);
                        bydOperator.executeSql(regGeomColumnSql);
                    }
                }
                long endtime = System.currentTimeMillis();
                String info = layerName + "       初始化数据表        " + starttime + "             " + endtime + "     " + (endtime - starttime);
                m_logger.info(info);
             

            }
        } catch (SQLException ex) {
            m_logger.log(Level.SEVERE, "初始化空间表异常！", ex);
        } 
        
          poDS.delete();
        
    }
    
    /**
     * 创建矢量数据的空间索引
     *
     * @param tableName 需要创建空间索引的表
     * @return 
     */
    private int createSpIndex(BydOperator bydOperator, String tableName) {

        long starttime = System.currentTimeMillis();
        if (bydOperator.existSpIndex(tableName)) {
            bydOperator.dropSpIndex(tableName);
        }
        String geoColumnName = bydOperator.getGeomColumnName(tableName);
        String bounds = bydOperator.getBoundsAsString(tableName, geoColumnName);
        int result = bydOperator.createSpatialIndex(tableName, geoColumnName, bounds);
        long endtime = System.currentTimeMillis();
        String peroid = TimeUtil.getCalFormatTime(endtime - starttime);
        if (result > 0) {
            //成功
            String msg = "图层 " + tableName
                    + " 完成空间索引的创建  "
                    + "空间范围：" + bounds
                    + "耗时：" + peroid;
            m_logger.info(msg);
            
        } else {
            String msg = "图层 " + tableName
                    + " 创建空间索引失败 "
                    + "耗时：" + peroid;
            m_logger.info(msg);
        }
        
        return result;
    }

    /**
     * 调用插入矢量文件方法，并返回总插入都数据总数
     *
     * @return 
     * @throws java.lang.Exception
     */
    public ImportTaskInfo importGeomTask() throws Exception {
      
        ImportTaskInfo taskInfo = null;

        DataSource poDS = ogr.Open(m_geomTask.importFile.getAbsolutePath(), 0);
        if (poDS == null) {
            String msg = "打开数据源 " + m_geomTask.importFile.getAbsolutePath() + " 出错！";
            m_logger.severe(msg);
            Thread.interrupted();
        }

        int totalcount = 0;  //初始化所有要素总数
        for (int i = 0; i < poDS.GetLayerCount(); i++) {
            Layer layer = poDS.GetLayer(i);
            String layerName = layer.GetName().toLowerCase();
            OgrDataImport dataImport = new OgrDataImport(dao, srid);
            
            String insertSql = dataImport.encodePrePareInsertSQL(layer,m_geomTask.GeomColumnName, srid);
            long starttime = System.currentTimeMillis();
            totalcount = dataImport.insert(layer,m_geomTask.GeomColumnName, insertSql);
         
            long endtime = System.currentTimeMillis();
            String msg = "图层 " + layerName
                    + " 数据插入完成,插入数据：" + totalcount
                    + "， 耗时：" + TimeUtil.getCalFormatTime(endtime - starttime);
            m_logger.info(msg);
        }

        poDS.delete();
        String info ="";
         if (totalcount > 0) {
            info += TaskInfoBase.info_Success;

        } else if (info.isEmpty()) {
            info += TaskInfoBase.info_Failed;
        }
        taskInfo = new ImportTaskInfo(totalcount, info, m_geomTask);
        return taskInfo;

    }
    
     @Override
    public void run() {

        try {
            ImportTaskInfo taskInfo = null;
            m_geomTask.taskProgressValue = 10;
           
            if (m_geomTask.importFile.getName().toLowerCase().contains(".shp")) {
                initGeomTable();
                taskInfo = importGeomTask();
            } else if (m_geomTask.importFile.getName().toUpperCase().contains("ZB")) {
                initGJBTable();
                taskInfo = importGJBTask();
            }
               
            if ( taskInfo.getTotalCount()>0&&m_geomTask.isCreateSpatialIndex) {
                createSpIndex(m_bydOperator, m_geomTask.targetDBTableName);
            }
            m_geomTask.taskProgressValue = 100;
   
            m_geomTask.taskStatus = Task.taskStatus_Finished;

            m_geomTask.taskInfo = taskInfo;
        } catch (Exception ex) {
            Logger.getLogger(ThreadGeomImportIntoDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * 初始化国军标 空间表
     */
    private void initGJBTable() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 导入GJB文件数据。
     * @return 
     */
    private ImportTaskInfo importGJBTask() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
