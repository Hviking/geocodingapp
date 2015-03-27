/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 倪永
 */
public class FeatureUtils {
    
    /**
     *将每行记录转为要素类对象 ，
     * 行记录中必须包含经纬度坐标列
     * @param captions 标题行
     * @param records  xls行记录
     * @param longitudeColumnName 经度列名称
     * @param latitudeColumnName 纬度列名称
     * @return
     */
    public static  ArrayList<Feature> readPoints2Features(String[] captions,
            String[][] records,
            String longitudeColumnName,String latitudeColumnName)
    {
        ArrayList<Feature> features = null;

        if (records != null && records.length > 0) {

            //读取标题行

            boolean isExist = false;

            int longIndex =0;
            int latIndex=0;
            for (int i = 0; i < captions.length; i++) {
                if (captions[i].equals(longitudeColumnName)
                       ) {
                    longIndex = i;
                  
                }
                if( captions[i].equals(latitudeColumnName)){
                    latIndex=i;
                }
            }
            isExist= true;
            if (!isExist) {
                Logger.getLogger(FeatureUtils.class.getName())
                        .log(Level.SEVERE, "excel没有列名称是" + longitudeColumnName + "和"
                                + latitudeColumnName + "的经纬度列");
                return null;
            }
        //    latIndex=longIndex+1;
            
            features = new ArrayList<>();
            Feature fea = new Feature();

            ArrayList<BydField> fields = new ArrayList<>();

            for (int i = 0; i < captions.length; i++) {
                if (i == longIndex ||i==latIndex) {
                    continue;
                }
                String caption = captions[i];
                BydField field = new BydField();
                field.Name = caption.replace(" ", "_");
                //暂时全部设置成字符串类型，不影响结果评判
                field.Type = BydField.FieldType.varchar;
                fields.add(field);
            }
            BydField geofield = new BydField();
            geofield.Name = "geometry";
            //暂时全部设置成字符串类型，不影响结果评判
            geofield.Type = BydField.FieldType.Geometry;
            geofield.rov = BydField.RasterOrVector.vector;
            fields.add(geofield);
            
            fea.Fields = fields;

            for (int i = 0; i < records.length; i++) {
                  
                try {
                    String[] row = records[i];
                    Feature tmpFeature = (Feature) fea.clone();
                    ArrayList<BydField> tmpBydFields = tmpFeature.Fields;
                    int tmpIndex =0;
                    for (int j = 0; j < row.length; j++) {
                        if (j == longIndex ||j==latIndex) {
                            continue;
                        }
                        tmpBydFields.get(tmpIndex++).Value = row[j];
                    }
                    Point p = new Point(Double.parseDouble(row[longIndex]),
                            Double.parseDouble(row[latIndex]));
                    tmpBydFields.get(tmpIndex).Value =p;
                    features.add(tmpFeature);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(FeatureUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return features;
    }
    
     /**
     *将每行记录转为要素类对象,行记录是纯属性列，没有空间列
     * @param captions 标题行
     * @param records  xls行记录
     * @return
     */
    public static  ArrayList<Feature> readAttributes2Features(String[] captions,
            String[][] records)
    {
        ArrayList<Feature> features = null;

        if (records != null && records.length > 0) {
            //读取标题行

            features = new ArrayList<>();
            Feature fea = new Feature();

            ArrayList<BydField> fields = new ArrayList<>();

            for (int i = 0; i < captions.length; i++) {
      
                String caption = captions[i];
                BydField field = new BydField();
                field.Name = caption.replace(" ", "_");
                //暂时全部设置成字符串类型，不影响结果评判
                field.Type = BydField.FieldType.varchar;
                fields.add(field);
            }
               
            fea.Fields = fields;

            for (int i = 0; i < records.length; i++) {
                  
                try {
                    String[] row = records[i];
                    Feature tmpFeature = (Feature) fea.clone();//深度拷贝
                    ArrayList<BydField> tmpBydFields = tmpFeature.Fields;
                    for (int j = 0; j < row.length; j++) {
  
                        tmpBydFields.get(j).Value = row[j];
                    }
                  
                    features.add(tmpFeature);
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(FeatureUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return features;
    }
    
    /**
     * 将单行记录转为要素类对象,行记录是纯属性列，没有空间列
     *
     * @param captions 标题行
     * @param record 行记录
     * @return
     */
    public static Feature readAttributes2Feature(String[] captions,
            String[] record) {
        String[][] records = new String[1][];
        records[0] = record;
        return readAttributes2Features(captions, records).get(0);
    }
}
