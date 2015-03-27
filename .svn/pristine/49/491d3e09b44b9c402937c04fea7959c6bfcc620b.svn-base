package com.beyondb.gdal.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
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
 * 多线程数据插入
 *
 * @author guanml
 */
public class DataImport extends SwingWorker<Integer, Integer> {



    private NutDao dao;
    private JProgressBar bar;
    private Layer layer;
    private String sql;
    private int batch = 500;//一次固定执行数量
    private static final  Logger m_Logger= Logger.getLogger(DataImport.class.getName());

    public DataImport() {
   
    }

    public DataImport(JProgressBar bar, NutDao dao) {
        this.bar = bar;
        this.dao = dao;
    }

    public DataImport(JProgressBar bar, NutDao dao, Layer layer, String sql) {
        this.bar = bar;
        this.dao = dao;
    }

    public NutDao getDao() {
        return dao;
    }

    public void setDao(NutDao dao) {
        this.dao = dao;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        layer.ResetReading();
        Feature feature;
        int count = layer.GetFeatureCount();
        String msg ="图层 " + layer.GetName() + " 要素总数：" + count;
        m_Logger.log(Level.INFO, msg);
        int batchCount = 0;
        int totalCount = 0;
        int lastCount = 0;
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
                    insSql.params().set("shape", null);
//                    continue;
                } else {
                    //  feature.GetGeometryRef();
                    insSql.params().set("shape", geom.ExportToWkb());
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
                    totalCount += batch; //默认每500条数据添加一次
                    bar.setValue(100 * totalCount / count); //设置进度
                }
            } catch (Exception ex) {

                m_Logger.log(Level.SEVERE, "插入数据异常", ex);
                batchCount = 0;
                insSql.clearBatch();
            }
        }

        try {
            //final exeuteBatch
            if (batchCount <= batch && batchCount > 0) {
                dao.execute(insSql);
                insSql.clearBatch();
                totalCount += batchCount; //加入最后一次提交的数目
                bar.setValue(100 * totalCount / count); //设置进度
                msg ="图层 " + layer.GetName() + " 总共插入数据：" + totalCount + " 条";
                m_Logger.log(Level.OFF, msg);
            }
        } catch (Exception ex) {
            msg ="最后一次提交数据发生异常！，发生异常前已提交 " + totalCount + " 条数据。";
            m_Logger.log(Level.SEVERE, msg, ex);
        } finally {
            layer.delete();
        }
        return totalCount;
    }

    @Override
    protected void process(List<Integer> chunks) {
        super.process(chunks); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void done() {
        super.done(); //To change body of generated methods, choose Tools | Templates.
    }

}
