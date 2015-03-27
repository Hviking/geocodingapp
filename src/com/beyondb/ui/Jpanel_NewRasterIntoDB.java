/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.ui;

import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSource;
import com.beyondb.datasource.DataSourceUtils;
import com.beyondb.raster.RasterMode;
import com.beyondb.raster.StoreMode;
import com.beyondb.task.raster.RasterTask;
import com.beyondb.utils.FileUtils;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author lbs
 */
public class Jpanel_NewRasterIntoDB extends NewTaskIntoDB{

    private StoreMode m_StoreMode;
    private String m_TableName="";
    /**
     * Creates new form Jpanel_NewRasterIntoDB
     */
    public Jpanel_NewRasterIntoDB() {
        initComponents();
        SupportFileType=(String[]) initSystemParams.getInstance()
                .getSystemParam(initSystemParams.PARAM.PARAM_RASTER_FILE_TYPE);
        
        initComboFileType();
        m_ShowDataBaseTablesPanel = new JPanel_DataBaseTables(this);
        jPanel_DBtbls.add(m_ShowDataBaseTablesPanel);
        jPanel_DBtbls.setLayout(new GridLayout(1, 1));
        
        m_TablePanel=jTablePanel;

        this.m_Table = null;
        initTableModel();

    }

    private void initComboFileType() {
        jComboBox_FileType.removeAllItems();
        jComboBox_FileType.addItem("所有支持类型");
        for (String type : SupportFileType) {
            jComboBox_FileType.addItem(type);
        }
    }


     @Override
    protected void initTableModel() {
          m_TableModel = new DefaultTableModel(
                  new Object[][]{},
                  new String[]{
                      "全选", "大小", "文件名","文件类型","位置"
                  }
          ) {
              Class[] types = new Class[]{
                  java.lang.Boolean.class,
                  java.lang.String.class,
                  java.lang.String.class,
                  java.lang.String.class,
                  java.lang.String.class
              };
              boolean[] canEdit = new boolean[]{
                  true, false, false, false,false
              };

              @Override
              public Class getColumnClass(int columnIndex) {
                  return types[columnIndex];
              }

              @Override
              public boolean isCellEditable(int rowIndex, int columnIndex) {
                  return canEdit[columnIndex];
              }
          };
    }

    @Override
    protected void initImportParams() {
       //do nothing
    }
    
