/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MiniPrograms;

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import mapOpener.Convertor;
import org.opencv.core.Core;
import static org.opencv.core.CvType.CV_32F;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.FileUtils;
import utils.Scalr;
import utils.SpecialKernels;

/**
 *
 * @author Laptop
 */
public class GaussianVisualizer extends javax.swing.JFrame {

    /**
     * Creates new form GaussianVisualizer
     */
    String fileName = "ConfigFiles/DoGValues.txt";
    String originalImageFile = "ConfigFiles/Paris.JPG";
    JTextField[] fields;
    Mat DoGFilter;
    BufferedImage fImage;
    BufferedImage imageFile;
    BufferedImage filteredImage;
    double norm1 = 0;
    double sum1 = 0;
    double sum2 = 0;
    int w = 250;
    int h = 250;

    public GaussianVisualizer() {
        initComponents();
        modifyLabel();
        File file = new File(fileName);
        String fileContent = FileUtils.readFile(file);
        String values[] = fileContent.split(" ");
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
        loadFields();
        loadFileValues(values);
        visualize();
        convolution();
    }

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

    private BufferedImage convertType(BufferedImage eleScreenshot, int type) {
        BufferedImage bi = new BufferedImage(eleScreenshot.getWidth(), eleScreenshot.getHeight(), type);
        Graphics g = bi.getGraphics();
        g.drawImage(eleScreenshot, 0, 0, null);
        g.dispose();
        return bi;
    }

    void convolution() {
        Mat mImage = Convertor.bufferedImageToMat(imageFile);
        Mat fImage = new Mat();
        Imgproc.filter2D(mImage, fImage, CV_32F, DoGFilter);
        filteredImage = Convertor.ConvertMat2Image2(fImage);
        convolvedImage.setIcon(new ImageIcon(filteredImage));
        saveValues();
    }

    void summation() {
        Double sum = Core.sumElems(DoGFilter).val[0];
        norm1 = sum;
        sumLabel.setText(String.format("%.3f", sum));
    }

    void loadFields() {
        fields = new JTextField[13];
        fields[0] = sizef;
        fields[1] = A1f;
        fields[2] = x1f;
        fields[3] = y1f;
        fields[4] = sx1f;
        fields[5] = sy1f;
        fields[6] = t1f;
        fields[7] = A2f;
        fields[8] = x2f;
        fields[9] = y2f;
        fields[10] = sx2f;
        fields[11] = sy2f;
        fields[12] = t2f;

    }

    void loadFileValues(String[] values) {
        for (int i = 0; i < 13; i++) {
            fields[i].setText(values[i]);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        filterImage = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        originalImage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        convolvedImage = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        sizef = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        zoomSlider = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        t1f = new javax.swing.JTextField();
        A1f = new javax.swing.JTextField();
        A2f = new javax.swing.JTextField();
        t2f = new javax.swing.JTextField();
        x1f = new javax.swing.JTextField();
        x2f = new javax.swing.JTextField();
        y1f = new javax.swing.JTextField();
        y2f = new javax.swing.JTextField();
        sx1f = new javax.swing.JTextField();
        sx2f = new javax.swing.JTextField();
        sy1f = new javax.swing.JTextField();
        sy2f = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sumLabel = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        copy1 = new javax.swing.JButton();
        copy2 = new javax.swing.JButton();
        paste1 = new javax.swing.JButton();
        paste2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Filter:");
        jPanel3.add(jLabel1);

        filterImage.setText("[]");
        jPanel3.add(filterImage);

        jPanel1.add(jPanel3);

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Original:");
        jPanel5.add(jLabel12);

        originalImage.setText("[]");
        jPanel5.add(originalImage);

        jPanel1.add(jPanel5);

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Convolved:");
        jPanel4.add(jLabel14);

        convolvedImage.setText("[]");
        jPanel4.add(convolvedImage);

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 310));

