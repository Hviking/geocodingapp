/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.ui.display;

import com.beyondb.task.Task;
import com.beyondb.task.TaskMgr;
import com.beyondb.task.geom.GeomTaskMgr;
import com.beyondb.task.raster.RasterTaskMgr;
import com.beyondb.ui.JPanel_IntoDB;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lbs
 */
public class TaskTableModel extends DefaultTableModel {

    private JPanel_IntoDB m_IntoDB=null;//展示该表数据模型的应用界面
 

    public enum  TaskType{
        /**
         * 栅格类
         */
        Raster,
        /**
         * 几何类
         */
        Geometry,
        /**
         * 多媒体类型
         */
        MultiMedia
    }
    
 
       
     
      
    private TaskMgr m_TaskMgr=null;
    /**
     * 任务状态轮询线程
     */

    /**
     * 任务状态轮询线程
     * @param panel 用来展示数据模型的应用界面
     * @param data   数据
     * @param columneNames  标题栏  
     * @param taskType  与工作界面相对应的要处理的任务类型
     */
    public TaskTableModel(JPanel_IntoDB panel, Object[][] data, Object[] columneNames, TaskType taskType) {

        super(data, columneNames);

        m_IntoDB = panel;

        if (taskType == TaskType.Raster) {
            m_TaskMgr = new RasterTaskMgr();
        } else if (taskType == TaskType.Geometry) {
            m_TaskMgr = new GeomTaskMgr();
        }
    }


    public void addTask(Task rowTask) {
        Object[] rowData =m_IntoDB.parseTask2TableModelRowObjectArray(rowTask);
        super.addRow(rowData);
        m_TaskMgr.addTask(rowTask);
    }
    public void removeTask(int row) {
        Task task = m_TaskMgr.getTask(row);
        
        removeTask(task);
    }

    public void removeTask(Task rowTask) {
        int index = m_TaskMgr.getTaskIndex(rowTask);
        if (m_TaskMgr.removeTask(rowTask)) {
            super.removeRow(index);
        }
    }

    public Task getTask(int row) {
        return m_TaskMgr.getTask(row);
    }

    public void changeRowTask(int row) {

        Object[] rowData = m_IntoDB.parseTask2TableModelRowObjectArray(getTask(row));
        for (int i = 0; i < super.getColumnCount(); i++) {
            Object obj = rowData[i];
            super.setValueAt(obj, row, i);
        }
    }

    /**
     * 更新所有的任务
     */
    public void changeRowTasks() {
        for (int rI = 0; rI < super.getRowCount(); rI++) {
            changeRowTask(rI);
        }

    }

    public void suspendTask(int row) {

        if (m_TaskMgr.suspendTask(getTask(row))) {
            changeRowTask(row);
         
        }
    }

    public void startTask(int row) {

        if (m_TaskMgr.startTask(getTask(row))) {
            changeRowTask(row);
        }
    }
    
  
    

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task rasterTask : m_TaskMgr.getTaskList()) {
            tasks.add(rasterTask);
        }
        return tasks;
    }

    public boolean hasTask(Task task) {
        return m_TaskMgr.getTaskIndex(task) > -1;
    }

    /**
     * 关闭
     */
    public void close() {
        m_TaskMgr.getTaskList().clear();
    }



}
