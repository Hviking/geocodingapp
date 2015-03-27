/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.io;

import java.io.File;

/**
 * 从CSV/EXCEL/TXT文档读取table内容 的超类
 *
 * @author 倪永
 */
public abstract class ParseTableUtil implements I_ParseTableContent{


    protected File m_File;
    protected int m_RowNum;
    protected int m_ColumnNum;
    protected Object[][] m_TableStr;

    public MyTableModel getTableModel() {

        Object[] columnName = new Object[m_ColumnNum];
        for (int i = 0; i < m_ColumnNum; i++) {
            columnName[i] = m_TableStr[0][i];
        }
        Object[][] value = new Object[m_RowNum - 1][m_ColumnNum];

        for (int i = 0; i < m_RowNum - 1; i++) {
            for (int j = 0; j < m_ColumnNum; j++) {
                value[i][j] = m_TableStr[i + 1][j];
            }
        }
        return new MyTableModel(value, columnName, (ParseTableUtil) this);
    }

 
}
