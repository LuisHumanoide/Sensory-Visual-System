/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.Config;
import utils.layoutManager;

/**
 *
 * @author Laptop
 */
public class VisualizerFrame extends javax.swing.JFrame implements KeyListener {

    private JLabel[] labels;
    final String strings[];
    int indexh = 0;
    int indexv = 0;
    int index;
    int maxwidth = 0;
    int maxheight = 0;
    int skip = 1 * Config.width;
    int maxLabel = 0;

    /**
     * Creates new form Visualizer
     */
    public VisualizerFrame(int nFields) {
        loadNimbus();
        initComponents();
        addKeyListener(this);

        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        layoutManager.initLayout();
        previous.setLocation(previous.getWidth(), this.getHeight() - previous.getWidth());
        next.setLocation(this.getWidth() - next.getWidth(), this.getHeight() - next.getWidth());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.width * 0.75), screenSize.height - 30);
        this.setLocation((int) (screenSize.width * 0.24), 0);
        jPanel1.setPreferredSize(new Dimension(this.getSize()));
        labels = new JLabel[nFields];
        strings = new String[nFields];
        for (int i = 0; i < nFields; i++) {
            strings[i] = "";
            labels[i] = new JLabel();
            labels[i].setHorizontalTextPosition(JLabel.CENTER);
            labels[i].setLocation(new Point(layoutManager.points.get(i).x + jPanel2.getWidth(), layoutManager.points.get(i).y));
            labels[i].setVisible(true);
            labels[i].setSize(Config.width, Config.heigth);
            Listener listener = new Listener(strings[i], labels[i], this, i);
            labels[i].addMouseListener(listener);
            jPanel1.add(labels[i]);
        }
        repaint();
        this.setVisible(true);
    }

    public void restartLabels() {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setIcon(null);
        }
    }

    public void restartLabelPositions() {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setLocation(new Point(layoutManager.points.get(i).x + jPanel2.getWidth(), layoutManager.points.get(i).y));
        }
    }

    void loadNimbus() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUITest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    public void setImage(BufferedImage image, String title, int index) {
        labels[index].setIcon(new ImageIcon(image));
        strings[index] = title;
        maxLabelIndex(index);

        //repaint();
    }

    void maxLabelIndex(int index) {
        if (index > maxLabel) {
            maxLabel = index;
        }
    }
    int lindex = -1;

    public void methodListener(int index) {
        positionx = labels[index].getX();
        positiony = labels[index].getY();
        text = strings[index];
        lindex = index;
        if (labels[index].getIcon() != null) {
            isInLabel = true;
            repaint();
        } else {
            isInLabel = false;

        }
    }

    public void next() {
        //if (labels[maxLabel].getX() >= this.getWidth() - labels[0].getSize().width) {
        move(1);
        //}
    }

    public void previous() {
        if (labels[0].getX() < jPanel2.getWidth()) {
            move(-1);
        }
    }

    public void up() {
        if (labels[0].getY() < 0) {
            moveV(-1);
        }
    }

    public void down() {
        //if (labels[maxLabel].getY() >= this.getHeight() - labels[0].getSize().height) {
        moveV(1);
        // }
    }

    public void move(int n) {
        for (int i = 0; i < labels.length; i++) {
            if (n == 1) {
                labels[i].setLocation(labels[i].getX() - (skip), labels[i].getY());
            }
            if (n == -1) {
                labels[i].setLocation(labels[i].getX() + (skip), labels[i].getY());
            }
        }
        //repaint();
    }
    int ox[];
    int oy[];

    public void olocations() {
        ox = new int[labels.length];
        oy = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            ox[i] = labels[i].getX();
            oy[i] = labels[i].getY();
        }
    }

    public void movexy(int x, int y) {
        for (int i = 0; i < labels.length; i++) {
            labels[i].setLocation(ox[i] - x, oy[i] - y);
        }
    }

    public void moveV(int n) {
        for (int i = 0; i < labels.length; i++) {
            if (n == 1) {
                labels[i].setLocation(labels[i].getX(), labels[i].getY() - (skip));
            }
            if (n == -1) {
                labels[i].setLocation(labels[i].getX(), labels[i].getY() + (skip));
            }
        }
    }

    public void copyImage(int index) {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
            ImageIcon image = (ImageIcon) labels[index].getIcon();
            ImageSelection dh = new ImageSelection(image.getImage());
            cb.setContents(dh, null);
        } catch (Exception e) {
        }
    }

    int positionx = 0;
    int positiony = 0;
    String text = "";
    boolean isInLabel = false;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isInLabel) {
            g.setColor(new Color(0, 50, 10));
            g.drawRect(positionx + 7, positiony + 30, Config.width, Config.heigth);
            g.drawRect(positionx + 7, positiony + Config.heigth + 30, Config.width, 30);
            g.setColor(new Color(10, 10, 10, 250));
            g.fillRect(positionx + 7, positiony + Config.heigth + 30, Config.width, 30);
            g.setColor(Color.WHITE);
            g.drawString(text, positionx + 10, positiony + Config.heigth + 50);
        }
    }

    public void update(Graphics g) {
        Graphics offgc;
        Image offscreen = null;
        Dimension d = size();

        // create the offscreen buffer and associated Graphics
        offscreen = createImage(d.width, d.height);
        offgc = offscreen.getGraphics();
        // clear the exposed area
        offgc.setColor(getBackground());
        offgc.fillRect(0, 0, d.width, d.height);
        offgc.setColor(getForeground());
        // do normal redraw
        paint(offgc);
        // transfer offscreen to window
        g.drawImage(offscreen, 0, 0, this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        previous = new javax.swing.JButton();
        next1 = new javax.swing.JButton();
        next2 = new javax.swing.JButton();
        next = new javax.swing.JButton();
        moveButton = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 1000), new java.awt.Dimension(0, 1000), new java.awt.Dimension(0, 2000));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 50), new java.awt.Dimension(0, 50), new java.awt.Dimension(32767, 50));
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Activation Viewer");

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));

        jPanel2.setBackground(new java.awt.Color(20, 20, 20));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        previous.setBackground(new java.awt.Color(31, 47, 56));
        previous.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        previous.setForeground(new java.awt.Color(255, 255, 255));
        previous.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/left.png"))); // NOI18N
        previous.setBorder(null);
        previous.setBorderPainted(false);
        previous.setMinimumSize(new java.awt.Dimension(32, 32));
        previous.setOpaque(false);
        previous.setPreferredSize(new java.awt.Dimension(32, 32));
        previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel2.add(previous, gridBagConstraints);

        next1.setBackground(new java.awt.Color(31, 47, 56));
        next1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        next1.setForeground(new java.awt.Color(255, 255, 255));
        next1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/up.png"))); // NOI18N
        next1.setBorder(null);
        next1.setBorderPainted(false);
        next1.setMinimumSize(new java.awt.Dimension(32, 32));
        next1.setOpaque(false);
        next1.setPreferredSize(new java.awt.Dimension(32, 32));
        next1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                next1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel2.add(next1, gridBagConstraints);

        next2.setBackground(new java.awt.Color(31, 47, 56));
        next2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        next2.setForeground(new java.awt.Color(255, 255, 255));
        next2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/down.png"))); // NOI18N
        next2.setBorder(null);
        next2.setBorderPainted(false);
        next2.setMinimumSize(new java.awt.Dimension(42, 42));
        next2.setOpaque(false);
        next2.setPreferredSize(new java.awt.Dimension(32, 32));
        next2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                next2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel2.add(next2, gridBagConstraints);

        next.setBackground(new java.awt.Color(31, 46, 56));
        next.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        next.setForeground(new java.awt.Color(255, 255, 255));
        next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/right.png"))); // NOI18N
        next.setBorder(null);
        next.setBorderPainted(false);
        next.setMinimumSize(new java.awt.Dimension(32, 32));
        next.setOpaque(false);
        next.setPreferredSize(new java.awt.Dimension(32, 32));
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel2.add(next, gridBagConstraints);

        moveButton.setBackground(new java.awt.Color(31, 48, 56));
        moveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/8_direction.png"))); // NOI18N
        moveButton.setFocusable(false);
        moveButton.setIconTextGap(0);
        moveButton.setMaximumSize(new java.awt.Dimension(36, 36));
        moveButton.setMinimumSize(new java.awt.Dimension(42, 42));
        moveButton.setPreferredSize(new java.awt.Dimension(42, 42));
        moveButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                moveButtonMouseDragged(evt);
            }
        });
        moveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                moveButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                moveButtonMouseReleased(evt);
            }
        });
        moveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel2.add(moveButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        jPanel2.add(filler1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel2.add(filler2, gridBagConstraints);

        jButton1.setBackground(new java.awt.Color(31, 48, 56));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/reset.png"))); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(32, 32));
        jButton1.setMinimumSize(new java.awt.Dimension(32, 32));
        jButton1.setPreferredSize(new java.awt.Dimension(32, 32));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jButton1, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 390, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_nextActionPerformed

    private void previousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousActionPerformed
        // TODO add your handling code here:
        previous();
    }//GEN-LAST:event_previousActionPerformed

    private void next1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_next1ActionPerformed
        // TODO add your handling code here:
        up();
    }//GEN-LAST:event_next1ActionPerformed

    private void next2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_next2ActionPerformed
        // TODO add your handling code here:
        down();
    }//GEN-LAST:event_next2ActionPerformed

    private void moveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moveButtonActionPerformed

    int dx = 0;
    int dy = 0;
    private void moveButtonMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveButtonMouseDragged
        // TODO add your handling code here:
        moveButton.setBackground(Color.white);
        int dx = evt.getX() * 4 - px;
        int dy = evt.getY() * 4 - py;
        movexy(dx, dy);
    }//GEN-LAST:event_moveButtonMouseDragged

    private void moveButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveButtonMouseReleased
        // TODO add your handling code here:
        moveButton.setBackground(new Color(31, 48, 56));
    }//GEN-LAST:event_moveButtonMouseReleased

    int px = 0;
    int py = 0;
    private void moveButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_moveButtonMousePressed
        // TODO add your handling code here:
        px = evt.getX();
        py = evt.getY();
        olocations();
    }//GEN-LAST:event_moveButtonMousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        restartLabelPositions();
    }//GEN-LAST:event_jButton1ActionPerformed
    int c = 0;
    int p1;
    int p2;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JButton moveButton;
    private javax.swing.JButton next;
    private javax.swing.JButton next1;
    private javax.swing.JButton next2;
    private javax.swing.JButton previous;
    // End of variables declaration//GEN-END:variables

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            previous();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            next();
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
