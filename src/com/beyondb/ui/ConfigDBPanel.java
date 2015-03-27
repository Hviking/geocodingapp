/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.ui;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.datasource.DataSource;
import com.beyondb.io.DBConfig;
import com.beyondb.ui.display.UIUtils;
import com.beyondb.utils.initSystemParams;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 * 数据库配置窗口
 *
 * @author 倪永
 */
public class ConfigDBPanel extends javax.swing.JPanel {

    DataSource m_DataSource;
    private final String m_DBConfigPath = (String)initSystemParams.getInstance()
            .getSystemParam(initSystemParams.PARAM.PARAM_DATASOURCE_PATH);

    private DataSourceTableModel m_TableModel;

    /**
     * Creates new form ConfigDBPanel
     *
     */
    public ConfigDBPanel() {
        initComponents();
        readConfig();
        displayDBlist();
    }

    /**
     * 展示数据源列表
     */
    private void displayDBlist() {
        //设置表格的列名
        String[] columnName = new String[3];
        columnName[0] = "数据库标识";
        columnName[1] = "连接字符串";
        columnName[2] = "用户名";

        Object[][] data = new Object[0][columnName.length];
        m_TableModel = new DataSourceTableModel(data, columnName);
        ArrayList<DataSource> dsList = DBConfig.readDBConfig1(m_DBConfigPath);
        for (DataSource ds : dsList) {
            m_TableModel.addRow(ds);
        }

        final JTable jTable = UIUtils.displayTaskList(m_TableModel, jPanel_DBlist);
        TableColumn secondColumn = jTable.getColumnModel().getColumn(1);
        secondColumn.setPreferredWidth(300);
        secondColumn.setMaxWidth(600);
        secondColumn.setMinWidth(200);
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                final int[] selectedRows = jTable.getSelectedRows();

                if (selectedRows.length > 0) {

                    final int currenRow = selectedRows[0];

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        JPopupMenu pop = new JPopupMenu("pop");
                        JMenuItem item1 = new JMenuItem("修改");
                        JMenuItem item2 = new JMenuItem("删除");

                        item1.addMouseListener(new MouseAdapter() {
                            
                            private void PopDialog(Component component) {
                                DataSource ds = m_TableModel.getRow(currenRow);
                                String old_ID = ds.getID();
                                EditDBPanel panel = new EditDBPanel(ds);

                                int res = JOptionPane.showConfirmDialog(component, panel,
                                        "数据库源编辑", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                                if (res == 0) {
                                    //保存
                                    String new_ID = panel.getDataSource().getID();
                                    if (!old_ID.equals(new_ID)&&m_TableModel.hasID(new_ID)) {
                               
                                        //ID标识发生变更，与列表中标识有重复
                                            //提示重新编辑ID
                                            String tip ="您修改的标识号:“"+new_ID+"”" +"在数据源列表中已存在!\n\n请改用其它标识";
                                            JOptionPane.showMessageDialog(component,tip);
                                            PopDialog(component);
                                        
                                    } else {
                                        m_TableModel.updateRow(currenRow, panel.getDataSource());
                                    }
                                }
                            }
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                //弹出修改对话框
                                if (e.getComponent().isEnabled()) {
                                    PopDialog(e.getComponent());
                                }                                
                                
                            }
                        });

                        item2.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent e) {

                                for (int i = selectedRows.length-1; i > -1; i--) {
                                    m_TableModel.removeRow(selectedRows[i]);
                                }

                            }
                        });

                        pop.add(item1);
                        pop.add(item2);
                        if (selectedRows.length>1) {
                            item1.setEnabled(false);
                        }
                        //e.getComponent()表示与右键菜单关联的组件，这里指button2  
                        pop.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

        });

    }

    private void readConfig() {
        m_DataSource = DBConfig.readDBConfig(m_DBConfigPath);
        if (m_DataSource != null) {
            jTextField_URL.setText(m_DataSource.getUrl());
            jTextField_UserName.setText(m_DataSource.getName());
            jPasswordField.setText(m_DataSource.getPassword());
        }
    }

    public void saveConfig() {
        DBConfig.saveDBConfig(m_DBConfigPath, m_TableModel.getRows());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jTextField_URL = new javax.swing.JTextField();
        jTextField_UserName = new javax.swing.JTextField();
        jPasswordField = new javax.swing.JPasswordField();
        jButton_Add = new javax.swing.JButton();
        jPanel_DBlist = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton_TestConnect = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(360, 500));
        setMinimumSize(new java.awt.Dimension(360, 350));
        setName("数据库"); // NOI18N
        setPreferredSize(new java.awt.Dimension(360, 360));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setText("用户名：");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));
        add(jTextField_URL, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 243, -1));
        add(jTextField_UserName, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 243, -1));
        add(jPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 243, -1));

        jButton_Add.setText("增加");
        jButton_Add.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AddActionPerformed(evt);
            }
        });
        add(jButton_Add, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 110, 60, -1));

        jPanel_DBlist.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel_DBlistLayout = new javax.swing.GroupLayout(jPanel_DBlist);
        jPanel_DBlist.setLayout(jPanel_DBlistLayout);
        jPanel_DBlistLayout.setHorizontalGroup(
            jPanel_DBlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel_DBlistLayout.setVerticalGroup(
            jPanel_DBlistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        add(jPanel_DBlist, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 380, 160));

        jLabel3.setText("密码：");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        jLabel1.setText("数据库URL：");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButton_TestConnect.setText("连接");
        jButton_TestConnect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_TestConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_TestConnectActionPerformed(evt);
            }
        });
        add(jButton_TestConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 60, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_TestConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_TestConnectActionPerformed
        // TODO add your handling code here:
        //测试连接
        DataSource tmpDS = new BydDataSource();
        tmpDS.setUrl(jTextField_URL.getText());
        tmpDS.setName(jTextField_UserName.getText());
        StringBuilder pwd = new StringBuilder();
        for (char ch : jPasswordField.getPassword()) {
            pwd.append(ch);
        }
        tmpDS.setPassword(pwd.toString());
        try {
            if (DBConfig.testConnect(tmpDS)) {
                JOptionPane.showMessageDialog(this, "连接成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "连接失败", "提示", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "连接失败：SQL异常\n" + ex.getMessage(),
                    "提示", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_jButton_TestConnectActionPerformed

    private void jButton_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AddActionPerformed
        // TODO add your handling code here:

        DataSource ds = new DataSource();

        ds.setUrl(jTextField_URL.getText());
        ds.setID(ds.getUrl());//数据库标识默认是url
        ds.setName(jTextField_UserName.getText());

        StringBuilder pwd = new StringBuilder();
        for (char ch : jPasswordField.getPassword()) {
            pwd.append(ch);
        }
        ds.setPassword(pwd.toString());
        m_TableModel.addRow(ds);
    }//GEN-LAST:event_jButton_AddActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Add;
    private javax.swing.JButton jButton_TestConnect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel_DBlist;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JTextField jTextField_URL;
    private javax.swing.JTextField jTextField_UserName;
    // End of variables declaration//GEN-END:variables

    private class DataSourceTableModel extends DefaultTableModel {

        private ArrayList<DataSource> m_DataSourceList;

        public DataSourceTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
            m_DataSourceList = new ArrayList<>();
        }

        /**
         * 检查该标识是否已被使用
         *
         * @param id 标识
         * @return
         */
        public boolean hasID(String id) {
            for (Iterator<DataSource> it = m_DataSourceList.iterator(); it.hasNext();) {
                DataSource dataSource = it.next();
                if (dataSource.getID().equals(id)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 检查该数据源是否已被使用
         *
         * @param ds 数据源
         * @return
         */
        public boolean hasRow(DataSource ds) {
            return m_DataSourceList.contains(ds);
        }

        private Object[] parseDataSource2RowData(DataSource ds) {
            Object[] rowData = new String[3];
            rowData[0] = ds.getID();
            rowData[1] = ds.getUrl();
            rowData[2] = ds.getName();
            return rowData;
        }

        public void addRow(DataSource ds) {
            m_DataSourceList.add(ds);
            super.addRow(parseDataSource2RowData(ds));
        }

        @Override
        public void removeRow(int index) {
            super.removeRow(index);
            m_DataSourceList.remove(index);
        }

        public void updateRow(int rowindex,DataSource ds) {
            Object[] objects = parseDataSource2RowData(ds);
            for (int i = 0; i < super.getColumnCount(); i++) {
                Object obj = objects[i];
                super.setValueAt(obj, rowindex, i);
            }
            m_DataSourceList.set(rowindex, ds);
            
        }

        public DataSource getRow(int index) {
            return m_DataSourceList.get(index);
        }

        /**
         * 获取所有的数据源
         *
         * @return
         */
        public ArrayList<DataSource> getRows() {
            return m_DataSourceList;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
     
    }
}
