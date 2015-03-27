/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.io.MyTableModel;
import com.beyondb.io.ParseTableFactory;
import com.beyondb.ui.JPanel_txtIntoDB;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *删除列

 * @author lbs
 */
public class DeleteTableColumnSwingWorker extends SuperSwingWorker{
JPanel_txtIntoDB m_Panel;
    public DeleteTableColumnSwingWorker(JPanel_txtIntoDB app) {
       
         super(app, app);
        m_Panel =app;
    }

     public void setParseTableFactory(ParseTableFactory factory) {
      m_Factory =factory;
    }
    @Override
    protected String doInBackground()  {
         m_Table.setEnabled(false);
         m_Table.setAutoCreateColumnsFromModel(false);// // Disable autoCreateColumnsFromModel to prevent
    // the reappearance of columns that have been removed but
    // whose data is still in the table model
         
         m_SelectedColIndexs = m_Table.getSelectedColumns();
        if (m_SelectedColIndexs.length > 0) {
             setDisplayStatus("执行任务:" + m_TaskName + "\n ");
            MyTableModel myTableModel= ((MyTableModel)m_TableModel);
            try {
                boolean flag = myTableModel.deleteColumn(m_SelectedColIndexs);
                if (flag) {

//                    TableColumnModel columnModel = m_Table.getColumnModel();
//                    int count = myTableModel.getColumnCount();
//                    for (int i = m_SelectedColIndexs.length - 1; i > -1; i--) {
//                        if (isStop) {
//                            return "";
//                        }
//                        if (m_SelectedColIndexs[i] > columnModel.getColumnCount() - 1) {
//                            //异常
//                            throw new ArrayIndexOutOfBoundsException("删除列索引超过实际列数");
//                        }
//                        TableColumn column = columnModel.getColumn(m_SelectedColIndexs[i]);
//                        m_Table.removeColumn(column);
//                        myTableModel.getColumnIndetifiers().removeElementAt(m_SelectedColIndexs[i]);
//                    }
//                    for (int i = 0; i < myTableModel.getDataVector().size(); i++) {
//                        if (isStop) {
//                            return "";
//                        }
//                        //每行数据
//                        Vector v = (Vector) myTableModel.getDataVector().elementAt(i);
//                        for (int rI = m_SelectedColIndexs.length - 1; rI >= 0; rI--) {
//                            v.removeElementAt(m_SelectedColIndexs[rI]);
//                        }
//                    }

                    m_Panel.displayTableContent(m_Factory.getTableModel());
                    setDisplayStatus(m_TaskName + "\n  执行成功");
                    setDisplayProgress(100);
                } else {
                    m_Dialog.setShowMessage(m_TaskName + "\n  执行失败！", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                m_Dialog.setShowMessage(m_TaskName + "\n  执行失败！" + "\n" + e.getLocalizedMessage(),
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        return "";
    }
      @Override
    protected  void done()
    {
        setDisplayStop();
           //done()本身是在EDT中执行的    
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    m_Table.setAutoCreateColumnsFromModel(true);
                    m_Table.updateUI();
                    m_Table.setEnabled(true);
                    
                    String msg = (String) get();
                    if (!msg.isEmpty()) {
                        m_Dialog.setShowMessage(msg, JOptionPane.ERROR);
                    }              
                } catch (ExecutionException | InterruptedException ex) {
                    Logger.getLogger(DeleteTableColumnSwingWorker
                            .class.getName()).log(Level.SEVERE,"",ex);
                }
            }
        });
    }
}
