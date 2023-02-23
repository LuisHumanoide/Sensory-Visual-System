/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V1Cells;

import VisualMemory.Cell;
import utils.Config;

/**
 * This class encapsulates the properties of stereo cells where cells with the same disparity <br>
 * but different spatial frequencies are brought together for the binocular noise reduction process.
 * @author Luis Humanoide
 */
public class StereoscopicMergedCells {
    
    public double disparity;
    //[orientation index]
    public Cell[] cells;
    public Cell composedCell;

    /**
     * Class constructor which receives the preferred disparity of the cells
     * @param disparity preferred disparity of the cell.
     */
    public StereoscopicMergedCells(double disparity) {
        this.disparity = disparity;
        
        cells = new Cell[Config.gaborOrientations];

        for (int i = 0; i < Config.gaborOrientations; i++) {
            cells[i] = new Cell();
        }
        composedCell = new Cell();        
    }
    
    
    
}
