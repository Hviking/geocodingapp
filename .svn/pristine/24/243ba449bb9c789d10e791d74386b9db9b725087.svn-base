/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.thread;

import com.beyondb.bean.ImportTaskInfo;
import com.beyondb.bean.TaskInfoBase;
import com.beyondb.datasource.BydDataSource;
import com.beyondb.raster.StoreMode;
import com.beyondb.spacialization.Feature;
import com.beyondb.spacialization.FeatureUtils;
import com.beyondb.task.Task;
import com.beyondb.task.raster.RasterTask;
import com.beyondb.ui.JPanelDialog;
import com.beyondb.utils.FileUtils;
import com.beyondb.utils.LoggerUtil;
import com.beyondb.utils.initSystemParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * 单个栅格任务完成入库
 *
 * @author 倪永
 */
public class ThreadRasterImportIntoDB extends ThreadImportIntoDB {

    private static Logger m_logger = Logger.getLogger(ThreadRasterImportIntoDB.class.getName());
    private RasterTask m_Task;
    private final String m_encode = "GBK";
    private boolean m_OverwriteRecord;
    /**
     * 主键字段
     */
    private final String[] m_PrimaryKeys = {"tf_id", "name"};

    private void init(RasterTask task, boolean overwriteRecord) {
        this.m_Task = task;
        this.m_OverwriteRecord = overwriteRecord;
    }



    protected Feature makeTask2Feature() {
        String[] record = new String[m_PrimaryKeys.length];
        record[0] = m_Task.taskID;
        record[1] = m_Task.taskName;
        return FeatureUtils.readAttributes2Feature(m_PrimaryKeys, record);
    }

    protected ArrayList<String> getPrimaryKeys() {
        ArrayList<String> primaryKeys = new ArrayList<>();
        primaryKeys.addAll(Arrays.asList(m_PrimaryKeys));
        return primaryKeys;
    }

    protected boolean hasRasterRecord() throws SQLException {
        Feature feature = makeTask2Feature();
        ArrayList<String> primaryKeys = getPrimaryKeys();
        return m_bydOperator.hasRecord(m_Task.targetDBTableName, feature.Fields, primaryKeys);
    }

