/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import mapOpener.AmapViewer;
import middlewareVision.nodes.Visual.Retina.RetinaProccess;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import utils.Config;
import utils.Convertor;
import utils.FileUtils;

/**
 *
 * @author Laptop
 */
public class RetinaPanel extends javax.swing.JPanel {

    int index;
    String folder;
    String route = "images/";
    String rightKeyword = "Right";
    String leftKeyword = "Left";
    BufferedImage img;
    BufferedImage blackImage;
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
        }
    });

    /**
     * Creates new form RetinaPanel
     */
    RetinaProccess rp;
    String filename = "";

    public RetinaPanel(RetinaProccess rp2) {
        blackImage=blackImage();
        rp = rp2;
        
        initComponents();
        folder = "images";
        timer.start();
        root = new DefaultMutableTreeNode("", true);
        jLabel1.setSize(Config.width, Config.heigth);
        //jPanel1.setLayout(null);

        jTree1.setForeground(new java.awt.Color(204, 204, 255));
        modifyLabel();
        updateTree();
        renderTree();
        createImage(1);
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
                        setImage(getImage(f), null);
                        filename = f.toString();

                    }
                } catch (Exception e) {
                }
                return true;
            }

        };
        jLabel1.setTransferHandler(th);
    }

    boolean timelineEnabled = true;

    void enableTimeline(boolean flag) {
        timeline.setEnabled(flag);
        jButton2.setEnabled(flag);
        playButton.setEnabled(flag);
        jButton3.setEnabled(flag);
        timelineEnabled = flag;
    }

    public BufferedImage getImage(File f) {
        BufferedImage img2 = null;
        try {
            BufferedImage m;
            m = ImageIO.read(f);
            Mat src = utils.Convertor.bufferedImageToMat(m);
            Imgproc.resize(src, src, new Size(Config.width, Config.heigth));
            img2 = utils.Convertor.Mat2Img2(src);
            enableTimeline(false);
            play = false;
            playButton.setText("▶");

        } catch (IOException ex) {
            Logger.getLogger(AmapViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return img2;
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

    BufferedImage blackImage() {
        Mat mat;
        String path = "black.jpg";
        File file = new File(path);
        BufferedImage black;
        BufferedImage img2;
        try {
            black = ImageIO.read(file);
            mat = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            mat = Convertor.bufferedImageToMat(black);
            Imgproc.resize(mat, mat, new Size(Config.width, Config.heigth));
            img2 = Convertor.Mat2Img2(mat);
            return img2;

        } catch (Exception ex) {
            Logger.getLogger(RetinaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void setImage(BufferedImage image, BufferedImage image2) {
        jLabel1.setIcon(new ImageIcon(image));
        if (stereo) {
            jLabel2.setIcon(new ImageIcon(image2));
            rp.setImage(image, image2);
        }
        if (!stereo) {
            rp.setImage(image, blackImage);
        }
        //rp.setImage(image, image2);
        //repaint();
    }

    /**
     * put the image on the retina
     *
     * @return
     * @throws IOException
     */
    public void createImage(int move) {
        Mat srcL;
        Mat srcR;
        String pathL = getImageName(folder, move);
        String pathR = pathL.replace(leftKeyword, rightKeyword);
        File fileL = new File(pathL);
        File fileR = new File(pathR);
        BufferedImage imgL;
        BufferedImage imgR;
        try {
            imgL = ImageIO.read(fileL);
            imgR = ImageIO.read(fileR);
            srcL = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            srcR = Mat.zeros(Config.width, Config.heigth, CvType.CV_32FC1);
            srcL = Convertor.bufferedImageToMat(imgL);
            srcR = Convertor.bufferedImageToMat(imgR);
            srcL.convertTo(srcL, -1, Config.contr, Config.bright);
            srcR.convertTo(srcR, -1, Config.contr, Config.bright);
            Imgproc.resize(srcL, srcL, new Size(Config.width, Config.heigth));
            Imgproc.resize(srcR, srcR, new Size(Config.width, Config.heigth));
            BufferedImage img2L = Convertor.Mat2Img2(srcL);
            BufferedImage img2R = Convertor.Mat2Img2(srcR);
            setImage(img2L, img2R);
        } catch (Exception ex) {
            //Logger.getLogger(RetinaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int count = -1;
    String lastFolder = "";
    String[] files2;
    String files[];

    String getImageName(String folder, int move) {
        String path = folder + "/";
        String imageName = "";
        if (!folder.equals(lastFolder)) {
            files2 = FileUtils.getFiles(path);
            files = leftImages(files2);
        }
        lastFolder = folder;
        timeline.setMaximum(files.length - 1);
        if (files != null && files.length > 0) {
            int size = files.length;
            if (move == 1) {
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
            if (move == 0 && count >= 0) {
                imageName = files[(count) % size];
            }
            timeline.setValue((count) % size);
        }

        return imageName;
    }

    String[] leftImages(String[] list) {
        String newList[];
        boolean isStereo = false;
        for (String name : list) {
            if (name.contains(leftKeyword)) {
                isStereo = true;
                stereo = true;
                check3d.setSelected(true);
                break;
            }
        }
        if (isStereo) {
            int j = 0;
            newList = new String[list.length / 2];
            for (int i = 0; i < list.length; i++) {
                if (list[i].contains(leftKeyword)) {
                    newList[j] = list[i];
                    j++;
                }
            }
            return newList;
        }
        if (!isStereo) {
            stereo = false;
            check3d.setSelected(false);
            jLabel2.setIcon(null);
            return newList = list;
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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

        setBackground(new java.awt.Color(51, 51, 51));

        playButton.setText("▶");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        jButton2.setText("→");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("←");
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

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Image folders");

        timeline.setBackground(new java.awt.Color(20, 20, 20));
        timeline.setValue(0);
        timeline.setMinimum(0);
        timeline.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                timelineMouseDragged(evt);
            }
        });
        timeline.setBackground(new Color(102, 153, 153));
        jButton2.setBackground(new Color(102, 153, 153));
        jButton3.setBackground(jButton2.getBackground());
        playButton.setBackground(new Color(153, 204, 255));

        BoxLayout layout1 = new BoxLayout(this, BoxLayout.Y_AXIS);

        jLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        jPanel1.setBackground(new Color(20, 20, 20));
        jPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        jPanel1.add(jLabel1);
        jPanel1.add(jLabel2);
        this.setLayout(layout1);
        this.add(jPanel1);
        this.add(timeline);
        JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bPanel.setBackground(new Color(40, 40, 40));

        jButton3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jButton2.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        bPanel.add(jButton3);
        bPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        bPanel.add(playButton);
        bPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        bPanel.add(jButton2);

        this.add(bPanel);
        check3d = new JCheckBox("Stereo", false);
        check3d.setForeground(Color.white);
        check3d.setAlignmentX(Component.CENTER_ALIGNMENT);

        check3d.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                itemChanged();
            }
        });

        this.add(Box.createRigidArea(new Dimension(15, 15)));
        this.add(check3d);
        this.add(Box.createRigidArea(new Dimension(15, 30)));
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(jLabel3);
        this.add(Box.createRigidArea(new Dimension(15, 10)));
        this.add(jSlider1);

        this.add(Box.createRigidArea(new Dimension(15, 30)));
        jLabel4.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(Box.createRigidArea(new Dimension(15, 10)));
        JPanel pTree = new JPanel();
        jTree1.setAlignmentX(Component.LEFT_ALIGNMENT);
        pTree.add(jTree1);
        pTree.add(Box.createRigidArea(new Dimension(15, 30)));
        jTree1.setBackground(new Color(120, 120, 120));
        JScrollPane scroll = new JScrollPane(jTree1);
        pTree.setBackground(jTree1.getBackground());
        controls = new ControlsPanel();
        tabbed = new JTabbedPane();
        tabbed.add("Folders", scroll);
        tabbed.add("Controls", controls);
        this.add(tabbed);
        this.add(Box.createRigidArea(new Dimension(15, 50)));

    }// </editor-fold>                        

    /**
     * if stereo3d check change
     */
    public void itemChanged() {
        if (check3d.isSelected()) {
            stereo = true;
        } else {
            stereo = false;
            jLabel2.setIcon(null);
        }
        createImage(0);
    }

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        if (play) {
            playButton.setText("▶");
            play = false;
        } else {
            playButton.setText("⏸");
            play = true;
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        createImage(1);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:}
        createImage(-1);
    }

    private void jSlider1MouseDragged(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        rate = jSlider1.getValue();
    }

    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        // TODO add your handling code here:
        folder = evt.getPath().getLastPathComponent().toString();
        count = -1;
        createImage(1);
        enableTimeline(true);
    }

    private void timelineMouseDragged(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        if (timelineEnabled) {
            count = timeline.getValue();
            createImage(0);
        }
    }
    public boolean play = false;

    ControlsPanel controls;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    JCheckBox check3d;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton playButton;
    private javax.swing.JSlider timeline;
    JTabbedPane tabbed;

}
