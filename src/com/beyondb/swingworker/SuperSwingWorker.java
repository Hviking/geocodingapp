/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.io.ParseTableFactory;
import com.beyondb.spacialization.BydField;
import com.beyondb.spacialization.Feature;
import com.beyondb.ui.AppendOrUpdatePanel;
import com.beyondb.ui.IDialog;
import com.beyondb.ui.ProgressStatusFrame;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;

/**
 *有进度条的任务
 * @author 倪永
 */
public abstract class SuperSwingWorker extends SwingWorker<String, Object> {

    /**
     *将Map<Integer,Object>转换成String[][]。
     * @param recordsMap
     * @return
     */
    public static String[][] transRecordsMap2StringArray(Map<Integer, Object> recordsMap) {
        if (recordsMap != null && recordsMap.size() > 0) {
            int columnCount = 0;
            int rowCount = recordsMap.size();
            String[][] records = null;
            Set<Map.Entry<Integer, Object>> entrySet = recordsMap.entrySet();
            Iterator<Map.Entry<Integer, Object>> iterator = entrySet.iterator();
            if (iterator.hasNext()) {
                Map.Entry<Integer, Object> entry = iterator.next();
                Object value = entry.getValue();
                String[] row = (String[]) value;
                columnCount = row.length;
                records = new String[rowCount][columnCount];
                records[0] = row;
            }
            int idex = 1;
            while (iterator.hasNext()) {
                Map.Entry<Integer, Object> entry = iterator.next();
                Object value = entry.getValue();
                String[] row = (String[]) value;
                records[idex++] = row;
            }
            return records;
        }
        return null;
    }

    protected JComponent m_Componet;
    protected JFrame m_Frame;
    protected IDialog m_Dialog;
    protected DataSource m_DataSource;
    protected String m_TableName;
    protected String m_TaskName;
    protected String[] m_Captions;
    protected int[] m_SelectedColIndexs;
    protected ProgressStatusFrame m_DisplayStatusframe;
     
    protected ParseTableFactory m_Factory;
    protected int m_Count = 0;
    protected int m_SucessCounter = 0;
    protected TableModel  m_TableModel;
    protected  JTable m_Table;
    protected ArrayList<Feature> m_failsImportFeatures;
      
      
    private  boolean  m_isDisplay=true;
  
    public SuperSwingWorker(IDialog dialog) {
        m_Dialog = dialog;
    }
    public SuperSwingWorker(IDialog dialog,JComponent jpanel) {
        m_Dialog = dialog;
        m_Componet =jpanel;
    }
    public SuperSwingWorker(IDialog dialog,JFrame jFrame) {
        m_Dialog = dialog;
        m_Frame =jFrame;
    }
   
    public void setTableModel(TableModel tableModel) {
        m_TableModel = tableModel;
    }

    public void setJTable(JTable jtable) {
        m_Table = jtable;
    }

