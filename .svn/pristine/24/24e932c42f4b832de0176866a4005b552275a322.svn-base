/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 倪永
 */
public class CSVControl extends  ParseTableUtil{  

    private FileInputStream m_FileInputStream = null;
    private InputStreamReader m_InputStreamReader = null;
    private BufferedReader m_BufferedReader = null;
    private String m_ENCODE;

    private FileOutputStream m_FileOutputStream;
    private BufferedWriter m_BufferedWriter;

    public CSVControl(File file) {
        m_File = file;
       m_RowNum = 1;
        m_ColumnNum = 1;
        m_TableStr = new String[m_RowNum][m_ColumnNum];
    }
    
    /**
     *
     * @param encode ENCODETYPE的静态成员变量值 “UTF-8”，“GBK”等
     */
    public void setEncode(String encode) {

        m_ENCODE = encode;
      
    }

    private void initInputStream() {
        try {
            m_FileInputStream = new FileInputStream(m_File);
            m_InputStreamReader = new InputStreamReader(m_FileInputStream, m_ENCODE);
            m_BufferedReader = new BufferedReader(m_InputStreamReader);
        
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "读取CSV、TXT文件时出错", e);
        }
    }
     private void initOutputStream() {
        try {
           
            m_FileOutputStream = new FileOutputStream(m_File,false);
            OutputStreamWriter osWriter = new OutputStreamWriter(m_FileOutputStream, m_ENCODE);

            m_BufferedWriter = new BufferedWriter(osWriter);
        
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "写入CSV、TXT文件时出错", e);
        }
    }
    private void closeInputStream() {
      
        try {
            m_InputStreamReader.close();
            m_BufferedReader.close();
            m_FileInputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(CSVControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        private void closeOutputStream() {
      
        try {
            m_BufferedWriter.close();
            m_FileOutputStream.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /** 
     * 从CSV文件流中读取一个CSV行。 
     * 
     * @return 
     * @throws java.io.IOException 
     */  
    public String readLine() throws IOException {  
  
        StringBuilder readLine = new StringBuilder();  
        boolean bReadNext = true;  
  
        while (bReadNext) {  
            //  
            if (readLine.length() > 0) {  
                readLine.append("\r\n");  
            }  
            // 一行  
            String strReadLine = m_BufferedReader.readLine();  
  
            // readLine is Null  
            if (strReadLine == null) {  
                return null;  
            }  
            readLine.append(strReadLine);  
  
            // 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
            bReadNext = countChar(readLine.toString(), '"', 0) % 2 == 1;  
        }  
        return readLine.toString();  
    }  
  
    /** 
     *把CSV文件的一行转换成字符串数组。指定数组长度，不够长度的部分设置为null。 
     * @param source
     * @param size 数组长度
     * @return 
     */  
    public static String[] transCSVLinetoArray(String source, int size) {  
        ArrayList tmpArray = transCSVLinetoArray(source);  
        if (size < tmpArray.size()) {  
            size = tmpArray.size();  
        }  
        String[] rtnArray = new String[size];  
        tmpArray.toArray(rtnArray);  
        return rtnArray;  
    }  
  
    /** 
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。 
     * @param source
     * @return 
     */  
    public static ArrayList transCSVLinetoArray(String source) {  
        if (source == null || source.length() == 0) {  
            return new ArrayList();  
        }
        int currentPosition = 0;  
        int maxPosition = source.length();  
        int nextComma = 0;  
        ArrayList rtnArray = new ArrayList();  
        while (currentPosition < maxPosition) {  
            nextComma = nextComma(source, currentPosition);  
            rtnArray.add(nextToken(source, currentPosition, nextComma));  
            currentPosition = nextComma + 1;  
            if (currentPosition == maxPosition) {  
                rtnArray.add("");  
            }  
        }  
        return rtnArray;  
    }  
  
  
    /** 
     * 把字符串类型的数组转换成一个CSV行。（输出CSV文件的时候用） 
     * @param strArray
     * @return 
     */  
    public static String transArraytoCSVLine(String[] strArray) {  
        if (strArray == null) {  
            return "";  
        }  
        StringBuilder cvsLine = new StringBuilder();  
        for (int idx = 0; idx < strArray.length; idx++) {
            String item = strArray[idx];
            if (item.contains("\r")
                    ||item.contains("\n")
                    ||item.contains(",")) {
                item = addQuote(item);
            }
            cvsLine.append(item);  
            if (strArray.length - 1 != idx) {  
                cvsLine.append(',');  
            }  
        }  
        return cvsLine.toString();  
    }  
  
    /** 
     * 字符串类型的List转换成一个CSV行。（输出CSV文件的时候用） 
     * @param strArrList
     * @return 
     */  
    public static String transArraytoCSVLine(ArrayList strArrList) {  
        if (strArrList == null) {  
            return "";  
        }  
        String[] strArray = new String[strArrList.size()];  
        for (int idx = 0; idx < strArrList.size(); idx++) {  
            strArray[idx] = (String) strArrList.get(idx);  
        }  
        return transArraytoCSVLine(strArray);  
    }  
  
    // ==========以下是内部使用的方法=============================  
    /** 
     *计算指定文字的个数。 
     * 
     * @param str 文字列 
     * @param c 文字 
     * @param start  开始位置 
     * @return 个数 
     */  
    private int countChar(String str, char c, int start) {  
        int i = 0;  
        int index = str.indexOf(c, start);  
        return index == -1 ? i : countChar(str, c, index + 1) + 1;  
    }  
  
    /** 
     * 查询下一个逗号的位置。 
     * 
     * @param source 文字列 
     * @param st  检索开始位置 
     * @return 下一个逗号的位置。 
     */  
    private static int nextComma(String source, int st) {  
        int maxPosition = source.length();  
        boolean inquote = false;  
        while (st < maxPosition) {  
            char ch = source.charAt(st);  
            if (!inquote && ch == ',') {  
                break;  
            } else if ('"' == ch) {  
                inquote = !inquote;  
            }  
            st++;  
        }  
        return st;  
    }  
  
    /** 
     * 取得下一个字符串 
     */  
    private static String nextToken(String source, int st, int nextComma) {  
        StringBuilder strb = new StringBuilder();  
        int next = st;  
        while (next < nextComma) {  
            char ch = source.charAt(next++);  
            if (ch == '"') {  
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {  
                    strb.append(ch);  
                    next++;  
                }  
            } else {  
                strb.append(ch);  
            }  
        }  
        return strb.toString();  
    }  
  
    /** 
     * 在字符串的外侧加双引号。如果该字符串的内部有双引号的话，把"转换成""。 
     * 
     * @param item  字符串 
     * @return 处理过的字符串 
     */  
    private static String addQuote(String item) {  
        if (item == null || item.length() == 0) {  
            return "\"\"";  
        }  
        StringBuilder sb = new StringBuilder();  
        sb.append('"');  
        for (int idx = 0; idx < item.length(); idx++) {  
            char ch = item.charAt(idx);  
            if ('"' == ch) {  
                sb.append("\"\"");  
            } else {  
                sb.append(ch);  
            }  
        }  
        sb.append('"');  
        return sb.toString();  
    }  

    @Override
    public Object[][] readTableContent()throws Exception {
        initInputStream();
        //获取总行数
        ArrayList<String[]> rowArrayList = new ArrayList<>();
        String tmpLine = null;
        int tmplength = 0;
  
        try {
            while ((tmpLine = readLine()) != null) {
                ArrayList<String> record = transCSVLinetoArray(tmpLine);
                if (record.isEmpty()) {
                    continue;
                }
                tmplength =record.size();
                String[] recordArray = new String[record.size()];
                record.toArray(recordArray);
                rowArrayList.add(recordArray);
            }
            if (rowArrayList.isEmpty()) {
                throw new Exception("打开的文件内容为空");
            }
            m_RowNum = rowArrayList.size();
            m_ColumnNum = rowArrayList.get(0).length;
            m_TableStr = new Object[m_RowNum][m_ColumnNum];

            for (int i = 0; i < m_RowNum; i++) {
                String[] record = rowArrayList.get(i);
                System.arraycopy(record, 0, m_TableStr[i], 0, m_ColumnNum);
//                for (String v : record) {
//                     System.err.println(v); 
//                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName())
                    .log(Level.SEVERE, "从csv或txt文件中读取数据", e);
            throw  new Exception( "csv或txt文件格式可能有误", e);
        } finally {
            closeInputStream();
        }
        return m_TableStr;
    }

    @Override
    public boolean updateColumn(int[] columnIndex, Object[][] columnData) throws FileNotFoundException, IOException {
        initInputStream();
        boolean flag = true;

         try {
            ArrayList<String[]> rowArrayList = new ArrayList<>();
            String tmpLine ;
            String captionLine = readLine();

            ArrayList<String> captionRecord = transCSVLinetoArray(captionLine);
            String[] captionRecordArray = new String[captionRecord.size()];
            captionRecord.toArray(captionRecordArray);
            rowArrayList.add(captionRecordArray);

            int i = 0;

            while ((tmpLine = readLine()) != null) {
                ArrayList<String> record = transCSVLinetoArray(tmpLine);

                for (int j = 0; j < columnIndex.length; j++) {
                    String newValue = String.valueOf(columnData[i][j]);
                    record.set(columnIndex[j], newValue);
                }
                i++;
                String[] recordArray = new String[record.size()];
                record.toArray(recordArray);
                rowArrayList.add(recordArray);
            }
            closeInputStream();
            initOutputStream();
            saveRecordsToCSV(rowArrayList);

            closeOutputStream();

        } catch (FileNotFoundException ex) {
            flag=false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.SEVERE, "修改指定列内容到CSV/TXT文件出错", ex);
            throw  ex;
        } catch (IOException ex) {
            flag=false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.SEVERE, "修改指定列内容到CSV/TXT文件出错", ex);
            throw ex;
        }
        return flag;
    }
    
        /**
     * records的类型是string[][]
     * 将记录保存到CSV文件 中 
     * @param records 内容
     * @return
     */
    public  boolean  saveRecordsToCSV(String[][]  records)
    {
        try {
            if (records ==null) {
                return  false;
            }
            if (m_File.exists() && m_File.isFile()) {
                    for (int i = 0; i < records.length; i++) {
                        String[] row = records[i];
                        String csvRow= transArraytoCSVLine(row);
                        m_BufferedWriter.write(csvRow);
                        m_BufferedWriter.newLine();
                    }
            } 
           
        } catch (IOException ex) {
            Logger.getLogger(ExcelControl.class.getName())
                    .log(Level.SEVERE, "记录保存到csv文件中时出错", ex);
            return false;
        }
        
         return true;
    }
    
    
    public boolean saveRecordsToCSV(ArrayList<String[]> records) {

        String[][] rowStringses = new String[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            rowStringses[i] = records.get(i);
        }
        return saveRecordsToCSV(rowStringses);
    }

    @Override
    public boolean addColumn(Object[] columnName, Object[][] columnData) throws FileNotFoundException, IOException  {
        initInputStream();
        boolean flag = true;

        ArrayList<String[]> rowArrayList = new ArrayList<>();
        String tmpLine = "";
        try {
            String captionLine = readLine();
            ArrayList<String> captionRecord = transCSVLinetoArray(captionLine);

            //新增列
            int columnsCount = captionRecord.size();
            int newColumnIndex = columnsCount;
            for (int i = 0; i < columnName.length; i++) {
                 captionRecord.add(newColumnIndex+i, String.valueOf(columnName[i]));
            }
           
            String[] captionRecordArray = new String[captionRecord.size()];
            captionRecord.toArray(captionRecordArray);
            rowArrayList.add(captionRecordArray);

            int rowIndex = 0;

            while ((tmpLine = readLine()) != null) {
                ArrayList<String> record = transCSVLinetoArray(tmpLine);
                if (rowIndex > columnData.length) {
                    break;
                }
                for (int colIndex = 0; colIndex < columnName.length; colIndex++) {
                     record.add(newColumnIndex+colIndex, columnData[rowIndex][colIndex].toString());
                }
               

                String[] recordArray = new String[record.size()];
                record.toArray(recordArray);
                rowArrayList.add(recordArray);
                rowIndex++;
            }
            closeInputStream();
            initOutputStream();
            saveRecordsToCSV(rowArrayList);

            closeOutputStream();
        } catch (FileNotFoundException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.OFF, "CSV增加列，文件找不到异常", ex);
            throw ex;
        } catch (IOException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.OFF, "CSV增加列，IO异常", ex);
   
            throw ex;
        }
        return flag;
    }

 
    @Override
    public boolean deleteColumn(int[] columnIndex) throws FileNotFoundException, IOException {
        initInputStream();
        boolean flag = true;

        String tmpLine = "";
        try {

            ArrayList<String[]> rowArrayList = new ArrayList<>();
            String captionLine = readLine();

            //标题行
            ArrayList<String> captionRecord = transCSVLinetoArray(captionLine);
            for (int i = columnIndex.length - 1; i >-1; i--) {
                //从最大索引号开始删除
                captionRecord.remove(columnIndex[i]);
            }
            String[] recordStrings = new String[captionRecord.size()];
            
            rowArrayList.add(captionRecord.toArray(recordStrings));

            while ((tmpLine = readLine()) != null) {
                ArrayList<String> record = transCSVLinetoArray(tmpLine);
                for (int i = columnIndex.length - 1; i >-1; i--) {
                    //从最大索引号开始删除
                    record.remove(columnIndex[i]);
                }
                 recordStrings = new String[record.size()];
                 rowArrayList.add(record.toArray(recordStrings));
            }
            closeInputStream();
            initOutputStream();
            saveRecordsToCSV(rowArrayList);
            closeOutputStream();
        } catch (FileNotFoundException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.OFF, "CSV删除列，文件找不到异常", ex);
            throw ex;
        } catch (IOException ex) {
            flag = false;
            Logger.getLogger(this.getClass().getName())
                    .log(Level.OFF, "CSV删除列，IO异常", ex);
            throw new IOException("CSV删除列",  ex);
        }
        return flag;
    }
}  
