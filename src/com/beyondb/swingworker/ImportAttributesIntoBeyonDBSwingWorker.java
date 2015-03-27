/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.datasource.DataSource;
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
 *常规属性数据入库
 * @author 倪永
 */
public class ImportAttributesIntoBeyonDBSwingWorker extends  SuperSwingWorker{
     Map<Integer, Object> m_RecordMap=null;
     JPanel_txtIntoDB m_Panel;
     
    public ImportAttributesIntoBeyonDBSwingWorker(JPanel_txtIntoDB app) {
        super(app, app);
        m_Panel = app;
    }

    /**
     *传递参数
     * @param tableName 表名称
     * @param cations 列标题
     * @param recordMap  记录
     * @param datasource 数据源
     */
    public  void setParameters(String tableName,
            String[] cations,
            Map<Integer,Object> recordMap,DataSource datasource)
   {
        m_TableName = tableName;
        m_Captions = cations;
        m_RecordMap = recordMap;
        m_DataSource = datasource;
   }
    @Override
    protected String doInBackground() throws Exception {
        m_Table.setEnabled(false);
            ArrayList<Feature> features = FeatureUtils
                    .readAttributes2Features(m_Captions,
                            transRecordsMap2StringArray(m_RecordMap));           
            setDisplayStatus("正在常规数据记录保存到数据库表中...");

             importFeaturesIntoDB(m_TableName, features);

            return "";
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
                    if (m_SucessCounter != m_Count) {
                        m_Panel.setShowMessage(m_TaskName+"\n已成功导入记录数：" + m_SucessCounter + "/" + m_Count,
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                           
                    String msg = (String) get();
                    if (!msg.isEmpty()) {
                        m_Panel.setShowMessage(msg, JOptionPane.ERROR);
                    }

                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "", ex);
                } finally {
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
