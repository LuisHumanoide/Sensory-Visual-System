/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory.MTCells;

import VisualMemory.PatternCell;

/**
 *
 * @author HumanoideFilms
 */
public class PatternCells {
    
    
    PatternCell Cells [];
    
    public PatternCells(int n, int size){
        Cells=new PatternCell[n];
        for(int i=0;i<n;i++){
            Cells[i]=new PatternCell(size);
        }
    }
    
    
    
}
