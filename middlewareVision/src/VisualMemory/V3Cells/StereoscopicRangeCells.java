/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V3Cells;

import VisualMemory.Cell;
import utils.Config;
import utils.MathFunctions;

/**
 *
 * @author Luis Humanoide
 */
public class StereoscopicRangeCells {

    public double disparity;
    public double deviation;
    //[orientation index]
    public Cell[] cells;
    public Cell composedCell;
    public double xvalues[];
    public double yvalues[];

    /**
     * Initialize the StereoscopicRangeCells
     *
     * @param disparity
     * @param deviation
     */
    public StereoscopicRangeCells(double disparity, double deviation) {
        this.disparity = disparity;
        this.deviation = deviation;
        cells = new Cell[Config.gaborOrientations];

        for (int i = 0; i < Config.gaborOrientations; i++) {
            cells[i] = new Cell();
        }
        composedCell = new Cell();
    }

    /**
     * set the x values corresponsig to the absolute disparity values from v1
     * @param xvalues an array of the x values
     */
    public void setXvalues(double[] xvalues) {
        this.xvalues = xvalues;
        yvalues = new double[xvalues.length];
    }

    /**
     * Create the gaussian values for sum the absolute disparity maps<br>
     * In this algorithm, 2 cycles are performed, in the first one the sum of the generated Gaussian is found and then normalized.<br>
     * Then proceed to find the values of the normalized Gaussian
     */
    public void createGaussian() {
        double sum = 0;
        /**
         * Process for normalizing the gaussian
         */
        for (int i = 0; i < xvalues.length; i++) {
            sum = sum + MathFunctions.Gauss(deviation, disparity, 1, xvalues[i]);
        }
        for (int i = 0; i < xvalues.length; i++) {
            yvalues[i] = MathFunctions.Gauss(deviation, disparity, (double) (1 / sum), xvalues[i]);
        }
    }

}
