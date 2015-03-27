/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import com.beyondb.ui.display.ColumnSelectableJTable;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableModel;

/**
 *
 * @author lbs
 */
public class AppendOrUpdatePanel extends javax.swing.JPanel {

    private TableModel  m_TableModel;
    ColumnSelectableJTable m_Table;
    /**
     * Creates new form AppendOrUpdatePanel
     */
    public AppendOrUpdatePanel() {
        initComponents();
         init();
    }

    
    private void init()
    {
        jRadioButton_Append.setSelected(true); 
  
    }
    private void displayExcelContent() {
        m_Table = new ColumnSelectableJTable(m_TableModel);
        m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//设置JTable的列宽度是否随着JTable的变化而变化。
        m_Table.setAutoscrolls(true);
        JScrollPane jScrollp = new JScrollPane(m_Table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollp.setBounds(m_Table.getLocation().x, m_Table.getLocation().y,
                jTablePanel1.getWidth(),
                jTablePanel1.getHeight());
        jTablePanel1.removeAll();
        jTablePanel1.add(jScrollp);

        jTablePanel1.setVisible(true);

        //默认选择第一列作为主键
        //可按列选择
        
        m_Table.setColumnSelectionAllowed(true);
        //不可按行选择  
        m_Table.setRowSelectionAllowed(false);
        m_Table.clearSelection();
        m_Table.addColumnSelectionInterval(0, 0);
    }
        
    
    public boolean  isAppend()
    {
        return jRadioButton_Append.isSelected();
    }
    public void setTableModel(TableModel model)
    {
        m_TableModel = model;
    }
    /**
     *
     * @return 获取作为主键的列
     */
    public  ArrayList<String>  getPrimaryKeyColumns()
    {
        if (m_Table==null) {
            return null;
        }
        ArrayList<String> al = new ArrayList<>();
        for (int index :   m_Table.getSelectedColumns()) {
            //约定
            al.add(m_Table.getColumnName(index).replace(" ", "_"));
        }
       
        return al;  
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton_Update = new javax.swing.JRadioButton();
        jRadioButton_Append = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jTablePanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("该表已经存在数据"));

        buttonGroup1.add(jRadioButton_Update);
        jRadioButton_Update.setText("使用导入行取代现有行(务必选择下列表字段作主键，按shift多选)");
        jRadioButton_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_UpdateActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton_Append);
        jRadioButton_Append.setSelected(true);
        jRadioButton_Append.setText("附加导入行在现有表");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton_Update)
                    .addComponent(jRadioButton_Append)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jRadioButton_Append)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_Update)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel1.setText("最后预览：");

        jTablePanel1.setBackground(new java.awt.Color(255, 255, 255));
        jTablePanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTablePanel1.setPreferredSize(new java.awt.Dimension(607, 245));

        javax.swing.GroupLayout jTablePanel1Layout = new javax.swing.GroupLayout(jTablePanel1);
        jTablePanel1.setLayout(jTablePanel1Layout);
        jTablePanel1Layout.setHorizontalGroup(
            jTablePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jTablePanel1Layout.setVerticalGroup(
            jTablePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/anniu4.png"))); // NOI18N
        jButton1.setText("文件内容");
        jButton1.setContentAreaFilled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTablePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTablePanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        displayExcelContent();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jRadioButton_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_UpdateActionPerformed
        // TODO add your handling code here:
          if (jRadioButton_Update.isSelected()) {
               displayExcelContent(); 
        }
    }//GEN-LAST:event_jRadioButton_UpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton_Append;
    private javax.swing.JRadioButton jRadioButton_Update;
    private javax.swing.JPanel jTablePanel1;
    // End of variables declaration//GEN-END:variables
}