/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import com.beyondb.task.Task;
import com.beyondb.ui.display.TaskTableModel;
import com.beyondb.utils.DialogUtil;
import com.beyondb.utils.LoggerUtil;
import com.beyondb.utils.initSystemParams;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *栅格数据入库
 * @author lbs
 */
public class RasterIntoDB extends  JPanel_IntoDB{

    public RasterIntoDB() {
        setName("栅格                       ");
        m_ThreadMonitor.setName("全部任务的状态监控-栅格");
        m_SingleTaskStatusAskThread.setName("单个任务状态的监控-栅格");
    }
    
        @Override
     public TaskTableModel getTableModel() {
          //设置表格的列名
        String[] columnName = new String[7];
        columnName[0] = "任务ID";
        columnName[1] = "任务名";
        columnName[2] = "文件大小";
        columnName[3] = "进度";
        columnName[4] = "状态";
        columnName[5] = "文件路径";
        columnName[6] = "存储文件的数据库表";
        Object[][] data = new Object[0][columnName.length];
        TaskTableModel tableModel = new TaskTableModel(this,data, columnName,TaskTableModel.TaskType.Raster);
        return tableModel;
    }
    /**
     * 将 Task转换为Object[]
     *
     * @param rowTask
     * @return
     */
     @Override
     public Object[] parseTask2TableModelRowObjectArray(Task rowTask) {
        Object[] rowData = new Object[7];
        for (int j = 0; j < 7; j++) {
            Object obj = null;
            if (j == 0) {
                obj = rowTask.taskID;
            } else if (j == 1) {
                obj = rowTask.taskName;
            } else if (j == 2) {
                obj = rowTask.taskSize;
            } else if (j == 3) {
                obj = rowTask.taskProgressValue;
            } else if (j == 4) {
                obj = rowTask.parseTaskStatus();
            } else if (j == 5) {
                obj = rowTask.importFile.getAbsolutePath();
            } else if (j == 6) {
                obj = rowTask.targetDBTableName;
            }
            rowData[j] = obj;
        }
        return rowData;
    }
   
        @Override
     public void newTask() {
        final Jpanel_NewRasterIntoDB newRasterIntoDB = new Jpanel_NewRasterIntoDB();

         DialogUtil dialogUtil = new DialogUtil();
        int res = dialogUtil.showDialog(null, newRasterIntoDB, "新建栅格任务");
//        int res = JOptionPane.showConfirmDialog(this, newRasterIntoDB,
//                "新建任务", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res == 0) {
            for (Task task : newRasterIntoDB.getTasks()) {
                m_TableModel.addTask(task);
            }
        }
    }
     
     /**
      * 打开栅格日志记录
      */
        @Override
     public void openLog()
     {
        String logPath = LoggerUtil.getLogAbsoluteName((String) initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_LOG_RAST));
        File logFile = new File(logPath);
        if (logFile.exists()) {
            try {
                //打开当天的日志
                Desktop.getDesktop().open(new File(logFile.getAbsolutePath()));
            } catch (IOException ex) {
               Logger.getLogger(JPanel_IntoDB.class.getName()).log(Level.SEVERE, "读取日志内容出错", ex);
            }
//            Runtime run = Runtime.getRuntime();
//            try {
//                String OS = System.getProperty("os.name").toLowerCase();
//                Process p = null;
//                String cmd = "";
//                if (OS.contains("windows")) {
//                    cmd = "notepad " + logFile.getAbsolutePath();
//                } else if (OS.contains("linux")) {
//                    cmd = "vim " + logFile.getAbsolutePath();
//                }
//                p = run.exec(cmd);
//            } catch (IOException ex) {
//                Logger.getLogger(JPanel_IntoDB.class.getName()).log(Level.SEVERE, "读取日志内容出错", ex);
//            }
        } else {
             JOptionPane.showMessageDialog(this, "没有当天的最新日志, 原因： 没有正在进行的任务", "提示",JOptionPane.YES_OPTION);
        }
     }

}
