/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

import com.beyondb.utils.OpenURLUtil;
import com.beyondb.utils.initSystemParams;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *主窗体
 * @author  倪永 
 */
public class MainFrame extends javax.swing.JFrame {
    private DataEditor m_DataEditor;


          
    /**
     * Creates new form MainJFrame
     */
    public MainFrame() {
        initComponents();
    }

        GeocodingApp m_GeocodingApp;
      
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jBtn_ConfigDB = new javax.swing.JButton();
        jBtn_Load = new javax.swing.JButton();
        jButton_Publish = new javax.swing.JButton();
        jButton_Edit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("数据层解决方案平台");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setOpaque(false);

        jBtn_ConfigDB.setFont(new java.awt.Font("幼圆", 0, 10)); // NOI18N
        jBtn_ConfigDB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujukushezhi.png"))); // NOI18N
        jBtn_ConfigDB.setBorder(null);
        jBtn_ConfigDB.setContentAreaFilled(false);
        jBtn_ConfigDB.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jBtn_ConfigDB.setHideActionText(true);
        jBtn_ConfigDB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtn_ConfigDB.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujukushezhidianji.png"))); // NOI18N
        jBtn_ConfigDB.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtn_ConfigDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_ConfigDBActionPerformed(evt);
            }
        });

        jBtn_Load.setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        jBtn_Load.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujujianku.png"))); // NOI18N
        jBtn_Load.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jBtn_Load.setBorderPainted(false);
        jBtn_Load.setContentAreaFilled(false);
        jBtn_Load.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtn_Load.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujujiankudianji.png"))); // NOI18N
        jBtn_Load.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtn_Load.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_LoadActionPerformed(evt);
            }
        });
        jBtn_Load.setContentAreaFilled(false);

        jButton_Publish.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButton_Publish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujufabu.png"))); // NOI18N
        jButton_Publish.setContentAreaFilled(false);
        jButton_Publish.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jButton_Publish.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_Publish.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujufabudianji.png"))); // NOI18N
        jButton_Publish.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_Publish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PublishActionPerformed(evt);
            }
        });

        jButton_Edit.setFont(new java.awt.Font("微软雅黑", 0, 14)); // NOI18N
        jButton_Edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujubianji.png"))); // NOI18N
        jButton_Edit.setContentAreaFilled(false);
        jButton_Edit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton_Edit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shujubianjidianji.png"))); // NOI18N
        jButton_Edit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton_Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EditActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("宋体", 0, 10)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/denglu.png"))); // NOI18N
        jButton1.setToolTipText("");
        jButton1.setBorder(null);
        jButton1.setContentAreaFilled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/dengludianji.png"))); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("宋体", 0, 10)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/bangzhu.png"))); // NOI18N
        jButton4.setBorder(null);
        jButton4.setContentAreaFilled(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/bangzhudianji.png"))); // NOI18N
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jButton5.setFont(new java.awt.Font("宋体", 0, 10)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/tuichu.png"))); // NOI18N
        jButton5.setBorder(null);
        jButton5.setContentAreaFilled(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/tuichudianji.png"))); // NOI18N
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jBtn_ConfigDB, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jBtn_Load, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56)
                        .addComponent(jButton_Edit)
                        .addGap(41, 41, 41)
                        .addComponent(jButton_Publish)
                        .addGap(72, 72, 72)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBtn_ConfigDB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Publish))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(98, 98, 98)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_Edit)
                            .addComponent(jBtn_Load, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/pingtaibeijing3.png"))); // NOI18N
        jLabel2.setOpaque(true);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2)
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtn_ConfigDBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn_ConfigDBActionPerformed
        // TODO add your handling code here:
        
        ConfigPanel configDataBasePane = new ConfigPanel();
//        JOptionPane p = new JOptionPane();
//        Color color = new Color(102, 204, 255);
//        p.setBackground(color);
//        JDialog dialog = p.createDialog(this, "BeyonDB数据库配置");
//        dialog.setLayout(new GridLayout(1, 1));
//        dialog.setResizable(true);
//        dialog.setModal(true);
//        p.setMessage(configDataBasePane);
//        p.setMessageType(JOptionPane.PLAIN_MESSAGE);
//        p.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//        
////        Object[] objects = p.getOptions(); //都是按钮
////        if (objects != null) {
////            for (Object object : objects) {
////                JButton btn = (JButton) object;
////                btn.setBackground(Color.RED);
////            }
////        }
//        dialog.setContentPane(p);
//        dialog.setVisible(true);
//  
        int res = JOptionPane.showConfirmDialog(this, configDataBasePane,
                "设置", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//        int res = p.getOptionType();
        if (res == 0) {
            //保存
            configDataBasePane.saveConfig();
        }
    }//GEN-LAST:event_jBtn_ConfigDBActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }       
        
    private void jButton_EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EditActionPerformed
        // TODO add your handling code here:
         if (m_DataEditor==null) {
            m_DataEditor = new DataEditor();
         }
          showFrame(m_DataEditor,0.4,0.5);
    }//GEN-LAST:event_jButton_EditActionPerformed

    private void jBtn_LoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn_LoadActionPerformed
        // TODO add your handling code here:
        if (m_GeocodingApp==null) {
            m_GeocodingApp = new GeocodingApp();    
        }
        showFrame(m_GeocodingApp,0.6,0.8);

    }//GEN-LAST:event_jBtn_LoadActionPerformed

    private void showFrame(JFrame frame,double scale_X,double scale_Y)
    {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int sizeW = (int) Math.floor(width * scale_X);
        int sizeH = (int) Math.floor(height * scale_Y);
        frame.setSize(sizeW, sizeH);
        frame.setLocation((width - frame.getWidth()) / 2, (height - frame.getHeight()) / 2);
        frame.setVisible(true);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
          initSystemParams.getInstance().saveParams();
    }//GEN-LAST:event_formWindowClosing

    private void jButton_PublishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PublishActionPerformed
        // TODO add your handling code here:
        String url =String.valueOf(initSystemParams.getInstance().getSystemParam(initSystemParams.PARAM.PARAM_DATAPUBLISH_TOOL_URL));
        if (!url.isEmpty()) {
            OpenURLUtil.openURL(url);  
        }
       
    }//GEN-LAST:event_jButton_PublishActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

         initSystemParams.getInstance();
         
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               
                
                JFrame frame = new MainFrame();

                int width = Toolkit.getDefaultToolkit().getScreenSize().width;
                int height = Toolkit.getDefaultToolkit().getScreenSize().height;
               
                frame.setLocation((width  - frame.getWidth())/2, (height-frame.getHeight()) / 2);
                frame.setVisible(true);
      
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtn_ConfigDB;
    private javax.swing.JButton jBtn_Load;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton_Edit;
    private javax.swing.JButton jButton_Publish;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}