/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.task;

import com.beyondb.bean.TaskInfoBase;
import com.beyondb.raster.StoreMode;
import com.beyondb.task.geom.GeomTask;
import com.beyondb.task.raster.RasterTask;
import com.beyondb.thread.NewThread;
import com.beyondb.utils.initSystemParams;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lbs
 */
public abstract class TaskMgr {

    protected static int m_runningTaskCounter; //统计真正在在执行的任务个数

    public static String getTaskDetailInfo(Task task) {
        StringBuilder info = new StringBuilder();
        if (task == null) {
            info.append("");
        } else {
            info.append("任务ID：").append(task.taskID).append("\n");
            info.append("任务名称：").append(task.taskName).append("\n");
            info.append("任务大小：").append(task.taskSize).append("\n");
            info.append("任务状态：").append(task.parseTaskStatus()).append("\n");
            info.append("文件路径：").append(task.importFile.getAbsolutePath()).append("\n");
            info.append("目标数据库：").append(task.targetDataSource.getDatabaseName()).append("\n");
            info.append("目标数据库表：").append(task.targetDBTableName).append("\n");
            if (task instanceof RasterTask) {
                RasterTask rasterTask = (RasterTask) task;
                info.append("栅格存储模式：\n");
                StoreMode mode = rasterTask.rasterStoreMode;
                info.append("     *栅格模式：").append(mode.mode.name()).append("\n");
                info.append("     *栅格B表、B树或GateWay路径：").append(mode.tableBName).append("\n");
                info.append("     *是否分块：").append(mode.blocking).append("\n");
                info.append("     *分块大小(宽、高、波段)：").append(mode.blocksize).append("\n");
                info.append("     *BRP坐标值：").append(mode.brpcoord).append("\n");
                info.append("     *ULC坐标值：").append(mode.ulccoord).append("\n");
                info.append("     *BRP坐标值：").append(mode.brpcoord).append("\n");
                info.append("     *压缩格式：").append(mode.comp.name()).append("\n");
                info.append("     *压缩质量：").append(mode.quality).append("\n");
                info.append("     *像元交错类型：").append(mode.inter).append("\n");
                info.append("     *金字塔层级：").append(mode.pyd_level).append("\n");
                info.append("     *XML方案序号：").append(mode.metaschemaId).append("\n");
                info.append("     *栅格大小(宽、高、波段)：").append(mode.rastersize).append("\n");
                info.append("     *是否开启变换：").append(mode.transform).append("\n");
                info.append("     *是否有外部数据源：").append(mode.linexistB).append("\n");
                info.append("     *像元类型：").append(mode.cellType.name()).append("\n");
            }
            if (task instanceof GeomTask) {
                 GeomTask geomTask = (GeomTask) task;
                 
                info.append("是否覆盖：").append(geomTask.taskIsOverWrite?"是":"否").append("\n");
 
            }
            if (task.taskInfo != null) {
                info.append("***************************\n");
                info.append("导入结果：\n");
                info.append("         成功数量:").append(task.taskInfo.getTotalCount()).append("\n");
                info.append("         描述性信息:").append(task.taskInfo.getInfo()).append("\n");
            }
        }
        return info.toString();
    }
 
    
    protected ArrayList<Task> m_TaskList;
    protected ConcurrentLinkedQueue m_RunTaskQueue; //要执行的任务
    protected Thread fixedTask;
    protected HashMap<Task, NewThread> m_ThreadMap;

    public TaskMgr() {
         this.m_TaskList = new ArrayList<>();
        m_RunTaskQueue=new ConcurrentLinkedQueue();
        this.m_ThreadMap =  new HashMap<>();
    }

    public Task getTask(int index) {
        return m_TaskList.get(index);
    }

    public int getTaskIndex(Task task) {
        return m_TaskList.indexOf(task);
    }

    public ArrayList<Task> getTaskList() {
        return m_TaskList;
    }

    public void addTask(Task task) {
        m_TaskList.add(task);
    }

    public boolean startTask(Task task) {
        if (task.taskStatus == Task.taskStatus_Ready || task.taskStatus == Task.taskStatus_Suspend || (task.taskInfo != null && task.taskInfo.getInfo().equals(TaskInfoBase.info_Failed))) {
            //先进行排队
            if (!m_RunTaskQueue.contains(task)) {
                task.taskStatus = Task.taskStatus_Queue_up;
                m_RunTaskQueue.add(task);
                if (fixedTask == null || !fixedTask.isAlive()) {
                    Runnable runnable = new FixedTaskService();
                    fixedTask = new Thread(runnable);
                    fixedTask.setDaemon(true);
                    fixedTask.setName("控制正在进行中的任务的数量");
                    fixedTask.start();
                }
                return true;
            }
        }
        return false;
    }

