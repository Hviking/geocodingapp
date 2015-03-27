/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.spacialization.BydField;
import com.beyondb.spacialization.Feature;
import com.beyondb.spacialization.FeatureUtils;
import com.beyondb.ui.JPanel_txtIntoDB;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author 倪永
 */
public class ImportGeometryIntoBeyonDBSwingWorker extends SuperSwingWorker{
    

   private Map<Integer, Object> m_Record=null;
   JPanel_txtIntoDB m_Panel;
    private BydOperator m_BydOperator;
    private int isSuccessCreateIndex;
   public ImportGeometryIntoBeyonDBSwingWorker(JPanel_txtIntoDB app)
  {
       super(app, app);
         m_Panel =app;
  }

    /**
     *传递参数
     * @param tableName 表名称
     * @param cations 列标题
     * @param record  匹配结果
     * @param dataSource 数据源
     */
    public  void setParameters(String tableName,
            String[] cations,
            Map<Integer,Object> record,DataSource dataSource)
   {
        m_TableName = tableName;
        m_Captions = cations;
        m_Record = record;
        m_DataSource = dataSource;
        
        
        DataSourceUtils dataSourceUtils = new DataSourceUtils(m_DataSource);
        m_BydOperator = new BydOperator(dataSourceUtils);
        
   }
  
    @Override
    protected String doInBackground() throws Exception {
        m_Table.setEnabled(false);

        m_SelectedColIndexs = m_Table.getSelectedColumns();

        ArrayList<Feature> features = FeatureUtils
                .readPoints2Features(m_Captions,
                        transRecordsMap2StringArray(m_Record),
                        m_Captions[m_SelectedColIndexs[0]],
                        m_Captions[m_SelectedColIndexs[1]]);

        setDisplayStatus("保存记录到数据库表中...");
         boolean isSuccessImportFeature =importFeaturesIntoDB(m_TableName, features);

        if (isSuccessImportFeature) {
            setDisplayStatus("正在创建空间索引...");
            setDisplayProgress(10);
            if ((isSuccessCreateIndex=createSpIndex(m_TableName)) == 0) {
                setDisplayStatus("创建空间索引失败");
            }
            setDisplayProgress(100);
        }
        
        return "";
    }
    
        /**
     * 创建矢量数据的空间索引
     *
     * @param tableName 需要创建空间索引的表
     * @return true 表示创建成功，false 表示创建失败
     */
    private int createSpIndex(String tableName ) {

         String geoColumnName=m_BydOperator.getGeomColumnName(tableName);
        if (m_BydOperator.existSpIndex(tableName)) {
            m_BydOperator.dropSpIndex(tableName);
        }
        String bounds = m_BydOperator.getBoundsAsString(tableName, geoColumnName);
        return m_BydOperator.createSpatialIndex(tableName, geoColumnName, bounds);
    }

   
    /**
     * 计算空间表的范围
     *
     * @param tableName 空间表名称
     * @param geomColumn 空间列名称
     * @return 一组空间范围数组 double[0] =minx,double[1]=miny ,double[2] =
     * maxX,dobule[3]=maxY;
     */
    private double[] getBoundsAsDoubleArray(String tableName, String geomColumn) {
        double[] bounds = new double[4];
        String bound = m_BydOperator.getBoundsAsString(tableName, geomColumn);
        String res = bound.replaceAll("\\(", "").replaceAll("\\)", "");  //去除括号
        String[] values = res.split(",");
        double minx = Double.parseDouble(values[0]);
        double miny = Double.parseDouble(values[1]);
        double maxX = Double.parseDouble(values[2]);
        double maxY = Double.parseDouble(values[3]);
        bounds[0] = minx;
        bounds[1] = miny;
        bounds[2] = maxX;
        bounds[3] = maxY;
        return bounds;
    }
    
    @Override
    protected  void done()
    {
          //done()本身是在EDT中执行的    
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {    
                    setDisplayStop();
                    m_Table.setEnabled(true);

                    String msg="";
                    if (m_SucessCounter != m_Count) {
                        msg = m_TaskName + "\n已成功导入记录数：" + m_SucessCounter + "/" + m_Count;
                        msg += "\n" +"出现问题的记录（如果大于3条取前3条）是：";
                        msg += "\n" + "-=================================================-";
                        int i = 0;
                        for (Feature fea : m_failsImportFeatures) {
                            if (i > 2) {
                                break;
                            }
                            for (BydField bydField : fea.Fields) {
                                {
                                    msg += "\n" + bydField.Name + " " + bydField.Value;
                                }
                            }
                            msg += "\n" + "------------------------------------------------";
                            i++;
                        }
                        msg += "\n" + "-=================================================-";
                        
                    }
                    if (isSuccessCreateIndex == 0) {
                        msg += "\n" + "创建空间索引失败!";
                    }
                    if (!msg.isEmpty()) {
                        m_Dialog.setShowMessage(msg, JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                     msg = String.valueOf(get());
                    if (!msg.isEmpty()) {
                        m_Panel.setShowMessage(msg, JOptionPane.ERROR);
                    }

                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(ImportGeometryIntoBeyonDBSwingWorker
                            .class.getName()).log(Level.SEVERE,"",ex);
                }
                finally{
                   m_Panel.showDataBaseTables();
                   ArrayList<String> btnArrayList = new ArrayList<>();
                    btnArrayList.add("地址解析");
                    btnArrayList.add("常规入库");
                    btnArrayList.add("空间化入库");
                    btnArrayList.add("修改坐标系");
                    m_Panel.setButtonEnable(btnArrayList, true);
      
                }
            }
        });
        
    }
    
    
    
}
