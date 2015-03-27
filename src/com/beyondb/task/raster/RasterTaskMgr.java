/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.task.raster;

import com.beyondb.task.TaskMgr;
import com.beyondb.datasource.BydDataSource;
import com.beyondb.task.Task;
import com.beyondb.thread.NewThread;
import com.beyondb.thread.ThreadRasterImportIntoDB;
import com.beyondb.utils.SysPropUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 倪永
 */
public class RasterTaskMgr extends TaskMgr {

    @Override
    public NewThread createRealThread(Task task) {
        ThreadRasterImportIntoDB importTask = new ThreadRasterImportIntoDB((RasterTask)task, true);
        importTask.setBydDataSource((BydDataSource) task.targetDataSource);
        importTask.setFirstImport(false);//如果不是第一次导入会检查是否有重复
        return importTask;
    }
}
