/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.display;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;

/**
 * 界面展示的一些工具
 * @author 倪永
 */
public  class UIUtils {

    /**
     * 展示任务内容
     *
     * @param tableModel
     * @param panel
     * @return 
     */
    public static  JTable displayTaskList(TableModel tableModel, JPanel panel) {
        JTable jTable = new RowSelectableJTable(tableModel);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//设置JTable的列宽度是否随着JTable的变化而变化。
        jTable.setAutoscrolls(true);
        JScrollPane jScrollp = new JScrollPane(jTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.removeAll();
        panel.add(jScrollp);
        panel.setLayout(new GridLayout(1, 1));
        panel.setVisible(true);
        panel.updateUI();
        return jTable;
    }
    
   
    
}
