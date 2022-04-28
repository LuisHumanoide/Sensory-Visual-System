/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import MiniPrograms.CurvatureRF;
import MiniPrograms.GaborList;
import MiniPrograms.MotionV1Speeds;
import MiniPrograms.RFGeneratorNew;
import generator.NodeGenerator;
import generator.SmallNodeList;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import middlewareVision.config.Init;

/**
 *
 * @author HumanoideFilms
 */
public class ToolsJPanel extends javax.swing.JPanel {

    GaborList p1;
    MotionV1Speeds p2;
    RFGeneratorNew p3;
    CurvatureRF p4;
    RetinaPanel ret;
    SmallNodeList p5;
    /**
     * Creates new form ToolsJPanel
     */
    public ToolsJPanel(RetinaPanel ret) {
        initComponents();
        this.ret=ret;
        p1 = new GaborList();
        p2 = new MotionV1Speeds();
        p3 = new RFGeneratorNew();
        p4 = new CurvatureRF();
        p5 = new SmallNodeList();

        p1.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        p2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        p3.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        p4.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        p5.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        b1 = new javax.swing.JButton();
        b2 = new javax.swing.JButton();
        b3 = new javax.swing.JButton();
        b4 = new javax.swing.JButton();
        b6 = new javax.swing.JButton();
        b7 = new javax.swing.JButton();
        b5 = new javax.swing.JButton();

        setBackground(new java.awt.Color(57, 57, 57));
        setLayout(new java.awt.GridLayout(4, 2));

        b1.setBackground(new java.awt.Color(100, 114, 123));
        b1.setText("Gabor Bank List");
        b1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b1ActionPerformed(evt);
            }
        });
        add(b1);

        b2.setBackground(new java.awt.Color(100, 114, 123));
        b2.setText("Motion Bank List");
        b2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b2ActionPerformed(evt);
            }
        });
        add(b2);

        b3.setBackground(new java.awt.Color(100, 114, 123));
        b3.setText("Receptive Field List");
        b3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });
        add(b3);

        b4.setBackground(new java.awt.Color(100, 114, 123));
        b4.setText("Curvature Editor");
        b4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b4ActionPerformed(evt);
            }
        });
        add(b4);

        b6.setBackground(new java.awt.Color(100, 114, 123));
        b6.setText("Small Nodes Startup");
        b6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b6ActionPerformed(evt);
            }
        });
        add(b6);

        b7.setBackground(new java.awt.Color(100, 114, 123));
        b7.setText("Configuration File");
        b7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });
        add(b7);

        b5.setBackground(new java.awt.Color(100, 114, 123));
        b5.setText("Restart Banks");
        b5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });
        add(b5);
    }// </editor-fold>//GEN-END:initComponents

    private void b2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b2ActionPerformed
        // TODO add your handling code here:
        p2.setVisible(true);
    }//GEN-LAST:event_b2ActionPerformed

    private void b1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b1ActionPerformed
        // TODO add your handling code here:
        p1.setVisible(true);
    }//GEN-LAST:event_b1ActionPerformed

    private void b3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b3ActionPerformed
        // TODO add your handling code here:
        p3.setVisible(true);
    }//GEN-LAST:event_b3ActionPerformed

    private void b4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b4ActionPerformed
        // TODO add your handling code here:
        p4.setVisible(true);
    }//GEN-LAST:event_b4ActionPerformed

    private void b5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b5ActionPerformed
        // TODO add your handling code here:
       // Visualizer.vis.dispose();
        Visualizer.vis.restartLabels();
        Init.restart();
        ret.createImage(0, true);
    }//GEN-LAST:event_b5ActionPerformed

    private void b6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b6ActionPerformed
        // TODO add your handling code here:
        p5.setVisible(true);
    }//GEN-LAST:event_b6ActionPerformed

    Runtime run = Runtime.getRuntime();
    private void b7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b7ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            run.exec("cmd.exe /c "+System.getProperty("user.dir")+"\\ConfigFiles\\Configuration.xml");
            //run.exec("TemplateBigNode.java");
        } catch (IOException ex) {
            Logger.getLogger(NodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_b7ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b1;
    private javax.swing.JButton b2;
    private javax.swing.JButton b3;
    private javax.swing.JButton b4;
    private javax.swing.JButton b5;
    private javax.swing.JButton b6;
    private javax.swing.JButton b7;
    // End of variables declaration//GEN-END:variables
}