    public boolean importFeaturesIntoDB(String tableName, ArrayList<Feature> features) throws SQLException{
        boolean flag=false;
        if (features.isEmpty()) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "要素集合为空");
            return false;
        }
        boolean isAppend = true;
        ArrayList<String> primaryKeys = null;
 
        DataSourceUtils dataSourceUtils = new DataSourceUtils(m_DataSource);
        BydOperator bydOperator = new BydOperator(dataSourceUtils);
        if (bydOperator.hasTable(tableName)) {
            
            //Append or update提示
            if (m_TableModel != null) {
                AppendOrUpdatePanel appendOrUpdatePanel = new AppendOrUpdatePanel();
                appendOrUpdatePanel.setTableModel(m_TableModel);

                setDisplayStop();
                int res = m_Dialog.setShowConfirmDialog(appendOrUpdatePanel, "导入提示", JOptionPane.OK_CANCEL_OPTION);
                if (res == JOptionPane.OK_OPTION) {
                    isAppend = appendOrUpdatePanel.isAppend();
                    primaryKeys = appendOrUpdatePanel.getPrimaryKeyColumns();
                } else {
                    return flag;
                }
            }
        } else {//创建表

            try {
                Feature feature = features.get(0);
                String createTableSql = bydOperator.produceCreateTableSql(tableName, feature.Fields);
                bydOperator.executeSql(createTableSql);
                if (feature.getGeoField() != null) {
                    try {
                        String unRegGeoColumnSql = bydOperator.produceUnRegisterGeomColumnSql(tableName);
                        bydOperator.executeSql(unRegGeoColumnSql);
                    } catch (SQLException ex) {
                        //donothing
                    }
                    String regGeoColumnSql = bydOperator.produceRegisterGeomColumnSql(tableName, feature.Fields);
                    bydOperator.executeSql(regGeoColumnSql);
                }
            } catch (SQLException ex) {
                String info = "在BeyonDB数据库中创建表:" + tableName + "失败";
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, info);
                setDisplayStop();
                m_Dialog.setShowMessage(info + "/n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                return flag;
            }
        }//创建表结束
        
        Feature feature = null;
         m_failsImportFeatures = new ArrayList<>();
        try {
            Iterator<Feature> iterator = features.iterator();
            m_Count = 0;
            m_SucessCounter = 0;
            setProgress(m_Count);

            while (iterator.hasNext()) {
                feature = iterator.next();
                int result = 0;
                if (isAppend) {
                    result = bydOperator.insertRecord(tableName, feature.Fields);
                } else {

                    if (bydOperator.hasRecord(tableName, feature.Fields, primaryKeys)) {
                        //对于存在的数据采用更新
                        result = bydOperator.updateRecord(tableName, feature.Fields, primaryKeys);
                    } else {
                        //对于不存在的数据直接插入
                        result = bydOperator.insertRecord(tableName, feature.Fields);
                    }
                }
                if (result == 0) {
                    m_failsImportFeatures.add(feature);
                } else {
                    m_SucessCounter++;
                }
                m_Count++;

                publish("已处理记录：" + m_Count + "/" + features.size());
                setProgress(100 * m_Count / features.size());

                if (isStop) {
                    break;
                }
            }

            flag = true;
        } catch (SQLException ex) {
            String info = "在BeyonDB数据库表" + tableName + "中导入记录失败";
            info += "\n" + ex.getMessage();
            if (feature != null) {
                info += "\n" + "出现问题的记录是：";
                info += "\n" + "------------------------------------------------";
                for (BydField bydField : feature.Fields) {
                    {
                        info += "\n" + bydField.Name + " " + bydField.Value;
                    }
                }
                info += "\n" + "------------------------------------------------";
            }
            info += "\n" + "请检查要输入的记录是否符合要求！";
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, info);
            setDisplayStop();
            m_Dialog.setShowMessage(info, JOptionPane.ERROR_MESSAGE);
            flag = false;
        } 
        return flag;
    }
    
    /**
     *
     * @param valueList
     */
    @Override
    protected  void process(List<Object> valueList)
    {
        for (Object object : valueList) {
            String v =(String)object;
            int p = getProgress();
            setDisplayProgress(p);
            setDisplayStatus(v);
        }
    }

    

    private  void createDisplayStatusFrame()
    {
        m_DisplayStatusframe = new ProgressStatusFrame();
        m_DisplayStatusframe.setHost(this);
        if (m_Componet != null) {
            if (m_Componet.isShowing()) {
                int x = m_Componet.getWidth() / 2 + m_Componet.getLocationOnScreen().x - m_DisplayStatusframe.getWidth() / 2;
                int y = m_Componet.getHeight() / 2 + m_Componet.getLocationOnScreen().y - m_DisplayStatusframe.getHeight() / 2;
                m_DisplayStatusframe.setLocation(x, y);
            }
        }
        if (m_Frame != null) {
            if (m_Frame.isShowing()) {
                int x = m_Frame.getWidth() / 2 + m_Frame.getLocationOnScreen().x - m_Frame.getWidth() / 2;
                int y = m_Frame.getHeight() / 2 + m_Frame.getLocationOnScreen().y - m_Frame.getHeight() / 2;
                m_DisplayStatusframe.setLocation(x, y);
            }
        }
        if (m_TaskName != null) {
            m_DisplayStatusframe.setTitle(m_TaskName);
        }
         
        m_DisplayStatusframe.setVisible(true);
        m_DisplayStatusframe.setFocusable(true);
        m_DisplayStatusframe.setAlwaysOnTop(true);
       
    }
    
    /**
     *设置任务名称
     * @param value
     */
    public  void setTaskName(String value)
    {
        m_TaskName =value;
    }
    /**
     * 设置滚动条进度值
     *
     * @param value
     */
    public void setDisplayProgress(int value) {

        if (!m_isDisplay) {
            return;
        }
//        if (m_DisplayStatusframe == null) {
//            createDisplayStatusFrame();
//        }
        if (m_DisplayStatusframe != null) {
            m_DisplayStatusframe.setDisplayProgress(value);
        }

    }

     
    /**
     * 设置是否显示进度条
     * @param flag  是否显示
     */
    public void setProgressBarDisplay(boolean  flag) {
        m_isDisplay = flag;
    }
    public void setDisplayStop() {
        if (!m_isDisplay) {
            return;
        }
        if (m_DisplayStatusframe != null) {
            m_DisplayStatusframe.setVisible(false);
            //m_DisplayStatusframe.dispose();
            m_DisplayStatusframe = null;            
        }
    }

    /**
     * 设置状态值
     *
     * @param value
     */
    public void setDisplayStatus(String value) {

         if (!m_isDisplay) {
            return;
        }
        if (m_DisplayStatusframe == null) {
            createDisplayStatusFrame();
        }
        m_DisplayStatusframe.setDisplayStatus(value);
    }

    /**
     *关闭线程
     */
    public  void close()
    {
        this.setStop(true);
        if (m_DisplayStatusframe != null) {
            setDisplayStop();
        }
    }

    /**
     * 变量，控件任务的执行状态
     */
    protected  boolean isStop=false;

    
    public  void setStop(boolean  flag)
    {
        isStop =flag;
    }
}
