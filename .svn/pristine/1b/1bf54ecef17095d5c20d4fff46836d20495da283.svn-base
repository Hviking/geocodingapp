/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.ui.tabbedPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Icon;

/**
 *
 * @author lbs
 */
public class CloseIcon implements Icon {
    private int x_pos;
    private int y_pos;
    private final int width;
    private final int height;

    public CloseIcon() {
        this.height = 16;
        this.width = 16;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color color = g.getColor();
        g.setColor(Color.RED.darker());
        x_pos = x;
        y_pos = y;
        int y_p = y + 2;
        g.drawLine(x + 1, y_p, x + 12, y_p);
        g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
        g.drawLine(x, y_p + 1, x, y_p + 12);
        g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
        g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
        g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
        g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
        g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
        g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
        g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
        
        g.setColor(color);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    public Rectangle getBounds() {
        return new Rectangle(x_pos, y_pos, width, height);
    }
    
}
