/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.display;

import com.beyondb.bean.TaskInfoBase;
import com.beyondb.task.Task;
import javax.swing.JButton;

/**
 *根据任务状态，设置按钮的是否可用
 * @author 倪永
 */
public class TaskBtnControlBase {
    
        JButton startButton;
        JButton pauseButton;
        JButton removeButton;

        public void setStartButton(JButton btn) {
            startButton = btn;
        }

        public void setPauseButton(JButton btn) {
            pauseButton = btn;
        }

        public void setRemoveButton(JButton btn) {
            removeButton = btn;
        }

        public void initButton()
        {
            startButton.setEnabled(true);
            pauseButton.setEnabled(true);
            removeButton.setEnabled(true);
        }
         public void disableButton()
        {
            startButton.setEnabled(false);
            pauseButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
        public void controlBtnEnabled(Task task) {
            switch (task.taskStatus) {
                case Task.taskStatus_Ready:
                    startButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    removeButton.setEnabled(true);
                    break;
                case Task.taskStatus_Running:
                case Task.taskStatus_Queue_up:
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    break;
                case Task.taskStatus_Suspend:
                    startButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    removeButton.setEnabled(true);
                    break;
                case Task.taskStatus_Finished:
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    if (task.taskInfo!=null) {
                        if(task.taskInfo.getInfo().equals(TaskInfoBase.info_Failed))
                        {//失败允许重来
                            startButton.setEnabled(true);
                            pauseButton.setEnabled(false);
                            removeButton.setEnabled(true);
                        }
                    }
                    break;
            }
        }
        
}
