/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MTCells;

import VisualMemory.Cell;

/**
 *
 * @author HumanoideFilms
 */
public class MTBank {
    
    //[eye]
    public static ComponentCells[] MTCC;
    
    public static void initializeComponentCells(int d1, int d2, int size){
        for(int i=0;i<2;i++){
            MTCC[i]=new ComponentCells();
            MTCC[i].setComponentCells(d1, d2, size);
        }
    }
    
    
}
