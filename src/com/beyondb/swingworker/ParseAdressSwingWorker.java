/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.geocoding.Coordinate;
import com.beyondb.geocoding.ParseCoordinateFromAddressText;
import com.beyondb.io.ParseTableFactory;
import com.beyondb.ui.JPanel_txtIntoDB;
import com.beyondb.ui.ParseState;
import com.beyondb.utils.initSystemParams;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ParseAdressSwingWorker extends SuperSwingWorker{
    
    private  Map<Integer,Object> m_MatchRecord;
    private  Map<Integer,Object> m_NoMatchRecord;
    

     JPanel_txtIntoDB m_Panel;
    private String m_AddressParseType;

    public  ParseAdressSwingWorker(JPanel_txtIntoDB app)
    {
         super(app, app);
        m_Panel =app;
    }

    /**
     *传递参数
     * @param tableName 表名称  
     * @param cations 列标题
     * @param selectedColumnsIndex 选中的列
     */
    public  void setParameters(String tableName,
            String[] cations,
            int[] selectedColumnsIndex)
   {
        m_TableName = tableName;
        m_Captions = cations;
        m_SelectedColIndexs= selectedColumnsIndex;
   }
   
    /**
     *返回找到精确匹配结果的记录
     * @return
     */
    public Map<Integer,Object> getMatchRecordMap()
    {
        return m_MatchRecord;
    }
    
    /**
     *
     * @return  返回没有找到精确匹配结果的记录
     */
    public Map<Integer,Object> getNoMatchRecordMap()
    {
        return m_NoMatchRecord;
    }

    
    @Override
    protected String doInBackground() {
        m_Table.setEnabled(false);
        Map<Integer, Object[]> addressMap = new HashMap<>();
        String city=null;
        boolean isExist=false;
        for (int rowIndex = 0; rowIndex < m_TableModel.getRowCount(); rowIndex++) {
            setDisplayStatus("地址解析中:" + (rowIndex + 1) + "/" + m_TableModel.getRowCount());
            setDisplayProgress(100 * (rowIndex + 1) / m_TableModel.getRowCount());
            String detailAddr = "";
            for (int j = 0; j < m_SelectedColIndexs.length; j++) {
                detailAddr += " " + String.valueOf(m_TableModel.getValueAt(rowIndex, m_SelectedColIndexs[j]));
            }
            Coordinate coord = ParseCoordinateFromAddressText.parseCoordinate(m_AddressParseType,detailAddr, "");

            Object[] tmpRow = new Object[3];

            int statIndex = 0;
            int lonIndex = 1;
            int latIndex = 2;
            if (coord == null) {
                      //没有找到匹配结果
                //将该部分数据存入临时表，请求人工干预
                if (city==null&&!isExist) {
                    setDisplayStop();
                    city = m_Panel.setShowInputDialog("没有为“"+detailAddr
                            +"”找到精确匹配结果，\n请输入所在城市名来优化查询\n例如： 北京", "北京");
                    isExist=true;
                     setDisplayStatus("地址解析中:" + (rowIndex + 1) + "/" + m_TableModel.getRowCount());
                     setDisplayProgress(100 * (rowIndex + 1) / m_TableModel.getRowCount());
                }
                if (m_NoMatchRecord == null) {
                    m_NoMatchRecord = new HashMap<>();
                }
                Map<String,Coordinate> coordMap=  ParseCoordinateFromAddressText.parseSuggestionPlace(m_AddressParseType,detailAddr, city);
                if (coordMap != null) {
                    coord = coordMap.entrySet().iterator().next().getValue();//默认取第一个值作为推荐结果
                    tmpRow[statIndex] =  ParseState.NotSure;
                    tmpRow[lonIndex] =coord.Longitude; 
                    tmpRow[latIndex] =coord.Latitude;
                } else {
                    tmpRow[statIndex] =  ParseState.Failure;
                    double initValue=0;
                    tmpRow[lonIndex] =initValue;
                    tmpRow[latIndex] =initValue;
                }
                m_NoMatchRecord.put(rowIndex, tmpRow);
            } else {//匹配结果存到临时文件中
                if (m_MatchRecord == null) {
                    m_MatchRecord = new HashMap<>();
                }
                tmpRow[statIndex] =ParseState.Sucess;
                tmpRow[lonIndex] =  coord.Longitude;
                tmpRow[latIndex] =coord.Latitude;
                m_MatchRecord.put(rowIndex, tmpRow);
            }
            addressMap.put(rowIndex, tmpRow);
            
            
            

            if (isStop) {
                break;
            }
            
        }

        if (isStop) {
            return "";
        }
             
        int size = addressMap.size();
        Object[][] columnData = new Object[size][3];
        String[] columnName = new String[3];

        columnName[0] = initSystemParams.Table_Column_ParseStat;
        //判断解析类型
        switch (m_AddressParseType) {
            case initSystemParams.AddressParseType_BAIDU:
                columnName[1] = initSystemParams.Table_Column_CoordinateLong_BaiDu;
                columnName[2] = initSystemParams.Table_Column_CoordinateLat_BaiDu;
                break;
            case initSystemParams.AddressParseType_BEYONDB:
                columnName[1] = initSystemParams.Table_Column_CoordinateLong_BeyonDB;
                columnName[2] = initSystemParams.Table_Column_CoordinateLat_BeyonDB;
                break;
            case initSystemParams.AddressParseType_SKYMAP:
                columnName[1] = initSystemParams.Table_Column_CoordinateLong_SkyMap;
                columnName[2] = initSystemParams.Table_Column_CoordinateLat_SkyMap;
                break;
        }
        for (Map.Entry<Integer, Object[]> entry : addressMap.entrySet()) {
            Integer rowIndex = entry.getKey();
            Object[] row = entry.getValue();
            for (int i = 0; i < 3; i++) {
                columnData[rowIndex][i] = row[i];
            }
        }

        if (addressMap.size() > 0) {
            setDisplayStatus("地址解析结果正在保存到原文件中");
            boolean flag = false;
            try { 
                flag = m_Factory.saveCoordinateAsColumn(columnName,columnData);
                m_Panel.displayTableContent(m_Factory.getTableModel()); //暂时
            } catch (Exception e) {
                m_Panel.setShowMessage(m_TaskName+"\n地址解析结果保存到原文件失败！\n\n" + e.getMessage(),
                        JOptionPane.ERROR_MESSAGE);
                return "";
            }

            if (flag) {
               
              
                setDisplayProgress(100);
                setDisplayStatus("地址解析结果保存到原文件成功");
            } else {
                m_Panel.setShowMessage("地址解析结果保存到原文件失败", JOptionPane.ERROR_MESSAGE);
                return "";
            }
        }

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
                    String msg = (String) get();
                    if (!msg.isEmpty()) {
                        m_Panel.setShowMessage(msg, JOptionPane.ERROR);
                    }
                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(ParseAdressSwingWorker
                            .class.getName()).log(Level.SEVERE,"",ex);
                }
                finally{
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

    public void setParseTableFactory(ParseTableFactory factory) {
      m_Factory =factory;
    }

    public void setParseType(String parseType) {
        m_AddressParseType = parseType;
    }


}
