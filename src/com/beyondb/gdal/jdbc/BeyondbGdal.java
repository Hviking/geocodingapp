package com.beyondb.gdal.jdbc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.ogr.ogrConstants;

/**
 * 使用OGR 来解析矢量数据，构建创建 BeyonDB 空间数据库表的sql 语句，并且注册空间表，创建空间索引。
 *
 * @author guanml
 */
public class BeyondbGdal {

    String pszFormat = "ESRI Shapefile";
    String pszDataSource = null;
    String pszDestDataSource = null;
    boolean bAppend = false, bUpdate = false, bOverwrite = false;

    public void init() {
        ogr.DontUseExceptions();
        ogr.RegisterAll();
    }

    static int wkbFlatten(int eType) {
        return eType & (~ogrConstants.wkb25DBit);
    }

    static boolean IsNumber(String pszStr) {
        try {
            Double.parseDouble(pszStr);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public DataSource openDataSource(String pszDataSource, boolean update) {
        /* -------------------------------------------------------------------- */
        /*      Open data source.                                               */
        /* -------------------------------------------------------------------- */
        DataSource poDS;

        poDS = ogr.Open(pszDataSource, update);


        /* -------------------------------------------------------------------- */
        /*      Report failure                                                  */
        /* -------------------------------------------------------------------- */
        if (poDS == null) {
            System.err.println("FAILURE:\n"
                    + "Unable to open datasource ` " + pszDataSource + "' with the following drivers.");

            for (int iDriver = 0; iDriver < ogr.GetDriverCount(); iDriver++) {
                System.err.println("  . " + ogr.GetDriver(iDriver).GetName());
            }


        }
        return poDS;
    }

    public static void main(String[] args) {
        ogr.DontUseExceptions();
        ogr.RegisterAll();
        gdal.SetConfigOption("SHAPE_ENCODING", "GBK");
        DataSource poDS;
        String pszFormat = "ESRI Shapefile";
//        String pszDataSource = "/run/media/guanml/data/空间数据/data/NRW_river.shp";
        String pszDataSource = "/home/guanml/data/world/World.shp";
//        String pszDataSource = "/home/testnv/data/shapefiles/states.shp";
//        String pszDataSource = "/home/testnv/data/china/BOUNT_line.shp";
        poDS = ogr.Open(pszDataSource, 0);

        if (poDS == null) {
            System.out.println("Open datasource " + pszDataSource + " failed");
            System.exit(1);
        }
        Layer layer = poDS.GetLayerByName("World");
        layer.ResetReading();
        System.out.println(GeoTypeMap.getTypeName(layer.GetGeomType()));
        int typeCode = layer.GetGeomType();
        String pr2o = layer.GetSpatialRef().GetAttrValue("PROJECTION");


        System.out.println(pr2o);
//        String epsg = OSRGetAttrValue( sourceSRS, "AUTHORITY", 1 );
        System.out.println("geom typecode :" + typeCode);

        BeyondbGdal bydGdal = new BeyondbGdal();
        String createTable = bydGdal.encodeCreateTableSQL(layer);

        layer.ResetReading();
        String createSpidex = bydGdal.encodeCreateSpIndex(layer);
        String createSqe = bydGdal.encodeSequnce(layer.GetName());
        String regGeomColumn = bydGdal.encodeRegGeoColumn(layer.GetName(), 3, 4326);
        String insertSql = bydGdal.encodePrePareInsertSQL(layer, 4326);

        System.out.println("SQL create sqe : " + createSqe);
        System.out.println("SQL create table:" + createTable);
        System.out.println("SQL  reg GeomColumn :" + regGeomColumn);
        System.out.println("SQL create spidx : " + createSpidex);
        System.out.println("SQL insert data  : " + insertSql);
//        Feature feature;
//        while ((feature = layer.GetNextFeature()) != null) {
//            FeatureDefn fd = layer.GetLayerDefn();
//            int field;
//            for (field = 0; field < fd.GetFieldCount(); field++) {
//                FieldDefn fdf = fd.GetFieldDefn(field);
//                if (fdf.GetFieldType() == ogr.OFTInteger) {
//                    System.out.printf("%s : %d \n", fdf.GetName(), feature.GetFieldAsInteger(field));
//                } else if (fdf.GetFieldType() == ogr.OFTReal) {
//                    System.out.printf("%s : %.3f \n", fdf.GetName(), feature.GetFieldAsDouble(field));
//                } else if (fdf.GetFieldType() == ogr.OFTString) {
//                    System.out.printf("%s : %s \n", fdf.GetName(), feature.GetFieldAsString(field));
//                } else if (fdf.GetFieldType() == ogr.OFTDate) {
//                    String st = feature.GetFieldAsString(field);
//                    System.out.printf("%s : %s \n", fdf.GetName(), feature.GetFieldAsString(field));
//                } else {
//                    System.out.printf("%s : %s \n", fdf.GetName(), feature.GetFieldAsString(field));
//                }
//            }
//            Geometry geom;
//            geom = feature.GetGeometryRef();
//            String wkt = geom.ExportToWkt();
//            String type = geom.GetGeometryName();
//            System.out.println("GeometryNAme :" + type);
//            System.out.println("Geometry :" + wkt);
//            feature.delete();
//        }
//        poDS.delete();
    }

    public void handleGJBFile(DataSource ds) {
        int count = ds.GetLayerCount();
        for (int i = 0; i < count; i++) {
            Layer gjbLayer = ds.GetLayer(i);
            gjbLayer.ResetReading();
            System.out.println(GeoTypeMap.getTypeName(gjbLayer.GetGeomType()));
            int typeCode = gjbLayer.GetGeomType();
            String pr2o = gjbLayer.GetSpatialRef().GetAttrValue("PROJECTION");


            System.out.println(pr2o);
//        String epsg = OSRGetAttrValue( sourceSRS, "AUTHORITY", 1 );
            System.out.println("geom typecode :" + typeCode);

            BeyondbGdal bydGdal = new BeyondbGdal();
            String createTable = bydGdal.encodeCreateTableSQL(gjbLayer);

            gjbLayer.ResetReading();
            String createSpidex = bydGdal.encodeCreateSpIndex(gjbLayer);
            String createSqe = bydGdal.encodeSequnce(gjbLayer.GetName());
            String regGeomColumn = bydGdal.encodeRegGeoColumn(gjbLayer.GetName(), 3, 4326);
            String insertSql = bydGdal.encodePrePareInsertSQL(gjbLayer, 4326);

            System.out.println("SQL create sqe : " + createSqe);
            System.out.println("SQL create table:" + createTable);
            System.out.println("SQL  reg GeomColumn :" + regGeomColumn);
            System.out.println("SQL create spidx : " + createSpidex);
            System.out.println("SQL insert data  : " + insertSql);
        }
    }

    /**
     * 创建预处理语句 encode preparedstatement
     *
     * @param schema
     * @return 插入空间数据的sql预处理语句
     */
    public String encodePrePareInsertSQL(Layer layer, int srid) {
        int count = layer.GetLayerDefn().GetFieldCount();
        int typeCode = -1;
        String typeName = layer.GetName();
        int fc = layer.GetFeatureCount();
        Logger.getLogger(OgrDataImport.class.getName()).log(Level.INFO, layer.GetName()+" 要素总数：{0}", fc);
        StringBuilder builder = new StringBuilder();
        StringBuilder values = new StringBuilder();
        builder.append("insert into ").append(typeName).append(" (");
        typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            builder.append("shape");
            values.append("ST_GeomFromText(?,").append(srid).append(")");
        }
        //设置属性字段
        for (int u = 0; u < count; u++) {
            FieldDefn fdf = layer.GetLayerDefn().GetFieldDefn(u);
            System.out.println(fdf.GetName());
            builder.append(",").append(fdf.GetName());
            values.append(",?");
//              values.append(",@").append(fdf.GetName());
        }
        builder.append(") values (");
        values.append(")");
        return builder.toString() + values.toString();
    }

    /**
     * 生成创建空间表语句
     *
     * @param layer 图层
     * @return sql 语句
     */
    public String encodeCreateTableSQL(Layer layer) {
        StringBuilder builder = new StringBuilder();
        builder.append("create table ").append(layer.GetName()).append("(");
        builder.append("fid int ").append("default").append(" seq_").append(layer.GetName()).append(".nextval not null,");

        layer.ResetReading();

        Feature feature;
        int typeCode = layer.GetGeomType();
        if (typeCode != -1) {
            builder.append("shape st_geometry, ");
        }

        feature = layer.GetNextFeature();
        if (feature != null) {
            FeatureDefn fd = layer.GetLayerDefn();
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
            feature.delete();

        }
        return builder.toString();
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
        double extens[] = layer.GetExtent();
        double minx, miny, maxx, maxy;
        minx = extens[0];
        maxx = extens[1];
        miny = extens[2];
        maxy = extens[3];
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("create  index spidx_").append(layer.GetName()).append(" on ");
        sqlBuilder.append(layer.GetName()).append("(shape) with structure=rtree, range=((").append(minx)
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

        String regGeoColumn = "EXECUTE PROCEDURE ST_REG_GEOM_COLUMN(schema=NULL,tablename='" + layerName + "', geoColumn='shape', gtype=" + geoTypeCode + ", dimension=2,dtype=1, srid=" + srid + ")";
        return regGeoColumn;
    }
}
