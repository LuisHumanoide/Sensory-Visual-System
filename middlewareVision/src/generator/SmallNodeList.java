/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;

/**
 *
 * @author HumanoideFilms
 */
public class SmallNodeList extends javax.swing.JFrame {

    JCheckBox[] boxArray;
    JButton saveButton;

    /**
     * Creates new form SmallNodeList
     */
    public SmallNodeList() {
        initComponents();

        saveButton = new JButton("save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        ProcessList.openList();
        TreeMap<String, Object> tmap=new TreeMap();
        tmap.putAll(ProcessList.ProcessMap);

        boxArray = new JCheckBox[tmap.size()];
        setSize(300, tmap.size() * 30);
        this.add(Box.createRigidArea(new Dimension(20, 20)));

        int i = 0;
        for (String key : tmap.keySet()) {
            boxArray[i] = new JCheckBox(key, (boolean) tmap.get(key));
            this.add(boxArray[i]);
            i++;
        }

        this.add(Box.createRigidArea(new Dimension(20, 20)));
        this.add(saveButton);
    }
    
    

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        for(int i=0;i<boxArray.length;i++){
            ProcessList.setValue(boxArray[i].getText(), boxArray[i].isSelected());
        }
        ProcessList.saveProcessList();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Small Nodes List");
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SmallNodeList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SmallNodeList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SmallNodeList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SmallNodeList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SmallNodeList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
