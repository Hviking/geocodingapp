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
 *多媒体数据入库
 * @author lbs
 */
public class MultiMediaIntoDB extends  JPanel_IntoDB{

    public MultiMediaIntoDB() {
        setName("多媒体                       ");
         m_ThreadMonitor.setName("全部任务的状态监控-多媒体");
         m_SingleTaskStatusAskThread.setName("单个任务状态的监控-多媒体");
    }

     @Override
    TaskTableModel getTableModel() {
          //设置表格的列名
        String[] columnName = new String[9];
        columnName[0] = "任务ID";
        columnName[1] = "任务名";
        columnName[2] = "文件大小";
        columnName[3] = "进度";
        columnName[4] = "状态";
        columnName[5] = "文件路径";
        columnName[6] = "目标数据库";
        columnName[7] = "目标数据表";
        columnName[8] = "是否改写";
        Object[][] data = new Object[0][columnName.length];
        TaskTableModel tableModel = new TaskTableModel(this,data, columnName,TaskTableModel.TaskType.Geometry);
        return tableModel;
    }
    


    

    @Override
    void newTask() {
        final JPanel_NewMultiMediaIntoDB newMultiMediaIntoDB = new JPanel_NewMultiMediaIntoDB();

        DialogUtil dialogUtil = new DialogUtil();
        int result = dialogUtil.showDialog(null, newMultiMediaIntoDB, "新建多媒体任务");
        if (result == 0) {
            //成功
            for (Task task : newMultiMediaIntoDB.getTasks()) {
                m_TableModel.addTask(task);
            }
        }

    }

    @Override
    void openLog() {
        String logPath = LoggerUtil.getLogAbsoluteName(
                (String) initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_LOG_MULTIMEDIA));
        File logFile = new File(logPath);
        if (logFile.exists()) {
            try {
                //打开当天的日志
                Desktop.getDesktop().open(new File(logFile.getAbsolutePath()));
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "读取日志内容出错", ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "没有当天的最新日志, 原因： 没有正在进行的任务", "提示", JOptionPane.YES_OPTION);
        }
        
    }

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

 
}
