/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.V2Cells;

import VisualMemory.Cell;
import VisualMemory.MotionCell;
import java.io.File;
import utils.Config;
import utils.FileUtils;
import utils.Functions;

/**
 *
 * @author HumanoideFilms
 */
public class CornerMotionCells {
    
    //[speed][orientation]
    public MotionCell[][] cells;
    //Composed cell for a specific speed
    public Cell[] composedCells;
    
    public CornerMotionCells(){
        
    }
    
    public CornerMotionCells(String path){
        String lines[] = FileUtils.readFile(new File(path)).split("\n");
        if (lines.length > 0) {
            cells = new MotionCell[lines.length][Config.gaborOrientations * 2];
            composedCells = new Cell[lines.length];
            for (int i = 0; i < lines.length; i++) {
                String values[] = lines[i].split(" ");
                composedCells[i] = new Cell();
                for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                    cells[i][j] = new MotionCell(3);
                    /*
                    Set dx/dt and angles
                    */
                    cells[i][j].setDxDt(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                    cells[i][j].setId(j);
                    cells[i][j].setAngle((double)((Math.PI/Config.gaborOrientations)*j)%(2*Math.PI));
                }
            }
        } else {
            utils.Msg.print("there are no speeds for loading");
        }
    }
    
    /**
     * Create the composed cells using a max summation
     */
    public void createComposedCells() {
        for (int i = 0; i < cells.length; i++) {
            composedCells[i].mat = Functions.maxSum(cells[i]);
        }
    }

    public void setPrevious(Cell src[]) {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < Config.gaborOrientations * 2; j++) {
                cells[i][j].setPrevious(src[j % Config.gaborOrientations]);
            }
        }
    }
    
}
