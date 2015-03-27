/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.io;

import com.beyondb.geocoding.Coordinate;
import com.beyondb.utils.initSystemParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *倪永 修改
 * @author ZhangShuo
 */
public class ExcelControl extends ParseTableUtil implements  I_ParseTableContent{

    Workbook m_Workerbook = null;
    InputStream m_InputStream;
    
    public ExcelControl(File file)
    {
        m_File =file;
        m_RowNum = 1;
        m_ColumnNum = 1;
        m_TableStr = new String[m_RowNum][m_ColumnNum];
    }

    @Override
    public Object[][] readTableContent() throws IOException, InvalidFormatException, Exception
      {
        try {
            //OPCPackage pkg = OPCPackage.open(file);
//            InputStream m_InputStream = new FileInputStream(m_File);
               Sheet sheet=null;
//            if (!m_InputStream.markSupported()) {
//                m_InputStream = new PushbackInputStream(m_InputStream, 8);
//            } 
//            if (POIFSFileSystem.hasPOIFSHeader(m_InputStream)) {
//                HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(m_InputStream);
//                 sheet  = (Sheet)hSSFWorkbook.getSheetAt(0);
//            
//             } else if (POIXMLDocument.hasOOXMLHeader(m_InputStream)) {
//                XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(OPCPackage.open(m_File));
//               sheet  = (Sheet)xSSFWorkbook.getSheetAt(0);
//             }
//             else {
//                throw new IllegalArgumentException("你的excel版本目前poi解析不了");
//            }
            sheet = getSheet();
            if(sheet!=null)
            {
                if (sheet.getLastRowNum()==0) {
                    throw  new Exception("打开的Excel文件为空");
                }
                //获取总行数
                m_RowNum = sheet.getLastRowNum() + 1;
            
//                m_ColumnNum = sheet.getRow(0).getPhysicalNumberOfCells();
                  m_ColumnNum =  sheet.getRow(0).getLastCellNum();
                m_TableStr = new Object[m_RowNum][m_ColumnNum];

                for (int rindex =0;rindex< m_RowNum;rindex++) {
                    Row row = sheet.getRow(rindex);
                    for (int cindex=0;cindex<m_ColumnNum;cindex++) {
                        Cell cell = row.getCell(cindex);
    
                        if (cell==null) {
                              m_TableStr[rindex][cindex] = "";
                        }
                        else {
                            String value = "";
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
//                          System.out.println(cell.getRichStringCellValue().getString());                          
                               value = cell.getRichStringCellValue().getString().replace("\n", "");
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
//                                System.out.println(cell.getDateCellValue());
                                  value= cell.getDateCellValue().toString();
                                }else{
                                    
                                    DecimalFormat df = new DecimalFormat("#");
                                    value = String.valueOf(cell.getNumericCellValue());
                                    double d = cell.getNumericCellValue();
                                    int dInt = (int) d;
                                    BigDecimal b1 = new BigDecimal(value);
                                    BigDecimal b2 = new BigDecimal(Integer.toString(dInt));
                                    double dPoint = b1.subtract(b2).doubleValue();
                                    if (dPoint == 0) {
                                        //判断是否是整型
                                        value = df.format(cell.getNumericCellValue());
                                    }
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
//                            System.out.println(cell.getBooleanCellValue());
                              value = cell.getBooleanCellValue() + "";
                                break;
                            case Cell.CELL_TYPE_FORMULA:
//                            System.out.println(cell.getCellFormula());
                              value = cell.getCellFormula();
                                break;
                            case Cell.CELL_TYPE_BLANK:
                               value = "";
                            default:
//                            System.out.println();
                                value = "";
                        }
                         m_TableStr[row.getRowNum()][cell.getColumnIndex()] =value;
                        }
                    }
                }
            }

          } catch (IOException | InvalidFormatException e) {
              Logger.getLogger(ExcelControl.class.getName())
                      .log(Level.SEVERE, "从excel中读取数据", e);
              throw e;

          } catch (Exception ex) {
              Logger.getLogger(ExcelControl.class.getName())
                      .log(Level.SEVERE, "从excel中读取数据", ex);

              throw ex;
          }
        finally{
            m_InputStream.close();
        }
       

