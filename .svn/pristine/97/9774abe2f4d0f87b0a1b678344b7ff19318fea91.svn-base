/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 *获取操作系统的环境变量，而不是JVM的
 * @author lbs
 */
public class SysPropUtil {

   
//返回当前系统变量的函数，结果放在一个Properties里边，这里只针对win2k以上的，其它系统可以自己改进
    public static Properties getEnv() throws Exception {
        Properties prop = new Properties();
        String OS = System.getProperty("os.name").toLowerCase();
        Process p = null;
        if (OS.contains("windows")) {
            p = Runtime.getRuntime().exec("cmd /c set"); //其它的操作系统可以自行处理， 我这里是win
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            int i = line.indexOf("=");
            if (i > -1) {
                String key = line.substring(0, i);
                String value = line.substring(i + 1);
                prop.setProperty(key, value);
            }
        }
        return prop;
    }
}
