/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;
 
import java.awt.AWTException;  
import java.awt.Image;  
import java.awt.MenuItem;  
import java.awt.PopupMenu;  
import java.awt.SystemTray;  
import java.awt.Toolkit;  
import java.awt.TrayIcon;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;  
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;  
import javax.swing.ImageIcon;  
import javax.swing.JFrame;  
  
/**
 *创建托盘图像 
 * @author 倪永
 */
public class BickerTrayUtil  {
 
    TrayIcon trayIcon; // 托盘图标
    SystemTray tray; // 本操作系统托盘的实例
 
  public URL getRes(String str){  
        return this.getClass().getClassLoader().getResource(str);  
  }
 public BickerTrayUtil(final JFrame frame)
 {

  tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例
 // ImageIcon icon = new ImageIcon(getRes("com/beyondb/ui/images/foxmail.ico")); // 将要显示到托盘中的图标
  Image image = Toolkit.getDefaultToolkit().getImage("com/beyondb/ui/images/foxmail.ico"); 
  PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
  final MenuItem show = new MenuItem("打开程序");
  final MenuItem exit = new MenuItem("退出");
  pop.add(show);
  pop.add(exit);
  trayIcon = new TrayIcon(image,"BeyonDB入库工具", pop);//实例化托盘图标
 
  //为托盘图标监听点击事件
  trayIcon.addMouseListener(new MouseAdapter() 
  {
      public void mouseClicked(MouseEvent e)
      {
         if(e.getClickCount()== 2)//鼠标双击图标
         { 
          //tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标    
         frame.setExtendedState(JFrame.NORMAL);//设置状态为正常
            frame.setVisible(true);//显示主窗体
          }
      }
   });
  
  //选项注册事件
  ActionListener al2=new ActionListener()
     {
       public void actionPerformed(ActionEvent e)
       {
        //退出程序
        if(e.getSource()==exit)
        {
         System.exit(0);//退出程序
        }
        //打开程序
        if(e.getSource()==show)
        {
         frame.setExtendedState(JFrame.NORMAL);//设置状态为正常
      frame.setVisible(true);
        }
       }
      };
      exit.addActionListener(al2);
      show.addActionListener(al2);
        
      try
      {
           tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
      }
      catch(AWTException ex)
      {
           ex.printStackTrace();
       }
   
   //为主窗体注册窗体事件
   frame.addWindowListener(new WindowAdapter()
   {
    //窗体最小化事件
    public void windowIconified(WindowEvent e)
        {    
            frame.setVisible(false);//使窗口不可视
            frame.dispose();//释放当前窗体资源
         }   
   });
 }
 
 public static void main(String[] args) 
 {
     String path=System.getProperty("java.library.path");
     System.out.println(path);
//   final JFrame frame=new JFrame("系统托盘");
//  frame.setSize(300,200);
//  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//  frame.setVisible(true);
//  
//    new BickerTrayUtil(frame);
 }


}
