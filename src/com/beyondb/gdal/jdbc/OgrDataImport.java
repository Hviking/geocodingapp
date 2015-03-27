package com.beyondb.gdal.jdbc;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;

/**
 * OGR解析数据并将数据通过JDBC写入数据库
 *
 * @author guanml <guanminglin@gmail.com>
 */
public class OgrDataImport {

    private NutDao dao = null;
    private int srid = 0;
   
    private int batch = 500;
     private static final  Logger m_Logger= Logger.getLogger(OgrDataImport.class.getName());;

    public OgrDataImport(NutDao dao, int srid) {
        this.dao = dao;
        this.srid = srid;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(int batch) {
        this.batch = batch;
    }

    public NutDao getDao() {
        return dao;
    }

    public void setDao(NutDao dao) {
        this.dao = dao;
    }

    public int getSrid() {
        return srid;
    }

    public void setSrid(int srid) {
        this.srid = srid;
    }

    
    /**
     * 生成创建空间表语句
     * 
     * 增加空间字段名参数 by  niy
     *
     * @param layer 图层
     * @param layerName 指定的图层名称，即表名称
     * @param geomColumnName
     * @return sql 构建表的sql语句
     */
    public String encodeCreateTableSQL(Layer layer, String layerName,String geomColumnName) {
        StringBuilder builder = new StringBuilder();
        builder.append("create table ").append(layerName).append("(");
        builder.append("fid int ").append("default").append(" seq_").append(layerName).append(".nextval not null,");
        layer.ResetReading();
        int typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            if (!geomColumnName.isEmpty()) {
                 builder.append(geomColumnName);
            }else 
            {
                builder.append("shape");
            }
             builder.append(" st_geometry,");
        }
        FeatureDefn fd = layer.GetLayerDefn();
        if (fd != null) {
            int field;
            for (field = 0; field < fd.GetFieldCount(); field++) {
                FieldDefn fdf = fd.GetFieldDefn(field);
                if (fdf.GetFieldType() == ogr.OFTInteger) {
                    builder.append(fdf.GetName()).append(" integer");
                } else if (fdf.GetFieldType() == ogr.OFTReal) {
                    builder.append(fdf.GetName()).append(" float8");
                } else if (fdf.GetFieldType() == ogr.OFTString) {
                    builder.append(fdf.GetName()).append(" varchar(").append(fdf.GetWidth()).append(")");
                } else if (fdf.GetFieldType() == ogr.OFTDate) {
                    builder.append(fdf.GetName()).append(" date");
                } else if (fdf.GetFieldType() == ogr.OFTDateTime) {
                    builder.append(fdf.GetName()).append(" timestamp");
                } else if (fdf.GetFieldType() == ogr.OFTTime) {
                    builder.append(fdf.GetName()).append(" time");
                } else if (fdf.GetFieldType() == ogr.OFTWideString) {
                    builder.append(fdf.GetName()).append(" long varchar");
                } else if (fdf.GetFieldType() == ogr.OFTBinary) {
                    builder.append(fdf.GetName()).append(" Long Byte");
                }
                builder.append(",");
            }

            builder.append("primary key(fid)");
            builder.append(")");
            fd.delete();
        }
        return builder.toString();
    }

    /**
     * 生成创建军交格式空间表语句
     *
     * @param layer 图层
     * @return sql 语句
     */
    public String encodeCreateGJBTableSQL(Layer layer, String layerName) {
        StringBuilder builder = new StringBuilder();

        builder.append("create table ").append(layerName).append("(");
        builder.append("fid int ").append("default").append(" seq_").append(layerName).append(".nextval not null,");
        layer.ResetReading();
        int typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            builder.append("shape st_geometry,");
        }

