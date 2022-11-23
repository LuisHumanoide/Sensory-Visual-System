/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.pow;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import matrix.matrix;
import middlewareVision.config.XMLReader;
import org.math.plot.Plot3DPanel;
import org.math.plot.plots.Plot;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.Convertor;
import utils.FileUtils;
import utils.Scalr;
import utils.SpecialKernels;

/**
 *
 * @author Laptop
 */
public class GaborFilterVisualizer extends javax.swing.JFrame {

    /**
     * Creates new form GaborFilterVisualizer
     */
    double zoom = 1;
    Mat gaborFilter;
    String fileName = "ConfigFiles/GaborValues.txt";
    String originalImageFile = XMLReader.getValue("filterEditorImage");
    BufferedImage imageFile;
    BufferedImage filteredImage;
    int w = XMLReader.getIntValue("filterImageWidth");
    int h = XMLReader.getIntValue("filterImageHeight");
    
    public Plot3DPanel plot;

    double[] x;
    double[] y;
    double[][] z;
    
    /**
     * Constructor
     */
    public GaborFilterVisualizer() {
        initComponents();
        modifyLabel();
        File file = new File(fileName);
        String fileContent = FileUtils.readFile(file);
        String values[] = fileContent.split(" ");
        
        kernelSize.setText(values[0]);
        sigmaField.setText(values[1]);
        lambdaField.setText(values[2]);
        gammaField.setText(values[3]);
        psiField.setText(values[4]);
        thetaField.setText(values[5]);
        angleField.setText(values[6]);
        
        plot = new Plot3DPanel();
        visualize();
        convolvedImage.setText("");
        try {
            BufferedImage bi = ImageIO.read(new File(originalImageFile));
            imageFile = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, w, h);
            imageFile = convertType(imageFile, BufferedImage.TYPE_3BYTE_BGR);
            originalImage.setIcon(new ImageIcon(imageFile));
            originalImage.setText("");
        } catch (IOException ex) {
            Logger.getLogger(GaborFilterVisualizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        convolution();           
    }

    /**
     * Create the double array with z values of the Gabor filter
     */
    public void makeZValues() {
        matrix m = Convertor.MatToMatrix(gaborFilter);
        x = new double[m.getWidth()];
        y = new double[m.getHeight()];
        z = new double[m.getWidth()][m.getHeight()];
        for (int i = 0; i < x.length; i++) {
            x[i] = i;
        }
        for (int i = 0; i < y.length; i++) {
            y[i] = i;
        }
        for (int i = 0; i < m.getWidth(); i++) {
            for (int j = 0; j < m.getHeight(); j++) {
                z[j][i] = m.getValue(i, m.getHeight()-j-1);
            }
        }
    }

    /**
     * Update the Gabor 3D Plot
     */
    public void updatePlot() {
        makeZValues();
        plot.removeAllPlots();
        plot.addGridPlot("Gabor plot", x, y, z);
        plot.setFixedBounds(2, -1, 1);
        plot.setFixedBounds(0,0,gaborFilter.width());
        plot.setFixedBounds(1,0,gaborFilter.height());
        framePlot.setContentPane(plot);
        framePlot.repaint();
    }


    /**
     * Modify the label of the source image, the program allows to drop images
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
                        imageFile = Scalr.resize(bi, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, w, h);
                        imageFile = convertType(imageFile, BufferedImage.TYPE_3BYTE_BGR);
                        originalImage.setIcon(new ImageIcon(imageFile));
                        convolution();
                    }
                } catch (Exception e) {
                }
                return true;
            }

        };
        originalImage.setTransferHandler(th);
    }

    /**
     * Convert the type of the image in order to allow the opencv operations
     *
     * @param eleScreenshot
     * @param type
     * @return
     */
    private BufferedImage convertType(BufferedImage eleScreenshot, int type) {
        BufferedImage bi = new BufferedImage(eleScreenshot.getWidth(), eleScreenshot.getHeight(), type);
        Graphics g = bi.getGraphics();
        g.drawImage(eleScreenshot, 0, 0, null);
        g.dispose();
        return bi;
    }

    /**
     * Performs the convolution between the source image and the Gabor filter
     */
    void convolution() {
        Mat mImage = Convertor.bufferedImageToMat(imageFile);
        Mat fImage = new Mat();
        Imgproc.filter2D(mImage, fImage, CV_32F, gaborFilter);
        filteredImage = Convertor.Mat2Img2(fImage);
        convolvedImage.setIcon(new ImageIcon(filteredImage));
        saveValues();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        kernelSize = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sigmaField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        psiField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lambdaField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        gammaField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        thetaField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        angleField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jSlider1 = new javax.swing.JSlider();
        jLabel13 = new javax.swing.JLabel();
        filterSum = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        filterImage = new javax.swing.JLabel();
        jInternalFrame2 = new javax.swing.JInternalFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        originalImage = new javax.swing.JLabel();
        jInternalFrame3 = new javax.swing.JInternalFrame();
        jScrollPane3 = new javax.swing.JScrollPane();
        convolvedImage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        framePlot = new javax.swing.JInternalFrame();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gabor Visualization");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(76, 76, 76));
        jPanel2.setLayout(null);

        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Use UP and DOWN arrows to increase or decrease values");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(6, 6, 383, 16);

        jLabel1.setForeground(new java.awt.Color(204, 204, 204));
        jLabel1.setText("Kernel Size");
        jPanel2.add(jLabel1);
        jLabel1.setBounds(6, 28, 67, 16);

        kernelSize.setToolTipText("");
        kernelSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kernelSizeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                kernelSizeKeyReleased(evt);
            }
        });
        jPanel2.add(kernelSize);
        kernelSize.setBounds(91, 28, 63, 24);

        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("σ:");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(160, 28, 11, 16);

        sigmaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sigmaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sigmaFieldKeyReleased(evt);
            }
        });
        jPanel2.add(sigmaField);
        sigmaField.setBounds(177, 28, 76, 24);

        jLabel5.setForeground(new java.awt.Color(204, 204, 204));
        jLabel5.setText("Φ:");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(453, 28, 18, 16);

        psiField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                psiFieldActionPerformed(evt);
            }
        });
        psiField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                psiFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                psiFieldKeyReleased(evt);
            }
        });
        jPanel2.add(psiField);
        psiField.setBounds(477, 28, 75, 24);

        jLabel3.setForeground(new java.awt.Color(204, 204, 204));
        jLabel3.setText("λ:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(259, 28, 10, 16);

        lambdaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lambdaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lambdaFieldKeyReleased(evt);
            }
        });
        jPanel2.add(lambdaField);
        lambdaField.setBounds(275, 28, 75, 24);

        jLabel4.setForeground(new java.awt.Color(204, 204, 204));
        jLabel4.setText("γ:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(356, 28, 10, 16);

        gammaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                gammaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                gammaFieldKeyReleased(evt);
            }
        });
        jPanel2.add(gammaField);
        gammaField.setBounds(372, 28, 75, 24);

        jLabel6.setForeground(new java.awt.Color(204, 204, 204));
        jLabel6.setText("θ:");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(558, 28, 10, 16);

        thetaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                thetaFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                thetaFieldKeyReleased(evt);
            }
        });
        jPanel2.add(thetaField);
        thetaField.setBounds(574, 28, 75, 24);

        jLabel12.setForeground(new java.awt.Color(204, 204, 204));
        jLabel12.setText("Kernel Angle:");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(6, 62, 75, 16);

        angleField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                angleFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                angleFieldKeyReleased(evt);
            }
        });
        jPanel2.add(angleField);
        angleField.setBounds(93, 58, 61, 24);

        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setText("Zoom filter:");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(6, 100, 64, 16);

        jSlider1.setMinimum(10);
        jSlider1.setValue(1);
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider1MouseDragged(evt);
            }
        });
        jPanel2.add(jSlider1);
        jSlider1.setBounds(82, 100, 240, 16);

        jLabel13.setForeground(new java.awt.Color(204, 204, 204));
        jLabel13.setText("Filter sum:");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(370, 100, 59, 16);

        filterSum.setForeground(new java.awt.Color(156, 198, 196));
        filterSum.setText("[]");
        jPanel2.add(filterSum);
        filterSum.setBounds(440, 100, 80, 16);

        jButton1.setBackground(new java.awt.Color(51, 56, 59));
        jButton1.setForeground(new java.awt.Color(204, 204, 204));
        jButton1.setText("Visualize");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(10, 140, 81, 32);

        jButton3.setBackground(new java.awt.Color(51, 56, 59));
        jButton3.setForeground(new java.awt.Color(204, 204, 204));
        jButton3.setText("Convolution");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);
        jButton3.setBounds(90, 140, 96, 32);

        jButton4.setBackground(new java.awt.Color(51, 56, 59));
        jButton4.setForeground(new java.awt.Color(204, 204, 204));
        jButton4.setText("Copy Values");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);
        jButton4.setBounds(290, 140, 100, 32);

        jButton5.setBackground(new java.awt.Color(51, 56, 59));
        jButton5.setForeground(new java.awt.Color(204, 204, 204));
        jButton5.setText("Paste");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);
        jButton5.setBounds(390, 140, 63, 32);

        jButton2.setBackground(new java.awt.Color(51, 56, 59));
        jButton2.setForeground(new java.awt.Color(204, 204, 204));
        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(520, 140, 80, 32);

        jButton6.setBackground(new java.awt.Color(51, 56, 59));
        jButton6.setForeground(new java.awt.Color(204, 204, 204));
        jButton6.setText("LatexCopy");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6);
        jButton6.setBounds(610, 140, 90, 32);

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 730, 180));

        jPanel3.setBackground(new java.awt.Color(51, 51, 51));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jInternalFrame1.setForeground(new java.awt.Color(102, 102, 102));
        jInternalFrame1.setTitle("Filter");
        jInternalFrame1.setAutoscrolls(true);
        jInternalFrame1.setDoubleBuffered(true);
        jInternalFrame1.setVisible(true);

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        filterImage.setText("[]");
        jScrollPane1.setViewportView(filterImage);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
        );

        jPanel3.add(jInternalFrame1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 140));

        jInternalFrame2.setResizable(true);
        jInternalFrame2.setTitle("Original Image");
        jInternalFrame2.setVisible(true);

        originalImage.setText("[]");
        jScrollPane2.setViewportView(originalImage);

        javax.swing.GroupLayout jInternalFrame2Layout = new javax.swing.GroupLayout(jInternalFrame2.getContentPane());
        jInternalFrame2.getContentPane().setLayout(jInternalFrame2Layout);
        jInternalFrame2Layout.setHorizontalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
        );
        jInternalFrame2Layout.setVerticalGroup(
            jInternalFrame2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
        );

        jPanel3.add(jInternalFrame2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 280, 290));

        jInternalFrame3.setIconifiable(true);
        jInternalFrame3.setResizable(true);
        jInternalFrame3.setTitle("Convolved Image");
        jInternalFrame3.setAutoscrolls(true);
        jInternalFrame3.setVisible(true);

        convolvedImage.setText("[]");
        jScrollPane3.setViewportView(convolvedImage);

        javax.swing.GroupLayout jInternalFrame3Layout = new javax.swing.GroupLayout(jInternalFrame3.getContentPane());
        jInternalFrame3.getContentPane().setLayout(jInternalFrame3Layout);
        jInternalFrame3Layout.setHorizontalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
        );
        jInternalFrame3Layout.setVerticalGroup(
            jInternalFrame3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
        );

        jPanel3.add(jInternalFrame3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 0, 300, 290));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 730, 300));

        jPanel4.setBackground(new java.awt.Color(29, 29, 29));
        jPanel4.setLayout(null);

        framePlot.setTitle("3D Gabor Filter Plot");
        framePlot.setVisible(true);

        javax.swing.GroupLayout framePlotLayout = new javax.swing.GroupLayout(framePlot.getContentPane());
        framePlot.getContentPane().setLayout(framePlotLayout);
        framePlotLayout.setHorizontalGroup(
            framePlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        framePlotLayout.setVerticalGroup(
            framePlotLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );

        jPanel4.add(framePlot);
        framePlot.setBounds(0, 0, 388, 383);

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 0, 390, 480));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void psiFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_psiFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_psiFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        visualize();
        saveValues();
    }//GEN-LAST:event_jButton1ActionPerformed
    BufferedImage fimg;

    /**
     * Visualize the Gabor filter
     */
    public void visualize() {
        gaborFilter = new Mat();
        int kSize = Integer.parseInt(kernelSize.getText());
        double sigma = Double.parseDouble(sigmaField.getText());
        double lambda = Double.parseDouble(lambdaField.getText());
        double gamma = Double.parseDouble(gammaField.getText());
        double psi = Double.parseDouble(psiField.getText());
        double theta = Double.parseDouble(thetaField.getText());
        double angle = Double.parseDouble(angleField.getText());
        gaborFilter = Imgproc.getGaborKernel(new Size(kSize, kSize), sigma, theta, lambda, gamma, psi, CvType.CV_32F);
        gaborFilter = SpecialKernels.rotateKernel(gaborFilter, angle);
        filterSum.setText(String.format("%.3f", Core.sumElems(gaborFilter).val[0]));
        loadImageFilter();
        updatePlot();
    }

    /**
     * Modify the size of the Gabor filter image
     *
     * @param evt
     */
    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseDragged
        // TODO add your handling code here:
        zoom = jSlider1.getValue() * 0.1;
        loadImageFilter();
    }//GEN-LAST:event_jSlider1MouseDragged

    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Make the increment or decrement using the UP and DOWN key
     *
     * @param field
     * @param evt
     * @param value
     */
    void incDec(JTextField field, KeyEvent evt, double inc) {
        //df.setRoundingMode(RoundingMode.DOWN);
        double value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Double.parseDouble(field.getText());
                value = value + inc;
                field.setText(String.format("%.3f", value));
                visualize();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Double.parseDouble(field.getText());
                value = value - inc;
                field.setText(String.format("%.3f", value));
                visualize();
            }
        } catch (Exception ex) {

        }
    }

    private void sigmaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmaFieldKeyPressed
        // TODO add your handling code here:
        incDec(sigmaField, evt, 0.1);
    }//GEN-LAST:event_sigmaFieldKeyPressed

    private void lambdaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdaFieldKeyPressed
        // TODO add your handling code here:
        incDec(lambdaField, evt, 0.001);
    }//GEN-LAST:event_lambdaFieldKeyPressed

    private void gammaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammaFieldKeyPressed
        // TODO add your handling code here:
        incDec(gammaField, evt, 0.01);
    }//GEN-LAST:event_gammaFieldKeyPressed

    private void psiFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psiFieldKeyPressed
        // TODO add your handling code here:
        incDec(psiField, evt, 0.1);
    }//GEN-LAST:event_psiFieldKeyPressed

    private void thetaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetaFieldKeyPressed
        // TODO add your handling code here:
        incDec(thetaField, evt, 0.01);
    }//GEN-LAST:event_thetaFieldKeyPressed

    private void kernelSizeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kernelSizeKeyPressed
        // TODO add your handling code here:
        int value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Integer.parseInt(kernelSize.getText());
                value = value + 1;
                kernelSize.setText("" + value);
                visualize();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Integer.parseInt(kernelSize.getText());
                value = value - 1;
                kernelSize.setText("" + value);
                visualize();
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_kernelSizeKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        saveValues();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        convolution();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void kernelSizeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kernelSizeKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_kernelSizeKeyReleased

    private void sigmaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sigmaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sigmaFieldKeyReleased

    private void lambdaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lambdaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_lambdaFieldKeyReleased

    private void gammaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_gammaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_gammaFieldKeyReleased

    private void psiFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_psiFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_psiFieldKeyReleased

    private void thetaFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_thetaFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_thetaFieldKeyReleased

    private void angleFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_angleFieldKeyPressed
        // TODO add your handling code here:
        incDec(angleField, evt, 5);
    }//GEN-LAST:event_angleFieldKeyPressed

    private void angleFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_angleFieldKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_angleFieldKeyReleased

    /**
     * Copy the Gabor values in order to paste into the Gabor filter list
     *
     * @param evt
     */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String cString = "♦♣♠ " + kernelSize.getText() + " " + sigmaField.getText() + " " + lambdaField.getText() + " "
                + gammaField.getText() + " " + psiField.getText() + " " + thetaField.getText();
        StringSelection stringSelection = new StringSelection(cString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        paste();
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * Copy the Gabor filter values for be used in a latex document
     *
     * @param evt
     */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String cString = "\\sigma=" + String.format("%.2f", Float.parseFloat(sigmaField.getText())) + ",\\lambda=" + String.format("%.2f", Float.parseFloat(lambdaField.getText()))
                + ",\\gamma=" + String.format("%.2f", Float.parseFloat(gammaField.getText()));
        StringSelection stringSelection = new StringSelection(cString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * Paste values from the Gabor list
     */
    void paste() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);

        DataFlavor dataFlavorStringJava;
        try {
            dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
            if (t.isDataFlavorSupported(dataFlavorStringJava)) {
                String texto = (String) t.getTransferData(dataFlavorStringJava);
                if (texto.contains("♦♣♠")) {
                    String values[] = texto.split(" ");
                    kernelSize.setText(values[1]);
                    sigmaField.setText(values[2]);
                    lambdaField.setText(values[3]);
                    gammaField.setText(values[4]);
                    psiField.setText(values[5]);
                    thetaField.setText(values[6]);
                    visualize();
                    convolution();
                    saveValues();
                }
            }
        } catch (Exception ex) {

        }
    }

    /**
     * Load the image filter in toe filterImage label
     */
    void loadImageFilter() {
        fimg = Convertor.ConvertMat2FilterImage(gaborFilter);
        fimg = Scalr.resize(fimg, (int)(Integer.parseInt(kernelSize.getText()) * zoom));
        filterImage.setText("");
        filterImage.setIcon(new ImageIcon(fimg));
        
        //saveValues();
    }

    /**
     * Save the Gabor filter values in a file
     */
    void saveValues() {
        String sValues = kernelSize.getText() + " " + sigmaField.getText() + " " + lambdaField.getText() + " "
                + gammaField.getText() + " " + psiField.getText() + " " + thetaField.getText() + " " + angleField.getText();
        FileUtils.write(fileName.replaceAll(".txt", ""), sValues, "txt");
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
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GaborFilterVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GaborFilterVisualizer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField angleField;
    private javax.swing.JLabel convolvedImage;
    private javax.swing.JLabel filterImage;
    private javax.swing.JLabel filterSum;
    private javax.swing.JInternalFrame framePlot;
    private javax.swing.JTextField gammaField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JInternalFrame jInternalFrame2;
    private javax.swing.JInternalFrame jInternalFrame3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTextField kernelSize;
    private javax.swing.JTextField lambdaField;
    private javax.swing.JLabel originalImage;
    private javax.swing.JTextField psiField;
    private javax.swing.JTextField sigmaField;
    private javax.swing.JTextField thetaField;
    // End of variables declaration//GEN-END:variables
}
