/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import VisualMemory.LGNBank;
import VisualMemory.V1Bank;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import org.opencv.core.Mat;
import utils.Config;
import utils.Convertor;

/**
 *
 * @author Laptop
 */
public class Visualizer {

    static VisualizerFrame vis;
    public static HashMap<String, Integer> limits;

    public static void initVisualizer(int n) {
        vis = new VisualizerFrame(n);
        limits = new HashMap<String, Integer>();
        limits.put("init", 0);
    }

    public static void addLimit(String name, int value) {
        limits.put(name, value);
    }

    public static int getRow(String name) {
        return limits.get(name);
    }

    public static void setImage(BufferedImage image, String title, int index) {
        vis.setImage(image, title, index);
    }

    public static void setImage(BufferedImage image, String title, int col, int row) {
        vis.setImage(image, title, col * Config.h + row);
    }

    public static void setImage(Mat src, String title, int col, int row) {
        setImage(Convertor.Mat2Img(src), title, col, row);
    }

    public static void next() {
        vis.next();
    }

    public static void previous() {
        vis.previous();
    }

    public static void update() {
        for (int i = 0; i < 4; i++) {
            if (i < 3) {
                vis.setImage(Convertor.Mat2Img(LGNBank.SOC[0][0][0].Cells[i].mat), "dkl", 4 + i);
            }
            vis.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Even[i].mat), "even", 12 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.SC[0][0][0].Odd[i].mat), "odd", 12 + 4 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.CC[0][0][0].Cells[i].mat), "complex", 12 + 4 + 4 + i);
            vis.setImage(Convertor.Mat2Img(V1Bank.HCC[0][0][0].Cells[0][i].mat), "hyper", 12 + 4 + 4 + 4 + i);
        }
    }

}
