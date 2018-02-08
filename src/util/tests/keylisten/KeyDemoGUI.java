package util.tests.keylisten;

//KeyDemoGUI.java - JFrame subclass
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
//////////////////////////////////////////////////////////////// KeyDemoGUI
/** JFrame subclass for KeyDemo GUI.
    virtual keys (arrows), and characters.
    @author Fred Swartz
    @version 2004-05-06
*/
public class KeyDemoGUI extends JFrame {
    MovingTextPanel drawing;

    //==========================================================constructor
    public KeyDemoGUI() {
        drawing = new MovingTextPanel();
        this.getContentPane().setLayout(new BorderLayout());
        JLabel instructions = new JLabel("<html><ul><li>Type text.</li>"
                    + "<li>Use arrow keys to move text.</li>"
                    + "<li>Press shift arrows to move faster.</li></html>");
        this.getContentPane().add(instructions, BorderLayout.NORTH);
        this.getContentPane().add(drawing, BorderLayout.CENTER);
        
        this.setTitle("KeyDemo");
        this.pack();
        
        drawing.requestFocus();      // Give the panel focus.
    }//end constructor
}//endclass KeyDemoGUI