    public boolean suspendTask(Task task) {
        if (task.taskStatus == Task.taskStatus_Running) {
            NewThread thread = m_ThreadMap.get(task);
            if (thread != null) {
                task.taskStatus = Task.taskStatus_Suspend;
                thread.setSuspend(true);
                m_runningTaskCounter--;
                return true;
            }
        }
        if (task.taskStatus == Task.taskStatus_Queue_up) {
            //正在排队执行的
            if (m_RunTaskQueue.contains(task)) {
                task.taskStatus = Task.taskStatus_Suspend;
                m_RunTaskQueue.remove(task);
                return true;
            }
        }
        return false;
    }

    public boolean removeTask(Task task) {
        if (task.taskStatus == Task.taskStatus_Ready || task.taskStatus == Task.taskStatus_Finished || task.taskStatus == Task.taskStatus_Queue_up) {
            this.m_TaskList.remove(task);
            if (m_RunTaskQueue.contains(task)) {
                this.m_RunTaskQueue.remove(task);
            }
            return true;
        } else if (task.taskStatus == Task.taskStatus_Running || task.taskStatus == Task.taskStatus_Suspend) {
            NewThread thread = m_ThreadMap.get(task);
            if (thread != null) {
                if (thread.isAlive()) {
                    //任务已经启动还没有执行完毕
                    thread.setStop(true);
                    thread.interrupt();
                }
                this.m_TaskList.remove(task);
                this.m_ThreadMap.remove(task);
                if (m_RunTaskQueue.contains(task)) {
                    this.m_RunTaskQueue.remove(task);
                }
                m_runningTaskCounter--;
                return true;
            }
        }
        return false;
    }

    public void addTasks(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            addTask(task);
        }
    }
    /**
     * 创建真正的数据导入任务执行类
     * @param task
     * @return 
     */
    public abstract NewThread createRealThread(Task task) ;
  
      /**
     *真正启动任务，执行操作
     * @param task
     */
    private void realExecuteTask(Task task) {
        task.taskStatus = Task.taskStatus_Running;
        NewThread thread = m_ThreadMap.get(task);
        if (thread == null) {
                NewThread importTask = createRealThread(task);
                m_ThreadMap.put(task, importTask);
                //开始执行
                importTask.start();
        } else {
            //继续执行
            if (thread.isAlive()) {
                thread.setSuspend(false);
            } else {
                //任务已经结束，重新启动任务
                thread.setSuspend(false);
                Thread.State state = thread.getState();
                if (state == Thread.State.TERMINATED) {
                    thread = createRealThread(task);
                    //开始执行
                    thread.start();
                }
            }
        }
    }

    private class FixedTaskService implements Runnable {

        int fixedNum = 10;//默认支持同时10个导入任务
        ArrayList<Task> tmpList;

        public FixedTaskService() {
            Object obj = initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_TASK_PARALELLIMPORT_NUM);
            if (obj != null) {
                fixedNum = Integer.parseInt(String.valueOf(obj));
            }
            tmpList = new ArrayList<>();
        }

        @Override
        public void run() {
            traversalTask();
        }

        private void traversalTask() {

            while (!m_RunTaskQueue.isEmpty() || !tmpList.isEmpty()) {

                if (m_runningTaskCounter < fixedNum) {
                    //抛出 fixedNum-m_runningTaskCounter个任务并执行
                    Task task;
                    while ((task = (Task) m_RunTaskQueue.poll()) != null) {
                        tmpList.add(task);
                        realExecuteTask(task);
                        m_runningTaskCounter++;
                        if (m_runningTaskCounter >= fixedNum) {
                            break;
                        }
                    }
                }
                for (int i = tmpList.size() - 1; i >= 0; i--) {
                    Task rasterTask = (Task) tmpList.get(i);
                    if (rasterTask.taskStatus == Task.taskStatus_Finished) {
                        m_runningTaskCounter--;
                        tmpList.remove(rasterTask);
                    }
                }
                wait(10);
            }
        }

        private void wait(int millseconds) {
            try {
                Thread.sleep(millseconds);
            } catch (InterruptedException ex) {
                Logger.getLogger(TaskMgr.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
