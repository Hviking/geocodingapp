/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.datasource;

import com.beyondb.spacialization.BydField;
import com.beyondb.spacialization.BydField.FieldType;
import com.beyondb.spacialization.Geometry;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Beyondb 空间数据库表操作函数
 *
 * @author 倪永
 */
public class BydOperator {

    private final DataSourceUtils m_DsUtils;
    private static final String classTag = BydOperator.class.getName();

    public BydOperator(DataSourceUtils dsUtils) {
        m_DsUtils = dsUtils;
    }

    /**
     *
     * @param tableName  表名
     * @param fields     字段信息，如果为null，即删除全部表记录
     * @param primaryKeys   要删除记录的主键值
     * @return
     */
    public String produceDeleteSql(String tableName,ArrayList<BydField> fields, 
        ArrayList<String> primaryKeys) {
        
         //解析字段列表
        StringBuilder builder = new StringBuilder();
        builder.append("delete from   ").append(tableName);
        if (fields != null) {

            builder.append(" where ");
            for (BydField bydField : fields) {
                if (!primaryKeys.contains(bydField.Name)) {
                    continue;
                }
                builder.append(bydField.Name.replace("(", "、").replace(")", ""));
                builder.append("=");
                if (bydField.rov == BydField.RasterOrVector.raster) {
                //本项目暂时用不到，不考虑实现
                    //栅格数据的导入可以使用brimport工具 ，或st_import函数
                } else if (bydField.rov == BydField.RasterOrVector.vector) {
                //矢量字段
                    //借用ST_GeometryfromText函数
                    builder.append(((Geometry) bydField.Value).toString());
                } else {

                    if (bydField.Type == FieldType.integer
                            || bydField.Type == FieldType.float8
                            || bydField.Type == FieldType.date
                            || bydField.Type == FieldType.timestamp) {
                        builder.append(bydField.Value.toString());
                    } else if (bydField.Type == FieldType.Long_Byte || bydField.Type == FieldType.long_varchar) {
                        //暂不考虑
                        builder.append("'").append(bydField.Value.toString()).append("'");
                    } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                         *暂设为100，字符串数据没有太多信息
                         */
                        builder.append("'").append(bydField.Value.toString()).append("'");
                    } else {
                        //do nothing  //目前程序不可能执行到该步
                    }
                }
                builder.append(" and ");
            }
            builder.delete(builder.length() - 5, builder.length() - 1);

        }
        return builder.toString();
        
    }
    
    /**
     *检查表是否存在
     * @param tableName
     * @return
     */
    public boolean hasTable(String tableName) {
        PreparedStatement state = null;

        boolean isExist = false;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "Select count(*) from iitables where table_name =?";
                state = m_DsUtils.createPreparedStatement(sql);
                state.setString(1, tableName.toLowerCase());
                ResultSet rs = state.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        isExist = true;
                    }
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "检查表" + tableName + "是否存在出错", ee);
        } finally {
            m_DsUtils.closePrepareStatement(state);
            m_DsUtils.closeConnection();

        }

        return isExist;

    }
    
     /**
     * 检查是否存在对应的序列名称
     * @param name 表名称
     * @return  true标示存在，false 表示不存在。
     */
    public boolean hasSequence(String name) {
        boolean isExist = false;
        PreparedStatement state = null;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "select count(*) from  iisequences where seq_name=?";
                String seq_name = "seq_" + name.toLowerCase();

                state = m_DsUtils.createPreparedStatement(sql);
                state.setString(1, seq_name);
                ResultSet rs = state.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {

                        isExist = true;
                    }
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "检查序列是否存在", ex);
        }
        return isExist;

    }
    /**
     * 创建表的自动增长序列
     * @param layerName  表名称
     * @return
     */
    public int createSequence(String layerName){
        int res=-1;
        try {
            StringBuilder builder = new StringBuilder();
            String sql =builder.append("create sequence seq_").append(layerName.toLowerCase()).append(" minvalue 0 start with 0 nocache").toString();
            res= executeSql(sql);
        } catch (SQLException ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "创建序列", ex);
        }
        return res;
    }
    /**
     * 删除序列
     *
     * @param tableName
     * @return 
     */
    public int dropSequence(String tableName) {

        int res = -1;
        try {

            String sql = "drop sequence seq_" + tableName.toLowerCase();
            res = executeSql(sql);
        } catch (SQLException ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "删除序列", ex);
        }
        return res;
    }
     /**
     * 判断是否存在指定表名的空间索引
     *
     * @param tableName 空间表
     * @return true 存在，false 不存在
     */
    public boolean existSpIndex(String tableName) {
 
        
         Statement state = null;

         String result=null;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "select index_name from iiindex_columns where index_name='spidx_%s'";
                sql =String.format(sql, tableName);
                state = m_DsUtils.createStatement();
                
                ResultSet rs =state.executeQuery(sql);
                if (rs.next()) {
                    result = rs.getString("index_name");
                }
                rs.close();
            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "判断是否存在表"+tableName+"的空间索引时出错", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();

        }
        if (result == null) {
            return false;
        }
        return true;
    }
    
        /**
     * 删除已有空间索引
     *
     * @param tableName
     */
    public void dropSpIndex(String tableName) {
        
        try {
            executeSql("drop index spidx_" + tableName);
        } catch (SQLException ex) {
            Logger.getLogger(classTag)
                    .log(Level.SEVERE, "删除表"+tableName+"的空间索引", ex);
        }
        
    }
     /**
     * 计算空间表的范围
     *
     * @param tableName 空间表名称
     * @param geomColumn  空间列名
     * @return 空间范围字符串表示。例如：（（100，23），（124，56））
     */
    public String getBoundsAsString(String tableName, String geomColumn) {
       
        String result = null;

        Statement state = null;

        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "select st_astext(st_aggr_estimateextent(%s)) as bounds from %s";
                sql =String.format(sql, geomColumn,tableName);
                state = m_DsUtils.createStatement();
                
                ResultSet rs = state.executeQuery(sql);
                if (rs.next()) {
                    result = rs.getString("bounds");
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "计算空间表的范围出错", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }

        return result;
    }
    

    /**
     * 设置表主键
     *
     * @param tableName 表名称
     * @param column 需要创建主键的列。
     * @return 
     */
    public int setPrimeryKey(String tableName, String column) {
        PreparedStatement state = null;
        int update = 0;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "ALTER TABLE ?  ADD PRIMARY KEY(?)";
                state = m_DsUtils.createPreparedStatement(sql);
                state.setString(1, tableName);
                state.setString(1, column);
                update = state.executeUpdate();

            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "判断是否存在表" + tableName + "的空间索引时出错", ee);
        } finally {
            m_DsUtils.closePrepareStatement(state);
            m_DsUtils.closeConnection();
        }
        return update;
    }
    
    /**
     *查询数据库中的表
     * @return
     */
    public ArrayList<String> queryTables() 
    {
        PreparedStatement state = null;
        ArrayList<String> tbls = new ArrayList<>();
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "select table_name from iitables"
                        + " where table_owner=? and storage_structure=? order by table_name";
                state = m_DsUtils.createPreparedStatement(sql);
                state.setString(1, m_DsUtils.getDataSource().getName());
                state.setString(2, "HEAP");
                ResultSet rs = state.executeQuery();
                while (rs.next()) {
                    tbls.add(rs.getString(1).toLowerCase().trim());
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "查询数据库列表时出错", ee);
        } finally {
            m_DsUtils.closePrepareStatement(state);
            m_DsUtils.closeConnection();

        }

 
        return tbls;
       

    
    }
    
        /**
     * 读取多媒体对象的分块表名或分块文件系统根路径的名称。
     * @param tableName   表名
     * @param mediaColumn  多媒体列名
     * @return 
     */
    public String  readMediaDataLocation(String tableName,String mediaColumn)
    {
        String rbt = "";
        Statement state = null;
   
        try {
            String rbtSql = "select mt_Datalocation(%s) from %s";
            rbtSql = String.format(rbtSql, mediaColumn, tableName);

            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {
                state = m_DsUtils.createStatement();
                ResultSet rs = state.executeQuery(rbtSql);
                if (rs.next()) {
                    rbt = rs.getString(1);
                }
            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "读取多媒体对象的分块表名或分块文件系统根路径的名称", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        
        return rbt;
    }
    /**
     * 读取栅格对象的栅格分块表名或分块文件系统根路径的名称。
     * @param tableName   表名
     * @param rastColumn  栅格列名
     * @return 
     */
    public String  readRasterDataLocation(String tableName,String rastColumn)
    {
        String rbt = "";
        Statement state = null;
   
        try {
            String rbtSql = "select st_Datalocation(%s) from %s";
            rbtSql = String.format(rbtSql, rastColumn, tableName);

            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {
                state = m_DsUtils.createStatement();
                ResultSet rs = state.executeQuery(rbtSql);
                if (rs.next()) {
                    rbt = rs.getString(1);
                }
            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "读取栅格对象的栅格分块表名或分块文件系统根路径的名称", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        
        return rbt;
    }
    /**
     *删除栅格表
     * @param tableName
     */
    public void dropRasterTable(String tableName)
    {
        try {
            //与普通表的删除不同的是，栅格空间表的删除前，需要执行记录的清空。
            String deleteRecord_sql = produceDeleteSql(tableName, null, null);
            executeSql(deleteRecord_sql);

            //通过本操作，附属的分块结构将连带清空。
            //注意：如果表中存在较多的STRaster对象，则以上的删除操作将花费一定的时间。
            //2.空间栅格表的删除，包含删除A表和B表（如果有的话）。
            dropTable(tableName);
            String ratColumnName = getRasterColumnName(tableName);
            //检查是否有B表
            String rbt =readRasterDataLocation(tableName,ratColumnName);
            //检查B表是栅格分块表名或分块文件系统根路径的名称
            if (hasTable(rbt)) {
               dropTable(rbt);
            }
        
            //3.最后，执行Unreg_rast_column存储过程进行ST_Raster列的反注册。
            
            String unReg_rast_metadata = produceUnRegisterRasterColumnSql(tableName, ratColumnName);
            executeSql(unReg_rast_metadata);
        } catch (SQLException ex) {
            Logger.getLogger(classTag).log(Level.SEVERE, "删除栅格表", ex);
        }
    }
    
    public void dropGeomTable(String tableName)
    {
        if (tableName.isEmpty()) {
            return;
        }
        //        1从元数据表中反注册元数据。
        //例如：
        //EXECUTE PROCEDURE st_unreg_geom_column(schema=NULL, tablename=”roads”)
        //2.
        //删除带几何列的空间几何表。使用DROP TABLE等DDL SQL语句。
        //例如：
        //DROP TABLE roads;
        String geoColumnName = getGeomColumnName(tableName);
        if (!geoColumnName.isEmpty()) {
            //空间表
            try {
                String unRegGeoColumnSql = produceUnRegisterGeomColumnSql(tableName);
                executeSql(unRegGeoColumnSql);
                dropTable(tableName);
            } catch (SQLException ex) {
                 Logger.getLogger(classTag).log(Level.SEVERE, "删除矢量表", ex);
            }
        }
    }
    /**
     * 任何类型的表都能被删除
     * @param tableName 
     */
    public void dropAllTypeTable(String tableName) {
       if (isGeomTable(tableName)) {
          dropGeomTable(tableName);
        } else if (isRasterTable(tableName)) {
           dropRasterTable(tableName);
        } else {
          dropTable(tableName);
        }
    }
    /**
     *删除普通表
     * @param tableName
     * @return
     */
    public void dropTable(String tableName) {
   
        if (tableName.isEmpty()) {
            return;
        }
        Statement state = null;
        try {
            if (m_DsUtils.con == null
                    || m_DsUtils.con.isClosed() == true) {
                m_DsUtils.openConnection();
            }
            m_DsUtils.con.setAutoCommit(true);

            state = m_DsUtils.createStatement();
            //检查是否是空间表
//            String unRegGeomColumnSql = " EXECUTE PROCEDURE st_unreg_geom_column(schema=NULL, tablename=%s)";
            String dropSql = "drop table " + tableName.toLowerCase();
            //  state.execute(String.format(unRegGeomColumnSql, tableName));
//            m_DsUtils.con.commit();
              state.executeUpdate(dropSql);
                 

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "删除表" + tableName  , ee);

        } finally {

            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();

        }

    }
   
    /**
     *判断是否是栅格表
     * @param tableName
     * @return
     */
    public boolean  isRasterTable(String tableName)
    {
        boolean isTrue =false;
        if (!getRasterColumnName(tableName).isEmpty()) {
            isTrue=true;
        }
        return isTrue;
    }
 
        /**
     *判断是否是矢量表
     * @param tableName
     * @return
     */
    public boolean  isGeomTable(String tableName)
    {
        boolean isTrue =false;
        if (!getGeomColumnName(tableName).isEmpty()) {
            isTrue=true;
        }
        return isTrue;
    }
    
          /**
     *判断是否是多媒体表
     * @param tableName
     * @return
     */
       public boolean isMediaTable(String tableName) {
         boolean isTrue =false;
        if (!getMediaColumnName(tableName).isEmpty()) {
            isTrue=true;
        }
        return isTrue;
    }
    
    /**
     *读取栅格表列名
     * @param tableName
     * @return
     */
    public  String getRasterColumnName(String tableName)
    {
        String columnName = "";
        Statement state = null;
        try {
            String sql = "select column_name from st_raster_columns where table_schema='%s' and table_name ='%s'";
            sql = String.format(sql, m_DsUtils.getDataSource().getName(), tableName);

            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {
                state = m_DsUtils.createStatement();
                try (ResultSet rs = state.executeQuery(sql)) {
                    if (rs.next()) {
                        columnName = rs.getString(1).toLowerCase().trim();
                    }
                }
            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "读取栅格表列名", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        return columnName;
    }
    
             /**
     * 获得多媒体表列名
     * @param tableName  空间表名
     * @return   空间列字段名
     */
    public String getMediaColumnName(String tableName){
        
        String sql = "select column_name from mt_media_columns where table_schema='%s' and table_name ='%s'";
        Statement state = null;
        String columnName = "";
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                sql=String.format(sql, m_DsUtils.getDataSource().getName(),tableName);
                state = m_DsUtils.createStatement();
                ResultSet rs = state.executeQuery(sql);
                if (rs.next()) {
                    columnName=rs.getString(1).toLowerCase().trim();
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "查询表："+tableName+"多媒体字段", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        return columnName;
        
    }

     /**
     * 获得空间表列名
     * @param tableName  空间表名
     * @return   空间列字段名
     */
    public String getGeomColumnName(String tableName){
        
        String sql = "select column_name from st_geometry_columns where table_schema='%s' and table_name ='%s'";
        Statement state = null;
        String columnName = "";
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                sql=String.format(sql, m_DsUtils.getDataSource().getName(),tableName);
                state = m_DsUtils.createStatement();
                ResultSet rs = state.executeQuery(sql);
                if (rs.next()) {
                    columnName=rs.getString(1).toLowerCase().trim();
                }
                rs.close();
            }

        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "查询表："+tableName+"空间字段名称时出错", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        return columnName;
        
    }

    /**
     *创建表的空间索引
     * @param tableName   表名称
     * @param geoColumn   空间列名
     * @param bounds  创建索引 的范围
     * @return
     */
    public int  createSpatialIndex(String tableName,String geoColumn,String bounds)
    {
        if ("((0.000000,0.000000),(0.000000,0.000000))".equals(bounds)) {
            return -1;
        }
       
         Statement state = null;
        int update = 0;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                String sql = "create index spidx_%s on %s(%s) with structure=rtree,range=%s";
                sql =String.format(sql, tableName,tableName,geoColumn,bounds);
                state = m_DsUtils.createStatement();
                update = state.executeUpdate(sql);
            }
        } catch (SQLException ee) {
            Logger.getLogger(classTag).log(Level.SEVERE,
                    "创建表" + tableName + "的空间索引时出错", ee);
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        return update;
        
    }

    /**
     * 检查数据库表中是否有数据。
     * @param tableName
     * @return 
     */
    public boolean hasRecords(String tableName){
        boolean isExist = false;
        Statement state = null;
        StringBuilder builder = new StringBuilder();
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {
                builder.append("select count(*) from ").append(tableName);
                state = m_DsUtils.createStatement();
                ResultSet rs = state.executeQuery(builder.toString());
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {
                        isExist = true;
                    }
                }
                rs.close();
            }
        } catch (SQLException ssException) {
            Logger.getLogger(classTag).log(Level.SEVERE, "查询表" + tableName + "是否存在数据时出错", ssException);

        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();

        }
        return isExist;
    }
    
    public boolean hasRecord(String tableName,
            ArrayList<BydField> fields, ArrayList<String> primaryKeys) throws SQLException {
        if (!hasTable(tableName)) {
            return false;
        }

        boolean isExist = false;
        PreparedStatement state = null;
        StringBuilder builder = new StringBuilder();
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                builder.append("select count(*) from ").append(tableName);

                builder.append(" where ");
                for (BydField bydField : fields) {
                    if (!primaryKeys.contains(bydField.Name)) {
                        continue;
                    }
                    builder.append(bydField.Name.replace("(", "、").replace(")", ""));
                    builder.append("=");
                    if (bydField.rov == BydField.RasterOrVector.raster) {
                        //本项目暂时用不到，不考虑实现
                        //栅格数据的导入可以使用brimport工具 ，或st_import函数
                    } else if (bydField.rov == BydField.RasterOrVector.vector) {
                        //矢量字段
                        //借用ST_GeometryfromText函数
                        builder.append(((Geometry) bydField.Value).toString());
                    } else {
                        builder.append("?");
                    }
                    builder.append(" and ");
                }
                builder.delete(builder.length() - 5, builder.length() - 1);

                String sql = builder.toString();

                m_DsUtils.con.setAutoCommit(true);
                state = m_DsUtils.createPreparedStatement(sql);
                int i = 1;

                for (BydField bydField : fields) {
                    //设置主键值
                    if (!primaryKeys.contains(bydField.Name)) {
                        continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.raster) {
                        //本项目暂时用不到，不考虑实现
                        //栅格数据的导入可以使用brimport工具 ，或st_import函数
                        continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.vector) {
                        //借用ST_GeometryfromText函数
                        continue;
                    } else if (bydField.Type == FieldType.integer) {
                        state.setInt(i, Integer.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.float8) {
                        state.setFloat(i, Float.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.date) {
                        state.setDate(i, Date.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.timestamp) {

                        state.setTimestamp(i, Timestamp.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.long_varchar) {

                        state.setString(i, String.valueOf(bydField.Value));

                    } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                         *暂设为100，字符串数据没有太多信息
                         */
                        state.setString(i, String.valueOf(bydField.Value));
                    } else {
                        //do nothing  //目前程序不可能执行到该步
                    }
                    i++;

                }
                ResultSet rs = state.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) > 0) {

                        isExist = true;
                    }
                }
                rs.close();
            }

        } catch (SQLException ssException) {
            Logger.getLogger(classTag).log(Level.SEVERE, "查询数据记录存在与否时出错：表"
                    + tableName + "记录" + builder.toString(), ssException);
            throw ssException;
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();

        }
        return isExist;

    }
    
    public String produceCreateTableSql(String tableName,ArrayList<BydField> fields) {
        //解析字段列表
          StringBuilder builder = new StringBuilder();
        builder.append("create table ").append(tableName).append("(");
//          builder.append("fid int auto_increment not null,");
         builder.append("fid integer auto_increment ,");
        for (BydField bydField : fields) {
             builder.append(bydField.Name.replace("(", "、").replace(")", "")).append(" ");
            if (bydField.rov == BydField.RasterOrVector.raster) {
                builder.append(" ST_Raster");
            } else if (bydField.rov == BydField.RasterOrVector.vector) {
                 //矢量字段
                builder.append(" ST_Geometry");
            } else {
               
                if (bydField.Type == FieldType.integer
                        || bydField.Type == FieldType.float8
                        || bydField.Type == FieldType.date
                        || bydField.Type == FieldType.timestamp) {
                    builder.append(bydField.Type.name());
                } else if (bydField.Type == FieldType.Long_Byte || bydField.Type == FieldType.long_varchar) {
                    builder.append(bydField.Type.name().replace("_", " "));
                } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                     *暂设为100，字符串数据没有太多信息
                     */
                    builder.append(bydField.Type.name()).append("(100)");
                }
            }
            builder.append(",");
        }
        
        builder.deleteCharAt(builder.length() - 1);
        //  builder.append("primary key(id)");
        builder.append(")");
        
        return  builder.toString();
    }
    
    public String produceRegisterGeomColumnSql(String tableName, ArrayList<BydField> fields) {
        String geoColumnName = "";
        int srid = 0;
        for (BydField bydField : fields) {
            if (bydField.rov == BydField.RasterOrVector.vector) {
                geoColumnName = bydField.Name;
                srid = ((Geometry) bydField.Value).getSRID();
                break;
            }

        }
        return produceRegisterGeomColumnSql(tableName, geoColumnName, srid);
    }
    
    public String produceRegisterGeomColumnSql(String tableName, String geoColumn, int srid) {
        //使用存储过程ST_REG_GEOM_COLUMN将空间列注册到元数据表。
        // EXECUTE PROCEDURE ST_REG_SRS(sr_id=1, srs_name='Airy 1830 ellipsoid', organ='EPSG', organ_cood_id=1, dfn= 'GEOGCS["Unknown datum based upon the Bessel 1841 ellipsoid",DATUM["Not_specified_based_on_Bessel_1841_ellipsoid", SPHEROID["Bessel 1841", 6377397.155,299.1528128, AUTHORITY["EPSG","7004"]],AUTHORITY["EPSG","6004"]], PRIMEM["Greenwich",0,AUTHORITY["EPSG","8901"]], UNIT["degree",0.01745329251994328,AUTHORITY["EPSG","9122"]], AUTHORITY["EPSG","4004"]]', desc='+proj=longlat +ellps=bessel +no_defs ')
        String sql = " EXECUTE PROCEDURE ST_REG_GEOM_COLUMN(schema=NULL, tablename='%s', geoColumn='%s', gtype=1, dimension=2, dtype=1, srid=%d)";
        String format = String.format(sql, tableName, geoColumn, srid);
        return format;
    }
        /**
     * 生成注册空间字段的语句
     *
     * @param layerName 表名称 
     * @param geoColumn 
     * @param geoTypeCode 空间数据类型
     * @param srid 空间数据的srid
     * @return 
     */
    public String produceRegisterGeomColumnSql(String layerName, String geoColumn,int geoTypeCode, int srid) {
        String regGeoColumn = "EXECUTE PROCEDURE ST_REG_GEOM_COLUMN(schema=NULL,tablename='" + layerName.toLowerCase() + "', geoColumn='"+geoColumn+"', gtype=" + geoTypeCode + ", dimension=2,dtype=1, srid=" + srid + ")";
        return regGeoColumn;
    }
    public String produceUnRegisterGeomColumnSql(String tableName) {

        String sql = " EXECUTE PROCEDURE st_unreg_geom_column(schema=NULL, tablename='%s')";

        String format = String.format(sql, tableName);
        return format;
    }
    public String produceUnRegisterGeomColumnSql(String tableName, String geoColumn) {

        String sql = " EXECUTE PROCEDURE st_unreg_geom_column(schema=NULL, tablename='%s',geocolumn='%s')";

        String format = String.format(sql, tableName, geoColumn);
        return format;
    }
    public String produceUnRegisterRasterColumnSql(String tableName, String rasterColumn) {

        String sql = " EXECUTE PROCEDURE st_unreg_rast_column(schema ='%s', tablename ='%s', rastColumn='%s')";

        String format = String.format(sql, m_DsUtils.getDataSource().getName(), tableName, rasterColumn);
        return format;
    }
    public int insertRecord(String tableName,
            ArrayList<BydField> fields)
    {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into  ").append(tableName);
        builder.append("(");
        String values ="";
        for (BydField bydField : fields) {
            builder.append(bydField.Name.replace("(", "、").replace(")", ""));
            builder.append(",");

            if (bydField.rov == BydField.RasterOrVector.vector) {
                //借用ST_GeometryfromText函数
                values += "," + ((Geometry) bydField.Value).toString();
                continue;
            }
            values += ",?";
        }
        builder.deleteCharAt(builder.length() - 1);
        values =values.substring(1);
        builder.append(")");
        builder.append(" Values(");
        builder.append(values).append(")");
        
         String sql =builder.toString();
        int executeUpdate=0;
        PreparedStatement state = null;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                m_DsUtils.con.setAutoCommit(true);
                state = m_DsUtils.createPreparedStatement(sql);
                int i = 1;
                for (BydField bydField : fields) {
                        if (bydField.rov == BydField.RasterOrVector.vector) {
                            //借用ST_GeometryfromText函数
                            continue;
                        } else {
                        if (bydField.Type == FieldType.integer) {
                            state.setInt(i, Integer.valueOf(String.valueOf(bydField.Value)));
                        } else if (bydField.Type == FieldType.float8) {
                            state.setFloat(i, Float.valueOf(String.valueOf(bydField.Value)));
                        } else if (bydField.Type == FieldType.date) {
                            state.setDate(i, Date.valueOf(String.valueOf(bydField.Value)));
                        } else if (bydField.Type == FieldType.timestamp) {

                            state.setTimestamp(i, Timestamp.valueOf(String.valueOf(bydField.Value)));
                        } else if (bydField.Type == FieldType.long_varchar) {

                            state.setString(i, String.valueOf(bydField.Value));

                        } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                             *暂设为100，字符串数据没有太多信息
                             */
                            state.setString(i, String.valueOf(bydField.Value));
                        } else {
                            //do nothing  //目前程序不可能执行到该步
                        }
                        i++;
                    }
                  
                }
                executeUpdate = state.executeUpdate();
            }
        } catch (SQLException ssException) {
            Logger.getLogger(classTag).log(Level.SEVERE, "插入记录"
                    + sql, ssException);
        } finally {
            m_DsUtils.closePrepareStatement(state);
            m_DsUtils.closeConnection();
        }
        return executeUpdate;
       
     
    }  
    
        public int updateRecord(String tableName, ArrayList<BydField> fields, ArrayList<String> primaryKeys)
    {
         //解析字段列表
        StringBuilder builder = new StringBuilder();
        builder.append("update  ").append(tableName).append(" set ");
        for (BydField bydField : fields) {
            if (primaryKeys.contains(bydField.Name)) {
                continue;
            }

            builder.append(bydField.Name.replace("(", "、").replace(")", ""));
            builder.append("=");
            if (bydField.rov == BydField.RasterOrVector.raster) {
                //本项目暂时用不到，不考虑实现
                //栅格数据的导入可以使用brimport工具 ，或st_import函数
            } else if (bydField.rov == BydField.RasterOrVector.vector) {
                //矢量字段
                //借用ST_GeometryfromText函数
                builder.append(((Geometry) bydField.Value).toString());
            } else {
                builder.append("?");
            }
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" where ");
        for (BydField bydField : fields) {
            if (!primaryKeys.contains(bydField.Name)) {
                continue;
            }
            builder.append(bydField.Name.replace("(", "、").replace(")", ""));
            builder.append("=");
            if (bydField.rov == BydField.RasterOrVector.raster) {
                //本项目暂时用不到，不考虑实现
                //栅格数据的导入可以使用brimport工具 ，或st_import函数
            } else if (bydField.rov == BydField.RasterOrVector.vector) {
                //矢量字段
                //借用ST_GeometryfromText函数
                builder.append(((Geometry) bydField.Value).toString());
            } else {
                builder.append("?");
            }
            builder.append(" and ");
        }
        builder.delete(builder.length()-5, builder.length()-1);

         String sql =builder.toString();
        int executeUpdate=0;
        PreparedStatement state = null;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                m_DsUtils.con.setAutoCommit(true);
                state = m_DsUtils.createPreparedStatement(sql);
                int i = 1;
                for (BydField bydField : fields) {
                    //设置更新值
                    if (primaryKeys.contains(bydField.Name)) {
                        continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.raster) {
                        //本项目暂时用不到，不考虑实现
                        //栅格数据的导入可以使用brimport工具 ，或st_import函数
                         continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.vector) {
                        //借用ST_GeometryfromText函数
                        continue;
                    } else if (bydField.Type == FieldType.integer) {
                        state.setInt(i, Integer.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.float8) {
                        state.setFloat(i, Float.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.date) {
                        state.setDate(i, Date.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.timestamp) {

                        state.setTimestamp(i, Timestamp.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.long_varchar) {

                        state.setString(i, String.valueOf(bydField.Value));

                    } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                         *暂设为100，字符串数据没有太多信息
                         */
                        state.setString(i, String.valueOf(bydField.Value));
                    } else {
                        //do nothing  //目前程序不可能执行到该步
                    }
                    i++;
                  
                }
                 for (BydField bydField : fields) {
                     //设置主键值
                    if (!primaryKeys.contains(bydField.Name)) {
                        continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.raster) {
                        //本项目暂时用不到，不考虑实现
                        //栅格数据的导入可以使用brimport工具 ，或st_import函数
                         continue;
                    }
                    if (bydField.rov == BydField.RasterOrVector.vector) {
                        //借用ST_GeometryfromText函数
                        continue;
                    } else if (bydField.Type == FieldType.integer) {
                        state.setInt(i, Integer.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.float8) {
                        state.setFloat(i, Float.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.date) {
                        state.setDate(i, Date.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.timestamp) {

                        state.setTimestamp(i, Timestamp.valueOf(String.valueOf(bydField.Value)));
                    } else if (bydField.Type == FieldType.long_varchar) {

                        state.setString(i, String.valueOf(bydField.Value));

                    } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                         *暂设为100，字符串数据没有太多信息
                         */
                        state.setString(i, String.valueOf(bydField.Value));
                    } else {
                        //do nothing  //目前程序不可能执行到该步
                    }
                    i++;
                  
                }
                executeUpdate = state.executeUpdate();
            }
        } catch (SQLException ssException) {
            Logger.getLogger(classTag).log(Level.SEVERE, "更新记录"
                    + sql, ssException);
        } finally {
            m_DsUtils.closePrepareStatement(state);
            m_DsUtils.closeConnection();
        }
        return executeUpdate;
       
     
    }  


 
    /**
     * 执行 SQL语句
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public int  executeSql(String sql) throws  SQLException
    {
        if (sql==null ||sql.isEmpty()) {
            Logger.getLogger(classTag).log(Level.WARNING, "参数有误");
            return  0;
        }
        int executeUpdate=0;
        Statement state = null;
        try {
            boolean openConnection = m_DsUtils.openConnection();
            if (openConnection) {

                m_DsUtils.con.setAutoCommit(true);
                state = m_DsUtils.createStatement();
                executeUpdate = state.executeUpdate(sql);
            }
        } catch (SQLException ssException) {
            Logger.getLogger(classTag).log(Level.SEVERE, "执行sql出错："
                    + sql, ssException);
            throw  ssException;
        } finally {
            m_DsUtils.closeStatement(state);
            m_DsUtils.closeConnection();
        }
        return executeUpdate;
    }
    
    public String produceUpdateSql(String tableName, ArrayList<BydField> fields, ArrayList<String> primaryKeys) {
        //解析字段列表
        StringBuilder builder = new StringBuilder();
        builder.append("update  ").append(tableName).append(" set ");
        for (BydField bydField : fields) {
            if (primaryKeys.contains(bydField.Name)) {
                continue;
            }

            builder.append(bydField.Name.replace("(", "、").replace(")", ""));
            builder.append("=");
            if (bydField.rov == BydField.RasterOrVector.raster) {
                //本项目暂时用不到，不考虑实现
                //栅格数据的导入可以使用brimport工具 ，或st_import函数
            } else if (bydField.rov == BydField.RasterOrVector.vector) {
                //矢量字段
                //借用ST_GeometryfromText函数
                builder.append(((Geometry) bydField.Value).toString());
            } else {

                if (bydField.Type == FieldType.integer
                        || bydField.Type == FieldType.float8
                        || bydField.Type == FieldType.date
                        || bydField.Type == FieldType.timestamp) {
                    builder.append(bydField.Value.toString());
                } else if (bydField.Type == FieldType.Long_Byte || bydField.Type == FieldType.long_varchar) {
                    //暂不考虑
                    builder.append("'").append(bydField.Value.toString()).append("'");
                } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                     *暂设为100，字符串数据没有太多信息
                     */
                    builder.append("'").append(bydField.Value.toString()).append("'");
                } else {
                    //do nothing  //目前程序不可能执行到该步
                    builder.append("'").append(bydField.Value.toString()).append("'");
                }

            }
            builder.append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(" where ");
        for (BydField bydField : fields) {
            if (!primaryKeys.contains(bydField.Name)) {
                continue;
            }
            builder.append(bydField.Name.replace("(", "、").replace(")", ""));
            builder.append("=");
            if (bydField.rov == BydField.RasterOrVector.raster) {
                //本项目暂时用不到，不考虑实现
                //栅格数据的导入可以使用brimport工具 ，或st_import函数
            } else if (bydField.rov == BydField.RasterOrVector.vector) {
                //矢量字段
                //借用ST_GeometryfromText函数
                builder.append(((Geometry) bydField.Value).toString());
            } else {

                if (bydField.Type == FieldType.integer
                        || bydField.Type == FieldType.float8
                        || bydField.Type == FieldType.date
                        || bydField.Type == FieldType.timestamp) {
                    builder.append(bydField.Value.toString());
                } else if (bydField.Type == FieldType.Long_Byte || bydField.Type == FieldType.long_varchar) {
                    //暂不考虑
                    builder.append("'").append(bydField.Value.toString()).append("'");
                } else if (bydField.Type == FieldType.varchar) {//需要考虑字段宽度
                      /*
                     *暂设为100，字符串数据没有太多信息
                     */
                    builder.append("'").append(bydField.Value.toString()).append("'");
                } else {
                    //do nothing  //目前程序不可能执行到该步
                }
            }
            builder.append(" and ");
        }
        builder.delete(builder.length()-5, builder.length()-1);

        return builder.toString();
    }

 
           
}
