/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package MiniPrograms;

import java.io.File;
import utils.FileUtils;
import utils.MathFunctions;

/**
 *
 * @author HumanoideFilms
 */
public class DisparityList extends javax.swing.JFrame {

    /**
     * Creates new form DisparityList
     */
    public DisparityList() {
        initComponents();
        listPanel1.setColumnNames("Disparity (pixels)");
        listPanel1.setFilePath("Disparities", "txt");
        listPanel1.disableEditButtons();
        listPanel1.loadFile();

        listPanel2.setColumnNames("range", "center");
        listPanel2.setFilePath("Gaussians", "txt");
        listPanel2.loadFile();

        minMax();
        addGaussians();
        disparityPanel1.repaint();

        listPanel2.saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGaussians();
            }
        });
    }

    int min = 0;
    int max = 0;
    int[] xpoints;

    public void minMax() {
        String lines[] = FileUtils.fileLines("Disparities.txt");
        min = Integer.parseInt(lines[0]);
        max = Integer.parseInt(lines[lines.length - 1]);
        xpoints = new int[lines.length];
        int c = 0;
        for (String line : lines) {
            xpoints[c] = Integer.parseInt(line);
        }
        if (max < min) {
            int temp = max;
            max = min;
            min = temp;
        }
        setXpoints();
        disparityPanel1.setXpoints(xpoints);
    }

    public void setXpoints() {
        String lines[] = FileUtils.fileLines("Disparities.txt");
        xpoints = new int[lines.length];
        int c = 0;
        for (String line : lines) {
            xpoints[c] = Integer.parseInt(line);
            c++;
        }
    }

    public void addGaussians() {
        disparityPanel1.glist.clear();
        String lines[] = FileUtils.fileLines("Gaussians.txt");
        for (String line : lines) {
            String values[] = line.split(" ");
            double a = Double.parseDouble(values[0]);
            double b = Double.parseDouble(values[1]);
            disparityPanel1.addGaussian(a, b);
        }
        disparityPanel1.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        listPanel1 = new gui.components.ListPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fromField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        toField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        stepsField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        listPanel2 = new gui.components.ListPanel();
        disparityPanel1 = new gui.components.DisparityPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Disparity List");

        jLabel1.setText("Generate disparities");

        jLabel2.setText("From:");

        jLabel3.setText("to:");

        jLabel4.setText("steps:");

        jButton1.setText("Generate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout disparityPanel1Layout = new javax.swing.GroupLayout(disparityPanel1);
        disparityPanel1.setLayout(disparityPanel1Layout);
        disparityPanel1Layout.setHorizontalGroup(
            disparityPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 566, Short.MAX_VALUE)
        );
        disparityPanel1Layout.setVerticalGroup(
            disparityPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fromField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(toField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(stepsField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(listPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(listPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disparityPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(listPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(listPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(disparityPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(toField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stepsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int from = Integer.parseInt(fromField.getText());
        int to = Integer.parseInt(toField.getText());
        int steps = Integer.parseInt(stepsField.getText());
        listPanel1.removeAllRows();
        if (from < to) {
            int step = (to - from) / steps;
            for (int i = from; i <= to; i += step) {
                Object values[] = {i};
                listPanel1.setRow(values);
            }
        } else {
            int step = (from - to) / steps;
            for (int i = to; i <= from; i += step) {
                Object values[] = {i};
                listPanel1.setRow(values);
            }
        }
        listPanel1.save();
        addGaussians();
        disparityPanel1.repaint();
        minMax();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(DisparityList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DisparityList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DisparityList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DisparityList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DisparityList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.components.DisparityPanel disparityPanel1;
    private javax.swing.JTextField fromField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private gui.components.ListPanel listPanel1;
    private gui.components.ListPanel listPanel2;
    private javax.swing.JTextField stepsField;
    private javax.swing.JTextField toField;
    // End of variables declaration//GEN-END:variables
}
