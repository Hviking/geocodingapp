/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.display;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author ZhangShuo
 */
public class ColumnSelectableJTable extends JTable{
     public ColumnSelectableJTable(TableModel tableModel){  
        super(tableModel);  
        //可按列选择  
        setColumnSelectionAllowed(true);  
        //不可按行选择  
        setRowSelectionAllowed(false);  
        final JTableHeader header=getTableHeader();  
        header.addMouseListener(new MouseAdapter(){  
            //释放鼠标单击时启动  
            public void mouseReleased(MouseEvent e){  
                //取消所有选择  
                if(!e.isShiftDown()&&e.getButton()==1) clearSelection();  
                //获得单击的列数  
                int column=header.columnAtPoint(e.getPoint());  
                //只选择一列  
                addColumnSelectionInterval(column,column);  
            }  
        });  
    }  
}
