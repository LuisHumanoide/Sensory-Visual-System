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
public class SimpleOpponentCells {
    
    public int scale;
    public Cell[] Cells;

    public SimpleOpponentCells(int scale, Cell[] SimpleOpponentCells) {
        this.scale = scale;
        this.Cells = SimpleOpponentCells;
    }
    
    public SimpleOpponentCells(int scale,int number){
        this.scale=scale;
        Cells=new Cell[number];
        for(int i=0;i<number;i++){
            Cells[i]=new Cell();
        }
    }
}
