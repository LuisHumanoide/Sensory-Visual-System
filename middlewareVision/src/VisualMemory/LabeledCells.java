/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;

import java.util.HashMap;

/**
 *
 * @author luish
 */
public class LabeledCells {
    
    public static HashMap<String, Cell> LabelCellMapL;
    public static HashMap<String, Cell> LabelCellMapR;
    
    public static void initMap(){
        LabelCellMapL=new HashMap();
        LabelCellMapR=new HashMap();
    }
    
    public static void addCellL(String key, Cell cell){
        LabelCellMapL.put(key, cell);
    }
    
     public static void addCellR(String key, Cell cell){
        LabelCellMapR.put(key, cell);
    }
    
    
}