    @Override
    public ArrayList<RasterTask> getTasks() {
       
        ArrayList<RasterTask> list = new ArrayList<>();
        DataSource ds = m_ShowDataBaseTablesPanel.getDataSource();
        if (ds != null) {

            int count = m_TableModel.getRowCount();

              for (int i = 0; i < count; i++) {
          
                //没有勾选
                if (!Boolean.valueOf(String.valueOf(m_TableModel.getValueAt(i, 0)))) {
                    continue;
                }
                RasterTask task = new RasterTask();
                String filePath = String.valueOf(m_TableModel.getValueAt(i, 4));
                task.importFile = new File(filePath);
                //ID 格式是 “日期格式-序号”
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
                task.taskID = df.format(new Date()) + "-" + i;
                task.taskName =String.valueOf(m_TableModel.getValueAt(i, 2));
                task.taskProgressValue = 0;
                task.taskSize = String.valueOf(m_TableModel.getValueAt(i,1));
                task.taskStatus = RasterTask.taskStatus_Ready;
                if (m_TableName.isEmpty()) {
                    m_TableName = m_ShowDataBaseTablesPanel.getTableNameFromTree();
                    if (m_TableName.isEmpty()) {
                        m_TableName = m_ShowDataBaseTablesPanel.getFirstTableNameFromTree();
                    }
                }
                task.targetDataSource =ds ;
                task.targetDBTableName = m_TableName;
                if (m_StoreMode == null) {
                    m_StoreMode = new StoreMode();
                    m_StoreMode.tableBName = m_TableName + "_rbt";
                }
                task.rasterStoreMode = m_StoreMode;
                list.add(task);
            }

        }
        return list;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        openfilebtn = new javax.swing.JButton();
        jTextField_OpenFile = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTablePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox_FileType = new javax.swing.JComboBox();
        jButton_SetStoreMode = new javax.swing.JButton();
        jPanel_DBtbls = new javax.swing.JPanel();

        openfilebtn.setText("……");
        openfilebtn.setFocusable(false);
        openfilebtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openfilebtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        openfilebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openfilebtnActionPerformed(evt);
            }
        });

        jTextField_OpenFile.setMinimumSize(new java.awt.Dimension(6, 30));
        jTextField_OpenFile.setPreferredSize(new java.awt.Dimension(6, 30));
        jTextField_OpenFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_OpenFileKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("黑体", 1, 12)); // NOI18N
        jLabel2.setText("导入文件：");

        jTablePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jTablePanelLayout = new javax.swing.GroupLayout(jTablePanel);
        jTablePanel.setLayout(jTablePanelLayout);
        jTablePanelLayout.setHorizontalGroup(
            jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );
        jTablePanelLayout.setVerticalGroup(
            jTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 237, Short.MAX_VALUE)
        );

        jLabel3.setText("文件格式");

        jComboBox_FileType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox_FileTypeMouseClicked(evt);
            }
        });
        jComboBox_FileType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_FileTypeItemStateChanged(evt);
            }
        });

        jButton_SetStoreMode.setText("设置模式");
        jButton_SetStoreMode.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_SetStoreMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SetStoreModeActionPerformed(evt);
            }
        });

        jPanel_DBtbls.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel_DBtblsLayout = new javax.swing.GroupLayout(jPanel_DBtbls);
        jPanel_DBtbls.setLayout(jPanel_DBtblsLayout);
        jPanel_DBtblsLayout.setHorizontalGroup(
            jPanel_DBtblsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 182, Short.MAX_VALUE)
        );
        jPanel_DBtblsLayout.setVerticalGroup(
            jPanel_DBtblsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jTextField_OpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(openfilebtn))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jComboBox_FileType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(jButton_SetStoreMode)))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_DBtbls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(openfilebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField_OpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_SetStoreMode)
                            .addComponent(jComboBox_FileType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel_DBtbls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void openfilebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openfilebtnActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        
//        fileChooser.setAcceptAllFileFilterUsed(false);//取消所有文件选项
        FileNameExtensionFilter tempFileFilter;
        fileChooser.setDialogTitle("打开文件");

        tempFileFilter = new FileNameExtensionFilter("tiff文件|*.tiff", "tiff");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("tif文件|*.tif", "tif");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("img文件|*.img", "img");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("位图文件|*.bmp", "bmp");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("EGC文件|*.egc", "EGC");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("jpg文件|*.jpg", "jpg");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        tempFileFilter = new FileNameExtensionFilter("JPEG文件|*.jpeg", "jpeg");
        fileChooser.addChoosableFileFilter(tempFileFilter);
        
        //上次路径
        String lastPath = readLastAccessPath(this.getClass());

        if (!lastPath.isEmpty()) {
            fileChooser.setCurrentDirectory(new File(lastPath));
        }
        fileChooser.showOpenDialog(this);

        File[] files =fileChooser.getSelectedFiles();
        if (files!=null) {

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

    private void jButton_SetStoreModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SetStoreModeActionPerformed
        // TODO add your handling code here:
          //对于栅格数据的导入需要配置导入模式：inline,outline,gateway
        SetRasterModePanel rasterModePanel = new SetRasterModePanel();
        DataSource ds = m_ShowDataBaseTablesPanel.getDataSource();
        if (ds == null) {
            setShowMessage("当前数据源不可用，请检查", JOptionPane.WARNING_MESSAGE);
            return;
        }
        rasterModePanel.setDataSource(ds);
        if (!m_TableName.isEmpty() && m_StoreMode != null) {
            rasterModePanel.setStoreMode(m_StoreMode);
            rasterModePanel.setTableAName(m_TableName);
        } else {
            
            String tableAname = m_ShowDataBaseTablesPanel.getTableNameFromTree();
            BydOperator bydOperator=new BydOperator(new DataSourceUtils(ds));

            if(!tableAname.isEmpty()&&!bydOperator.isRasterTable(tableAname))
            {
                setShowMessage("选中的"+tableAname+"不是栅格表\n 请选中栅格表再打开，或不做任何选择", JOptionPane.YES_OPTION);
                return;
            }else
            {
            rasterModePanel.setTableAName(tableAname);
            rasterModePanel.auotFileTableBName();
            rasterModePanel.setRasterMode(RasterMode.inline);
            }
        }
        int res = JOptionPane.showConfirmDialog(this, rasterModePanel,
                "设置栅格模式", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == 0) {
            m_StoreMode = rasterModePanel.getStoreMode();
            m_TableName = rasterModePanel.getTableAName();

        }
    }//GEN-LAST:event_jButton_SetStoreModeActionPerformed

    private void jComboBox_FileTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_FileTypeItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (m_TableModel != null) {
                m_TableModel.getDataVector().clear();
                openFile(jTextField_OpenFile.getText());
                displayTableContent(m_TableModel);
            }
        }
    }//GEN-LAST:event_jComboBox_FileTypeItemStateChanged

    private void jComboBox_FileTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox_FileTypeMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount()==2) {//双击
              if (m_TableModel != null) {
                m_TableModel.getDataVector().clear();
                openFile(jTextField_OpenFile.getText());
                displayTableContent(m_TableModel);
            }
        }
       
    }//GEN-LAST:event_jComboBox_FileTypeMouseClicked
 
    /**
     * 根据给出的路径打开文件内容，需要对路径参数进行判别
     *
     * @param path
     */
    @Override
    protected void openFile(String path) {
        File tmpFile = new File(path);
        if (tmpFile.isFile() && tmpFile.exists()) {
            //单个文件
            String name = tmpFile.getName().toLowerCase();
            boolean isOk = false;
            for (String typeString : SupportFileType) {
                if (name.endsWith(typeString.toLowerCase())) {
                    isOk = true;
                    break;
                }
            }
            if (isOk) {
                File[] files = {tmpFile};
                fillImportTable(files);
            }
        } else if (tmpFile.isDirectory()) {
            //目录
            String fullPath = tmpFile.getAbsolutePath();

            if (jComboBox_FileType.getSelectedItem() == "所有支持类型") {
                for (String fileType : SupportFileType) {
                    ArrayList<File> findFiles = FileUtils.findFiles(fullPath, fileType);
                    fillImportTable(findFiles);
                }
            } else {
                //仅支持的类型
                String type = String.valueOf(jComboBox_FileType.getSelectedItem());
                ArrayList<File> findFiles = FileUtils.findFiles(fullPath, type);
                fillImportTable(findFiles);
            }

        }

    }


    @Override
     protected void fillImportTable(ArrayList<File> findFiles) {
        if (findFiles.size() > 0) {
            for (File findFile : findFiles) {
                //  "全选", "大小", "文件名","文件类型","位置"
                String fullname = findFile.getName();
                String name = fullname.substring(0, fullname.indexOf("."));
                String type = fullname.substring(fullname.lastIndexOf("."));
                Object[] row = {true, FileUtils.formatFileSize(findFile.length()), name, type, findFile};
                m_TableModel.addRow(row);
            }
        }
    }

    /**
     *
     * @param findFiles
     */
    @Override
    protected void fillImportTable(File[] findFiles) {
        if (findFiles.length > 0) {
            for (File findFile : findFiles) {
                       //  "全选", "大小", "文件名","文件类型","位置"
                String fullname = findFile.getName();
                String name = fullname.substring(0, fullname.indexOf("."));
                String type = fullname.substring(fullname.lastIndexOf("."));
                Object[] row = {true, FileUtils.formatFileSize(findFile.length()), name, type, findFile};
                m_TableModel.addRow(row);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_SetStoreMode;
    private javax.swing.JComboBox jComboBox_FileType;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel_DBtbls;
    private javax.swing.JPanel jTablePanel;
    private javax.swing.JTextField jTextField_OpenFile;
    private javax.swing.JButton openfilebtn;
    // End of variables declaration//GEN-END:variables

   

   

 
}
