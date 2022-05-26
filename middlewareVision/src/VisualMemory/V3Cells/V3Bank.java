/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V3Cells;

import utils.FileUtils;

/**
 *
 * @author Luis Humanoide
 */
public class V3Bank {

    static String gaussiansFile = "DisparityGaussians.txt";
    static String DisparityFile = "Disparities.txt";
    //[disparity index]
    public static StereoscopicRangeCells[] SRC;

    /**
     * Initialize the V3 cells, mainly corresponsing to the relative disparity or disparity range cells
     */
    public static void initializeCells() {

        String[] dgaussians = FileUtils.fileLines(gaussiansFile);
        SRC = new StereoscopicRangeCells[dgaussians.length];
        
        String disparities[] = FileUtils.fileLines(DisparityFile);

        int i = 0;
        for (String line : dgaussians) {
            String values[] = line.split(" ");
            SRC[i] = new StereoscopicRangeCells(Double.parseDouble(values[1]), Double.parseDouble(values[0]));
            SRC[i].setXvalues(returnDisparities(disparities));
            SRC[i].createGaussian();
            i++;
        }

    }
    
    /**
     * Convert the disparities array string to double array
     * @param disparities
     * @return 
     */
    static double[] returnDisparities(String[] disparities){
        double[] dispArray=new double[disparities.length];
        for(int i=0;i<dispArray.length;i++){
            dispArray[i]=Double.parseDouble(disparities[i]);
        }
        return dispArray;
    }

}
