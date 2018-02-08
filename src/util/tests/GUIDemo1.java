package util.tests;

import java.awt.*;
import javax.swing.*;
 
public class GUIDemo1 { 
    public static void main(String[] args) { 
        JFrame demo = new JFrame();
        demo.setSize(400, 300);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        JCheckBox checkbox = new JCheckBox("JCheckBox");
        JRadioButton radiobutton = new JRadioButton("JRadiobutton");
        JButton button = new JButton("JButton");
        JLabel label = new JLabel("JLabel");
        JTextArea textarea = new JTextArea("JTextArea");
         
        demo.getContentPane().add(BorderLayout.EAST, checkbox);
        demo.getContentPane().add(BorderLayout.WEST, radiobutton);
        demo.getContentPane().add(BorderLayout.SOUTH, button);
        demo.getContentPane().add(BorderLayout.NORTH , label);
        demo.getContentPane().add(BorderLayout.CENTER, textarea);
         
        demo.setVisible(true);
    }
}
 
/* 《程式語言：教學誌》的範例程式
    http://pydoing.blogspot.com/
    檔名：GUIDemo1.java
    功能：示範 Java 程式 
    作者：張凱慶
    時間：西元 2011 年 4 月 */
