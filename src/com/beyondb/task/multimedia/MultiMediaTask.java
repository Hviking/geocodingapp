/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.task.multimedia;

import com.beyondb.multimedia.StoreMode;
import com.beyondb.task.Task;

/**
 *多媒体导入任务
 * @author lbs
 */
public class MultiMediaTask extends Task {
    
    /**
     * 是否覆盖
     */
    public boolean taskIsOverWrite = false;
    public String tableStructure;
    public String tablePageSize;

  
    /**
     * 导出命令
     */
    public String cmd;
  /**
     * 多媒体数据的存储选项
     */
    public StoreMode  mediaStoreMode;
}
