/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.thread;

import com.beyondb.bean.TaskInfoBase;
import com.beyondb.task.Task;
import com.beyondb.ui.display.TaskTableModel;
import com.beyondb.utils.PopWindowUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableModel;

/**
 *监视多个任务轮询线程,
 * 全部任务队列始终不变，并将成功的任务从正在运行列表移动到已完成列表。
 * @author lbs
 */
public class ThreadTaskStatusMonitor extends  Thread{
    TableModel tableModel_Running;
    TableModel tableModel_Finished;
    TableModel tableModel_All;
    private boolean m_IsStop;

    public ThreadTaskStatusMonitor(TableModel all, TableModel running, TableModel finished) {
        tableModel_Running = running;
        tableModel_All = all;
        tableModel_Finished = finished;
        this.setName("ThreadTaskStatusMonitor");
    }

    public TableModel getFinishedTableModel() {
        return tableModel_Finished;

    }

    public TableModel getRunningTableModel() {
        return tableModel_Running;
    }

    public void setStop(boolean b) {
        m_IsStop = b;
    }

     @Override
    public void run() {
   
         TaskTableModel tableModelAll = (TaskTableModel) tableModel_All;
         if (tableModelAll == null) {
             return;
         }
         TaskTableModel tableModelRunning = (TaskTableModel) tableModel_Running;
         TaskTableModel tableModelFinished = (TaskTableModel) tableModel_Finished;

         boolean popTipsBubble = false;
         while (!m_IsStop) {
                 //检查全部队列中的任务状态
                 for (int index = 0; index < tableModelAll.getRowCount(); index++) {
                     
                     Task task = tableModelAll.getTask(index);
                     switch (task.taskStatus) {
                         case Task.taskStatus_Running:
                             //加入正在执行队列
                             if (!tableModelRunning.hasTask(task)) {
                                 tableModelRunning.addTask(task);
                                 popTipsBubble = true;//允许弹出结束提示
                             }
                             break;
                         case Task.taskStatus_Finished:
                             //从正在执行队列中删除
                             if (tableModelRunning.hasTask(task)) {
                                 tableModelRunning.removeTask(task);
                             }
                             if (task.taskInfo != null) {
                                 if (task.taskInfo.getInfo().equals(TaskInfoBase.info_Success)) {
                                     //加入完成队列 
                                     if (!tableModelFinished.hasTask(task)) {
                                         tableModelFinished.addTask(task);
                                     }
                                 }
                             }
                             break;
                     }
                    
                 } //检查全部队列中的任务状态

             if (tableModelAll.getRowCount() > 0) {

                 //刷新正在进行的任务的队列状态
                 tableModelRunning.changeRowTasks();
                 //刷新全部任务的队列状态
                 tableModelAll.changeRowTasks();
                 //执行完毕的队列任务状态没有变化,不需要刷新

             }

             if (tableModelFinished.getRowCount()>0
                     &&tableModelRunning.getTasks().isEmpty()) {
                 //有执行成功的任务，同时又没有正在执行的任务，说明当前所有任务执行完毕
                 if (popTipsBubble) {
                       popSuccessBubble();
                       popTipsBubble=false;//不允许再弹出提示
                 }
             }
             wait(300);
         }

        
    }
    
    private void popSuccessBubble()
    {
        //弹出提示气泡
        PopWindowUtil pop =new PopWindowUtil();
 
        pop.setTip("数据入库已完成！");
    }
       private void wait(int millseconds)
        {
               try {
                    Thread.sleep(millseconds);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadTaskStatusMonitor.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    
   
}
