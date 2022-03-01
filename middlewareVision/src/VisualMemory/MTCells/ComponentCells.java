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
public class ComponentCells {
    
    //[speed][orientation]
    public Cell ComponentCells[][];
    
    int sizeComponent=0;
    
    public void setComponentCells(int d1, int d2, int size){
        sizeComponent=size;
        for(int i=0;i<d1;i++){
            for(int j=0;i<d2;j++){
                ComponentCells[i][j]=new Cell(sizeComponent);
            }
        }
    }
}
