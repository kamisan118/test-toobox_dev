package util.tests;

import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class GUItest { 
    public static void main(String[] args) { 
        JFrame demo = new JFrame("vocabulary_G_pmli_GUI_ver.");
        demo.setSize(800, 600);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel label1 = new JLabel("JLabel");        
        
        ImageIcon icon = new ImageIcon("msn.jpg"); 
		JLabel label2 = new JLabel(); 
		label2.setIcon(icon); 
         
        demo.getContentPane().add(BorderLayout.NORTH, label1);
        demo.getContentPane().add(BorderLayout.SOUTH, label2);
        icon = new ImageIcon("ho.gif");
        label2.setIcon(icon); 
        
        //GridLayout gl = new gridLayout(2,macthedImages.length/2);
        
        
        //demo.pack(); //resize window -- 
        demo.setVisible(true);
        //label.setText("change text");
        
        //ImgHandle.main(args);
    }
}

 
/**
 * This class demonstrates how to load an Image from an external file
 */
class ImgHandle extends Component {
           
    BufferedImage img;
 
    public ImgHandle(String pic) {
       try {
           img = ImageIO.read(new File(pic));
       } catch (IOException e) {
       } 
    }
    
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
    
    public Dimension getPreferredSize() {
        if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }
    }
    
    
    public static void showAnImg(String imgUrl) {
    	 
        JFrame f = new JFrame("Load Image Sample");
             
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
 
        f.add(new ImgHandle(imgUrl));
        //f.add(new ImgHandle("ho.gif"));
        f.pack();
        f.setVisible(true);
    }
    
    public static void main(String[] args) {
 
        JFrame f = new JFrame("Load Image Sample");
             
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
 
        f.add(new ImgHandle("msn.jpg"));
        //f.add(new ImgHandle("ho.gif"));
        f.pack();
        f.setVisible(true);
    }
}

 
/* 《程式語言：教學誌》的範例程式
    http://pydoing.blogspot.com/
    檔名：GUIDemo1.java
    功能：示範 Java 程式 
    作者：張凱慶
    時間：西元 2011 年 4 月 */
