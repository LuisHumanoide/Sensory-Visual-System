/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JLabel;

/**
 *
 * @author Laptop
 */
public class Listener implements MouseListener{
    
    String message="";
    JLabel label;
    VisualizerFrame frame;
    int index;
    
    public Listener(String message, JLabel label, VisualizerFrame frame, int index){
        this.message=message;
        this.label=label;
        this.frame=frame;
        this.index=index;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        frame.methodListener(index);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        frame.copyImage(index);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        frame.isInLabel=false;
        frame.repaint();
    }
}
