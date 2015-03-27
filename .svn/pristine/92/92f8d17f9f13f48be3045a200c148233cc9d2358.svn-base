/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.thread;

import com.beyondb.task.Task;
import com.beyondb.task.raster.RasterTaskMgr;
import com.beyondb.ui.display.TaskBtnControlBase;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

/**
 * 轮询单个任务的状态，并控制UI（按钮 和 详细信息文本）输出
 *
 * @author lbs
 */
public class ThreadAskTaskStatus extends Thread {

    Task mTask;
    TaskBtnControlBase m_BtnCtrl;
    JTextArea m_TextArea;
    TableModel m_TableModel;     
    private boolean m_isStop=false;
    private boolean enable;


    public ThreadAskTaskStatus() {
        this.m_BtnCtrl = new TaskBtnControlBase();
         this.setName("ThreadAskTaskStatus");
         enable=true;
    }

    /**
     *当前正在关注的task
     * @param task
     */
    public void setCurrentTask(Task task) {
        mTask = task;
        displayTaskDestailInfoSechdule(task);
    }
    

    public void setStartButton(JButton button) {
        m_BtnCtrl.setStartButton(button);
    }

    public void setPauseButton(JButton button) {
        m_BtnCtrl.setPauseButton(button);
    }

    public void setRemoveButton(JButton button) {
        m_BtnCtrl.setRemoveButton(button);
    }

    public void setTextArea(JTextArea area)
    {
        m_TextArea = area;      
    }

  
    
    public void setStop(boolean flag)
    {
        m_isStop= flag;
    }
    
      
    private void displayTaskDestailInfoSechdule(Task task) {
        String info = RasterTaskMgr.getTaskDetailInfo(task);
        if (m_TextArea != null) {
            if (!m_TextArea.getText().equals(info)) {
                m_TextArea.setText(info);
            }
        }
    }
    @Override
    public void run() {
        
//        long endtime = System.currentTimeMillis();
        while (!m_isStop) {//循环监听

            //每隔0.04秒查询一次状态,超过人眼识别频率
//            long tmpInterval = (System.currentTimeMillis() - endtime) / 10;
//            if (tmpInterval > 3) {
              updateBtnUI();
//                endtime = System.currentTimeMillis();
//            }
                wait(500);//暂停线程，不占资源
        }
        
    }

    /**
     * 刷新控件的UI
     */
    public void updateBtnUI() {

        if (!enable) {
            m_BtnCtrl.disableButton();
            displayTaskDestailInfoSechdule(null);
        } else {
            if (mTask != null && m_BtnCtrl != null) {
                m_BtnCtrl.controlBtnEnabled(mTask);
                displayTaskDestailInfoSechdule(mTask);

            } else if (m_BtnCtrl != null) {
                m_BtnCtrl.initButton();
                displayTaskDestailInfoSechdule(null);
            }
        }
    }
 
  public void setBtnUIenable(boolean  flag)
  {
      enable=flag;
  }

  private void wait(int millseconds)
        {
               try {
                    Thread.sleep(millseconds);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadAskTaskStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
   
 
    
 

}

