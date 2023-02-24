/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

import generator.graph.GArea;
import generator.graph.GSmallNode;
import generator.graph.MGraph;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import utils.FileUtils;

/**
 *
 * @author HumanoideFilms
 */
public class SmallNodeList extends javax.swing.JFrame {

    String route;
    JInternalFrame[] frames;
    Point[] positions;
    JCheckBox[] boxArray;
    TreeMap<String, Object> tmap;
    HashMap<String, String> presets;

    /**
     * Creates new form testgraph
     */
    public SmallNodeList() {
        initComponents();
        presets = new HashMap<String, String>();
        route = FileUtils.readFile(new File("route.txt")).trim();
        MGraph.generateGraphs2(route);

        generateFrames();
        loadChecks();
        int curv=12;
        //this.setGlassPane(linker);
        this.setGlassPane(new JComponent() {
            protected void paintComponent(Graphics g) {
                // Graphics2D g2d = (Graphics2D) g;

                g.setColor(new Color(0f, 0f, 0f, .3f));
                for (GSmallNode node : MGraph.nodes) {
                    JCheckBox from = obtainCheckByName(node.name);
                    int i=1;
                    for (String next : node.next) {
                        JCheckBox to = obtainCheckByName(next);
                        if (to != null) {
                            
                            if (Math.abs(checkLocation(from).x - checkLocation(to).x) < 10) {
                                int xpoints[] = {
                                    checkLocation(from).x + 5,
                                    checkLocation(from).x - (int) ((from.getY() + 2) * 0.2),
                                    checkLocation(from).x - (int) ((from.getY() + 2) * 0.2) - 5*i,
                                    checkLocation(from).x - (int) ((from.getY() + 2) * 0.2) - 5*i,
                                    checkLocation(from).x - (int) ((from.getY() + 2) * 0.2),
                                    checkLocation(from).x + 5};
                                int cap = 0;
                                if (checkLocation(from).y < checkLocation(to).y) {
                                    cap = curv;
                                } else {
                                    cap = -curv;
                                }
                                int ypoints[] = {
                                    checkLocation(from).y-5,
                                    checkLocation(from).y-5,
                                    checkLocation(from).y + cap-5,
                                    checkLocation(to).y - cap-5,
                                    checkLocation(to).y-5,
                                    checkLocation(to).y-5};

                                g.drawPolyline(xpoints, ypoints, 6);
                                g.drawOval(checkLocation(from).x , checkLocation(from).y-10, 10, 10);
                                g.setColor(new Color(1f, 1f, 1f, .5f));
                                g.fillOval(checkLocation(from).x , checkLocation(from).y-10, 10, 10);
                            } else {
                                g.setColor(new Color(0f, 0f, 0f, .3f));
                                g.drawLine(checkLocation(from).x + 195, checkLocation(from).y - 5,
                                        checkLocation(to).x + 10, checkLocation(to).y-5);
                                g.drawOval(checkLocation(from).x + 195-5, checkLocation(from).y - 10, 10, 10);
                                g.setColor(new Color(1f, 1f, 1f, .5f));
                                g.fillOval(checkLocation(from).x + 195-5, checkLocation(from).y - 10, 10, 10);
                            }
                            g.setColor(new Color(0f, 0f, 0f, .3f));
                            g.fillOval(checkLocation(to).x + 5, checkLocation(to).y - 8, 7, 7);

                        }
                        i++;
                    }
                }
            }
        });
        Container glassPane = (Container) this.getGlassPane();
        glassPane.setVisible(true);
        loadPresets();
        jComboBox1.setSelectedItem("default");

    }

    Point checkLocation(JCheckBox box) {
        return new Point((box.getLocationOnScreen().x - this.getLocationOnScreen().x) - box.getHeight(),
                (box.getLocationOnScreen().y - this.getLocationOnScreen().y) - box.getHeight());
    }

    void loadChecks() {
        ProcessList.openList();
        tmap = new TreeMap();
        tmap.putAll(ProcessList.ProcessMap);

        boxArray = new JCheckBox[tmap.size()];
        int i = 0;
        for (String key : tmap.keySet()) {
            boxArray[i] = new JCheckBox(key, (boolean) tmap.get(key));
            addBox(key, boxArray[i]);
            i++;
        }
    }

    void loadPresets() {
        try {
            String lines[] = FileUtils.fileLines("ConfigFiles\\presets.txt");
            if (lines.length > 0) {
                for (String s : lines) {
                    String key = s.split("♦")[0];
                    String value = s.split("♦")[1];
                    presets.put(key, value);
                }
                updateComboBox();
            }
        } catch (Exception ex) {
            saveDefault();
            loadPresets();
        }
    }

    void savePresetList() {
        String s = "";
        if (presets.size() > 0) {
            for (String key : presets.keySet()) {
                s = s + key + "♦" + presets.get(key) + "\n";
            }
        }
        FileUtils.write("ConfigFiles\\presets", s, "txt");
        //updateComboBox();
    }

