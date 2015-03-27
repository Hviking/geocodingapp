/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.task.multimedia;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.task.Task;
import com.beyondb.task.TaskMgr;
import com.beyondb.thread.NewThread;
import com.beyondb.thread.ThreadMultiMediaImportIntoDB;

/**
 *
 * @author lbs
 */
public class MultiMediaTaskMgr extends  TaskMgr{




    @Override
    public NewThread createRealThread(Task task) {
        if (task instanceof MultiMediaTask) {
            ThreadMultiMediaImportIntoDB importTask = new ThreadMultiMediaImportIntoDB((MultiMediaTask) task);
            return importTask;
        }
        return null;
    }

}
