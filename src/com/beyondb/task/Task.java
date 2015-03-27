/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.task;

import com.beyondb.bean.TaskInfoBase;
import com.beyondb.datasource.DataSource;
import java.io.File;

/**
 *
 * @author lbs
 */
public class Task {
    /**
     *就绪，可以被执行，等持提交到任务执行队列中
     */
    public static final int taskStatus_Ready = 0;
    /**
     *任务ID
     */
    public String taskID;
    /**
     *正在运行
     */
    public static final int taskStatus_Running = 1;
    /**
     *任务名称
     */
    public String taskName;
    /**
     *挂起
     */
    public static final int taskStatus_Suspend = 2;
    /**
     *要导入的文件
     */
    public File importFile;
    /**
     *完成
     */
    public static final int taskStatus_Finished = 3;
    
    /**
     * 任务排队中，已经提交运行，但还没被执行
     */
    public static final int taskStatus_Queue_up = 4;
    /**
     *任务大小
     */
    public String taskSize;
    /**
     *任务进度值
     */
    public int taskProgressValue;
    public int taskStatus;

    public TaskInfoBase taskInfo;
    
    public String parseTaskStatus() {
        String value = "";
        if (this.taskStatus == Task.taskStatus_Ready) {
            value = "就绪";
        } else if (this.taskStatus == Task.taskStatus_Running) {
            value = "进行中";
        } else if (this.taskStatus == Task.taskStatus_Suspend) {
            value = "挂起";
        } else if (this.taskStatus == Task.taskStatus_Finished) {
            value = "结束";
          } else if (this.taskStatus == Task.taskStatus_Queue_up) {
            value = "正在排队...";
        }
        return value;
    }
    
        /**
     * 存储文件的数据库表
     */
   
    public String targetDBTableName;


    public DataSource targetDataSource;
    
}