        FeatureDefn fd = layer.GetLayerDefn();
        if (fd != null) {
            int field;
            for (field = 0; field < fd.GetFieldCount(); field++) {
                FieldDefn fdf = fd.GetFieldDefn(field);
                if (fdf.GetFieldType() == ogr.OFTInteger) {
                    builder.append(fdf.GetName()).append(" integer");
                } else if (fdf.GetFieldType() == ogr.OFTReal) {
                    builder.append(fdf.GetName()).append(" float8");
                } else if (fdf.GetFieldType() == ogr.OFTString) {
                    builder.append(fdf.GetName()).append(" varchar(").append(fdf.GetWidth()).append(")");
                } else if (fdf.GetFieldType() == ogr.OFTDate) {
                    builder.append(fdf.GetName()).append(" date");
                } else if (fdf.GetFieldType() == ogr.OFTDateTime) {
                    builder.append(fdf.GetName()).append(" timestamp");
                } else if (fdf.GetFieldType() == ogr.OFTTime) {
                    builder.append(fdf.GetName()).append(" time");
                } else if (fdf.GetFieldType() == ogr.OFTWideString) {
                    builder.append(fdf.GetName()).append(" long varchar");
                } else if (fdf.GetFieldType() == ogr.OFTBinary) {
                    builder.append(fdf.GetName()).append(" Long Byte");
                }

                builder.append(",");
            }

            //添加图幅id
            builder.append("tf_name varchar(50),");
            builder.append("tf_id interger,");
//            builder.append(" foreign key(tf_id) references md_tf_info(tf_id),");
            builder.append("primary key(fid)");
            builder.append(")");
            fd.delete();

        }
        return builder.toString();
    }

    /**
     * 插入数据
     *
     * @param layer 数据图层
     * @param geoColumn  指定几何字段名称 added by niy
     * @param sql 插入数据的预编译 sql 语句。
     * @return 插入数据的总数
     */
    public int insert(Layer layer, String geoColumn,String sql) {
        layer.ResetReading();
        Feature feature;
        int count = layer.GetFeatureCount();
        String msg ="图层 " + layer.GetName() + " 要素总数：" + count;
                m_Logger.log(Level.INFO, msg);

        int batchCount = 0;
        int totalCount = 0;
        int lastCount =0;
        //预先设置sql 锁模型
        Sql setlevel = Sqls.create("set lockmode session where level=ROW");
        dao.execute(setlevel);
        Sql insSql = Sqls.create(sql);

        while ((feature = layer.GetNextFeature()) != null) {
            try {
                FeatureDefn fd = layer.GetLayerDefn();
                Geometry geom = feature.GetGeometryRef();
                if (geom == null) {
       
                       msg = "geometry is null";
                    
                    m_Logger.log(Level.INFO, msg);
                    insSql.params().set(geoColumn, null);
//                    continue;
                } else {
                    //  feature.GetGeometryRef();
                    insSql.params().set(geoColumn, geom.ExportToWkb());
//                insSql.params().set("shape", geom.ExportToWkt());
                }

                int field;
                for (field = 0; field < fd.GetFieldCount(); field++) {
                    FieldDefn fdf = fd.GetFieldDefn(field);
                    if (fdf.GetFieldType() == ogr.OFTInteger) {
                        insSql.params().set(fdf.GetName(), feature.GetFieldAsInteger(field));
                    } else if (fdf.GetFieldType() == ogr.OFTReal) {
                        insSql.params().set(fdf.GetName(), feature.GetFieldAsDouble(field));
                    } else if (fdf.GetFieldType() == ogr.OFTString) {
                        insSql.params().set(fdf.GetName(), feature.GetFieldAsString(field));
                    } else if (fdf.GetFieldType() == ogr.OFTDate) {
                        try {
                            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = feature.GetFieldAsString(field);
                            if (!dateString.isEmpty()) {
                                dateString = dateString.replaceAll("/", "-");
                                Date myDate = formater.parse(dateString);
                                insSql.params().set(fdf.GetName(), dateString);
                            } else {
                                insSql.params().set(fdf.GetName(), null);
                            }

                        } catch (ParseException ex) {
                            Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (fdf.GetFieldType() == ogr.OFTDateTime) {
                        try {
                            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateString = feature.GetFieldAsString(field);
                            if (!dateString.isEmpty()) {
                                dateString = dateString.replaceAll("/", "-");
                                Date myDate = formater.parse(dateString);
                                insSql.params().set(fdf.GetName(), dateString);
                            } else {
                                insSql.params().set(fdf.GetName(), null);
                            }

                        } catch (ParseException ex) {
                            Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (fdf.GetFieldType() == ogr.OFTTime) {
                        try {
                            SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
                            String dateString = feature.GetFieldAsString(field);
                            if (!dateString.isEmpty()) {
                                dateString = dateString.replaceAll("/", "-");
                                Date myDate = formater.parse(dateString);
                                insSql.params().set(fdf.GetName(), dateString);
                            } else {
                                insSql.params().set(fdf.GetName(), null);
                            }

                        } catch (ParseException ex) {
                            Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (fdf.GetFieldType() == ogr.OFTWideString) {
                        insSql.params().set(fdf.GetName(), feature.GetFieldAsString(field));
                    } else if (fdf.GetFieldType() == ogr.OFTBinary) {
                        insSql.params().set(fdf.GetName(), feature.GetFieldAsString(field));
                    }

                }

                feature.delete();
                insSql.addBatch();
                batchCount++;

                if (batchCount == batch) {
                    dao.execute(insSql);
                    insSql.clearBatch();
                    batchCount = 0;
//                    lastCount = totalCount;
                    totalCount += batch; //默认每500条数据添加一次
                  
                }
            } catch (Exception ex) {
                
                m_Logger.log(Level.SEVERE,"插入数据异常",ex);
                batchCount = 0;
                insSql.clearBatch();
            }
        }

        try {
            //final exeuteBatch
            if (batchCount <= batch && batchCount > 0) {
                dao.execute(insSql);
                insSql.clearBatch();
//                lastCount = totalCount;
                totalCount += batchCount; //加入最后一次提交的数目
                msg ="图层 " + layer.GetName() + " 总共插入数据：" + totalCount + " 条";
                 m_Logger.log(Level.INFO,msg); 
            }
        } catch (Exception ex) {
              msg ="最后一次提交数据发生异常！，发生异常前已提交 " + totalCount + " 条数据。";
            m_Logger.log(Level.SEVERE, msg, ex);
        } finally {
            layer.delete();
        }
        return totalCount;
    }

    /**
     * 插入gjb数据
     *
     * @param layer 数据图层
     * @param sql 插入数据的预编译 sql 语句。
     * @return
     */
    public int insertGJBData(Layer layer, String sql) {
        layer.ResetReading();
        Feature feature;
        int batchCount = 0;
        int totalCount = 0;
        Sql insSql = Sqls.create(sql);
        Sql setlevel = Sqls.create("set lockmode session where level=ROW");
        dao.execute(setlevel);
        try {
//            if (layer.GetName().toLowerCase().contains("r")) {
//                if (LOG.isDebugEnabled()) {
//                    LOG.debug("begin r layer");
//                }
//            }

            while ((feature = layer.GetNextFeature()) != null) {
                try {
                    FeatureDefn fd = layer.GetLayerDefn();

                    Geometry geom = feature.GetGeometryRef();
                    if (geom == null) {
//                        LOG.debug("geometry is null");
                        insSql.params().set("shape", null);
//                        continue;
                    } else {
                        //设置空间字段数据
                        insSql.params().set("shape", geom.ExportToWkb());
                    }

                    //设置数据插入时间
                    insSql.params().set("updatetime", new Timestamp(new Date().getTime()).toString());

                    int field;
                    for (field = 0; field < fd.GetFieldCount(); field++) {
                        FieldDefn fdf = fd.GetFieldDefn(field);
                        if (fdf.GetFieldType() == ogr.OFTInteger) {
                            insSql.params().set(fdf.GetName(), feature.GetFieldAsInteger(field));
                        } else if (fdf.GetFieldType() == ogr.OFTReal) {
                            if (feature.IsFieldSet(field)) {
                                insSql.params().set(fdf.GetName(), feature.GetFieldAsDouble(field));
                            } else {
                                insSql.params().set(fdf.GetName(), null);
                            }
                        } else if (fdf.GetFieldType() == ogr.OFTString) {
                            if (feature.IsFieldSet(field)) {
                                //增加乱码字符窜处理
                                insSql.params().set(fdf.GetName(), errorStringHandle(feature.GetFieldAsString(field)));
                            } else {
                                insSql.params().set(fdf.GetName(), null);
                            }
                        } else if (fdf.GetFieldType() == ogr.OFTDate) {
                            try {
                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = feature.GetFieldAsString(field);
                                if (!dateString.isEmpty()) {
                                    dateString = dateString.replaceAll("/", "-");
                                    Date myDate = formater.parse(dateString);
                                    insSql.params().set(fdf.GetName(), dateString);
                                } else {
                                    insSql.params().set(fdf.GetName(), null);
                                }

                            } catch (ParseException ex) {
                                Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (fdf.GetFieldType() == ogr.OFTDateTime) {
                            try {
                                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateString = feature.GetFieldAsString(field);
                                if (!dateString.isEmpty()) {
                                    dateString = dateString.replaceAll("/", "-");
                                    Date myDate = formater.parse(dateString);
                                    insSql.params().set(fdf.GetName(), dateString);
                                } else {
                                    insSql.params().set(fdf.GetName(), null);
                                }

                            } catch (ParseException ex) {
                                Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (fdf.GetFieldType() == ogr.OFTTime) {
                            try {
                                SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");
                                String dateString = feature.GetFieldAsString(field);
                                if (!dateString.isEmpty()) {
                                    dateString = dateString.replaceAll("/", "-");
                                    Date myDate = formater.parse(dateString);
                                    insSql.params().set(fdf.GetName(), dateString);
                                } else {
                                    insSql.params().set(fdf.GetName(), null);
                                }

                            } catch (ParseException ex) {
                                Logger.getLogger(OgrDataImport.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (fdf.GetFieldType() == ogr.OFTWideString) {
                            //增加乱码字符窜处理
                            insSql.params().set(fdf.GetName(), errorStringHandle(feature.GetFieldAsString(field)));
                        } else if (fdf.GetFieldType() == ogr.OFTBinary) {
                            insSql.params().set(fdf.GetName(), feature.GetFieldAsString(field));
                        }
                    }

                    feature.delete();
                    insSql.addBatch();
                    batchCount++;

                    if (batchCount == batch) {
                        dao.execute(insSql);
                        insSql.clearBatch();
                        batchCount = 0;
                        totalCount += batch; //默认每500个数据添加一次
                    }

                } catch (Exception ex) {
                  
                    batchCount = 0;
                    insSql.clearBatch();
                }
            }

            //final exeuteBatch
            if (batchCount <= 500 && batchCount > 0) {
                dao.execute(insSql);
                insSql.clearBatch();
                totalCount += batchCount; //加入最后一次提交的数目
            }
        } catch (Exception ex) {
            String msg = "最后一次提交数据发生异常！，发生异常前已提交 " + totalCount + " 条数据。";
            m_Logger.log(Level.SEVERE, msg, ex);
        } finally {
            layer.delete();
        }
        return totalCount;
    }

    /**
     * 处理转换编码错误的字符窜。
     *
     * @param errorString
     * @return
     */
    public String errorStringHandle(String errorString) {
        if (errorString.equals("***ERROR_CONVERT_RESULT_STRING***")) {
            errorString = "F";

        }
        return errorString;
    }

    /**
     * 字符串转换为java.util.Date<br>
     * 支持格式为 yyyy.MM.dd G 'at' hh:mm:ss z 如 '2002-1-1 AD at 22:10:59 PSD'<br>
     * yy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'<br>
     * yy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'<br>
     * yy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00' <br>
     * yy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am' <br>
     *
     * @param time String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date stringToDate(String time) {
        SimpleDateFormat formatter;
        int tempPos = time.indexOf("AD");
        time = time.trim();
        formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        if (tempPos > -1) {
            time = time.substring(0, tempPos)
                    + "公元" + time.substring(tempPos + "AD".length());//china 
            formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        }
        tempPos = time.indexOf("-");
        if (tempPos > -1 && (!time.contains(" "))) {
            formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
        } else if ((time.contains("/")) && (time.contains(" "))) {
            formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else if (time.contains("-") && time.contains(" ")) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (time.contains("/") && (time.contains("am")) || (time.contains("pm"))) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        } else if ((time.contains("-")) && (time.contains("am")) || (time.contains("pm"))) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        }
        ParsePosition pos = new ParsePosition(0);
        java.util.Date ctime = formatter.parse(time, pos);

        return ctime;
    }

    /**
     * 创建预处理语句 encode preparedstatement<br>
     * 表名称采用 Layer的名称。
     *
     * @param layer tuceng
     * @param geoColumnName 指定几何字段名称，added by niy
     * @param srid 空间参考srid，例如 4326
     * @return 插入空间数据的sql预处理语句
     */
    public String encodePrePareInsertSQL(Layer layer,String geoColumnName, int srid) {
        String sql = encodePrePareInsertSQL(layer, layer.GetName(), geoColumnName, srid);
        return sql;
    }

    /**
     * 创建预处理语句 encode preparedstatement
     *
     * @param layer 矢量图层
     * @param tableName 指定的图层名称即 表名称
     * @param geoColumnName  指定几何字段名称，added by niy
     * @param srid 空间参考srid，例如：4326
     * @return 插入空间数据的sql预处理语句
     */
    public String encodePrePareInsertSQL(Layer layer, String tableName, String geoColumnName,int srid) {
        int count = layer.GetLayerDefn().GetFieldCount();
        int typeCode = -1;
        StringBuilder builder = new StringBuilder();
        StringBuilder values = new StringBuilder();
        builder.append("insert into ").append(tableName).append(" (");
        typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            builder.append(geoColumnName);
            values.append("st_geomfrombinary(@").append(geoColumnName).append(",").append((long) srid).append(")");
//            values.append("ST_GeomFromText(@shape,").append(srid).append(")");
        }
        //设置属性字段
        for (int u = 0; u < count; u++) {
            FieldDefn fdf = layer.GetLayerDefn().GetFieldDefn(u);
//            if (LOG.isDebugEnabled()) {
//                LOG.debug("图层" + layerName + " 字段 " + u + " 名称：" + fdf.GetName());
//            }
            builder.append(",").append(fdf.GetName());
            values.append(",@").append(fdf.GetName());
        }
        builder.append(") values (");
        values.append(")");
        String sql = builder.toString() + values.toString();
//        if (LOG.isDebugEnabled()) {
//            LOG.debug("构建的预处理插入语句：" + sql);
//        }
        return sql;
    }

    /**
     * 创建预处理语句 encode preparedstatement
     *
     * @param layer
     * @param layerName
     * @param srid
     * @param tf_name
     * @param tf_id
     * @return 插入空间数据的sql预处理语句
     */
    public String encodePrePareGJBInsertSQL(Layer layer, String layerName, int srid, String tf_name, int tf_id) {
        int count = layer.GetLayerDefn().GetFieldCount();
        int typeCode = -1;
        StringBuilder builder = new StringBuilder();
        StringBuilder values = new StringBuilder();
        builder.append("insert into ").append(layerName).append(" (");
        typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            builder.append("shape");
            builder.append(",tf_name");
            builder.append(",tf_id");
            builder.append(",updatetime");
            values.append("ST_GeomFromBinary(@shape,").append((long) srid).append("),");
            values.append("'").append(tf_name).append("'");  //设置图幅名称
            values.append(",").append(tf_id);  //设置图幅序列号
            values.append(",").append("@updatetime");  //设置插入数据时间

        }
        //设置属性字段
        for (int u = 0; u < count; u++) {
            FieldDefn fdf = layer.GetLayerDefn().GetFieldDefn(u);
            builder.append(",").append(fdf.GetName());
            values.append(",@").append(fdf.GetName());
        }
        builder.append(") values (");
        values.append(")");
        String sql = builder.toString() + values.toString();
    
        return sql;
    }

    /**
     * 生成创建序列的语句
     *
     * @param layerName
     * @return
     */
    public String encodeSequnce(String layerName) {
        StringBuilder createSeq = new StringBuilder();
        return createSeq.append("create sequence seq_").append(layerName).append(" minvalue 0 start with 0 nocache").toString();
    }

    /**
     * 生成创建空间索引语句。
     *
     * @param layer 图层
     * @return sql 语句
     */
    public String encodeCreateSpIndex(Layer layer) {
        return encodeCreateSpIndex(layer, layer.GetName());
    }

    /**
     * 生成创建空间索引语句。
     *
     * @param layer 图层
     * @param layerName
     * @return sql 语句
     */
    public String encodeCreateSpIndex(Layer layer, String layerName) {
        double extens[] = layer.GetExtent();
        double minx, miny, maxx, maxy;
        minx = extens[0];
        maxx = extens[1];
        miny = extens[2];
        maxy = extens[3];
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("create  index spidx_").append(layerName).append(" on ");
        sqlBuilder.append(layerName).append("(shape) with structure=rtree,  range=((").append(minx)
                .append(",").append(miny).append("),(").append(maxx).append(",").append(maxy).append("))");

        return sqlBuilder.toString();
    }

    /**
     * 生成注册空间字段的语句
     *
     * @param layerName 表名称
     * @param geoTypeCode 空间数据类型
     * @param srid 空间数据的srid
     * @return sql 语句
     */
    public String encodeRegGeoColumn(String layerName, int geoTypeCode, int srid) {
        String regGeoColumn = "EXECUTE PROCEDURE ST_REG_GEOM_COLUMN(schema=NULL,tablename='" + layerName.toLowerCase() + "', geoColumn='shape', gtype=" + geoTypeCode + ", dimension=2,dtype=1, srid=" + srid + ")";
        return regGeoColumn;
    }

}