    void saveDefault() {
        String s = "";
        String comb = "";
        for (int i = 0; i < boxArray.length; i++) {
            if (boxArray[i].isSelected()) {
                comb = comb + "1";
            } else {
                comb = comb + "0";
            }
        }
        s = "default♦" + comb;
        FileUtils.write("ConfigFiles\\presets", s, "txt");
        updateComboBox();
    }

    void updateComboBox() {
        jComboBox1.removeAllItems();
        for (String key : presets.keySet()) {

            jComboBox1.addItem(key);

        }
    }

    void updateChecks(String key) {
        String values = presets.get(key);
        for (int i = 0; i < values.length(); i++) {
            if (values.charAt(i) == '1') {
                boxArray[i].setSelected(true);
            }
            if (values.charAt(i) == '0') {
                boxArray[i].setSelected(false);
            }
        }
    }

    void addBox(String key, JCheckBox box) {
        for (GArea a : MGraph.areas) {
            for (String name : a.smallNodes) {
                if (name.equals(key)) {
                    for (int i = 0; i < frames.length; i++) {
                        if (frames[i].getTitle().equals(a.name)) {
                            box.setVisible(true);
                            frames[i].add(box);
                            frames[i].add(Box.createVerticalGlue());
                        }
                    }
                }
            }
        }
    }

    public void generateFrames() {
        frames = new JInternalFrame[MGraph.areas.size()];
        positions = new Point[MGraph.areas.size()];
        loadPositions();
        int i = 0;
        for (GArea a : MGraph.areas) {
            frames[i] = new JInternalFrame();
            frames[i].setTitle(a.name);
            frames[i].setSize(200, a.smallNodes.size() * 40 + 30);
            frames[i].setLocation(positions[i].x, positions[i].y);
            frames[i].addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void componentMoved(ComponentEvent e) {
                    repaint(); //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void componentShown(ComponentEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
            frames[i].setLayout(new BoxLayout(frames[i].getContentPane(), BoxLayout.Y_AXIS));
            frames[i].setVisible(true);
            // frames[i].setResizable(true);
            jDesktopPane2.add(frames[i]);
            i++;
        }
    }

    int i = 0;

    void writePositions() {
        String s = "";
        for (int i = 0; i < positions.length; i++) {
            s = s + "" + (frames[i].getLocation().x) + "," + (frames[i].getLocation().y) + "\n";
        }
        FileUtils.write("ConfigFiles\\positions", s, "txt");
    }

    JCheckBox obtainCheckByName(String name) {
        for (JCheckBox box : boxArray) {
            if (box.getText().equals(name)) {
                return box;
            }
        }
        return null;
    }

    void initializePositions() {
        String s = "";
        for (int i = 0; i < positions.length; i++) {
            s = s + "" + 0 + "," + 0 + "\n";
        }
        FileUtils.write("ConfigFiles\\positions", s, "txt");
    }

    void loadPositions() {
        try {
            String s[] = FileUtils.fileLines("ConfigFiles\\positions.txt");
            for (int i = 0; i < positions.length; i++) {
                String svalues[] = s[i].split(",");
                int value1 = Integer.parseInt(svalues[0]);
                int value2 = Integer.parseInt(svalues[1]);
                positions[i] = new Point(value1, value2);
            }
        } catch (Exception ex) {
            initializePositions();
            loadPositions();
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

        jDesktopPane2 = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Node Diagram and List");
        setMinimumSize(new java.awt.Dimension(500, 720));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jDesktopPane2.setMinimumSize(new java.awt.Dimension(500, 720));

        jPanel1.setBackground(new java.awt.Color(194, 194, 194));

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Save Preset");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Preset:");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton3.setText("Remove preset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 244, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addGap(27, 27, 27))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDesktopPane2.add(jPanel1);
        jPanel1.setBounds(0, 0, 1090, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1084, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jDesktopPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        writePositions();
        for (int i = 0; i < boxArray.length; i++) {
            ProcessList.setValue(boxArray[i].getText(), boxArray[i].isSelected());
        }
        ProcessList.saveProcessList();
        String c = "";
        for (int i = 0; i < boxArray.length; i++) {
            if (boxArray[i].isSelected()) {
                c = c + "1";
            } else {
                c = c + "0";
            }
        }
        if (!presets.containsKey("default")) {
            jComboBox1.addItem("default");
        }
        presets.put("default", c);
        savePresetList();
        jComboBox1.setSelectedItem("default");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        String name = jTextField1.getText();
        if (name.length() > 0) {
            String c = "";
            for (int i = 0; i < boxArray.length; i++) {
                if (boxArray[i].isSelected()) {
                    c = c + "1";
                } else {
                    c = c + "0";
                }
            }
            if (!presets.containsKey(name)) {
                jComboBox1.addItem(name);
            }
            presets.put(name, c);
            savePresetList();
            jComboBox1.setSelectedItem(name);

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        updateChecks(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));
        jTextField1.setText(jComboBox1.getItemAt(jComboBox1.getSelectedIndex()));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (!jComboBox1.getSelectedItem().equals("default")) {
            presets.remove("" + jComboBox1.getSelectedItem());
            savePresetList();
            jComboBox1.removeItem(jComboBox1.getSelectedItem());
            jComboBox1.setSelectedItem("default");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SmallNodeList().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