        jPanel2.setBackground(new java.awt.Color(59, 62, 65));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sizef.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sizefKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sizefKeyReleased(evt);
            }
        });
        jPanel2.add(sizef, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 60, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Size:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jButton1.setText("Visualize");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 10, 94, -1));

        zoomSlider.setMinimum(10);
        zoomSlider.setValue(1);
        zoomSlider.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                zoomSliderMouseDragged(evt);
            }
        });
        jPanel2.add(zoomSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 300, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Zoom");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jButton2.setText("Fill with 0s");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 110, 20));

        jButton3.setText("Copy values");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 60, 110, 20));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("A:");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("x:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, -1, -1));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("y:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 10, -1, -1));

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("σx:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, -1, -1));

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("σy:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, -1, -1));

        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("θ:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        t1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t1fKeyReleased(evt);
            }
        });
        jPanel2.add(t1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 50, -1));

        A1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                A1fKeyReleased(evt);
            }
        });
        jPanel2.add(A1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 48, -1));

        A2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                A2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                A2fKeyReleased(evt);
            }
        });
        jPanel2.add(A2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 48, -1));

        t2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                t2fKeyReleased(evt);
            }
        });
        jPanel2.add(t2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, 50, -1));

        x1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                x1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                x1fKeyReleased(evt);
            }
        });
        jPanel2.add(x1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 50, -1));

        x2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                x2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                x2fKeyReleased(evt);
            }
        });
        jPanel2.add(x2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 50, -1));

        y1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                y1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                y1fKeyReleased(evt);
            }
        });
        jPanel2.add(y1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 50, -1));

        y2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                y2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                y2fKeyReleased(evt);
            }
        });
        jPanel2.add(y2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 50, -1));

        sx1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sx1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sx1fKeyReleased(evt);
            }
        });
        jPanel2.add(sx1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 50, -1));

        sx2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sx2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sx2fKeyReleased(evt);
            }
        });
        jPanel2.add(sx2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 60, 50, -1));

        sy1f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sy1fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sy1fKeyReleased(evt);
            }
        });
        jPanel2.add(sy1f, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 50, -1));

        sy2f.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sy2fKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sy2fKeyReleased(evt);
            }
        });
        jPanel2.add(sy2f, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 50, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Summation:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 50, -1, -1));

        sumLabel.setForeground(new java.awt.Color(204, 255, 255));
        sumLabel.setText("0");
        jPanel2.add(sumLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 70, -1, -1));

        jButton4.setBackground(new java.awt.Color(145, 212, 235));
        jButton4.setText("Normalize ");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, -1, 20));

        jButton5.setBackground(new java.awt.Color(145, 212, 235));
        jButton5.setText("Normalize ");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 60, -1, 20));

        jButton6.setText("Balance in 0");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 20, 110, 20));

        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Gauss 2");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        copy1.setBackground(new java.awt.Color(142, 184, 152));
        copy1.setText("copy");
        copy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copy1ActionPerformed(evt);
            }
        });
        jPanel2.add(copy1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, 20));

        copy2.setBackground(new java.awt.Color(142, 184, 152));
        copy2.setText("copy");
        copy2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copy2ActionPerformed(evt);
            }
        });
        jPanel2.add(copy2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 60, -1, 20));

        paste1.setBackground(new java.awt.Color(142, 184, 152));
        paste1.setText("paste");
        paste1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paste1ActionPerformed(evt);
            }
        });
        jPanel2.add(paste1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, -1, 20));

        paste2.setBackground(new java.awt.Color(142, 184, 152));
        paste2.setText("paste");
        paste2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paste2ActionPerformed(evt);
            }
        });
        jPanel2.add(paste2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, -1, 20));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 950, 140));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        for (int i = 7; i < 13; i++) {
            if (i != 10 && i != 11) {
                fields[i].setText("" + 0);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        saveValues();
        visualize();
        loadImageFilter();
        convolution();
    }//GEN-LAST:event_jButton1ActionPerformed

    void visualize() {
        Mat Gauss1 = new Mat();
        Mat Gauss2 = new Mat();
        Mat DoG = new Mat();

        int size = Integer.parseInt(fields[0].getText());

        double A1 = Double.parseDouble(fields[1].getText());
        double x1 = size / 2 + Double.parseDouble(fields[2].getText());
        double y1 = size / 2 - Double.parseDouble(fields[3].getText());
        double sx1 = Double.parseDouble(fields[4].getText());
        double sy1 = Double.parseDouble(fields[5].getText());
        double t1 = Double.parseDouble(fields[6].getText());

        double A2 = Double.parseDouble(fields[7].getText());
        double x2 = size / 2 + Double.parseDouble(fields[8].getText());
        double y2 = size / 2 - Double.parseDouble(fields[9].getText());
        double sx2 = Double.parseDouble(fields[10].getText());
        double sy2 = Double.parseDouble(fields[11].getText());
        double t2 = Double.parseDouble(fields[12].getText());

        Gauss1 = SpecialKernels.getAdvencedGauss(new Size(size, size), A1, y1, x1, sy1, sx1, t1);
        sum1 = Core.sumElems(Gauss1).val[0];
        if (twoGauss) {
            Gauss2 = SpecialKernels.getAdvencedGauss(new Size(size, size), A2, y2, x2, sy2, sx2, t2);
            sum2 = Core.sumElems(Gauss2).val[0];
            Core.add(Gauss1, Gauss2, DoG);
        } else {
            DoG = Gauss1.clone();
        }
        DoGFilter = DoG;
        summation();
        loadImageFilter();

    }

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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        for (int i = 1; i < 7; i++) {
            fields[i + 6].setText(fields[i].getText());
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    double zoom = 1;
    private void zoomSliderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_zoomSliderMouseDragged
        // TODO add your handling code here:
        zoom = zoomSlider.getValue() * 0.1;
        visualize();
    }//GEN-LAST:event_zoomSliderMouseDragged

    private void A1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A1fKeyPressed
        // TODO add your handling code here:
        incDec(A1f, evt, 0.01);
    }//GEN-LAST:event_A1fKeyPressed

    private void x1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_x1fKeyPressed
        // TODO add your handling code here:
        incDec(x1f, evt, 0.1);
    }//GEN-LAST:event_x1fKeyPressed

    private void y1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_y1fKeyPressed
        // TODO add your handling code here:
        incDec(y1f, evt, 0.1);
    }//GEN-LAST:event_y1fKeyPressed

    private void sx1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sx1fKeyPressed
        // TODO add your handling code here:
        incDec(sx1f, evt, 0.1);
    }//GEN-LAST:event_sx1fKeyPressed

    private void sy1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sy1fKeyPressed
        // TODO add your handling code here:
        incDec(sy1f, evt, 0.1);
    }//GEN-LAST:event_sy1fKeyPressed

    private void t1fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t1fKeyPressed
        // TODO add your handling code here:
        incDec(t1f, evt, 0.1);
    }//GEN-LAST:event_t1fKeyPressed

    private void A2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A2fKeyPressed
        // TODO add your handling code here:
        incDec(A2f, evt, 0.01);
    }//GEN-LAST:event_A2fKeyPressed

    private void x2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_x2fKeyPressed
        // TODO add your handling code here:
        incDec(x2f, evt, 0.1);
    }//GEN-LAST:event_x2fKeyPressed

    private void y2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_y2fKeyPressed
        // TODO add your handling code here:
        incDec(y2f, evt, 0.1);
    }//GEN-LAST:event_y2fKeyPressed

    private void sx2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sx2fKeyPressed
        // TODO add your handling code here:
        incDec(sx2f, evt, 0.1);
    }//GEN-LAST:event_sx2fKeyPressed

    private void sy2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sy2fKeyPressed
        // TODO add your handling code here:
        incDec(sy2f, evt, 0.1);
    }//GEN-LAST:event_sy2fKeyPressed

    private void t2fKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t2fKeyPressed
        // TODO add your handling code here:
        incDec(t2f, evt, 0.1);
    }//GEN-LAST:event_t2fKeyPressed

    private void sizefKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sizefKeyPressed
        // TODO add your handling code here:
        int value;
        try {
            if (evt.getKeyCode() == KeyEvent.VK_UP) {
                value = Integer.parseInt(sizef.getText());
                value = value + 1;
                sizef.setText("" + value);
                visualize();
            }
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                value = Integer.parseInt(sizef.getText());
                value = value - 1;
                sizef.setText("" + value);
                visualize();
            }
        } catch (Exception ex) {

        }
    }//GEN-LAST:event_sizefKeyPressed

    private void sizefKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sizefKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sizefKeyReleased

    private void A1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_A1fKeyReleased

    private void x1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_x1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_x1fKeyReleased

    private void y1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_y1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_y1fKeyReleased

    private void sx1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sx1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sx1fKeyReleased

    private void sy1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sy1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sy1fKeyReleased

    private void t1fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t1fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_t1fKeyReleased

    private void A2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_A2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_A2fKeyReleased

    private void x2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_x2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_x2fKeyReleased

    private void y2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_y2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_y2fKeyReleased

    private void sx2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sx2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sx2fKeyReleased

    private void sy2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sy2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_sy2fKeyReleased

    private void t2fKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t2fKeyReleased
        // TODO add your handling code here:
        convolution();
        saveValues();
    }//GEN-LAST:event_t2fKeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        double A1 = Double.parseDouble(A1f.getText());
        double Anorm = A1 / sum1;
        A1f.setText(String.format("%.3f", Anorm));
        visualize();
        convolution();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (twoGauss) {
            double A2 = Double.parseDouble(A2f.getText());
            double Anorm = -A2 / sum2;
            A2f.setText(String.format("%.3f", Anorm));
        }
        visualize();
        convolution();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        double A1 = Double.parseDouble(A1f.getText());
        double Anorm1 = A1 / sum1;
        double proportion = A1 / Anorm1;
        if (twoGauss) {
            double A2 = Double.parseDouble(A2f.getText());
            double Anorm2 = -A2 / sum2;
            /*double Anorm1=A1/sum1;
            double Anorm2=-A2/sum2;*/
            Double newA2 = Anorm2 * proportion;
            A2f.setText(String.format("%.3f", newA2));
        }
        visualize();
        convolution();
    }//GEN-LAST:event_jButton6ActionPerformed

    boolean twoGauss = true;
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
        change2GaussStatus();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void paste1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paste1ActionPerformed
        // TODO add your handling code here:
        paste1();
    }//GEN-LAST:event_paste1ActionPerformed

    public void paste1() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);

        DataFlavor dataFlavorStringJava;
        try {
            dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
            if (t.isDataFlavorSupported(dataFlavorStringJava)) {
                String texto = (String) t.getTransferData(dataFlavorStringJava);
                if (texto.contains("♦")) {
                    String values[] = texto.split(" ");
                    for (int i = 0; i < 7; i++) {
                        fields[i].setText(values[i + 1]);
                    }
                }
            }
        } catch (Exception ex) {

        }
        visualize();
        convolution();
    }
    private void copy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copy1ActionPerformed
        // TODO add your handling code here:
        try {
            String cString = "♦♣♠ ";
            for (int i = 0; i < 7; i++) {
                cString = cString + fields[i].getText();
                if (i < 6) {
                    cString = cString + " ";
                }
            }
            StringSelection stringSelection = new StringSelection(cString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_copy1ActionPerformed

    private void paste2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paste2ActionPerformed
        // TODO add your handling code here:
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = cb.getContents(this);

        DataFlavor dataFlavorStringJava;
        try {
            dataFlavorStringJava = new DataFlavor("application/x-java-serialized-object; class=java.lang.String");
            if (t.isDataFlavorSupported(dataFlavorStringJava)) {
                String texto = (String) t.getTransferData(dataFlavorStringJava);
                if (texto.contains("♦")) {
                    String values[] = texto.split(" ");
                    for (int i = 0; i < 7; i++) {
                        fields[i + 7].setText(values[i + 1]);
                    }
                }
            }
        } catch (Exception ex) {

        }
        visualize();
        convolution();
    }//GEN-LAST:event_paste2ActionPerformed

    private void copy2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copy2ActionPerformed
        // TODO add your handling code here:
        try {
            String cString = "♦♣♠ ";
            cString = cString + fields[0].getText() + " " + fields[7].getText() + " " + fields[8].getText() + " " + fields[9].getText() + " "
                    + fields[10].getText() + " " + fields[11].getText() + " " + fields[12].getText();
            StringSelection stringSelection = new StringSelection(cString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_copy2ActionPerformed

    public void change2GaussStatus() {
        twoGauss = jCheckBox1.isSelected();
        for (int i = 7; i < fields.length; i++) {
            fields[i].setVisible(twoGauss);
        }
        jButton2.setVisible(twoGauss);
        jButton6.setVisible(twoGauss);
        jButton3.setVisible(twoGauss);
        jButton5.setVisible(twoGauss);
        copy2.setVisible(twoGauss);
        paste2.setVisible(twoGauss);
        visualize();
        convolution();
    }

    void loadImageFilter() {
        fImage = Convertor.ConvertMat2FilterImage(DoGFilter);
        fImage = Scalr.resize(fImage, (int) (Integer.parseInt(sizef.getText()) * zoom));
        filterImage.setText("");
        filterImage.setIcon(new ImageIcon(fImage));
    }

    void saveValues() {
        String sValues = "";
        for (int i = 0; i < 12; i++) {
            sValues = sValues + fields[i].getText() + " ";
        }
        sValues = sValues + fields[12].getText();
        FileUtils.write(fileName.replaceAll(".txt", ""), sValues, "txt");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
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
            java.util.logging.Logger.getLogger(GaussianVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GaussianVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GaussianVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GaussianVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GaussianVisualizer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField A1f;
    private javax.swing.JTextField A2f;
    private javax.swing.JLabel convolvedImage;
    private javax.swing.JButton copy1;
    private javax.swing.JButton copy2;
    private javax.swing.JLabel filterImage;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    public javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel originalImage;
    private javax.swing.JButton paste1;
    private javax.swing.JButton paste2;
    private javax.swing.JTextField sizef;
    private javax.swing.JLabel sumLabel;
    private javax.swing.JTextField sx1f;
    private javax.swing.JTextField sx2f;
    private javax.swing.JTextField sy1f;
    private javax.swing.JTextField sy2f;
    private javax.swing.JTextField t1f;
    private javax.swing.JTextField t2f;
    private javax.swing.JTextField x1f;
    private javax.swing.JTextField x2f;
    private javax.swing.JTextField y1f;
    private javax.swing.JTextField y2f;
    private javax.swing.JSlider zoomSlider;
    // End of variables declaration//GEN-END:variables
}
