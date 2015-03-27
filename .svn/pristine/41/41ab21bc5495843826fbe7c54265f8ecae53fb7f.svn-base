/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.swingworker;

import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.io.DBConfig;
import com.beyondb.ui.IDialog;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * 展示数据库列表
 * @author 倪永
 */
public class ShowBeyonDBTablesSwingWorker extends  SuperSwingWorker{

    private final  JTree m_Tree;
    private ArrayList<String> m_TblList;

    private  DefaultMutableTreeNode m_TblRootNode;
    
    public ShowBeyonDBTablesSwingWorker(IDialog dialog, JTree jTree) {
        super(dialog);
        m_Dialog = dialog;
        m_Tree = jTree;
    }

    /**
     * 传递参数
     *
     * @param ds
     */
    public void setParameters(DataSource ds) {
        m_DataSource = ds;
    }

    private void readBeyondbTbls() {
   
        if (m_DataSource == null) {
            return;
        }
        setDisplayStatus("正在访问数据库" + m_DataSource.getID());
        setDisplayProgress(10);

        DataSourceUtils dataSourceUtils = new DataSourceUtils(m_DataSource);
        BydOperator bydOperator = new BydOperator(dataSourceUtils);

        try {
            if (DBConfig.testConnect(m_DataSource)) {
                //正常通信
                m_TblList = bydOperator.queryTables();
                m_TblRootNode.setUserObject(m_DataSource);
                if (m_TblList.isEmpty()) {
                    setDisplayStop();
                    m_Dialog.setShowMessage("数据库" + m_DataSource.getID() + "没有可用表", JOptionPane.ERROR_MESSAGE);
                    
                }
            }
        } catch (SQLException ex) {
            if (!isStop) {
                setDisplayStop();
                m_TblRootNode.setUserObject(m_DataSource.toString()+"(无法访问)");
                m_TblRootNode.removeAllChildren();
                m_Dialog.setShowMessage("无法访问数据库" + m_DataSource.getID() + "\n" + ex.getMessage(), JOptionPane.ERROR_MESSAGE);
               
            }
        } finally {
            setDisplayProgress(100);
            setDisplayStop();
        }
    }

    public void refreshTables(DefaultMutableTreeNode rootNode) {
        readBeyondbTbls();
        if (!isStop) {
            rootNode.removeAllChildren();
            for (String tbl : m_TblList) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(tbl);
               
                rootNode.add(node);
            }
        }
    }
    @Override
    protected String doInBackground() throws Exception {
       
        if (m_Tree != null) {
            m_TblRootNode = (DefaultMutableTreeNode) ((DefaultTreeModel) m_Tree.getModel()).getRoot();
            refreshTables(m_TblRootNode); 
            m_Tree.expandRow(0);
            m_Tree.clearSelection();
        }
        return "";
    }

   @Override
    protected  void done()
    {

           //done()本身是在EDT中执行的    
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (m_Tree!=null) {
                        m_Tree.updateUI();
                    }
                    if (get() != null) {
                        String msg = (String) get();
                        if (!msg.isEmpty()) {
                            m_Dialog.setShowMessage(msg, JOptionPane.ERROR);
                        }
                    }
                   
                } catch (ExecutionException | InterruptedException | CancellationException ex) {
                 //不输出消息
                }
            }
        });
    }


}
