/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.io;

import java.io.File;
import javax.swing.table.TableModel;

/**
 *
 * @author lbs
 */
public class ParseTableFactory {
    private final  File m_File;
    private ParseTableUtil m_ParseTableUtil;
    private  MyTableModel m_MyTableModel;
    private  String m_CharsetEncode;

    /**
     *
     * @param file
     * @param charsetEncode
     */
    public ParseTableFactory(File file,String charsetEncode)
    {
        m_File=file;
        this.m_CharsetEncode = charsetEncode!=null?charsetEncode:ENCODETYPE.UTF8;
        parseFileType();
    }
    

    public String getFileAbsolutePath()
    {
        return m_File.getAbsolutePath();
    }
    /**
     * 解析文件类型，支持的文件类型有xls\xlsd\txt\csv
     */
    private void parseFileType(){
      
        if (m_File.exists()) {
            String[] fileType={".xls",".xlsx",".txt",".csv"};
            String name = m_File.getName().toLowerCase();

            if (name.endsWith(fileType[0])
                    ||name.endsWith(fileType[1])) {
               
                m_ParseTableUtil = new ExcelControl(m_File);
                     
            }else if (name.endsWith(fileType[2])
                    ||name.endsWith(fileType[3])) {
                CSVControl csvc= new CSVControl(m_File);
                csvc.setEncode(m_CharsetEncode);
                m_ParseTableUtil = csvc;
            }
          
        }
           
    }

    public TableModel getTableModel() throws Exception{
        try {
            m_ParseTableUtil.readTableContent();
        } catch (Exception e) {
            throw  e;
        }
        m_MyTableModel = m_ParseTableUtil.getTableModel();
        return m_MyTableModel;
    }

    public boolean saveCoordinateAsColumn(Object[] columnName,Object[][] columnData) throws Exception {
        //将addressMap转为String[][] columnData, 从row 0 开始。
       
        return m_MyTableModel.addColumn(columnName, columnData);
    }

}
