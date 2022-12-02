/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import middlewareVision.config.XMLReader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import utils.Convertor;
import utils.FileUtils;
import utils.Functions;
import utils.MatrixUtils;
import utils.Scalr;
import utils.SpecialKernels;
import utils.GaborFilter;

/**
 *
 * @author Laptop
 */
public class CurvatureRF extends javax.swing.JFrame {

    JTextField[] fields;
    GaborFilter mainGabor;
    int radius;
    double angleDisp;
    double kernelRotation;
    Mat concaveFilters[];
    Mat convexFilters[];
    Mat composedFilter;
    int numberFilters;
    double mulFactor = 1;
    BufferedImage fimg;
    String folder = "RFV2/Curvature/";
    String originalImageFile = XMLReader.getValue("curvatureEditorImage");
    BufferedImage imageFile;
    BufferedImage filteredImage;
    int w = XMLReader.getIntValue("curvatureImageWidth");
    int h = XMLReader.getIntValue("curvatureImageHeight");

    /**
     * Creates new form NewJFrame
     */
    public CurvatureRF() {
        initComponents();
        loadFields();
        loadFieldList();
        modifyLabel();
        mainGabor = new GaborFilter();

        try {
            BufferedImage bi = ImageIO.read(new File(originalImageFile));
            imageFile = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, w, h);
            imageFile = convertType(imageFile, BufferedImage.TYPE_3BYTE_BGR);
            originalImage.setIcon(new ImageIcon(imageFile));
            originalImage.setText("");
        } catch (IOException ex) {
            Logger.getLogger(GaborFilterVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Change the image type in order to be acepted by the OpenCV algorithms
     * @param eleScreenshot screenshot of the image
     * @param type type
     * @return a image with a new type
     */
    private BufferedImage convertType(BufferedImage eleScreenshot, int type) {
        BufferedImage bi = new BufferedImage(eleScreenshot.getWidth(), eleScreenshot.getHeight(), type);
        Graphics g = bi.getGraphics();
        g.drawImage(eleScreenshot, 0, 0, null);
        g.dispose();
        return bi;
    }

    /**
     * Method for drag and drop images from the computer
     */
    public void modifyLabel() {
        TransferHandler th = new TransferHandler() {

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
                return true;
            }

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.size() == 1) {
                        File f = files.get(0);
                        originalImageFile = f.toString();
                        BufferedImage bi = ImageIO.read(new File(originalImageFile));
                        imageFile = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 200, 200);
                        imageFile = convertType(imageFile, BufferedImage.TYPE_3BYTE_BGR);
                        originalImage.setIcon(new ImageIcon(imageFile));
                    }
                } catch (Exception e) {
                }
                return true;
            }

        };
        originalImage.setTransferHandler(th);
    }

    /**
     * Load the values from a file to each J Text Field
     */
    void loadFieldList() {
        DefaultListModel model = new DefaultListModel();
        String fileNames[] = FileUtils.getFiles(folder);
        for (String fileName : fileNames) {
            model.addElement(fileName.substring(folder.length(), fileName.length()).replaceAll(".txt", ""));
        }
        jList1.setModel(model);
    }

    /**
     * Every field is mapped to an field array for making the things more easy
     */
    void loadFields() {
        fields = new JTextField[11];
        fields[0] = sizef;
        fields[1] = sigmaf;
        fields[2] = lambdaf;
        fields[3] = gammaf;
        fields[4] = psif;
        fields[5] = thetaf;
        fields[6] = radiusf;
        fields[7] = anglef;
        fields[8] = rotf;
        fields[9] = nfiltersf;
        fields[10] = mulf;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        rotf = new javax.swing.JTextField();
        sizef = new javax.swing.JTextField();
        sigmaf = new javax.swing.JTextField();
        lambdaf = new javax.swing.JTextField();
        gammaf = new javax.swing.JTextField();
        psif = new javax.swing.JTextField();
        thetaf = new javax.swing.JTextField();
        radiusf = new javax.swing.JTextField();
        anglef = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        namef = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel14 = new javax.swing.JLabel();
        nfiltersf = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        mulf = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        filterImage = new javax.swing.JLabel();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        originalImage = new javax.swing.JLabel();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jScrollPane4 = new javax.swing.JScrollPane();
        convolvedImage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Curvature editor");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(86, 83, 83));
        jPanel1.setLayout(null);

        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Gabor filter");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(20, 10, 63, 16);

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Curvature parameters");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(350, 10, 126, 16);

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Size");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(20, 40, 24, 16);

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("kernel rotation");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(470, 40, 83, 16);

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("σ");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(70, 40, 8, 16);

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("λ");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(120, 40, 7, 16);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("γ");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(170, 40, 7, 16);

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Φ");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(220, 40, 30, 16);

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("θ");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(270, 40, 29, 16);

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("radius");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(350, 40, 36, 16);

        rotf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rotfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                rotfKeyReleased(evt);
            }
        });
        jPanel1.add(rotf);
        rotf.setBounds(470, 60, 80, 24);

        sizef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sizefKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sizefKeyReleased(evt);
            }
        });
        jPanel1.add(sizef);
        sizef.setBounds(20, 60, 50, 24);

        sigmaf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sigmafKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sigmafKeyReleased(evt);
            }
        });
        jPanel1.add(sigmaf);
        sigmaf.setBounds(70, 60, 50, 24);

        lambdaf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lambdafKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lambdafKeyReleased(evt);
            }
        });
        jPanel1.add(lambdaf);
        lambdaf.setBounds(120, 60, 50, 24);

        gammaf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                gammafKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                gammafKeyReleased(evt);
            }
        });
        jPanel1.add(gammaf);
        gammaf.setBounds(170, 60, 50, 24);

        psif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                psifKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                psifKeyReleased(evt);
            }
        });
        jPanel1.add(psif);
        psif.setBounds(220, 60, 50, 24);

        thetaf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thetafKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                thetafKeyReleased(evt);
            }
        });
        jPanel1.add(thetaf);
        thetaf.setBounds(270, 60, 50, 24);

        radiusf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                radiusfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                radiusfKeyReleased(evt);
            }
        });
        jPanel1.add(radiusf);
        radiusf.setBounds(350, 60, 50, 24);

        anglef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                anglefKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                anglefKeyReleased(evt);
            }
        });
        jPanel1.add(anglef);
        anglef.setBounds(400, 60, 60, 24);

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Δθ");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(400, 40, 15, 16);

        jButton1.setText("Generate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(710, 60, 80, 32);

        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Name:");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(140, 100, 36, 16);
        jPanel1.add(namef);
        namef.setBounds(190, 100, 130, 24);

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(350, 100, 110, 32);

        jButton3.setText("Paste");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(710, 100, 80, 32);
        jPanel1.add(jSeparator1);
        jSeparator1.setBounds(20, 90, 770, 10);
        jPanel1.add(jSeparator2);
        jSeparator2.setBounds(10, 30, 780, 10);
        jPanel1.add(jSeparator3);
        jSeparator3.setBounds(130, 130, 660, 10);

        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("n filters");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(570, 40, 43, 16);

        nfiltersf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nfiltersfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nfiltersfKeyReleased(evt);
            }
        });
        jPanel1.add(nfiltersf);
        nfiltersf.setBounds(570, 60, 70, 24);

        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("mul factor");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(650, 40, 58, 16);

        mulf.setText("1");
        mulf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mulfKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mulfKeyReleased(evt);
            }
        });
        jPanel1.add(mulf);
        mulf.setBounds(650, 60, 50, 24);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 300, 820, 150));

        jPanel2.setBackground(new java.awt.Color(73, 71, 71));
        jPanel2.setLayout(null);

        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Files");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 20, 60, 16);

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 120, 300);

        jButton4.setText("Delete");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);
        jButton4.setBounds(10, 350, 100, 32);

        jButton5.setText("Open Folder");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);
        jButton5.setBounds(10, 390, 100, 32);

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 450));

        jPanel3.setBackground(new java.awt.Color(108, 108, 108));
        jPanel3.setLayout(null);

        jInternalFrame1.setResizable(true);
        jInternalFrame1.setTitle("Filter");
        jInternalFrame1.setVisible(true);

        filterImage.setText("[]");
        jScrollPane2.setViewportView(filterImage);

        jInternalFrame1.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel3.add(jInternalFrame1);
        jInternalFrame1.setBounds(0, 0, 160, 150);

        jInternalFrame2.setResizable(true);
        jInternalFrame2.setTitle("Original Image");
        jInternalFrame2.setVisible(true);

        originalImage.setText("[]");
        jScrollPane3.setViewportView(originalImage);

        jInternalFrame2.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel3.add(jInternalFrame2);
        jInternalFrame2.setBounds(180, 0, 290, 290);

        jInternalFrame3.setResizable(true);
        jInternalFrame3.setTitle("Convolved Image");
        jInternalFrame3.setVisible(true);

        convolvedImage.setText("[]");
        jScrollPane4.setViewportView(convolvedImage);

        jInternalFrame3.getContentPane().add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel3.add(jInternalFrame3);
        jInternalFrame3.setBounds(490, 0, 290, 290);

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 820, 300));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Paste values from the Gabor Filter Visualizer
     *
     * @param evt
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);

        DataFlavor dataFlavorStringJava;
        try {
            dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
            if (t.isDataFlavorSupported(dataFlavorStringJava)) {
                String texto = (String) t.getTransferData(dataFlavorStringJava);
                if (texto.contains("♦♣♠")) {
                    String values[] = texto.split(" ");
                    for (int i = 0; i < 6; i++) {
                        fields[i].setText(values[i + 1]);
                    }
                }
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        generateFilters();
        convolution();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * It makes the gabor filters for the curvature process
     */
    void generateFilters() {
        mainGabor.setParameters(toInt(fields[0].getText()), toDouble(fields[1].getText()),
                toDouble(fields[2].getText()), toDouble(fields[3].getText()), toDouble(fields[4].getText()),
                toDouble(fields[5].getText()));
        radius = toInt(fields[6].getText());
        angleDisp = toDouble(fields[7].getText());
        kernelRotation = toDouble(fields[8].getText());
        numberFilters = toInt(fields[9].getText().trim());
        mulFactor = toDouble(fields[10].getText().trim());
        makeFilters();
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        saveFile(namef.getText());
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * It makes load the values from the file that is clicked
     * @param evt 
     */
    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MouseClicked
        // TODO add your handling code here:
        String content = FileUtils.readFile(new File(folder + jList1.getSelectedValue() + ".txt"));
        String values[] = content.split(" ");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setText(values[i].trim());
        }
        namef.setText(jList1.getSelectedValue());
        generateFilters();
    }//GEN-LAST:event_jList1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        FileUtils.deleteFile(folder + jList1.getSelectedValue() + ".txt");
        loadFieldList();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            // TODO add your handling code here:
            Desktop.getDesktop().open(new File(folder));
        } catch (IOException ex) {
            Logger.getLogger(CurvatureRF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void sizefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sizefKeyPressed
        // TODO add your handling code here:
        incDecInt(sizef, evt, 1);
    }//GEN-LAST:event_sizefKeyPressed

    /**
     * Method for increasing or decreasing the value of the fields when a up or down key is pressed<br>
     * the inc/dec is in double/fractions
     * @param field JTextField
     * @param evt event
     * @param inc increasing value
     */
    void incDec(JTextField field, KeyEvent evt, double inc) {
        //df.setRoundingMode(RoundingMode.DOWN);
        double value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Double.parseDouble(field.getText());
                value = value + inc;
                field.setText(String.format("%.3f", value));
                generateFilters();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Double.parseDouble(field.getText());
                value = value - inc;
                field.setText(String.format("%.3f", value));
                generateFilters();
            }
        } catch (Exception ex) {

        }
    }

    /**
     * Method for increasing or decreading the value of the fields when a up or down key is pressed<br>
     * Increment or decrement with integer values
     * @param field field to increment or decrement the value of
     * @param evt event
     * @param inc value of increasing/decreasing
     */
    void incDecInt(JTextField field, KeyEvent evt, int inc) {
        int value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Integer.parseInt(field.getText());
                value = value + inc;
                field.setText("" + value);
                generateFilters();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Integer.parseInt(field.getText());
                value = value - inc;
                field.setText("" + value);
                generateFilters();
            }
        } catch (Exception ex) {

        }
    }
    private void sigmafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmafKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.1);
    }//GEN-LAST:event_sigmafKeyPressed

    private void lambdafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdafKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.001);
    }//GEN-LAST:event_lambdafKeyPressed

    private void psifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psifKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.1);
    }//GEN-LAST:event_psifKeyPressed

    private void thetafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetafKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.1);
    }//GEN-LAST:event_thetafKeyPressed

    private void radiusfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radiusfKeyPressed
        // TODO add your handling code here:
        incDecInt((JTextField) evt.getComponent(), evt, 1);
    }//GEN-LAST:event_radiusfKeyPressed

    private void anglefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anglefKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.01);
    }//GEN-LAST:event_anglefKeyPressed

    private void rotfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rotfKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.1);
    }//GEN-LAST:event_rotfKeyPressed

    private void nfiltersfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nfiltersfKeyPressed
        // TODO add your handling code here:
        incDecInt((JTextField) evt.getComponent(), evt, 1);
    }//GEN-LAST:event_nfiltersfKeyPressed

    private void sizefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sizefKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_sizefKeyReleased

    private void sigmafKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmafKeyReleased
        // TODO add your handling code here:
        convolution();

    }//GEN-LAST:event_sigmafKeyReleased

    private void lambdafKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdafKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_lambdafKeyReleased

    private void gammafKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammafKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.1);
    }//GEN-LAST:event_gammafKeyPressed

    private void gammafKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammafKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_gammafKeyReleased

    private void psifKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psifKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_psifKeyReleased

    private void thetafKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetafKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_thetafKeyReleased

    private void radiusfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_radiusfKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_radiusfKeyReleased

    private void anglefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_anglefKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_anglefKeyReleased

    private void rotfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rotfKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_rotfKeyReleased

    private void nfiltersfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nfiltersfKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_nfiltersfKeyReleased

    private void mulfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mulfKeyPressed
        // TODO add your handling code here:
        incDec((JTextField) evt.getComponent(), evt, 0.01);
    }//GEN-LAST:event_mulfKeyPressed

    private void mulfKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mulfKeyReleased
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_mulfKeyReleased

    /**
     * Curvature filtering processing<br>
     * This method is also found in the Functions.java class for more information
     * @param angle angle of rotation of the filters
     * @return a Mat with the curvature activation
     */
    Mat filterProcess(double angle) {
        Mat concaveFiltered[];
        Mat convexFiltered[];
        concaveFiltered = new Mat[numberFilters];
        convexFiltered = new Mat[numberFilters];
        Mat src = Convertor.bufferedImageToMat(imageFile);
        Mat concaveResult = Mat.zeros(src.rows(), src.cols(), 21);
        Mat convexResult = Mat.zeros(src.rows(), src.cols(), 21);
        Core.divide(src, Scalar.all(255), src);
        Core.add(concaveResult, Scalar.all(1), concaveResult);
        Core.add(convexResult, Scalar.all(1), convexResult);
        for (int i = 0; i < numberFilters; i++) {
            concaveFiltered[i] = Functions.filter2(src, SpecialKernels.rotateKernelRadians(concaveFilters[i], angle));
            //convexFiltered[i] = Functions.filter(src, SpecialKernels.rotateKernelRadians(convexFilters[i], angle));
            Core.multiply(concaveFiltered[i], Scalar.all(mulFactor), concaveFiltered[i]);
            //Core.multiply(convexFiltered[i], Scalar.all(mulFactor), convexFiltered[i]);
            concaveResult = concaveResult.mul(concaveFiltered[i]);
            //convexResult = convexResult.mul(convexFiltered[i]);
        }
        //Core.subtract(concaveResult, convexResult, concaveResult);
        return concaveResult;
        //convolvedImage.setText("");
        //convolvedImage.setIcon(new ImageIcon(Convertor.Mat2Img(concaveResult)));
    }

    /**
     * Performs the curvature process with several angles<br>
     * and then it makes a summation for archieving the angular invariance<br>
     * with more n steps, the process will be more slow
     */
    void convolution() {
        int n = 18;
        Mat results[] = new Mat[n];
        float inc = (float) (2 * Math.PI / n);
        for (int i = 0; i < n; i++) {
            results[i] = filterProcess(inc * i);
        }
        Mat result = MatrixUtils.maxSum(results);
        convolvedImage.setText("");
        convolvedImage.setIcon(new ImageIcon(Convertor.Mat2Img(result)));
    }

    /**
     * Save the parameters from the fields into a file
     * @param name name of the file filter
     */
    void saveFile(String name) {
        String ac = "";
        for (int i = 0; i < fields.length; i++) {
            if (i < fields.length - 1) {
                ac = ac + fields[i].getText() + " ";
            } else {
                ac = ac + fields[i].getText();
            }
        }
        FileUtils.write(folder + name, ac, "txt");
        loadFieldList();
        jList1.setSelectedIndex(indexOf(namef.getText()));
    }

    /**
     * Index of the file selected from the list of files
     * @param string string to search
     * @return the index of the string
     */
    int indexOf(String string) {
        int index = 0;
        for (int i = 0; i < jList1.getModel().getSize(); i++) {
            if (jList1.getModel().getElementAt(i).equals(string)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * It makes the concave and convex filters, these filters are rotate Gabor filters<br>
     * these filters are applied to make a convolution and the result will be multiplied in the method: filterProcess
     */
    void makeFilters() {
        concaveFilters = new Mat[numberFilters];
        convexFilters = new Mat[numberFilters];
        for (int i = 0; i < numberFilters; i++) {
            concaveFilters[i] = new Mat();
            convexFilters[i] = new Mat();
        }
        Mat mainFilter = mainGabor.makeFilter();
        concaveFilters[0] = mainFilter.clone();
        convexFilters[0] = mainFilter.clone();
        composedFilter = Mat.zeros(concaveFilters[0].height(), concaveFilters[0].width(), CvType.CV_32FC1);
        for (int i = 1; i < numberFilters; i++) {
            if (i % 2 == 0) {
                concaveFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, radius, 0, angleDisp * (i / 3 + 1));
                convexFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, -radius, 0, angleDisp * (i / 3 + 1));
            } else {
                concaveFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, radius, 0, -angleDisp * (i / 3 + 1));
                convexFilters[i] = SpecialKernels.rotateKernelRadians(mainFilter, -radius, 0, -angleDisp * (i / 3 + 1));
            }
        }
        /*for (int i = 0; i < numberFilters; i++) {

            concaveFilters[i] = SpecialKernels.rotateKernelRadians(concaveFilters[i], kernelRotation);
            convexFilters[i] = SpecialKernels.rotateKernelRadians(convexFilters[i], kernelRotation);

        }*/
        makeComposedFilter();
        loadImageFilters();
    }

    /**
     * This method make the composed filter in order to show in the visualizer<br>
     * the composed filter is not used in the curvature process, it's only for visualizing purposes
     */
    void makeComposedFilter() {
        for (int i = 0; i < numberFilters; i++) {
            Core.add(composedFilter, concaveFilters[i], composedFilter);
        }
        /*for (int i = 1; i < numberFilters; i++) {
            Core.add(composedFilter, convexFilters[i], composedFilter);
        }*/
    }

    /**
     * Convert the composed filter into a image to be shown in the label 'filterImage'
     */
    void loadImageFilters() {
        fimg = Convertor.ConvertMat2FilterImage(composedFilter);
        //fimg = Scalr.resize(fimg, Integer.parseInt(kernelSize.getText()) * zoom);
        filterImage.setText("");
        filterImage.setIcon(new ImageIcon(fimg));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
            java.util.logging.Logger.getLogger(CurvatureRF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CurvatureRF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CurvatureRF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CurvatureRF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CurvatureRF().setVisible(true);
            }
        });
    }

    /**
     * Methods for shorten the conversion from string to double
     *
     * @param text
     * @return
     */
    double toDouble(String text) {
        return Double.parseDouble(text);
    }

    /**
     * Method for shorten the conversion from string to integer
     * @param text
     * @return 
     */
    int toInt(String text) {
        return Integer.parseInt(text);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField anglef;
    private javax.swing.JLabel convolvedImage;
    private javax.swing.JLabel filterImage;
    private javax.swing.JTextField gammaf;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField lambdaf;
    private javax.swing.JTextField mulf;
    private javax.swing.JTextField namef;
    private javax.swing.JTextField nfiltersf;
    private javax.swing.JLabel originalImage;
    private javax.swing.JTextField psif;
    private javax.swing.JTextField radiusf;
    private javax.swing.JTextField rotf;
    private javax.swing.JTextField sigmaf;
    private javax.swing.JTextField sizef;
    private javax.swing.JTextField thetaf;
    // End of variables declaration//GEN-END:variables
}