    protected boolean deleteRasterRecord() {
        try {
            Feature feature = makeTask2Feature();
            ArrayList<String> primaryKeys = getPrimaryKeys();
            String sql = m_bydOperator.produceDeleteSql(m_Task.targetDBTableName, feature.Fields, primaryKeys);
            int result = m_bydOperator.executeSql(sql);
            if (result == 0) {
                //执行失败;
                return false;
            }
        } catch (SQLException ex) {
            m_logger.log(Level.SEVERE, "删除栅格数据记录失败", ex);
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @param task
     * @param overwriteRecord 如果存在改栅格对象记录，是否覆盖
     */
    public ThreadRasterImportIntoDB(
            RasterTask task, boolean overwriteRecord) {
        init(task, overwriteRecord);
        LoggerUtil.setLogingProperties(m_logger, Level.ALL, (String) initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_LOG_RAST));

        this.setName("导入单个栅格文件任务线程");

    }

    public static String createCommand(RasterTask task) {
        StoreMode mode = task.rasterStoreMode;
        BydDataSource datasource = (BydDataSource) task.targetDataSource;

        StringBuilder builder = new StringBuilder();

        builder.append("brimport ");
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
        builder.append("  -ac \"raster\"");
        builder.append("  -ds \"(id Integer,tf_id VARCHAR(100),name VARCHAR(100), RASTER ST_Raster,updateTime datetime)\"");
        builder.append("  -ai \"VALUES(1,'").append(task.taskID)
                .append("','").append(task.taskName)
                .append("',st_create('").append(mode.tableBName)
                .append("'),'").append(new Timestamp(new Date().getTime()).toString()).append("')\"");

        builder.append("  -pl ").append(mode.pyd_level);
        builder.append("  -r \"nn\"");
        builder.append("  -f  \"").append(task.importFile.getAbsolutePath()).append("\"");
        builder.append("  -b \"").append(mode.blocksize).append("\"");
        builder.append("  -c \"").append(mode.comp.name()).append("\"");
        builder.append("  -cq ").append(mode.quality);
        builder.append("  -s 0");
        builder.append("  -ulc \"").append(mode.ulccoord).append("\"");
        builder.append("  -brp \"").append(mode.brpcoord).append("\"");
        builder.append("  -iv \"").append(mode.inter.name()).append("\"");
        builder.append("  -m ").append(mode.metaschemaId);
        builder.append("  -rastmode  \"").append(mode.mode.name()).append("\"");
//        if (mode.transform) {
//            CalcTFupLeftCoordinate calc = new CalcTFupLeftCoordinate();
//            calc.setNewTFName(tf_id);
//            Point2D ulpoint = calc.calcUpLeftCoordinate();
//            if (ulpoint!=null) {
//                builder.append("  -ulx  ").append(ulpoint.getX());
//                builder.append("  -uly  ").append(ulpoint.getY());
//                builder.append("  -rw  ").append(calc.getDeltX());
//                builder.append("  -rh  ").append(calc.getDeltY());
//            }
//            else
//            {
//                LOG.error("图幅号"+tf_id+"有误，无法开启仿射变换，文件"
//                        +file.getAbsolutePath()+"导入失败");
//               return count;
//            }
//           
//        }
        if (!mode.SRID.isEmpty()) {
            builder.append("  -s  ").append(mode.SRID);
        }

        String cmd = "";
//        cmd ="@echo off\n";
//
////                   prefix+="rem 判断当前盘符是否是系统盘\n");
////                   prefix+="rem echo current driver:%~d0\n");
////                   prefix+="rem echo system driver: %HOMEDRIVE%\n");
//        cmd += "If not %~d0==%II_SYSTEM:~,3% cd /d %II_SYSTEM:~,3%\n";
//        cmd += "set  beyondbBin=%II_SYSTEM%%\\beyondb\\bin\n";
//        cmd += "cd %beyondbBin%\n";
//
//        cmd += builder.toString();

        cmd = builder.toString();

        return cmd;

    }

    /**
     * 采用brimport工具
     *
     * @param task

     * @return
     */
    private int insertRasterRecord(RasterTask task) throws InterruptedException {
        int count = 0;

       // UUID randomUUID = java.util.UUID.randomUUID();
//        String commandfileName = randomUUID.toString() + ".bat";
//         String commandfileName =task.taskID+".bat";
        try {
//            if (!m_PathFile.exists()) {
//                m_PathFile.mkdir();
//            }
//            File cmdFile = new File(m_PathFile.getAbsolutePath() + File.separator + commandfileName);
//            if (cmdFile.exists()) {
//                cmdFile.delete();
//            }
//            cmdFile.createNewFile();
//
//            // run.exec("attrib " + "\"" + cmdFile.getAbsolutePath() + "\"" + " +H");
//            cmdFile.setExecutable(true);
            String cmd = "";
            if (m_Task.cmd == null) {
                cmd = createCommand(task);
                m_Task.cmd = cmd;

            }

//            try (FileOutputStream fileOutputStream = new FileOutputStream(cmdFile, true); OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream)) {
//                writer.append(cmd);
//            }
            task.taskProgressValue = 20;
            if (m_isStop) {
                return 0;
            }
            while (m_isSuspend) {
                Thread.sleep(100);//循环
                task.taskProgressValue = 40;
            }
            count = ExecuteShell(false); //执行bash命令是无法中途停止的。
        } catch (InterruptedException ex) {
            m_logger.log(Level.SEVERE, "采用brimport工具导入栅格数据线程中断异常", ex);
        }
        return count;

    }

    public ImportTaskInfo call() throws Exception {

        ImportTaskInfo taskInfo = null;

        int totalcount = 0;
        String info = "";

        if (m_FirstImport) {

            //初始导入，不需要判断数据在库中是否存在，直接插入
            totalcount = insertRasterRecord(m_Task);
        } else {
            //非第一次导入
            if (hasRasterRecord()) {
                //有 
                if (m_OverwriteRecord) {
                    //删除原有记录
                    if (deleteRasterRecord()) {
                        totalcount = insertRasterRecord(m_Task);
                    } else {
                        //正常情况不会出现删除失败，如果出现，此时对于要覆盖的数据来说未必是用户期望的。 
                        totalcount = 0;
                    }

                } else {
                    totalcount = 1;
                }
            } else {
                //没有重复记录
                totalcount = insertRasterRecord(m_Task);
            }
        }

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
            //执行完毕
//            if (taskInfo.getTotalCount() > 0) {
//                m_Task.taskProgressValue = 100;
//            }else
//            {
//                 m_Task.taskProgressValue = 0;
//            }
            m_Task.taskStatus = Task.taskStatus_Finished;

            m_Task.taskInfo = taskInfo;
        } catch (Exception ex) {
            Logger.getLogger(ThreadRasterImportIntoDB.class.getName()).log(Level.SEVERE, null, ex);
            JPanelDialog jPanelDialog = new JPanelDialog();
            jPanelDialog.setShowMessage("栅格数据入库出错\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }

    }

    private int ExecuteShell(boolean  isExport) {
        int count = 0;
        try {

            Runtime run = Runtime.getRuntime();
            Process exec = run.exec(m_Task.cmd);

            String message;
            String info;
            // 得到相应的控制台输出信息
            try (InputStream inputS = exec.getInputStream(); // 得到相应的控制台输出信息
                    InputStreamReader bi = new InputStreamReader(inputS, m_encode);
                    BufferedReader br = new BufferedReader(bi)) {
                message = "";
                info = "";
                message = br.readLine();
                while (message != null && !"".equals(message)) {
                    // 将信息输出
                    info += message + "\n";
                    message = br.readLine();
                }
                if (info.length() > 0) {

                    String flagString = "";
                    String flagString2 = "";
                    if (isExport) {
                    //不同版本，表现成功的方式不同
                    flagString = "0..10..20..30..40..50..60..70..80..90..100..";

                } else {
                    flagString = "0%.0.10%.0.20%.0.30%.0.40%.0.50%.0.60%.0.70%.0.80%.0.90%.0.100%.0.100%";
                    flagString2 = "0.10.20.30.40.50.60.70.80.90.100";
                }

                info = "当前任务ID：   " + m_Task.taskID + "\n" + "命令：" + m_Task.cmd + " \n" + info;

                if (info.contains(flagString)
                        || info.contains(flagString2)) {
                    count = 1;
                    m_logger.log(Level.INFO, info);
                } else {
                    //失败
                    m_logger.log(Level.SEVERE, info);
                }
                
            }              }

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
            m_logger.log(Level.SEVERE, "执行命令工具出错", e);

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
