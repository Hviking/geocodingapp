/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 打开网页类
 *
 * @author 倪永
 */
public class OpenURLUtil {

    private static Logger m_logger = Logger.getLogger(OpenURLUtil.class.getName());

    public static void openURL(String url) {
        try {
            browse(url);
        } catch (Exception e) {
            m_logger.log(Level.SEVERE, "打开网址异常", e);
        }
    }

    private static void browse(String url) throws Exception {
        //获取操作系统的名字  
        Runtime run = Runtime.getRuntime();
//            Process exec = run.exec(file.getAbsolutePath());
        Process exec = null;
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            //苹果的打开方式  
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
            openURL.invoke(null, new Object[]{url});
        } else if (osName.startsWith("Windows")) {
           //windows的打开方式。  

            exec = run.exec("rundll32 url.dll,FileProtocolHandler " + url);

        } else {
            // Unix or Linux的打开方式  
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) //执行代码，在brower有值后跳出，  
            //这里是如果进程创建成功了，==0是表示正常结束。  
            {
                if (run.exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new Exception("Could not find web browser");
            } else //这个值在上面已经成功的得到了一个进程。  
            {
                exec = run.exec(new String[]{browser, url});
            }
        }

        InputStream inputS = exec.getInputStream();

        // 得到相应的控制台输出信息
        InputStreamReader bi = new InputStreamReader(inputS, "UTF-8");
        BufferedReader br = new BufferedReader(bi);
        String line = "";

        while ((line = br.readLine()) != null) {
            // 将信息输出
            System.out.println(line);
        }

        br.close();
        bi.close();
        inputS.close();

        InputStream errorStream = exec.getErrorStream();

        InputStreamReader errorisr = new InputStreamReader(errorStream, "UTF-8");

        BufferedReader errorbr = new BufferedReader(errorisr);

        String outInfo = "";
        while ((line = errorbr.readLine()) != null) {
            // 将信息输出
            System.err.println(line);
            outInfo += line + "\n";

        }
        if (!outInfo.isEmpty()) {
            m_logger.log(Level.SEVERE, outInfo);
        }

        errorbr.close();
        errorisr.close();
        errorStream.close();
    }
}
