/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.io;

import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * 从Excel ，CVS中读取表数据
 *
 * @author lbs
 */
public class MyTableModel extends DefaultTableModel {

    private final ParseTableUtil parseTableUtil;

    /**
     *
     * @param value 内容
     * @param columnName 列名
     * @param parseTableUtil
     */
    public MyTableModel(Object[][] value, Object[] columnName, ParseTableUtil parseTableUtil) {
        super(value, columnName);
        this.parseTableUtil = parseTableUtil;
    }

    /**
     *
     * @param columnName
     * @param columnData
     * @return 
     * @throws java.lang.Exception
     */
    public boolean addColumn(final Object[] columnName, final Object[][] columnData) throws Exception{
        boolean flag =parseTableUtil.addColumn(columnName, columnData);
        final DefaultTableModel model = this;
        if (flag) {
            Object[] column = new Object[columnData.length];
            for (int i = 0; i < columnName.length; i++) {
                //取相应列
                for (int j = 0; j < column.length; j++) {
                    Object object = columnData[j][i];
                    column[j] = object;
                }
                Object colName = columnName[i];    
//                addColumnWithThread(colName, column, model);
                 this.addColumn(colName, column);
                 fireTableDataChanged();//通知模型更新 
            }
        }
          
        return flag;
    }

    /**
     *返回列
     * @return
     */
    public Vector getColumnIndetifiers()
    {
        return columnIdentifiers;
    }
    
 
    /**
     *
     * @param columnIndexs
     * @param columnData   按行排序，一行之后是另一行数据，只记录与columnIndexs 对应的数据。
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean updateColumn(int[] columnIndexs, Object[][] columnData) throws Exception {
        boolean flag = parseTableUtil.updateColumn(columnIndexs, columnData);
        if (flag) {
            for (int rI = 0; rI < columnData.length; rI++) {
                Object[] row = columnData[rI];
                for (int i = 0; i < columnIndexs.length; i++) {
                    setValueAt(row[i], rI, columnIndexs[i]);
                }
            }
        }
        return flag;
    }
    
    /**
     *
     * @param columnIndexs
     * @return
     * @throws Exception
     */
    public  boolean deleteColumn(int[] columnIndexs)  throws Exception
    {
        boolean flag = this.parseTableUtil.deleteColumn(columnIndexs);
        if (flag) {

//            for (int rI = columnIndexs.length - 1; rI >= 0; rI--) {
//                columnIdentifiers.removeElementAt(columnIndexs[rI]);
//            }
//            
//            for (int i = 0; i < dataVector.size(); i++) {
//                //每行数据
//                Vector v = (Vector) dataVector.elementAt(i);
//                for (int rI = columnIndexs.length - 1; rI >= 0; rI--) {
//                     v.removeElementAt(columnIndexs[rI]);
//                }
//            }
//            setDataVector(dataVector, columnIdentifiers);
            
        }
        return flag;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
