/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package gui.components;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author HumanoideFilms
 */
public class DisparityPanel extends javax.swing.JPanel {

    /**
     * Creates new form DisparityPanel
     */
    public DisparityPanel() {
        initComponents();
        setBackground(Color.BLACK);
        repaint();
    }
    
    int[] xpoints;
    int min;
    int max;
    boolean set=false;
    
    public void setXpoints(int[] xpoints){
        this.xpoints=xpoints;
        min=xpoints[0];
        max=xpoints[xpoints.length-1];
        if(min>max){
            int temp=max;
            min=max;
            max=temp;
        }
        set=true;
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(10,200,200));
        if(set){
            g.drawLine(0, 200, width, 200);
            int step=width/xpoints.length;
            for(int i=0;i<xpoints.length;i++){
                g.fillOval(i*step, 197, 5, 5);
                g.drawString(xpoints[i]+"", i*step, 220);
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

class gaussian{
    double a;
    double b;
    double m;

    public gaussian(double a, double b, double m) {
        this.a = a;
        this.b = b;
        this.m = m;
    }
}
