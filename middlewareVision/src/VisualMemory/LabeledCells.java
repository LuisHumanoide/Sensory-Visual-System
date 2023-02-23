/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import java.util.HashMap;

/**
 * Class for label the cells that will be used in the V4 Simple Shape Process
 * @author luish
 */
public class LabeledCells {
    
    //Labeled cells from the left eye
    public static HashMap<String, Cell> LabelCellMapL;
    //Labeled cells from the right eye
    public static HashMap<String, Cell> LabelCellMapR;
    
    /**
     * Initialize L and R maps
     */
    public static void initMap(){
        LabelCellMapL=new HashMap();
        LabelCellMapR=new HashMap();
    }
    
    /**
     * Add a cell to the left map
     * @param key name or key of the cell
     * @param cell cell
     */
    public static void addCellL(String key, Cell cell){
        LabelCellMapL.put(key, cell);
    }
    
    /**
     * Add a cell to the right map
     * @param key name or key of the cell
     * @param cell cell
     */
     public static void addCellR(String key, Cell cell){
        LabelCellMapR.put(key, cell);
    }
    
    
}
