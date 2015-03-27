/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;



/**
 *创建对话框
 * @author niy
 */
public class DialogUtil {
    int  result = -1;
     JDialog dialog = null;
     boolean isAvailable =true;
     
      public  int  showDialog(JFrame father,final JPanel panel,String title)
    { 
        return showDialog(father,  panel,null, title);
    }

     
    /**
     *
     * @param father
     * @param panel
     * @param check
     * @param title
     * @return
     */
    public  int  showDialog(JFrame father, final JPanel panel,final ICheckAvailable check,String title)
    {    
        JButton confirm, cancel;
        
        confirm = new JButton("确定");
        // confirm.setBounds(100, 40, 60, 20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (check!=null) {
                    isAvailable=check.isAvailabled();
                }
                
                if (isAvailable) {
                    result = 0;
                    dialog.dispose();
                }
            }
        });
        cancel = new JButton("取消");
        //cancel.setBounds(190, 40, 60, 20);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  if (check!=null) {
                    isAvailable=check.isAvailabled();
                }
                
                if (isAvailable) {
                    result = 1;
                    dialog.dispose();                    
                }
                
            }
        });
        dialog = new JDialog(father, true);
        dialog.setTitle(title);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        panel.setBorder(null);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(null);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        FlowLayout flowLayout =new FlowLayout(10);
        flowLayout.setAlignment(FlowLayout.RIGHT);

        buttonPanel.setLayout(flowLayout);
        buttonPanel.add(confirm);
        buttonPanel.add(cancel );

      
        dialog.pack();
        dialog.setSize(new Dimension(panel.getWidth(),panel.getHeight()+100));
        dialog.setLocationRelativeTo(father);
        dialog.setVisible(true);
        
        return  result;
    }


        public static void main(String[] args) {
            JFrame f = new JFrame();
            f.setSize(500, 500);
            f.setLocation(600,0);
            f.setVisible(true);
             DialogUtil dialogUtil =new DialogUtil();
             JPanel jPanel =new JPanel();
             jPanel.setForeground(Color.red);
             jPanel.setBackground(Color.GREEN);
             dialogUtil.showDialog(f, jPanel, "test");
    }

}
