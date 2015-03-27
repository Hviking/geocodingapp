/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.tabbedPanel;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JTabbedPane;

/**
 *
 * @author lbs
 */
public class TabbedPanelMouseListenerImpl implements MouseListener {

    public TabbedPanelMouseListenerImpl() {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTabbedPane tabbedPane = (JTabbedPane) e.getComponent();
        int selectedIndex = tabbedPane.getSelectedIndex();
        if (selectedIndex > -1) {
            CloseIcon ic = (CloseIcon) (tabbedPane.getIconAt(selectedIndex));
            Rectangle bounds = ic.getBounds();
            Rectangle rec = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
            if (rec.contains(e.getX(), e.getY())) {
                tabbedPane.remove(selectedIndex);
                
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    
}
