/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.tabbedPanel;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author lbs
 */
public class TabbedPanel {

    public static void refreshTabbedPanel(JTabbedPane tabbedPane) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getIconAt(i) == null) {
                tabbedPane.setIconAt(i, new CloseIcon());
            }
        }
    }

    /**
     * 展示选项卡
     * @param tabbedPane
     * @param panelClassName
     */
    public static void displayTab(JTabbedPane tabbedPane, String panelClassName) {
        try {
            Object object = null;
            Class cls = null;
            boolean isExist = false;
            for (Component c : tabbedPane.getComponents()) {
                try {
                    if (panelClassName.equals(c.getClass().getName())) {
                        isExist = true;
                        object = c;
                        break;
                    }
                } catch (Exception e) {
                }
            }
            if (isExist) {
                tabbedPane.setSelectedComponent((Component) object);
            } else {
                cls = Class.forName(panelClassName);
                object = cls.newInstance();
                tabbedPane.add(((JPanel) object).getName(), (Component) object);
                refreshTabbedPanel(tabbedPane);
                tabbedPane.setSelectedComponent((Component) object);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TabbedPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(TabbedPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
