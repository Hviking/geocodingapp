/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.ui;

import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.multimedia.MediaMode;
import com.beyondb.multimedia.StoreMode;
import com.beyondb.task.Task;
import com.beyondb.task.multimedia.MultiMediaTask;
import com.beyondb.utils.DialogUtil;
import com.beyondb.utils.initSystemParams;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 倪永
 */
public class JPanel_NewMultiMediaIntoDB extends NewTaskIntoDB {

     private StoreMode m_StoreMode;
    private String m_TableName="";
    /**
     * Creates new form JPanel_txtIntoDB
     */
     @SuppressWarnings("empty-statement")
    public JPanel_NewMultiMediaIntoDB() {
        initComponents();

    
        SupportFileType=(String[]) initSystemParams.getInstance()
                .getSystemParam(initSystemParams.PARAM.PARAM_MULTIMEDIA_FILE_TYPE);
        m_TablePanel=jTablePanel;//必须要有
        
        m_ShowDataBaseTablesPanel = new JPanel_DataBaseTables(this);
        jPanel_DBtbls.add(m_ShowDataBaseTablesPanel);
        jPanel_DBtbls.setLayout(new GridLayout(1, 1));

        this.m_Table = null;
        initTableModel();



    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel_NewIntoDB = new javax.swing.JPanel();
        jTextField_OpenFile = new javax.swing.JTextField();
        openfilebtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel_DBtbls = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTablePanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox_ImportType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jCheckBox_CreateSpatialIndex = new javax.swing.JCheckBox();
        jCheckBox_IgnoreError = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jComboBox_TableStructure = new javax.swing.JComboBox();
        jComboBox_PageSize = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTextField_location = new javax.swing.JTextField();
        jTextField_GeoColumnName = new javax.swing.JTextField();
        jButton_setStoreMode = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setToolTipText("");
        setMaximumSize(new java.awt.Dimension(820, 369));
        setMinimumSize(new java.awt.Dimension(820, 369));
        setName("文本数据入库                           "); // NOI18N
        setPreferredSize(new java.awt.Dimension(820, 369));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel_NewIntoDB.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_NewIntoDB.setPreferredSize(new java.awt.Dimension(820, 369));

        jTextField_OpenFile.setMinimumSize(new java.awt.Dimension(6, 30));
        jTextField_OpenFile.setPreferredSize(new java.awt.Dimension(6, 30));
        jTextField_OpenFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_OpenFileKeyPressed(evt);
            }
        });

        openfilebtn.setText("……");
        openfilebtn.setFocusable(false);
        openfilebtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openfilebtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openfilebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openfilebtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("黑体", 1, 12)); // NOI18N
        jLabel2.setText("导入文件：");

        jPanel_DBtbls.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel_DBtblsLayout = new javax.swing.GroupLayout(jPanel_DBtbls);
        jPanel_DBtbls.setLayout(jPanel_DBtblsLayout);
        jPanel_DBtblsLayout.setHorizontalGroup(
            jPanel_DBtblsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );
        jPanel_DBtblsLayout.setVerticalGroup(
            jPanel_DBtblsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("文件列表"));
        jPanel2.setToolTipText("");
        jPanel2.setName(""); // NOI18N

        jTablePanel.setBackground(new java.awt.Color(255, 255, 255));
        jTablePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTablePanel.setName(""); // NOI18N
        jTablePanel.setPreferredSize(new java.awt.Dimension(607, 245));

        javax.swing.GroupLayout jTablePanelLayout = new javax.swing.GroupLayout(jTablePanel);
        jTablePanel.setLayout(jTablePanelLayout);
        jTablePanelLayout.setHorizontalGroup(
            jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jTablePanelLayout.setVerticalGroup(
            jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 259, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("导入参数"));

        jLabel3.setText("导入方式");

        jComboBox_ImportType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "改写", "追加" }));
        jComboBox_ImportType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_ImportTypeItemStateChanged(evt);
            }
        });

        jLabel4.setText("指定新建几何字段名称");

        jCheckBox_CreateSpatialIndex.setText("创建空间索引");

        jCheckBox_IgnoreError.setText("忽略错误");

        jLabel5.setText("页面大小");

        jLabel6.setText("表结构");

        jLabel7.setText("位置");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox_PageSize, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_TableStructure, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_GeoColumnName, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(34, 34, 34)
                        .addComponent(jTextField_location, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox_IgnoreError)
                            .addComponent(jCheckBox_CreateSpatialIndex))
                        .addGap(140, 140, 140))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox_ImportType, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox_ImportType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField_GeoColumnName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_CreateSpatialIndex)
                    .addComponent(jLabel7)
                    .addComponent(jTextField_location, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox_IgnoreError)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox_TableStructure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox_PageSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton_setStoreMode.setText("设置模式");
        jButton_setStoreMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_setStoreModeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_NewIntoDBLayout = new javax.swing.GroupLayout(jPanel_NewIntoDB);
        jPanel_NewIntoDB.setLayout(jPanel_NewIntoDBLayout);
        jPanel_NewIntoDBLayout.setHorizontalGroup(
            jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_NewIntoDBLayout.createSequentialGroup()
                .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                                .addComponent(jTextField_OpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 461, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openfilebtn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_setStoreMode))
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                        .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel_DBtbls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel_NewIntoDBLayout.setVerticalGroup(
            jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel2)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                                .addGroup(jPanel_NewIntoDBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_OpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(openfilebtn))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                                .addComponent(jButton_setStoreMode)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel_NewIntoDBLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel_DBtbls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        add(jPanel_NewIntoDB);
    }// </editor-fold>//GEN-END:initComponents

   
   
    private void openfilebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openfilebtnActionPerformed
        // TODO add your handling code here:

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setAcceptAllFileFilterUsed(false);//取消所有文件选项
        fileChooser.setDialogTitle("打开多媒体文件或所在目录");
        fileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter tempFileFilter;
        tempFileFilter = new FileNameExtensionFilter("MPG文件|*.mpg", "mpg");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("AVI文件|*.avi", "avi");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("WMV文件|*.wmv", "wmv");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("MOV文件|*.mov", "mov");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("FLV文件|*.flv", "flv");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("RMVB文件|*.rmvb", "rmvb");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("MKV文件|*.mkv", "mkv");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("MP4文件|*.mp4", "mp4");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("MP3文件|*.mp3", "mp3");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        
        String lastPath = readLastAccessPath(this.getClass());

        if (!lastPath.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(lastPath));
        }
        fileChooser.showOpenDialog(this);

       m_TableModel.getDataVector().clear();
   
       
        File[] files = fileChooser.getSelectedFiles();
        if (files != null) {
        
            for (File file : files) {
                openFile(file.getAbsolutePath());
            }
            if (files.length > 1) {//选择多个文件或目录
                File dir = fileChooser.getCurrentDirectory();
                jTextField_OpenFile.setText(dir.getAbsolutePath());
                saveLastAccessPath(this.getClass(),dir.getAbsolutePath());
            } else if (files.length == 1) {//单个文件或目录
                jTextField_OpenFile.setText(files[0].getAbsolutePath());
                saveLastAccessPath(this.getClass(),files[0].getAbsolutePath());
            }
            
        } 
        
        jTextField_OpenFile.updateUI();
        displayTableContent(m_TableModel);

    }//GEN-LAST:event_openfilebtnActionPerformed

  
    
    private void jTextField_OpenFileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_OpenFileKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             m_TableModel.getDataVector().clear();
             openFile(jTextField_OpenFile.getText());
             displayTableContent(m_TableModel);
        }
    }//GEN-LAST:event_jTextField_OpenFileKeyPressed

    private void jComboBox_ImportTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_ImportTypeItemStateChanged
        // TODO add your handling code here:
           if (evt.getStateChange()==ItemEvent.SELECTED) {
               if (jComboBox_ImportType.getSelectedIndex()==1) {
                   //追加
                   jTextField_GeoColumnName.setEnabled(false);
                   jTextField_location.setEnabled(false);
                   jComboBox_PageSize.setEnabled(false);
                   jComboBox_TableStructure.setEnabled(false);
                   jCheckBox_CreateSpatialIndex.setEnabled(false);
                  
               }else
               {
                   jTextField_GeoColumnName.setEnabled(true);
                   jTextField_location.setEnabled(true);
                   jComboBox_PageSize.setEnabled(true);
                   jComboBox_TableStructure.setEnabled(true);
                   jCheckBox_CreateSpatialIndex.setEnabled(true);
               }
        }
    }//GEN-LAST:event_jComboBox_ImportTypeItemStateChanged

    private void jButton_setStoreModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_setStoreModeActionPerformed
        // TODO add your handling code here:
          //对于栅格数据的导入需要配置导入模式：inline,outline,gateway
        SetMediaModePanel mediaModePanel = new SetMediaModePanel();
        DataSource ds = m_ShowDataBaseTablesPanel.getDataSource();
        if (ds == null) {
            setShowMessage("当前数据源不可用，请检查", JOptionPane.WARNING_MESSAGE);
            return;
        }
        mediaModePanel.setDataSource(ds);
        if (!m_TableName.isEmpty() && m_StoreMode != null) {
            mediaModePanel.setStoreMode(m_StoreMode);
            mediaModePanel.setTableAName(m_TableName);
        } else {
            
            String tableAname = m_ShowDataBaseTablesPanel.getTableNameFromTree();
            BydOperator bydOperator=new BydOperator(new DataSourceUtils(ds));

            if(!tableAname.isEmpty()&&!bydOperator.isMediaTable(tableAname))
            {
                setShowMessage("选中的"+tableAname+"不是多媒体表\n 请选中多媒体表再打开，或不做任何选择", JOptionPane.YES_OPTION);
                return;
            }else
            {
            mediaModePanel.setTableAName(tableAname);
            mediaModePanel.auotFileTableBName();
            mediaModePanel.setMediaMode(MediaMode.inline);
            }
        }

        int res = (new DialogUtil()).showDialog(null, mediaModePanel,mediaModePanel,"设置栅格模式");
        if (res == 0) {
            m_StoreMode = mediaModePanel.getStoreMode();
            m_TableName = mediaModePanel.getTableAName();
        }
    }//GEN-LAST:event_jButton_setStoreModeActionPerformed



    @Override
    public ArrayList<MultiMediaTask> getTasks() {
       
        ArrayList<MultiMediaTask> list = new ArrayList<>();
        DataSource ds = m_ShowDataBaseTablesPanel.getDataSource();
        if (ds != null) {
            int count = m_TableModel.getRowCount();

              for (int i = 0; i < count; i++) {
          
                //没有勾选
                if (!Boolean.valueOf(String.valueOf(m_TableModel.getValueAt(i, 0)))) {
                    continue;
                }
                MultiMediaTask task = new MultiMediaTask();
                String filePath = String.valueOf(m_TableModel.getValueAt(i, 1));
                task.importFile = new File(filePath);
                //ID 格式是 “日期格式-序号”
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                task.taskID = df.format(new Date()) + "-" + i;
                task.taskName =String.valueOf(m_TableModel.getValueAt(i, 3));//图层名称
                task.taskProgressValue = 0;
                task.taskSize =String.valueOf(m_TableModel.getValueAt(i, 2));//文件大小
                task.taskStatus = Task.taskStatus_Ready;
                task.targetDataSource = ds;
                task.targetDBTableName = String.valueOf(m_TableModel.getValueAt(i, 3));//图层名称即表名
                 //改写
                task.taskIsOverWrite = (jComboBox_ImportType.getSelectedIndex() == 0);
             
           
                list.add(task);
            }

        }
        return list;
    }



    
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_setStoreMode;
    private javax.swing.JCheckBox jCheckBox_CreateSpatialIndex;
    private javax.swing.JCheckBox jCheckBox_IgnoreError;
    private javax.swing.JComboBox jComboBox_ImportType;
    private javax.swing.JComboBox jComboBox_PageSize;
    private javax.swing.JComboBox jComboBox_TableStructure;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel_DBtbls;
    private javax.swing.JPanel jPanel_NewIntoDB;
    private javax.swing.JPanel jTablePanel;
    private javax.swing.JTextField jTextField_GeoColumnName;
    private javax.swing.JTextField jTextField_OpenFile;
    private javax.swing.JTextField jTextField_location;
    private javax.swing.JButton openfilebtn;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void initImportParams() {
        
    }
 
  
    

   


}