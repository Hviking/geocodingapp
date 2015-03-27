/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import com.beyondb.task.Task;

/**
 *
 * @author lbs
 */
public interface I_ParseTask2TableRowData {

    /**
     * 将Task对象转换为TableModel中的一行数据，方便展示
     * @param rowTask
     * @return
     */
    Object[] parseTask2TableModelRowObjectArray(Task rowTask);
    
}
