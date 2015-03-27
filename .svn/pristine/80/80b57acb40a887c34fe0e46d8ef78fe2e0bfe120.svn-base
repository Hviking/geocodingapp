/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author lbs
 */
public class JPanelDialog extends javax.swing.JPanel implements IDialog   {

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
    public int setShowConfirmDialog(JPanel panel,String title,int JOptionPanestyle) {
        return JOptionPane.showConfirmDialog(this,panel, title,JOptionPanestyle,JOptionPane.PLAIN_MESSAGE);
           
    }
    
}
