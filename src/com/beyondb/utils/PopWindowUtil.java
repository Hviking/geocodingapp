/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *右下角弹出窗口
 * @author 倪永
 */
public class PopWindowUtil {
    private JFrame frame;
    private JPanel m_panel;
     private TextArea textArea = new TextArea();
    public PopWindowUtil() {
        createWindow();
        popWindow();
    }
    public void setPanel(JPanel panel)
    {
        m_panel=panel;
    }
    
    public void setTip(String tip)
    {
        textArea.setText(tip);
    }
    /**
     * 创建桌面右下角窗口，
     * 
     */
    private  void createWindow()
    {
    
        textArea.setText("窗口内容");
        textArea.setForeground(Color.green);
        Font font = new Font("宋体",Font.BOLD,12); 
        textArea.setFont(font);
        JFrame f =new JFrame();
        f.setSize(150,150);
        f.setBackground(Color.yellow);
        f.setLayout(new BorderLayout(1, 1));
        if (m_panel!=null) {
             f.add(m_panel);
        }else
        {
            f.add(textArea);      
        }
       
        frame =f;
    }
    /**
     * 可在此修改窗口弹出位置
     */
    private  void popWindow()
    {
        //  获取屏幕的边界
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
        //  获取底部任务栏高度
        int taskBarHeight = screenInsets.bottom;
  
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        frame.setLocation(width - frame.getWidth(), height-taskBarHeight- frame.getHeight());
        frame.setVisible(true);
    
    }
    public static void main(String[] args) {
        PopWindowUtil util =new PopWindowUtil();
        util.setTip("数据入库已完成！");
    }
    
}