        return m_TableStr;
    }

    
    private org.apache.poi.ss.usermodel.Sheet getSheet()
            throws  IOException, InvalidFormatException {
        org.apache.poi.ss.usermodel.Sheet sheet = null;

        try {
          
            m_InputStream = new FileInputStream(m_File);

            if (!m_InputStream.markSupported()) {
                m_InputStream = new PushbackInputStream(m_InputStream, 8);
            }
            if (POIFSFileSystem.hasPOIFSHeader(m_InputStream)) {
                POIFSFileSystem poifsfs = new POIFSFileSystem(m_InputStream);
                m_Workerbook = WorkbookFactory.create(poifsfs);

            } else if (POIXMLDocument.hasOOXMLHeader(m_InputStream)) {
                m_Workerbook = WorkbookFactory.create(OPCPackage.open(m_File));
            } else {
                throw new IllegalArgumentException("你的excel版本目前不支持解析");
            }
            sheet = m_Workerbook.getSheetAt(0);
        } catch (FileNotFoundException ex) {

            Logger.getLogger(ExcelControl.class.getName())
                    .log(Level.SEVERE, "EXCEL文件不存在", ex);
        } catch (IOException | InvalidFormatException ex) {
            Logger.getLogger(ExcelControl.class.getName())
                    .log(Level.SEVERE, "读取EXCEL文件出错", ex);
            throw ex;
        }
        return sheet;
    }
    
    private static void setCellValue(Cell cell,Object value)
    {
         switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cell.setCellValue((String) value);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cell.setCellValue((Date) value);
                } else {
                    double v =Double.valueOf(value.toString());
                    int dInt = (int)Math.floor(v); 
                    
                    if (v - dInt == 0) {
                        cell.setCellValue(dInt);
                    } else {
                        cell.setCellValue(v);

                    }
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cell.setCellValue((boolean) value);
                break;
            case Cell.CELL_TYPE_FORMULA:
                cell.setCellFormula((String) value);
                break;
            case Cell.CELL_TYPE_BLANK:
                cell.setCellValue("");
            default:

        }
    }

    @Override
    public boolean deleteColumn(int[] columnIndex) throws FileNotFoundException, IOException, InvalidFormatException  {
        boolean flag = true;
        Sheet sheet = null;
        try {
            sheet =getSheet();
            if (sheet == null) {
                return false;
            }
            
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                //可编辑区域
                Row tmpRow = sheet.getRow(i);
           
                for (int j = columnIndex.length-1; j > -1; j--) {
                    //移除当前列，把后面的列前移
                    for (int k = columnIndex[j]; k < tmpRow.getLastCellNum(); k++) {
                        Cell tmpCell = tmpRow.getCell(k);
                        if (null != tmpCell) {
                            tmpRow.removeCell(tmpCell);
                        }
                        Cell rightCell = tmpRow.getCell(k + 1);
                        if (null != rightCell) {
                            HSSFRow hr = (HSSFRow) tmpRow;
                            hr.moveCell((HSSFCell) rightCell, (short) k);
                        }
                    }

                }
            }
            m_InputStream.close();
            try ( // Write the output to a file
            final FileOutputStream fileOut = new FileOutputStream(m_File)) {
                m_Workerbook.write(fileOut);
            }
        } catch (FileNotFoundException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName()).log(Level.SEVERE, "删除列", ex);
            throw ex;
        } catch (IOException | InvalidFormatException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName()).log(Level.SEVERE, "删除列", ex);
            throw ex;
        }
        return flag;
    }
     

    @Override
    public boolean addColumn(Object[] columnName, Object[][] columnData) throws FileNotFoundException, IOException, InvalidFormatException {
         boolean flag = true;
        Row rowCaption;
        Sheet sheet=null;

        try {
            sheet = getSheet();
            if (sheet==null) {
                return false;
            }
           //设置标题行
               rowCaption =   sheet.getRow(0);
            if (rowCaption!=null) {
                int columnsCount = rowCaption.getLastCellNum();
                for (int i = 0; i < columnName.length; i++) {
                    Cell cell = rowCaption.createCell(columnsCount + i);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(String.valueOf(columnName[i]));
                }
               
                for (int i = 0; i < sheet.getLastRowNum(); i++) {
                    //可编辑区域
                    Row tmpRow = sheet.getRow(i+1 );
                    
                    for (int cIndex = 0; cIndex < columnName.length; cIndex++) {
                         Cell cell = tmpRow.getCell(columnsCount + cIndex);
                        if (cell==null) {
                            cell = tmpRow.createCell(columnsCount + cIndex);
                        }                  
                        //检查数据类型
                        Object obj = columnData[i][cIndex];
                        if (obj.getClass().getName().equals(Double.class.getName())) {
                             cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        }else if(obj.getClass().getName().equals(String.class.getName()))
                        {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }else {
                            //默认处理方法
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                        }
                        setCellValue(cell,obj);
                    }
               
                }
            }
            
             m_InputStream.close();
            try ( // Write the output to a file
                    FileOutputStream fileOut = new FileOutputStream(m_File)) {
                m_Workerbook.write(fileOut);
            }
        } catch (FileNotFoundException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName())
                    .log(Level.SEVERE, "增加列", ex);
            throw  ex;

        } catch (IOException | InvalidFormatException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName())
                    .log(Level.SEVERE, "增加列", ex);
            throw  ex;
        }

        return flag;
    }

    @Override
    public boolean updateColumn(int[] columnIndexs, Object[][] columnData) throws FileNotFoundException, IOException, InvalidFormatException {
        boolean flag = true;
        Sheet sheet = null;
        try {
            sheet =getSheet();
            if (sheet == null) {
                return false;
            }
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                //可编辑区域
                Row tmpRow = sheet.getRow(i+1 );
                for (int j = 0; j < columnIndexs.length; j++) {
                    Cell cell = tmpRow.getCell(columnIndexs[j]);
                    if (cell != null) {
                        setCellValue(cell, columnData[i][j]);
                    }
                }
            }
            m_InputStream.close();
            try ( // Write the output to a file
            final FileOutputStream fileOut = new FileOutputStream(m_File)) {
                m_Workerbook.write(fileOut);
            }
           
        } catch (FileNotFoundException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName()).log(Level.SEVERE, "更新列数据", ex);
            throw ex;
        } catch (IOException | InvalidFormatException ex) {
            flag = false;
            Logger.getLogger(ExcelControl.class.getName()).log(Level.SEVERE, "更新列数据", ex);
            throw ex;
        }
        return flag;
        
    }

}
