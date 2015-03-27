/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.thread;

import com.beyondb.bean.ImportTaskInfo;
import com.beyondb.bean.TaskInfoBase;
import com.beyondb.datasource.BydDataSource;
import com.beyondb.multimedia.StoreMode;
import com.beyondb.task.Task;
import com.beyondb.task.multimedia.MultiMediaTask;
import com.beyondb.utils.FileUtils;
import com.beyondb.utils.LoggerUtil;
import com.beyondb.utils.initSystemParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *单个多媒体任务完成入库
 * @author 倪永
 */
public class ThreadMultiMediaImportIntoDB extends ThreadImportIntoDB{
    private static Logger m_logger = Logger.getLogger(ThreadMultiMediaImportIntoDB.class.getName());
       private MultiMediaTask m_Task;
    /**
     *
     *
     * @param task
     */
    public ThreadMultiMediaImportIntoDB(
            MultiMediaTask task) {

        LoggerUtil.setLogingProperties(m_logger, Level.ALL,
                (String) initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_LOG_MULTIMEDIA));
        this.setName("导入单个多媒体任务线程");
        m_Task = task;
    }
    

    private final String m_encode = "GBK";


    
    public static String createCommand(MultiMediaTask task) {
        StoreMode mode = task.mediaStoreMode;
        BydDataSource datasource = (BydDataSource) task.targetDataSource;

        StringBuilder builder = new StringBuilder();

        builder.append("bmimport ");
        if (!datasource.getVirtualNode().isEmpty()) {
            builder.append(" -d \"").append(datasource.getVirtualNode()).append("::")
                    .append(datasource.getDatabaseName()).append("\"");
        } else {

            builder.append(" -d \"").append(datasource.getDatabaseName()).append("\"");
            if (!datasource.getIp().isEmpty() && !datasource.getInstance().isEmpty()) {
                builder.append(" -h \"").append(datasource.getIp()).append("\" ");
                builder.append(" -i \"").append(datasource.getInstance()).append("\" ");
            }
        }

        builder.append("  -u \"").append(datasource.getName()).append("\"");
        builder.append("  -p \"").append(datasource.getPassword()).append("\"");
        builder.append("  -a \"").append(task.targetDBTableName).append("\"");
        builder.append("  -ac \"media\"");
        if (task.taskIsOverWrite) {
            builder.append("  -w \"mm_id='").append(task.taskID).append("' and name='").append(task.taskName).append("' \"");
        }
        builder.append("  -ds \"(mm_id VARCHAR(100),name VARCHAR(100), media  mt_media,updateTime datetime)\"");
        builder.append("  -ai \"VALUES('").append(task.taskID)
                .append("','").append(task.taskName)
                .append("',st_create('").append(mode.tableBName)
                .append("'),'").append(new Timestamp(new Date().getTime()).toString()).append("')\"");

        builder.append("  -ib ").append(mode.blocking);
        builder.append("  -f  \"").append(task.importFile.getAbsolutePath()).append("\"");
        builder.append("  -bs \"").append(mode.blocksize).append("\"");
        builder.append("  -m ").append(mode.metaschemaId);
        //导入后多媒体数据格式，默认值为多媒体数据原有格式
        if (mode.fileformat != null) {
            builder.append("  -fm ").append(mode.fileformat.name());
        }
        //导入后多媒体数据比特率，默认值为多媒体数据原有比特率
        if (mode.bitRate>0) {
            builder.append("  -br  ").append(mode.bitRate);
        }

        //导入后多媒体数据帧速率，默认值为多媒体数据原有帧速率
        if (mode.frameRate>0) {
            builder.append("  -fr  ").append(mode.frameRate);
        }
           //导入后多媒体数据分辨率，默认值为多媒体数据原有分辨率
        if (mode.frameResolution>0) {
            builder.append("  -r  ").append(mode.frameResolution);
        }


        return builder.toString();

    }

    /**
     * 采用bmimport工具
     *
     * @param task
     * @return
     */
    private int insertMultiMediaRecord(MultiMediaTask task) throws InterruptedException {
        int count = 0;

    
        try {
            if (m_Task.cmd == null) {
                m_Task.cmd = createCommand(task);
            }

            task.taskProgressValue = 20;
            if (m_isStop) {
                return 0;
            }
            while (m_isSuspend) {
                Thread.sleep(100);//循环
                task.taskProgressValue = 40;
            }
            count = ExecuteShell(); //执行bash命令是无法中途停止的。
        } catch (InterruptedException ex) {
            m_logger.log(Level.SEVERE, "采用bmimport工具导入多媒体数据", ex);
        }

        return count;

    }

    public ImportTaskInfo call() throws Exception {

        ImportTaskInfo taskInfo;


        String info = "";

        int totalcount = insertMultiMediaRecord(m_Task);

        if (totalcount > 0) {
            info += TaskInfoBase.info_Success;

        } else if (info.isEmpty()) {
            info += TaskInfoBase.info_Failed;
        }

        taskInfo = new ImportTaskInfo(totalcount, info, m_Task);
        return taskInfo;
    }

    @Override
    public void run() {

        try {
            ImportTaskInfo taskInfo = null;
            m_Task.taskProgressValue = 10;
            taskInfo = call();

            m_Task.taskProgressValue = 100;
            m_Task.taskStatus = Task.taskStatus_Finished;
            
            m_Task.taskInfo = taskInfo;
        } catch (Exception ex) {
            m_logger.log(Level.SEVERE, "导入多媒体数据", ex);
        }

    }

    private int ExecuteShell() {
        int count = 0;
        try {

            Runtime run = Runtime.getRuntime();
            Process exec = run.exec(m_Task.cmd);

            String info;
            String message;
            // 得到相应的控制台输出信息
            try (InputStream inputS = exec.getInputStream(); // 得到相应的控制台输出信息
                    InputStreamReader bi = new InputStreamReader(inputS, m_encode); 
                    BufferedReader br = new BufferedReader(bi)) {
                info = "";
                message = br.readLine();
                while (message != null && !"".equals(message)) {
                    // 将信息输出
                    info += message + "\n";
                    message = br.readLine();
                }
                if (info.length() > 0) {

                    String flagString;
                    String flagString2;

                    flagString = "0%.0.10%.0.20%.0.30%.0.40%.0.50%.0.60%.0.70%.0.80%.0.90%.0.100%.0.100%";
                    flagString2 = "0.10.20.30.40.50.60.70.80.90.100";

                    info = "当前任务ID：   " + m_Task.taskID + "\n" + "命令：" + m_Task.cmd + " \n" + info;

                    if (info.contains(flagString)
                            || info.contains(flagString2)) {
                        count = 1;
                        m_logger.log(Level.INFO, info);
                    } else {
                        //失败
                        m_logger.log(Level.SEVERE, info);
                    }

                }
            }

            try (InputStream errorStream = exec.getErrorStream();
                    InputStreamReader errorisr = new InputStreamReader(errorStream, m_encode);
                    BufferedReader errorbr = new BufferedReader(errorisr)) {   
                info = "";
                message = errorbr.readLine();
                
                while (message != null && !"".equals(message)) {
                    info += message + "\n";
                    message = errorbr.readLine();
                }
                if (info.length() > 0) {
                    m_logger.log(Level.SEVERE, info);
                }
                
            }

        } catch (IOException e) {
            m_logger.log(Level.SEVERE, "执行bmimport命令工具", e);

        }
        return count;
    }

    /**
     * 读取命令文件内容
     * @param file
     * @return
     */
    public String readCmdFileContent(File file) {
        
        return FileUtils.readFileContent(file);
    }

}
