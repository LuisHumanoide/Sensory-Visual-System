/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import VisualMemory.LGNCells.LGNBank;
import VisualMemory.V1Cells.V1Bank;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import org.opencv.core.Mat;
import utils.Config;
import utils.Convertor;
import utils.Scalr;

/**
 *
 * @author Laptop
 */
public class Visualizer {

    static VisualizerFrame vis;
    public static HashMap<String, Integer> limits;
    public static HashSet<String> lockedLimits;
    static boolean opened = false;

    public static void initVisualizer(int n) {
        if (!opened) {
            vis = new VisualizerFrame(n);
            opened = true;
        }
        limits = new HashMap<String, Integer>();
        lockedLimits = new HashSet();
        limits.put("init", 0);
    }
    

    /**
     * Add a limit, limits are values for the rows
     * @param name
     * @param value 
     */
    public static void addLimit(String name, int value) {
        limits.put(name, value);
    }

    /**
     * Get the index of a row by name
     * @param name name of the row
     * @return 
     */
    public static int getRow(String name) {
        if (limits.containsKey(name)) {
            return limits.get(name);
        } else {
            return -1;
        }
    }

    /**
     * Visualize an image
     * @param image
     * @param title
     * @param index 
     */
    public static void setImage(BufferedImage image, String title, int index) {
        vis.setImage(image, title, index);
    }

    /**
     * Visualize a image in a label
     * @param image Image
     * @param title title of the image
     * @param col column
     * @param row row of the image
     */
    public static void setImage(BufferedImage image, String title, int col, int row) {
        vis.setImage(image, title , col * Config.h + row);
    }

    /**
     * Visualize an image adding a group, the group is useful for avoid calculating the row number
     * @param image
     * @param title
     * @param col
     * @param row
     * @param group 
     */
    public static void setImage(BufferedImage image, String title, int col, int row, String group) {
        vis.setImage(image, title, col * Config.h + row);
        if (!lockedLimits.contains(group)) {
            setLimitByGroup(col, group);
        }
    }

    public static void setImage(Mat src, String title, int col, int row) {
        setImage(Convertor.Mat2Img(src), title, col, row);
    }

    public static void setImageFull(Mat src, String title, int col, int row) {
        setImage(Scalr.resize(Convertor.Mat2Img(src), Config.heigth), title, col, row);
    }

    public static void setImage(Mat src, String title, int col, int row, String group) {
        setImage(src, title, col, row);
        if (!lockedLimits.contains(group)) {
            setLimitByGroup(col, group);
        }
    }

    static void setLimitByGroup(int col, String group) {
        int value = 0;
        if (limits.containsKey(group)) {
            value = limits.get(group);
        }
        if (col > value) {
            limits.put(group, col);
        }
    }

    public static void lockLimit(String group) {
        lockedLimits.add(group);
    }

    public static void setImageFull(Mat src, String title, int col, int row, String group) {
        setImage(Scalr.resize(Convertor.Mat2Img(src), Config.heigth), title, col, row, group);
    }

    public static void setImageFull(BufferedImage image, String title, int col, int row, String group) {
        setImage(Scalr.resize(image, Config.heigth), title, col, row, group);
    }

    public static void next() {
        vis.next();
    }

    public static void previous() {
        vis.previous();
    }

    public static void setImage(Mat mat, String even_Cell, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
