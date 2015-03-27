/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui;

/**
 *
 * @author lbs
 */
public class ConfigPanel extends javax.swing.JPanel {
    private  ConfigDBPanel dbConfig;
    private final ConfigDataPublishPanel publishConfig;
    private final ConfigDataEditPanel editConfig;

    /**
     * Creates new form ConfigPanel
     */
    public ConfigPanel() {
        initComponents();
      
        dbConfig = new ConfigDBPanel();
        editConfig = new ConfigDataEditPanel();
        publishConfig = new ConfigDataPublishPanel();
        jTabbedPane1.add("数据库", dbConfig);
        jTabbedPane1.add("数据编辑工具", editConfig);
        jTabbedPane1.add("数据发布工具", publishConfig);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    void saveConfig() {
        //保存配置
        dbConfig.saveConfig();
        editConfig.saveConfig();
        publishConfig.saveConfig();
    }
}