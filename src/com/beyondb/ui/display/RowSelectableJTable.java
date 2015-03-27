/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.display;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author lbs
 */
public class RowSelectableJTable extends  JTable{
     public RowSelectableJTable(TableModel tableModel){  
        super(tableModel);  
        //不可按列选择  
        setColumnSelectionAllowed(false);  
        //可按行选择  
        setRowSelectionAllowed(true);  
        
//        final JTableHeader header=getTableHeader();  
//        header.addMouseListener(new MouseAdapter(){  
//            //释放鼠标单击时启动  
//            public void mouseReleased(MouseEvent e){  
//                //取消所有选择  
//                if(!e.isShiftDown()) clearSelection();  
//                //获得单击的列数  
//                int column=header.columnAtPoint(e.getPoint());  
//                //只选择一列  
//                addColumnSelectionInterval(column,column);  
//            }  
//        });  
    }  
}
