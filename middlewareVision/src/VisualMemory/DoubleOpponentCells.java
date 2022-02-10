/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VisualMemory;


/**
 *
 * @author Laptop
 */
public class DoubleOpponentCells {
    
    public int scale;
    public Cell[] Cells;

    public DoubleOpponentCells(int scale, Cell[] DoubleOpponentCells) {
        this.scale = scale;
        this.Cells = DoubleOpponentCells;
    }
    
    public DoubleOpponentCells(int scale,int number){
        this.scale=scale;
        Cells=new Cell[number];
        for(int i=0;i<number;i++){
            Cells[i]=new Cell();
        }
    }
    
    public DoubleOpponentCells(int number){
        Cells=new Cell[number];
        for(int i=0;i<number;i++){
            Cells[i]=new Cell();
        }
    }
    
}
