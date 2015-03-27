/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import com.beyondb.ui.display.RowSelectableJTable;
import com.beyondb.utils.FileUtils;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *创建新任务，打开一个加载 新任务 的对话框
 * @author lbs
 */
public abstract class NewTaskIntoDB extends JPanel implements IDialog, IGetTasks {
    protected   String[] SupportFileType =null;
    JTable m_Table;
    DefaultTableModel m_TableModel;
    protected  JPanel_DataBaseTables m_ShowDataBaseTablesPanel;
    JPanel  m_TablePanel;
    public NewTaskIntoDB() {
    }

      /**
     * 初始化数据表模型
     */
     protected void initTableModel() {
          m_TableModel = new DefaultTableModel(
                  new Object[][]{},
                  new String[]{
                      "全选", "位置", "大小", "文件名称"
                  }
          ) {
              Class[] types = new Class[]{
                  java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
              };
              boolean[] canEdit = new boolean[]{
                  true, false, false, false
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

    /**
     * 读取最后访问的路径
     * @param c
     * @return
     */
    protected String readLastAccessPath(Class c) {
        //上次路径
        Preferences pref = Preferences.userRoot().node(c.getName().replace(".", "/"));
        return pref.get("lastPath", "");
    }

    /**
     * 保留最后访问的路径
     *
     * @param c 类
     * @param path
     */
    protected void saveLastAccessPath(Class c,String path) {
        Preferences pref = Preferences.userRoot().node(c.getName().replace(".", "/"));
        pref.put("lastPath", path);
    }


    /**
     * 根据给出的路径打开文件内容，需要对路径参数进行判别
     *
     * @param path
     */
       protected   void openFile(String path) {
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
            for (String type : SupportFileType) {
                ArrayList<File> findFiles = FileUtils.findFiles(fullPath, type);
                fillImportTable(findFiles);
            }
        }
    }

    protected void fillImportTable(ArrayList<File> findFiles) {
        if (findFiles.size() > 0) {
            for (File findFile : findFiles) {
                //  "全选", "位置", "大小", "文件名称"
                Object[] row = {true, findFile, FileUtils.formatFileSize(findFile.length()), findFile.getName().substring(0, findFile.getName().indexOf("."))};
                m_TableModel.addRow(row);
            }
        }
    }

    protected void fillImportTable(File[] findFiles) {
        if (findFiles.length > 0) {
            for (File findFile : findFiles) {
                Object[] row = {true, findFile, FileUtils.formatFileSize(findFile.length()), findFile.getName().substring(0, findFile.getName().indexOf("."))};
                m_TableModel.addRow(row);
            }
        }
    }

    /**
     * 创建快捷菜单
     *
     * @return
     */
    protected JPopupMenu createPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu("表格工具");
        JMenuItem deleteSelectedRow = new JMenuItem("删除选中的行");
        JMenuItem deleteAll = new JMenuItem("删除所有行");
        //删除选中都表内容
        deleteSelectedRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = m_Table.getSelectedRows();
                //删除所有选中列
                for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                    m_TableModel.getDataVector().removeElementAt(rows[rowIndex]);
                    m_TableModel.fireTableDataChanged();
                }
            }
        });
        //删除所有表内容
        deleteAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (m_TableModel.getRowCount() > 0) {
                    m_TableModel.getDataVector().removeAllElements();
                    m_TableModel.fireTableDataChanged();
                }
            }
        });
        jPopupMenu.add(deleteSelectedRow);
        jPopupMenu.add(deleteAll);
        return jPopupMenu;
    }

    /**
     * 展示表格文件内容
     *
     * @param tableModel
     */
    public void displayTableContent(TableModel tableModel) {
        m_Table = new RowSelectableJTable(tableModel);
        m_Table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); //设置JTable的列宽度是否随着JTable的变化而变化。
        m_Table.setAutoscrolls(true);
        m_Table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    JPopupMenu pop = createPopupMenu();
                    pop.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        //设置表头第一列为checkBox
        final CheckBoxTableHeaderRenderer check = new CheckBoxTableHeaderRenderer();
        TableColumnModel columnModel = m_Table.getColumnModel();
        columnModel.getColumn(0).setHeaderRenderer(check);
        check.setSelected(true); //默认
        m_Table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int columnIndex = m_Table.getTableHeader().columnAtPoint(e.getPoint());
                int rowCount = m_TableModel.getRowCount();
                if (columnIndex == 0) {
                    //如果点击的是第0列，即checkbox这一列
                    boolean flag = !check.isSelected();
                    check.setSelected(flag);
                    m_Table.getTableHeader().repaint();
                    for (int i = 0; i < rowCount; i++) {
                        m_TableModel.setValueAt(flag, i, columnIndex); //把这一列都设成和表头一样
                    }
                }
            }
        });
        JScrollPane jScrollp = new JScrollPane(m_Table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        m_TablePanel.removeAll();
        m_TablePanel.add(jScrollp);
        m_TablePanel.setLayout(new GridLayout(1, 1));
        m_TablePanel.setVisible(true);
        m_TablePanel.updateUI();
        //初化化面板导入参数....
        initImportParams();
    }

    /**
     * 初始化面板上的导入参数
     */
    protected abstract void initImportParams(); 

    /**
     * 弹出对话框
     *
     * @param msg 内容
     * @param JOptionPanestyle 样式
     */
    @Override
    public void setShowMessage(String msg, int JOptionPanestyle) {
        JOptionPane.showMessageDialog(this, msg, "提示", JOptionPanestyle);
    }

    @Override
    public int setShowConfirmDialog(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "确认", JOptionPane.YES_NO_OPTION);
    }

    /**
     *
     * @param panel 面板
     * @param title  标题
     * @param JOptionPanestyle 样式
     * @return
     */
    @Override
    public int setShowConfirmDialog(JPanel panel, String title, int JOptionPanestyle) {
        return JOptionPane.showConfirmDialog(this, panel, title, JOptionPanestyle, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     *
     * @param title  标题
     * @param initValue  初始化值
     * @return
     */
    public String setShowInputDialog(String title, String initValue) {
        return JOptionPane.showInputDialog(this, title, initValue);
    }
    
      class CheckBoxTableHeaderRenderer extends JCheckBox implements TableCellRenderer{
 
    public CheckBoxTableHeaderRenderer() {
        this.setBorderPainted(true);
        this.setText("全选");
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub  
    
        return this;
    }   
    
}
}
