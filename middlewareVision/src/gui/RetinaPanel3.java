/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.Config;
import utils.Convertor;
import utils.FileUtils;

/**
 *
 * @author Laptop
 */
public class RetinaPanel3 extends javax.swing.JPanel {

    int index;
    String folder;
    String route = "images/";
    BufferedImage img;
    int rate = 3;
    int c = 0;
    boolean stereo = false;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    Timer timer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // Aquí el código que queramos ejecutar.
            if (play) {
                c++;
                if (c >= rate) {
                    createImage(1);
                    c = 0;
                }
            }
            //Logger.getLogger(RetinaFrame.class.getName()).log(Level.SEVERE, null, ex);

        }
    });

    /**
     * Creates new form RetinaPanel
     */
    public RetinaPanel3() {
        initComponents();

        root = new DefaultMutableTreeNode("", true);
        jLabel1.setSize(Config.width, Config.heigth);
        //jPanel1.setLayout(null);
        jPanel1.setBounds(0, 0, Config.width, Config.heigth);
        if (stereo) {
            jLabel2.setSize(Config.width, Config.heigth);
        }
        if (!stereo) {
            jLabel1.setBounds(200, 0, Config.width, Config.heigth);
           // jLabel2.setBounds(300, 0, Config.width, Config.heigth);
           repaint();
        }

        jTree1.setForeground(new java.awt.Color(204, 204, 255));
        createImage(1);
        updateTree();
        renderTree();
    }

    void renderTree() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree1.getCellRenderer();
        Icon closedIcon = new ImageIcon("icon.png");
        Icon openIcon = new ImageIcon("icon.png");
        Icon leafIcon = new ImageIcon("icon.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
    }

    void updateTree() {
        root.removeAllChildren();
        getList(root, new File(route));
        treeModel = new DefaultTreeModel(root);
        jTree1.setModel(treeModel);
        expandAllNodes();
    }

    /**
     * Obtener la lista para la creación del arbol de directorios
     *
     * @param node
     * @param f
     */
    public void getList(DefaultMutableTreeNode node, File f) {
        if (f.isDirectory()) {
            // We keep only JAVA source file for display in this HowTo
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
            node.add(child);
            File fList[] = f.listFiles();
            for (int i = 0; i < fList.length; i++) {
                getList(child, fList[i]);
            }
        }
    }

    /**
     * Expande todos los nodos del arbol
     */
    private void expandAllNodes() {
        int j = jTree1.getRowCount();
        int i = 0;
        while (i < j) {
            jTree1.expandRow(i);
            i += 1;
            j = jTree1.getRowCount();
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setImage(BufferedImage image) {
        jLabel1.setIcon(new ImageIcon(image));
        if (stereo) {
            jLabel2.setIcon(new ImageIcon(image));
        }
        repaint();
    }
    public Mat src;

    /**
     * put the image on the retina
     *
     * @return
     * @throws IOException
     */
    public void createImage(int move) {
        String path = getImageName(folder, move);
        File file = new File(path);
        BufferedImage img;
        try {
            img = ImageIO.read(file);
            src = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            src = Convertor.bufferedImageToMat(img);
            src.convertTo(src, -1, Config.contr, Config.bright);
            //cvtColor(src, src, COLOR_BGR2GRAY);
            Imgproc.resize(src, src, new Size(Config.width, Config.heigth));
            BufferedImage img2 = Convertor.Mat2Img2(src);
            setImage(img2);
        } catch (Exception ex) {
            //Logger.getLogger(RetinaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int count = -1;

    String getImageName(String folder, int move) {

        String path = folder + "/";
        String imageName = "";
        String[] files = FileUtils.getFiles(path);
        timeline.setMaximum(files.length - 1);
        if (files != null && files.length > 0) {
            int size = files.length;
            if (move == 1) {
                /*if (count >= size - 1) {
                    count = -1;
                }*/
                count++;
                imageName = files[(count) % size];
            }
            if (move == -1) {
                if (count <= 0) {
                    count = size;
                }
                count--;
                imageName = files[(count) % size];
            }
            if (move == 0) {
                imageName = files[(count) % size];
            }
            timeline.setValue((count) % size);
        }
//        Controls.setImageName(imageName + "");
        return imageName;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        playButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSlider1 = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timeline = new javax.swing.JSlider();
        jCheckBox1 = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(51, 51, 51));

        playButton.setText("▶");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        jButton2.setText(">");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("<");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSlider1.setMaximum(50);
        jSlider1.setMinimum(1);
        jSlider1.setValue(3);
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jSlider1MouseDragged(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Delay");

        jTree1.setBackground(new java.awt.Color(102, 102, 102));
        jTree1.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jTree1.setForeground(new java.awt.Color(51, 51, 51));
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);

        jPanel1.setBackground(new java.awt.Color(19, 19, 19));

        jLabel1.setBackground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Image folders");

        timeline.setBackground(new java.awt.Color(23, 23, 23));
        timeline.setValue(0);
        timeline.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                timelineMouseDragged(evt);
            }
        });

        jCheckBox1.setText("jCheckBox1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSlider1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(96, 96, 96)
                                .addComponent(jCheckBox1))
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(playButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(timeline, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(timeline, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playButton)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jCheckBox1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(151, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        // TODO add your handling code here:
        if (play) {
            playButton.setText("▶");
            play = false;
        } else {
            playButton.setText("⏸");
            play = true;
        }
    }//GEN-LAST:event_playButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        createImage(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:}
        createImage(-1);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSlider1MouseDragged
        // TODO add your handling code here:
        rate = jSlider1.getValue();
    }//GEN-LAST:event_jSlider1MouseDragged

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree1ValueChanged
        // TODO add your handling code here:
        folder = evt.getPath().getLastPathComponent().toString();
        count = -1;
        createImage(1);
    }//GEN-LAST:event_jTree1ValueChanged

    private void timelineMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timelineMouseDragged
        // TODO add your handling code here:
        count = timeline.getValue();
        createImage(0);
    }//GEN-LAST:event_timelineMouseDragged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    boolean play = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton playButton;
    private javax.swing.JSlider timeline;
    // End of variables declaration//GEN-END:variables
}
