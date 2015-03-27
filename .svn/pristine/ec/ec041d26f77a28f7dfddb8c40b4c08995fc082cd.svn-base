/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *日志操作类
 * @author 倪永
 */
public class LoggerUtil {
    


    /**
     * 得到要记录的日志的路径及文件名称
     * @param logName  日志文件的名称
     * @return
     */
    
    public static String getLogAbsoluteName(String logName) {
        String logPath = JarUtils.getJarUtilPath() + File.separatorChar + "log";
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logPath += File.separatorChar +logName.toUpperCase()+ sdf.format(new Date()) + ".log";

        return logPath;
    }
    
    
    /**
     * 配置Logger对象输出日志文件路径
     * @param logger 日志对象
     * @param level 在日志文件中输出level级别以上的信息
     * @param logName  要保存的日志文件名称
     * @throws SecurityException
     */
    public static void setLogingProperties(Logger logger,Level level,String logName) {
        FileHandler fh;
        try {
            fh = new FileHandler(getLogAbsoluteName(logName),true);
            logger.addHandler(fh);//日志输出文件
            logger.setLevel(level);
            fh.setFormatter(new SimpleFormatter());//输出格式
            //logger.addHandler(new ConsoleHandler());//输出到控制台
        } catch (SecurityException e) {
            logger.log(Level.SEVERE, "安全性错误", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"读取文件日志错误", e);
        }
    }
    
    public static void main(String [] args) {
        Logger logger = Logger.getLogger("sgg");
            LoggerUtil.setLogingProperties(logger,Level.SEVERE,"testlog");
            logger.log(Level.INFO, "ddddd");
            logger.log(Level.INFO, "eeeeee");
            logger.log(Level.INFO, "ffffff");
            logger.log(Level.INFO, "gggggg");
            logger.log(Level.SEVERE, "hhhhhh");
    
        
    }
}

