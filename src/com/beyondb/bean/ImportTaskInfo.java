/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.bean;


import com.beyondb.datasource.BydDataSource;
import com.beyondb.task.Task;


/**
 * task的返回对象
 *
 * @author beyondb
 */
public class ImportTaskInfo extends TaskInfoBase {


    private final  Task m_Task;
    public ImportTaskInfo(int count, String info,Task task) {
        m_totalcount = count;
        m_info = info;
        m_Task = task;
        m_DataSource =task.targetDataSource;
    }

    /**
     * 获取任务
     * @return 
     */
    public  Task getTask()
    {
        return m_Task;
    }


      
     
}
