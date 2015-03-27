/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.task.geom;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.task.Task;
import com.beyondb.task.TaskMgr;
import com.beyondb.thread.NewThread;
import com.beyondb.thread.ThreadGeomImportIntoDB;

/**
 *
 * @author 倪永
 */
public class GeomTaskMgr extends TaskMgr {

    @Override
    public NewThread createRealThread(Task task) {
        if (task instanceof GeomTask) {
            ThreadGeomImportIntoDB importTask = new ThreadGeomImportIntoDB((GeomTask) task);
            importTask.setBydDataSource((BydDataSource) task.targetDataSource);
            return importTask;
        }
        return null;
    }
    
  
}

