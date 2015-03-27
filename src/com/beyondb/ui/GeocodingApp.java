/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.ui;

import com.beyondb.ui.tabbedPanel.TabbedPanel;
import com.beyondb.ui.tabbedPanel.TabbedPanelMouseListenerImpl;

/**
 *
 * @author ZhangShuo
 */
public class GeocodingApp extends javax.swing.JFrame {

    /**
     * Creates new form GeocodingApp
     */
    public GeocodingApp() {
        initComponents();

        jTabbedPane1.addMouseListener(new TabbedPanelMouseListenerImpl());
       
       
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jButton4Txt = new javax.swing.JButton();
        jButton4Raster = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton4Geom = new javax.swing.JButton();
        jButton4Video = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("数据入库工具");

        jPanel1.setBackground(new java.awt.Color(42, 142, 206));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setOpaque(true);

        jButton4Txt.setFont(new java.awt.Font("宋体", 0, 10)); // NOI18N
        jButton4Txt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/wenbenshujuruku1.5x1.5.png"))); // NOI18N
        jButton4Txt.setToolTipText("");
        jButton4Txt.setBorder(null);
        jButton4Txt.setContentAreaFilled(false);
        jButton4Txt.setDefaultCapable(false);
        jButton4Txt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4Txt.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4Txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4TxtActionPerformed(evt);
            }
        });

        jButton4Raster.setFont(new java.awt.Font("宋体", 0, 10)); // NOI18N
        jButton4Raster.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/shiliangshangeshujuruku1.5x1.5.png"))); // NOI18N
        jButton4Raster.setBorder(null);
        jButton4Raster.setContentAreaFilled(false);
        jButton4Raster.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4Raster.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4Raster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4RasterActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/beyondb/ui/images/beyonLOGO.png"))); // NOI18N

        jButton4Geom.setText("矢量数据入库");
        jButton4Geom.setToolTipText("");
        jButton4Geom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4GeomActionPerformed(evt);
            }
        });

        jButton4Video.setText("多媒体数据入库");
        jButton4Video.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4VideoActionPerformed(evt);
            }
        });

        jButton3.setText("数据空间化迁移");
        jButton3.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4Txt, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4Raster, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton4Geom)
                .addGap(18, 18, 18)
                .addComponent(jButton4Video)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 261, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton4Txt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4Raster, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton4Geom)
                                .addComponent(jButton4Video)
                                .addComponent(jButton3))
                            .addComponent(jLabel1))))
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 453, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
    
    private void jButton4TxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4TxtActionPerformed
        // TODO add your handling code here:
       TabbedPanel.displayTab(jTabbedPane1,JPanel_txtIntoDB.class.getName());
    }//GEN-LAST:event_jButton4TxtActionPerformed

    
    private void jButton4RasterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4RasterActionPerformed
        // TODO add your handling code here:
        TabbedPanel.displayTab(jTabbedPane1,RasterIntoDB.class.getName());
    }//GEN-LAST:event_jButton4RasterActionPerformed

    private void jButton4GeomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4GeomActionPerformed
        // TODO add your handling code here:
        TabbedPanel.displayTab(jTabbedPane1,GeomIntoDB.class.getName());
    }//GEN-LAST:event_jButton4GeomActionPerformed

    private void jButton4VideoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4VideoActionPerformed
        // TODO add your handling code here:
          TabbedPanel.displayTab(jTabbedPane1,MultiMediaIntoDB.class.getName());
    }//GEN-LAST:event_jButton4VideoActionPerformed

   
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
            java.util.logging.Logger.getLogger(GeocodingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GeocodingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GeocodingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GeocodingApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GeocodingApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4Geom;
    private javax.swing.JButton jButton4Raster;
    private javax.swing.JButton jButton4Txt;
    private javax.swing.JButton jButton4Video;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}