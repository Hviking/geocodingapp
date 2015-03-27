/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.utils;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author beyondb
 */
public class JarUtils {
    
    // jar包名   
    private String jarName;  
    // jar包所在绝对路径   
    private String jarPath;  
   private static final Logger m_Logger = Logger.getLogger(JarUtils.class.getName());
    public JarUtils(Class clazz) {  
        String path = clazz.getProtectionDomain().getCodeSource().getLocation()  
                .getFile();  
        try {  
            path = java.net.URLDecoder.decode(path, "UTF-8");  
        } catch (java.io.UnsupportedEncodingException ex) {  
            m_Logger.log(Level.SEVERE, null,  
                    ex);  
        }  
        java.io.File jarFile = new java.io.File(path);  
        this.jarName = jarFile.getName();  
        java.io.File parent = jarFile.getParentFile();  
        if (parent != null) {  
            this.jarPath = parent.getAbsolutePath();  
        }  
    }  
  
    /** 
     * 获取Class类所在Jar包的名称 
     *  
     * @return Jar包名 (例如：C:\temp\demo.jar 则返回 demo.jar ) 
     */  
    public String getJarName() {  
        try {  
            return java.net.URLDecoder.decode(this.jarName, "UTF-8");  
        } catch (java.io.UnsupportedEncodingException ex) {  
             m_Logger.log(Level.SEVERE, null,  
                    ex);  
        }  
        return null;  
    }  
  
    /** 
     * 取得Class类所在的Jar包路径 
     *  
     * @return 返回一个路径 (例如：C:\temp\demo.jar 则返回 C:\temp ) 
     */  
    public String getJarPath() {  
        try {  
            return java.net.URLDecoder.decode(this.jarPath, "UTF-8");  
        } catch (java.io.UnsupportedEncodingException ex) {  
            m_Logger.log(Level.SEVERE, null,  
                    ex);  
        }  
        return null;  
    }  

    public static String findFile(String fileName) {
        JarUtils jarUtils = new JarUtils(JarUtils.class);
        String path = "";
        //遍历文件夹
        File rootFile = new File(jarUtils.getJarPath());
        for (File f : rootFile.listFiles()) {
            if (f.getName().toLowerCase().equals(fileName.toLowerCase())) {
                path = f.getAbsolutePath();
                break;
            }
        }

        return path;
    }
      public static String getJarUtilPath() {
        JarUtils jarUtils = new JarUtils(JarUtils.class);
        return jarUtils.getJarPath();
        
    }
    public static void main(String[] args) throws Exception {  
        JarUtils ju = new JarUtils(JarUtils.class);  
        JOptionPane.showMessageDialog(null,  
                ju.getJarPath() + "/" + ju.getJarName());  
    }  

}
