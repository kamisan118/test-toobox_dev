package util.tests;

import java.awt.*; 
import javax.swing.*; 

public class IMGinJPanel extends JFrame 
{ 
	public IMGinJPanel() 
	{ 
		setSize(500,500); 
		JPanel panel = new JPanel(); 
		panel.setBackground(Color.CYAN); 
		ImageIcon icon = new ImageIcon("msn.jpg"); 
		JLabel label = new JLabel(); 
		label.setIcon(icon); 
		panel.add(label); 
		this.getContentPane().add(panel); 
		
		setVisible(true); 
	}
	
	public static void main (String[] args) // no args expected 
	{ 
		new IMGinJPanel(); 
	} // end main 
} // end class ImageDisplay
