/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MSTCells;

import VisualMemory.Cell;
import org.opencv.core.Mat;
import utils.Config;

/**
 * Experimental MST Polar cells
 *
 * @author HumanoideFilms
 */
public class MSTPolar {

    public static Mat[] polarSrc;
    public static Cell[] expC;//for expansion
    public static Cell[] contC;//for contractions
    public static Cell[] rotC;//for rotations
    public static Cell[] invRC;//Inverse rotation

    /**
     * Initialize cells for both eyes
     */
    public static void initializeCells() {
        polarSrc = new Mat[2];
        expC = new Cell[2];
        contC = new Cell[2];
        rotC = new Cell[2];
        invRC = new Cell[2];
        for (int i = 0; i < 2; i++) {
            polarSrc[i] = new Mat();
            expC[i] = new Cell(Config.motionHeight);
            contC[i] = new Cell(Config.motionHeight);
            rotC[i] = new Cell(Config.motionHeight);
            invRC[i] = new Cell(Config.motionHeight);
        }
    }

}
