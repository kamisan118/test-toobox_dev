package util.tests.keylisten;

//KeyDemo.java - Demonstrates handling three kinds of keys:
import javax.swing.*;
/** Demonstrates handling of different types of keys:
    virtual keys (arrows), modifiers (shift), and characters.
    @author Fred Swartz
    @version 2004-05-06
*/
public class KeyDemo extends JFrame {
    public static void main(String[] args) {
        JFrame window = new KeyDemoGUI();
        
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}//endclass KeyDemo
