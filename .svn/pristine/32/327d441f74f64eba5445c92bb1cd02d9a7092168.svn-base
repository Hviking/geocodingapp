/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.io;

/**
 *
 * @author lbs
 */
public interface I_ParseTableContent {
     public  Object[][] readTableContent()throws Exception;
     /**
     *
     * @param columnName  列名 数组
     * @param columnData  列数据
     * @return
     * @throws java.lang.Exception
     */
    public  boolean addColumn(Object[] columnName, Object[][] columnData) throws Exception;

    /**
     * 修改指定列
     *
     * @param columnIndexs  列索引
     * @param columnData    列数据
     * @return
     * @throws java.lang.Exception
     */
    public  boolean updateColumn(int[] columnIndexs, Object[][] columnData) throws Exception;

    /**
     * 删除指定列
     * @param columnIndex  列索引
     * @return 
     * @throws java.lang.Exception 
     */
    public   boolean deleteColumn(int[] columnIndex)  throws Exception;
}
