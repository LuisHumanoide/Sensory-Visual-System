/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MTCells;

import VisualMemory.Cell;
import VisualMemory.V1Cells.V1Bank;
import java.util.ArrayList;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import utils.MatrixUtils;

/**
 *
 * @author HumanoideFilms
 */
public class MTBank {

    //[eye]
    public static ComponentCells[] MTCC;

    /**
     * Initialize <b>MT Component Cells</b><br>
     *
     * @param d1 number of velocities/speeds
     * @param d2 number of orientations
     * @param size size of the component cells
     */
    public static void initializeComponentCells(int d1, int d2, int size) {
        MTCC = new ComponentCells[2];
        for (int i = 0; i < 2; i++) {
            MTCC[i] = new ComponentCells(d1, d2, size);
        }
    }
    
}
