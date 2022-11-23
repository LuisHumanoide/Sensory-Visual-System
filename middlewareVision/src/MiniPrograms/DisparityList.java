/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package MiniPrograms;

import java.awt.Color;
import java.util.ArrayList;
import org.math.plot.Plot2DPanel;
import utils.FileUtils;
import utils.MathFunctions;

/**
 * GUI for setting the absolute and relative disparity values
 *
 * @author HumanoideFilms
 */
public class DisparityList extends javax.swing.JFrame {
    //2D plot panel
    public Plot2DPanel plot;
    //list of gaussians
    public ArrayList<gaussian> glist;
    //absolute disparities
    String aDisparityName = "ConfigFiles/Disparities";
    //relative disprities
    String rDisparityName = "ConfigFiles/DisparityGaussians";

    /**
     * Creates new form DisparityList
     */
    public DisparityList() {
        initComponents();
        glist = new ArrayList();
        listPanel1.setColumnNames("Disparity (pixels)");
        listPanel1.setFilePath(aDisparityName, "txt");
        listPanel1.disableEditButtons();
        listPanel1.loadFile();

        listPanel2.setColumnNames("range", "center");
        listPanel2.setFilePath(rDisparityName, "txt");
        listPanel2.loadFile();
        
        
        plot = new Plot2DPanel();

        minMax();

        addGaussians();        
        
        /**
         * reload the gaussians when the save button is pressed
         */
        listPanel2.saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGaussians();
            }
        });
        
        listPanel2.table.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateGaussians();
            }
        });
        
        listPanel2.removeButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updateGaussians();
            }
        });
    }

    /**
     * variables used to store the min and max value from the absolute disparity
     */
    int min = 0;
    int max = 0;
    //values of the absolute disparity, these values will be used for finding the y values of the gaussian
    int[] xpoints;

    /**
     * Found the minimun and maximun value from the absolute disparities
     */
    public void minMax() {
        //load the disparity file 
        String lines[] = FileUtils.fileLines(aDisparityName + ".txt");
        //the min and max value are the values of the extremes
        min = Integer.parseInt(lines[0]);
        max = Integer.parseInt(lines[lines.length - 1]);
        xpoints = new int[lines.length];
        int c = 0;
        //creates the x points values
        for (String line : lines) {
            xpoints[c] = Integer.parseInt(line);
        }
        //if the min value is the maximun, then a swap process is performed
        if (max < min) {
            int temp = max;
            max = min;
            min = temp;
        }
        setXpoints();
    }

    /**
     * Set the x points or the absolute disparity points to the visualizer
     */
    public void setXpoints() {
        String lines[] = FileUtils.fileLines(aDisparityName + ".txt");
        xpoints = new int[lines.length];
        int c = 0;
        for (String line : lines) {
            xpoints[c] = Integer.parseInt(line);
            c++;
        }
    }

    /**
     * Update the plot of disparities
     */
    public void updatePlot() {
        
        plot.removeAllPlots();
        int c = 0;
        for (gaussian ga : glist) {
            double f=(double)((double)c/(double)glist.size());
            double xvalues[] = new double[xpoints.length];
            double yvalues[] = new double[xpoints.length];

            double sum = 0;

            for (int i = 0; i < xpoints.length; i++) {
                xvalues[i] = i;
                yvalues[i] = (-MathFunctions.Gauss(ga.a, ga.b, 1, xpoints[i]));
                sum = sum + MathFunctions.Gauss(ga.a, ga.b, ga.m, xpoints[i]);
            }
            //performs a normalization
            for (int i = 0; i < xpoints.length; i++) {
                yvalues[i] = MathFunctions.Gauss(ga.a, ga.b, (double) (1 / sum), xpoints[i]);
            }
            plot.addLinePlot("Plot " + c, new Color((int)(f*255),(int)(1.5*f*255)%255,255-(int)(f*255)),xvalues, yvalues);          
            c++;
        }
        
        plotFrame.setContentPane(plot);
        plotFrame.repaint();

    }

    /**
     * Add the gaussian filters to the glist, from the file
     */
    public void addGaussians() {
        glist.clear();
        String lines[] = FileUtils.fileLines(rDisparityName + ".txt");
        for (String line : lines) {
            String values[] = line.split(" ");
            double a = Double.parseDouble(values[0]);
            double b = Double.parseDouble(values[1]);
            addGaussian(a, b);
        }
        updatePlot();
    }
    
    /**
     * Add the gaussian filters to the glist, from the table
     */
    public void updateGaussians() {
        glist.clear();
        for (int i = 0; i < listPanel2.table.getRowCount(); i++) {
            if (listPanel1.CompleteRow(i)) {
                addGaussian(Double.parseDouble((String) listPanel2.table.getValueAt(i, 0)), Double.parseDouble((String) listPanel2.table.getValueAt(i, 1)));
            }
        }
        updatePlot();
    }



    /**
     * Add a gaussian that has 2 values
     *
     * @param a the deviation
     * @param b the center
     */
    public void addGaussian(double a, double b) {
        glist.add(new gaussian(a, b, 1));
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
        plotFrame = new javax.swing.JInternalFrame();

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

        plotFrame.setTitle("Disparity plot");
        plotFrame.setVisible(true);

        javax.swing.GroupLayout plotFrameLayout = new javax.swing.GroupLayout(plotFrame.getContentPane());
        plotFrame.getContentPane().setLayout(plotFrameLayout);
        plotFrameLayout.setHorizontalGroup(
            plotFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );
        plotFrameLayout.setVerticalGroup(
            plotFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addComponent(plotFrame)
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
                    .addComponent(plotFrame)
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

    /**
     * Generate the range of absolute disparities
     *
     * @param evt
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int from = Integer.parseInt(fromField.getText());
        int to = Integer.parseInt(toField.getText());
        int steps = Integer.parseInt(stepsField.getText());
        listPanel1.removeAllRows();
        //if TO is greater than FROM
        if (from < to) {
            int step = (to - from) / steps;
            for (int i = from; i <= to; i += step) {
                Object values[] = {i};
                listPanel1.setRow(values);
            }
            //if FROM is greater than TO
        } else {
            int step = (from - to) / steps;
            for (int i = to; i <= from; i += step) {
                Object values[] = {i};
                listPanel1.setRow(values);
            }
        }
        //save the values
        listPanel1.save();
        //update the gaussian plot
        addGaussians();
        updatePlot();
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
    private javax.swing.JTextField fromField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private gui.components.ListPanel listPanel1;
    private gui.components.ListPanel listPanel2;
    private javax.swing.JInternalFrame plotFrame;
    private javax.swing.JTextField stepsField;
    private javax.swing.JTextField toField;
    // End of variables declaration//GEN-END:variables
}

class gaussian {

    double a;
    double b;
    double m;
    /**
     * Make a abstraction of a gaussian
     * @param a Amplitude
     * @param b Center
     * @param m 
     */
    public gaussian(double a, double b, double m) {
        this.a = a;
        this.b = b;
        this.m = m;
    }
}
