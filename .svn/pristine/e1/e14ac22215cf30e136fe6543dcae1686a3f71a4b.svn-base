package com.beyondb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 文件操作的工具类
 * @author guanml
 */
public class FileUtils {
      
    /**
     * 查找给定文件夹下所有矢量文件，使用递归的方式不断的搜索目录，知道最后一个目录为止
     *
     * @param findDir 所要查找的文件夹
     * @param fileType 文件类型
     * @return
     */
    public static  ArrayList<File> findFiles(String findDir, String fileType) {
        ArrayList<File> fileList = new ArrayList<>();
        File directory = new File(findDir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    addAllFiles(fileList, file.getAbsolutePath(), fileType);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(fileType) || fileName.endsWith(fileType.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        } else {
            String fileName = directory.getName();
            if (fileName.endsWith(fileType) || fileName.endsWith(fileType.toLowerCase())) {
                fileList.add(directory);
            }

        }
        return fileList;
    }

    /**
     * 添加所有文件
     *
     * @param fileList 一个用来保存文件对象的列表
     * @param findDir 包含文件的路径
     * @param fileType 过滤的文件类型
     * @return
     */
    public static ArrayList<File> addAllFiles(ArrayList<File> fileList, String findDir, String fileType) {
        File directory = new File(findDir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files==null) {//处理异常 by 倪永
                return  fileList;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    findFiles(file.getAbsolutePath(), fileType);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(fileType) || fileName.endsWith(fileType.toLowerCase())) {
                        fileList.add(file);
                    }
                }
            }
        } else {
            String fileName = directory.getName();
            if (fileName.endsWith(fileType) || fileName.endsWith(fileType.toLowerCase())) {
                fileList.add(directory);
            }

        }
        return fileList;
    }
    /**
     * 添加所有文件
     *
     * @param fileList 一个用来保存文件对象的列表
     * @param findDir 包含文件的路径
     * @param fileType 过滤的文件类型
     * @return
     */
    public static ArrayList<File> addAllGJBFiles(ArrayList<File> fileList, String findDir, String fileType) {
        File directory = new File(findDir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files==null) {//小胖代码有bug  //处理NullPointerException异常 by 倪永
                return fileList;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    findGJBFiles(file.getAbsolutePath());
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith("ZB") || fileName.endsWith("ZB".toLowerCase())) {
                        if (!(fileName.toLowerCase().endsWith("yzb")||fileName.toLowerCase().endsWith("zzb")
                                ||fileName.toLowerCase().endsWith("xzb")||fileName.toLowerCase().endsWith("vzb")
                                ||fileName.toLowerCase().endsWith("wzb")||fileName.toLowerCase().endsWith("yzb")
                                ||fileName.toLowerCase().endsWith("uzb")||fileName.toLowerCase().endsWith("szb")
                                ||fileName.toLowerCase().endsWith("tzb"))) {
                            fileList.add(file);
                        }
                    }
                }
            }
        } else {
            String fileName = directory.getName();
            if (fileName.endsWith(fileType) || fileName.endsWith(fileType.toLowerCase())) {
                fileList.add(directory);
            }

        }
        return fileList;
    }

    
    
    /**
     * 获取地图目录下的所有分幅数据目录
     * @param path 地图数据根目录
     * @return 比例尺文件夹列表
     */
    public static ArrayList<File> getDirList(String path) {
        ArrayList<File> dirList = new ArrayList<>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            File[] dirs = dir.listFiles();
            for (File file : dirs) {
                if (file.isDirectory()) {
                    dirList.add(file);
                }
            }
        }
        return dirList;

    }

    
      /**
     * 查找含有坐标的文件
     *
     * @param findDir
     * @return 所有包含坐标的文件列表
     */
    public static ArrayList findGJBFiles(String findDir) {

        ArrayList<File> fileList = new ArrayList<>();
        File directory = new File(findDir);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    addAllGJBFiles(fileList, file.getAbsolutePath(), "ZB");
                } else {
                    String fileName = file.getName();
//                    if (fileName.endsWith("ZB") || fileName.endsWith("ZB".toLowerCase())) {
//                        if(!fileName.toLowerCase().endsWith("yzb")){
//                             fileList.add(files[i]);
//                        }
//                       
//                    }
                    if (fileName.endsWith("ZB") || fileName.endsWith("ZB".toLowerCase())) {
                        if (!(fileName.toLowerCase().endsWith("yzb")||fileName.toLowerCase().endsWith("zzb")
                                ||fileName.toLowerCase().endsWith("xzb")||fileName.toLowerCase().endsWith("vzb")
                                ||fileName.toLowerCase().endsWith("wzb")||fileName.toLowerCase().endsWith("yzb")
                                ||fileName.toLowerCase().endsWith("uzb")||fileName.toLowerCase().endsWith("szb")
                                ||fileName.toLowerCase().endsWith("tzb"))) {
                            fileList.add(file);
                        }
                    }
                }
            }
        } else {
            String fileName = directory.getName();
            if (fileName.endsWith("ZB") || fileName.endsWith("ZB".toLowerCase())) {
                fileList.add(directory);
            }

        }
        return fileList;
        
       
    }

    
     public static long getFileSize(File f) throws Exception{//取得文件大小
        long s=0;
         if (f.exists()) {
             FileInputStream fis = new FileInputStream(f);
            s= fis.available();
         } else {
             f.createNewFile();
             System.out.println("文件不存在");
         }
         return s;
     }
     // 递归
    public static long getDirctorySize(File f)throws Exception//取得文件夹大小
    {
         long size = 0;
         File flist[] = f.listFiles();
        for (File flist1 : flist) {
            if (flist1.isDirectory()) {
                size = size + getDirctorySize(flist1);
            } else {
                size = size + flist1.length();
            }
        }
         return size;
     }

    public  static String formatFileSize(long fileS) {//转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
         String fileSizeString;
         if (fileS < 1024) {
             fileSizeString = df.format((double) fileS) + "B";
         } else if (fileS < 1048576) {
             fileSizeString = df.format((double) fileS / 1024) + "K";
         } else if (fileS < 1073741824) {
             fileSizeString = df.format((double) fileS / 1048576) + "M";
         } else {
             fileSizeString = df.format((double) fileS / 1073741824) + "G";
         }
         return fileSizeString;
     }

    
     public static long getlist(File f){//递归求取目录文件个数

         File flist[] = f.listFiles();
         long size=flist.length;
        for (File flist1 : flist) {
            if (flist1.isDirectory()) {
                size = size + getlist(flist1);
                size--;
            }
        }
         return size;
  
   }
     
      public static String readFileContent(File file) {
        String msg = "";

        try {
            try (FileInputStream finput = new FileInputStream(file);
                    InputStreamReader reader = new InputStreamReader(finput, "UTF-8");
                    BufferedReader in = new BufferedReader(reader)) {
                String line ;
                while ((line = in.readLine()) != null) {
                    msg += line + "\n";
                }
            }
        } catch (FileNotFoundException |
                UnsupportedEncodingException ex) {
            System.err.println(ex.getLocalizedMessage());

        } catch (IOException ex) {
             System.err.println(ex.getLocalizedMessage());
        }
        return msg;
    }
}
